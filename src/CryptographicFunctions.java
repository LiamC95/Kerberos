import java.util.Scanner;
import java.util.Stack;
import java.math.BigInteger;
import java.util.Random;


/**
 *
 * @author Liam Clarke
 */
public class CryptographicFunctions {

    public static void main(String[] args)
    {


        try
        { 
            menuChoice();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
       

    }


/*
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!
!   Miller - Rabin Probabilistic Primality Checks
!   Input = n and t
!       n >= 3
!       t >= 1 & is a security check
!
!   output if n is prime or not 
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
*/
public static boolean MillerRabinProbPrimeTest(BigInteger n, BigInteger t)
{

    //* Checks for n and t to ensure 
    //* n is odd and n > 2
    //* 0 < t <= n
    if(n.compareTo(new BigInteger("3"))==0||t.equals(BigInteger.ZERO)||BigInteger.ZERO.equals(modBigInteger(n, new BigInteger("2"))))
    {

        if(BigInteger.ZERO.equals(modBigInteger(n, new BigInteger("2"))))
        {
            return false;
        }
        return false; 
    }
    BigInteger s = new BigInteger("0");
    BigInteger r;
    BigInteger factor = n.subtract(BigInteger.ONE);


    while(BigInteger.ZERO.equals(modBigInteger(factor, new BigInteger("2"))))
    {
        s = s.add(BigInteger.ONE);
        factor = factor.divide(new BigInteger("2"));
    }
    r = factor;
    for(BigInteger a = new BigInteger("2"); a.compareTo(t)==-1; a = a.add(BigInteger.ONE))
    {
        BigInteger y = modularExponentiationOfaPowbModn(a, r, n);
        if(!y.equals(BigInteger.ONE)&&!y.equals(n.subtract(BigInteger.ONE)))
        {
            BigInteger j = BigInteger.ONE;

            while(j.compareTo(s)==-1&&!y.equals(n.subtract(BigInteger.ONE)))
            {
                y = modularExponentiationOfaPowbModn(y, new BigInteger("2"), n);
                j = j.add(BigInteger.ONE);
            }
            if(!y.equals(n.subtract(BigInteger.ONE)))
            {
                return false;
            }
        }
    }

    return true;


}

public BigInteger nextRandomBigInteger(BigInteger n) {
    Random rand = new Random();
    BigInteger result = new BigInteger(n.bitLength(), rand);
    while( result.compareTo(n) >= 0 ) {
        result = new BigInteger(n.bitLength(), rand);
    }
    return result;
}


/*
public static BigInteger[] fillBigIntegers(BigInteger arraySize, BigInteger maxVal)
{
    BigInteger[] testVals = new BigInteger[arraySize.intValue()];
    BigInteger count = BigInteger.ZERO;
    while(!count.equals(arraySize))
    {
        boolean add =true;
       BigInteger a = randomBigInteger(maxVal.subtract(BigInteger.ONE));
       for(BigInteger b : testVals)
       {
           if(a.equals(b))
           {
                add =false;
           }
       }
       if(add)
       {
           System.out.println(a);
           testVals[count.intValue()].add(a);
           count = count.add(BigInteger.ONE);
       }
    }
    return testVals;
}

public static BigInteger randomBigInteger(BigInteger maxVal)
{
    
    BigInteger aMax = maxVal.subtract(new BigInteger("2"));
    BigInteger aMin = new BigInteger("2");
    
    BigInteger bigInteger = aMax.subtract(aMin);
    Random randNum = new Random();
    int len = aMax.bitLength();
    BigInteger a = new BigInteger(len, randNum);
    if (a.compareTo(aMin) < 0)
    {
        a = a.add(aMin);
    }
    if (a.compareTo(bigInteger) >= 0)
    {
        a = a.mod(bigInteger).add(aMin);
    }
    
    return a;
}
*/
public static BigInteger randomBigInteger()
{
    BigInteger bi;

    // create and assign value to bitLength
    int bitLength = 10;

    // create a random object
    Random rnd = new Random();

    // assign probablePrime result to bi using bitLength and rnd
    // static method is called using class name
    bi = BigInteger.probablePrime(bitLength, rnd);
    return bi;
}



/*
!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!   Euclidian Algorithm GCD(a,b)
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!
*/
    public static BigInteger greatestCommonDivissorBigInteger(BigInteger a, BigInteger b)
    {


        //* Checking input
        if(a.compareTo(BigInteger.ONE)== -1 ||b.compareTo(BigInteger.ONE)==-1)
        {
            throw new ArithmeticException("A and B values must be non negative integers");
        }

        //todo : Have to if a is greater than b value or swap values

        if(a.compareTo(b) == -1)
        {
           return greatestCommonDivissorBigInteger(b, a);
        }




        BigInteger r;
        BigInteger q;
        while(!b.equals(BigInteger.ZERO))
        {
            // Find the remainder of the division
            q = a.divide(b);
            r = a.subtract(q.multiply(b));

            //* pass the b value into the a position
            a = b;

            //* pass the remainder value into b
            b = r;
        }

        return a;
    }

/*
!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!          Extended Euclidian Algorithm
!   input BigInteger a,b;
!
!   finds xa + yb = d
!
!   outputs Array = [d,x,y]
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!
*/
    public static BigInteger[] extendedEuclidianBigIntegers(BigInteger a, BigInteger b)
    {
        /*
        !      Array Values
        !   arrayDXY[0] == d;
        !   arrayDXY[1] == x;
        !   arrayDXY[2] == y;
        */
        BigInteger[] arrayDXY = new BigInteger[3];
        if(a.compareTo(BigInteger.ONE)== -1 ||b.compareTo(BigInteger.ONE)==-1)
        {
            throw new ArithmeticException("A and B values must be non negative integers");
        }
        //todo : Have to if a is greater than b value or swap values
        if(a.compareTo(b) == -1)
        {
           return extendedEuclidianBigIntegers(b, a);
        }

        //* Now to begin coding the algorithm
        if(b.equals(BigInteger.ZERO))
        {
            //? if b = 0 then d = a, x = 1, y = 0
            arrayDXY[0] = a;
            arrayDXY[1] = BigInteger.ONE;
            arrayDXY[2] = BigInteger.ZERO;
            return arrayDXY;
        }
        
        BigInteger[] xValue = new BigInteger[2];
        BigInteger[] yValue = new BigInteger[2];
        BigInteger q;
        BigInteger r; 
        xValue[0] = BigInteger.ZERO;
        xValue[1] = BigInteger.ONE;
        yValue[0] = BigInteger.ONE;
        yValue[1] = BigInteger.ZERO;

        //todo : while b>0 do 
        while(b.compareTo(BigInteger.ZERO) == 1)
        {
            
            //todo : q ← ba/bc
            q = a.divide(b);
            
            //todo : r ← a − qb
            r = a.subtract(q.multiply(b));
            
            

            //todo :  x ← x2 − qx1
            arrayDXY[1] = xValue[1].subtract(q.multiply(xValue[0]));



            //todo : y ← y2 − qy1
            arrayDXY[2] = yValue[1].subtract(q.multiply(yValue[0]));
            

            //todo : a, b ← r, x2 ← x1, x1 ← x, y2 ← y1 and y1 ← y.
            a = b;
            b = r;


            xValue[1] = xValue[0];

            xValue[0] = arrayDXY[1];

            yValue[1] = yValue[0];

            yValue[0] = arrayDXY[2];
        }

        //todo : d ← a, x ← x2, y ← y2, and return (d, x, y).
        arrayDXY[0] = a;
        arrayDXY[1] = xValue[1];
        arrayDXY[2] = yValue[1];
        
        
        return arrayDXY;
    }
 
/*
!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!       Modular Inverse of a
!           input = A mod b
!           output = A^-1
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!
*/
    public static BigInteger modularInverseOfaBigInteger(BigInteger a, BigInteger n)
    {
        BigInteger[] DXY = extendedEuclidianBigIntegers(a, n);
        //* Looks to see if ints are coprime
        if(DXY[0].compareTo(BigInteger.ONE) == 1)
        {
            throw new ArithmeticException("Numbers are not co-prime! Inverse doesn't exsist");
        }
        else{
            //* returning X value 
            return DXY[1];
        }
    }
    

    /*
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!
!       Modular exponentiation
!       
!       Input   = a^k mod n
!
!       output  = BigInter r
!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    */

    public static BigInteger modularExponentiationOfaPowbModn(BigInteger a, BigInteger k, BigInteger n)
    {
        BigInteger b = BigInteger.ONE;
        
        

        if(!k.equals(BigInteger.ZERO))
        {
            BigInteger A = a;
            Stack<Boolean>  binaryOfK = convertkToBinaryStack(k);
            if(binaryOfK.pop())
            {
                b =a;
            }
            while(!binaryOfK.empty())
            {

                A = modBigInteger(A.multiply(A), n);

                if(binaryOfK.pop())
                {
                    b = modBigInteger(A.multiply(b), n);
                }
            }

            
        }

        return b;
    }



    public static Stack<Boolean> convertkToBinaryStack(BigInteger k)
    {
        Stack<Boolean> binaryStack = new Stack<>();

        String s = k.toString(2);
        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i)=='1')
            {
                binaryStack.add(true);
            }else{
                binaryStack.add(false);
            }
        }
        return binaryStack;
    }



