
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Client
{

    public static void main(final String[] args) {
        Client client = new Client();
        client.start();
        
    }

    public void start() {

        BigInteger a = CryptographicFunctions.randomBigInteger();
        BigInteger g = CryptographicFunctions.randomBigInteger();
        BigInteger secretKey = BigInteger.ZERO;
        BigInteger n = BigInteger.ZERO;
        boolean isPrime = false;
        while(!isPrime)
        {
            BigInteger temp = CryptographicFunctions.randomBigInteger();
            BigInteger tenPct = temp.divide(BigInteger.TEN);
            if(CryptographicFunctions.MillerRabinProbPrimeTest(temp, tenPct))
            {
                isPrime = true;
                n = temp;
            }
        }

        BigInteger A = CryptographicFunctions.modularExponentiationOfaPowbModn(g, a, n);
        

        final Scanner in = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 8080); // connect to server socket, and open new socket
            OutputStream os = socket.getOutputStream();
            PrintWriter socketWriter = new PrintWriter(os, true);// true=> auto flush buffers
            Scanner socketReader = new Scanner(socket.getInputStream());

            

            System.out.println("Client: Port# of this client : " + socket.getLocalPort());
            System.out.println("Client: Port# of Server :" + socket.getPort());

            System.out.println("Client: This Client is running and has connected to the server");

            
            int option = 0;
            boolean play = true;
            in.nextLine();
            User you = newUser(in);
            
            socketWriter.println("sin,"+ g +"," + n +"," + A + "," + you.getUName());

            String[] response = getResponse(socketReader);
            BigInteger B = new BigInteger(response[1]);
            secretKey = CryptographicFunctions.modularExponentiationOfaPowbModn(B, a, n);
            System.out.println("Secret Key = "+secretKey);

//!                 
//!                 Sending our message
//!
            while(play)
            {
                option = run(in);

                switch(option)
                {
                    case 1:
                        in.nextLine();
                        you = newUser(in);
                        socketWriter.println("sin,"+ g +"," + n +"," + A + "," + you.getUName());
                        break;
                    case 2:
                        BigInteger cipher = CryptographicFunctions.encrypt(secretKey, you.getmessage());
                        socketWriter.println("share,"+ you.getUName() + "," + cipher );
                        break;
                    case 3:
                        socketWriter.println("getUsers");
                        break;
                    case 4:
                        in.nextLine();
                        String userYouWant = getUser(in);

                        socketWriter.println("get,"+ you.getUName()+"," + userYouWant);
                        break;
                    case -1:
                        socketWriter.println("Exit");

                        return;

                    default:
                    break;
            
                }



                //! Gets the reponses form the server
                
                response = getResponse(socketReader);
                switch(response[0])
                {
                    case"Ack":
                        B = new BigInteger(response[1]);
                        secretKey = CryptographicFunctions.modularExponentiationOfaPowbModn(B, a, n);
                        System.out.println("Secret Key = "+secretKey);
                        break;

                    case"UsersList":
                        for(String s: response)
                        {
                            System.out.println("User: "+s);
                        }
                        break;
                    case"UserMessage":
                        BigInteger mess = CryptographicFunctions.decrypt(secretKey, new BigInteger(response[1]));

                        System.out.println("The user's message is : "+ mess);
                        break;    

                    default:
                    for(String s: response)
                    {
                        System.out.println(s);
                    }
                    break;
                }

            }

                    

               
            in.close();
            socketWriter.close();
            socketReader.close();
            socket.close();

            System.out.println("Finished");

        } catch (final IOException e) {
            System.out.println("Client message: IOException: " + e);
        }
    }

    /*

    Todo    Step 1 - Create User Alice
    Todo    Step 2 - Authenticate to the Server and share Key(Pasword)
    Todo    Step 3 - Receive TGT when authenticated
    Todo    Step 4 - Find other users on the server(Bob)
    Todo    Step 5 - Make a Requst for Bob's info(Using the TGT)
    Todo    Step 6 - Get the TGS for Bob's info and then submit it to the server encrypted with Alice's key(Password)
    Todo    Step 7 - Retrieve the Information back from the Server and Decrypt
    */


    public static String getUser(Scanner in){
        in.nextLine();
        System.out.println("What user's message would you like to get?");
        return in.nextLine();
                        
    }

    public static int run(Scanner in){
        
        
        int option = 0;
        while(option != -1)
        {
            printMenu();
            if(in.hasNextInt())
            {
                option = in.nextInt();
            }
            return option;

        }
        
        return -1;
        
        
    }
    public static void printMenu()
    {
        System.out.println(" 1 - Create Shared Key");
        System.out.println(" 2 - Share Your Message");
        System.out.println(" 3 - Get All Users");
        System.out.println(" 4 - Get user message");
        System.out.println("-1 - Exit app");
    }
    public static User newUser(Scanner in){
       
        System.out.println("Please enter Username:");
        String username = in.nextLine();

        
        
        System.out.println("Enter Your secret Message (BigInteger)");
        String message = in.nextLine();


        User you = new User(username, message);
            return you;
    }
    

    public String[] getResponse(Scanner socketReader)
    {
        String response = socketReader.nextLine();
        return splitString(response);
    }
    public String[] splitString(String response)
    {
        String delims = "[,]";
        String[] tokens = response.split(delims);
        return tokens;

    }


        
    
        
        
        
        
        
        
        /*
        System.out.println(
                        "[Commands: \"Time\" to get time, or \"Echo message\" to get echo), or \"End\" To close Application]");
                System.out.println("Please enter a command: ");

                final String command = in.nextLine(); // read a command from the user

                final OutputStream os = socket.getOutputStream();

                final PrintWriter socketWriter = new PrintWriter(os, true);// true=> auto flush buffers
                socketWriter.println(command); // write command to socket

                final Scanner socketReader = new Scanner(socket.getInputStream());

                if (command.startsWith("Time")) // we expect the server to return a time (in milliseconds)
                {
                    final long time = socketReader.nextLong(); // wait for, and read time (as we expect time reply)
                    final Date date = new Date(time);
                    System.out.println("Client: Response from server: Time: " + date.toString());

                }else {
                    final String input = socketReader.nextLine();// wait for, and retrieve the echo ( or other message)
                    System.out.println("Client: Response from server: \"" + input + "\"");
                }
    }
    */
}




/*
public class User{

    private String uName;
    private String password;
    private String privateInfo;

    public User()
    {
        this.uName ="";
        this.password = "";
        this.privateInfo = "NONE";
    }
    public User(String uName, String password, String privateInfo){
        
        this.uName = uName;
        
        this.password = password;
        
        this.privateInfo = privateInfo;
    }
    public void setUName(String uName){
        this.uName = uName;
    }
    public void setPassword(String password){
        this.password =password;
    }
    public void setPrivateInfo(String privateInfo)
    {
        this.privateInfo = privateInfo;
    }

    public String getPrivateInfo()
    {
        return this.privateInfo;
    }
    public String getUName()
    {
        return this.uName;
    }
    public String getPassword()
    {
        return this.password;
    }
    
    



}*/
