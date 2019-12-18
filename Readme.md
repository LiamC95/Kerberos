# Kerberos Documentation
 --- 
 ## Introduction

In this brief Kerberos protocol will be explained and analysed, it's limitations and strengths will also be examined.The title Kerberos was chosen because the process uses three entities mimicking the greek legend it's named after **Cerberus** the three headed gaurd dog of the underworld.
Kerberos was designed by MIT and is used in computer authentication protocol and is a form of symmetric key cryptography. Symmetric key's were used because when designing Kerberos RSA had patented assymetric key cryptography or public key cryptography. 
The three entities invloved in our explanation of the Kerberos protocol are **Alice** the user trying to access some inforamtion on a **Resource Server** and the **Key Distribution Center**(KDC).



## Alice uses Kerberos

1.  **Alice's Kerberos authentication**

    - To authenticate Alice must pass her name and a current timestamp(**M**),known as the authenticator. M is encrypted using a symmetric key(**K**) created and shared with the Resource server and K is used to enrypt and decrypt M.

    `CT = Encrypt(K,CT)`

    - The (CT) is passed to the resource server and decrypted using **K**.

    M = Decrypt(K,CT)

    - Now that the resource server has Alices details they are then authenticated to the server.

    The Resource server can authenticates Alice's information using the timestamp and comparing it to the local time. If there is a big enough difference between the two times Alice will not be authenticated.
    This process of Alice authentication herself to the server can also be done in reverse with the server authenticating itself to Alice. 
    A major flaw with this step is the distribution of the K, if an attacker was to get access to the K step 1 would be useless. 

2.  **The KDC's Role in the protocol**

    To provide a more scalable system Kerberos' developers included the concept of a KDC. A KDC is a trusted third party that manages secret keys throughout a network. The KDC shares a secret key with every entity on the network called a master key. 
    The KDC itself is provides two services.
    
- Authentication Service
    This service grants ticket-granting tickets that can then be used by the ticket granting service. The authenticating service ensures that the details supplied are correct and allows the user to use the TGS by offering up the TGT.

- Ticket Granting Service    
    