/*
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!       Mod Biginteger 
!
!       input BigInteger a,b
!
!       output a%b
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
*/
    
    public static BigInteger modBigInteger(BigInteger a, BigInteger n)
    {
        BigInteger q = a.divide(n);
        return a.subtract(n.multiply(q));
    }



    /*
    !
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    !       Menu System
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    !
    */
    public static void menuChoice()
    {
        Scanner in = new Scanner(System.in);
        int option = 1;

        
        BigInteger d;
        BigInteger[] DXY = new BigInteger[3]; 

        

        boolean exit = false;
        while(!exit)
        {

            printMenu();
            option = in.nextInt();
            if(option==1)
            {   
                
            }
            else if(option==2)
            {
                System.out.println("Enter Value for A:");
                BigInteger a = in.nextBigInteger();
                System.out.println("Enter Value for B:");
                BigInteger b = in.nextBigInteger();
                d = greatestCommonDivissorBigInteger(a, b);
                display(a, b, d);
            }
            else if(option == 3)
            {
                System.out.println("Enter Value for A:");
                BigInteger a = in.nextBigInteger();
                System.out.println("Enter Value for B:");
                BigInteger b = in.nextBigInteger();
                DXY = extendedEuclidianBigIntegers(a, b);
                display(a, b, DXY);
            }
            else if(option == 4)
            {
                System.out.println("Enter Value for A:");
                BigInteger a = in.nextBigInteger();
                System.out.println("Enter Value for B:");
                BigInteger b = in.nextBigInteger();
                BigInteger aInverse = modularInverseOfaBigInteger(a, b);
                display(aInverse);
            }
            else if(option == 5)
            {
                System.out.println("Enter Value for A:");
                BigInteger a = in.nextBigInteger();
                System.out.println("Enter Value for K:");
                BigInteger k = in.nextBigInteger();
                System.out.println("Enter Value for N:");
                BigInteger n = in.nextBigInteger();
                
                System.out.println("\n"+a+"^"+k+" MOD "+n+" = "+modularExponentiationOfaPowbModn(a,k,n));
                System.out.println("True output = "+a.pow(k.intValue()).mod(n));
            }
            else if(option == 6)
            {
                System.out.println("Enter a number you would like to see if its prime");
                BigInteger p = in.nextBigInteger();

                System.out.println("Enter a security parameter");
                BigInteger t = in.nextBigInteger();

                System.out.println(MillerRabinProbPrimeTest(p, t));
            }
            else if(option == 7)
            {
                exit = true;
            }
            
        }


       in.close();
    }






    /*
    !
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!
    !       DISPLAYS
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!
    !
    */

    public static void printMenu()
    {
        System.out.println("Cryptographic Functions:");
        System.out.println("Add Numbers             \t-1");
        System.out.println("Euclidian               \t-2");
        System.out.println("Extended Euclidian      \t-3");
        System.out.println("Find Modular Inverse    \t-4");
        System.out.println("Modular Exponentiation  \t-5");
        System.out.println("Primality Chacker       \t-6");
        System.out.println("Exit                    \t-7\n");
    }
    public static void display(BigInteger a, BigInteger b)
    {
        System.out.println("Integer Values :");
        System.out.println("A value =   "+a);
        System.out.println("B value =   "+b);
        System.out.println("\n");
        
    }
    public static void display(BigInteger a, BigInteger b, BigInteger d)
    {
        System.out.println("Integer Values :");
        System.out.println("A value =   "+a);
        System.out.println("B value =   "+b);
        System.out.println("D value =   "+d);
        System.out.println("\n");
        
    }
    public static void display(BigInteger a, BigInteger b, BigInteger[] DXY)
    {
        System.out.println("Integer Values :");
        System.out.println("A value =   "+a);
        System.out.println("B value =   "+b);
        System.out.println("D value =   "+DXY[0]);
        System.out.println("X value =   "+DXY[1]);
        System.out.println("Y value =   "+DXY[2]);
        System.out.println("line    =   xa + yb = d");
        System.out.println("Line equation = ("+DXY[1] + " x " + a + ") + (" + DXY[2] + " x " + b + ") = " + DXY[0]);
        System.out.println("\n");
        
    }
    public static void display( BigInteger aInverse)
    {
        System.out.println("Integer Values :");
        System.out.println("A^-1 value  =   "+aInverse);
        System.out.println("\n");
        
    }

    public static BigInteger encrypt(BigInteger key, BigInteger message)
    {
        BigInteger cipher = BigInteger.ZERO;

        cipher = message.multiply(key);


        return cipher;
    }

    public static BigInteger decrypt(BigInteger key, BigInteger ciper)
    {
        BigInteger message = BigInteger.ZERO;

        message = ciper.divide(key);

        return message;
    }

}