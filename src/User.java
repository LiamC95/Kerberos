
import java.util.Scanner;
import java.math.BigInteger;
import java.util.Random;

public class User{

    private String uName;
    private BigInteger message;
    
    

    final int maxBitLength = 15;
    public User()
    {
        this.uName ="";
        this.message = BigInteger.ONE;
        
    }
    public User(String uName, String message){
        
        this.uName = uName;
        
        this.message = new BigInteger(message);
        
    }
    public void setUName(final String uName){
        this.uName = uName;
    }
    public void setmessage(final String message){
        this.message =new BigInteger(message);
    }
    
    public String getUName()
    {
        return this.uName;
    }
    public BigInteger getmessage()
    {
        return this.message;
    }


    @Override 
    public String toString()
    {
        return getUName() +","+ getmessage();
    }



    
    
}
