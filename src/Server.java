

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server
{
    public static void main(String[] args)
    {
        Server server = new Server();
        server.start();
    }

    public void start()
    {
        HashMap <String, BigInteger> userKeys = new HashMap<>();
        HashMap <String, BigInteger> userMessages = new HashMap<>();
        try
        {
            ServerSocket ss = new ServerSocket(8080);  // set up ServerSocket to listen for connections on port 8080
            System.out.println("Server: Server started. Listening for connections on port 8080...");
            
            int clientNumber = 0;  // a number for clients that the server allocates as clients connect
            // UserKeys store the keys for each user
            // userKeys(Username, Keys)
            
            while (true)    // loop continuously to accept new client connections
            {
                Socket socket = ss.accept();    // listen (and wait) for a connection, accept the connection, 
                                                // and open a new socket to communicate with the client
                clientNumber++;
                
                System.out.println("Server: Client " + clientNumber + " has connected.");
                
                System.out.println("Server: Port# of remote client: " + socket.getPort());
                System.out.println("Server: Port# of this server: " + socket.getLocalPort());

                Thread t = new Thread(new ClientHandler(socket, clientNumber, userKeys, userMessages)); // create a new ClientHandler for the client,
                t.start(); 

                System.out.println("Server: ClientHandler started in thread for client " + clientNumber + ". ");
                System.out.println("Server: Listening for further connections...");
            }
            
        } catch (IOException e)
        {
            System.out.println("Server: IOException: " + e);
        }
        
        System.out.println("Server: Server exiting, Goodbye!");
    }

    public class ClientHandler implements Runnable   // each ClientHandler communicates with one Client
    {
        BufferedReader socketReader;
        PrintWriter socketWriter;
        Socket socket;
        int clientNumber;
        // Contains Uname and keys
        HashMap<String, BigInteger> userKeys;
        // Contains Uname and cipher
        HashMap<String, BigInteger> messages;

        public ClientHandler(Socket clientSocket, int clientNumber, HashMap<String, BigInteger> userKeys, HashMap<String, BigInteger> messages)
        {
            try
            {
                InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());
                this.socketReader = new BufferedReader(isReader);
                
                OutputStream os = clientSocket.getOutputStream();     
                this.socketWriter = new PrintWriter(os, true); // true => auto flush socket buffer
                
                this.clientNumber = clientNumber;  // ID number that we are assigning to this client
                
                this.socket = clientSocket;  // store socket ref for closing 
                this.userKeys = userKeys;
                this.messages = messages;
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        public void run()
        {
            String message;
            try
            {

                
                //! Server's secret value for diffie hellman key exchange
            BigInteger b = CryptographicFunctions.randomBigInteger();
                                

                BigInteger g = BigInteger.ZERO;
                BigInteger n = BigInteger.ZERO;
                BigInteger A = BigInteger.ZERO;
                

                while ((message = socketReader.readLine()) != null)
                {
                    System.out.println("Server: (ClientHandler): Read command from client " + clientNumber + ": " + message);
                    

                    if(message.startsWith("sin"))
                    {
                        
                        String[] tokens = splitString(message);
                        g = new BigInteger(tokens[1]);
                        n = new BigInteger(tokens[2]);
                        A = new BigInteger(tokens[3]);
                        String username = tokens[4];

                        BigInteger B = CryptographicFunctions.modularExponentiationOfaPowbModn(g, b, n); 
                        BigInteger sharedKey = CryptographicFunctions.modularExponentiationOfaPowbModn(A, b, n);


                        System.out.println("Secret Key = "+ sharedKey);

                        userKeys.put(username, sharedKey);

                        socketWriter.println("Ack,"+B);

                    }
                    else if(message.startsWith("share"))
                    {
                        String[] tokens = splitString(message);
                        //token 1 user name
                        // token 2 is the cipher text
                        String name = tokens[1];
                        String cipher = tokens[2];
                        BigInteger key = userKeys.get(name);
                        BigInteger value = CryptographicFunctions.decrypt(key, new BigInteger(cipher));
                        messages.put(name, value);
                        socketWriter.println("Added To map");
                    }
                    else if(message.startsWith("getUsers"))
                    {
                        socketWriter.println("UsersList,"+messages.keySet());
                    }
                    else if(message.startsWith("get"))
                    {
                        String[] tokens = splitString(message);
                        /*
!                       tokens[0] is command
!                       Tokens[1] is the username
!                       Tokens[2] is the username of the user we want
!
                        */
                        
                        BigInteger mess = messages.get(tokens[2]);
                        BigInteger key = userKeys.get(tokens[1]);

                        socketWriter.println("UserMessage,"+ CryptographicFunctions.encrypt(key, mess));


                    }
                    else if (message.startsWith("Exit"))
                    {
                        socketWriter.println("Goodbye:(");
                    }
                    else{
                        socketWriter.println("I don't unsersatnd You're command");
                    }
                }
                
                socket.close();
                
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
            System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
        }
    }
    
    public String[] splitString(String message)
    {
        String delims = "[,]";
        String[] tokens = message.split(delims);
        return tokens;

    }

    
}
