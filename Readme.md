# Kerberos Documentation
 --- 
 ## Introduction

In this brief Kerberos protocol will be explained and analysed, it's limitations and strengths will also be examined.The title Kerberos was chosen because the process uses three entities mimicking the greek legend it's named after **Cerberus** the three headed gaurd dog of the underworld.
Kerberos was designed by MIT and is used in computer authentication protocol and is a form of symmetric key cryptography. Symmetric key's were used because when designing Kerberos RSA had patented assymetric key cryptography or public key cryptography. 
The three entities invloved in our explanation of the Kerberos protocol are **Alice** the user trying to access some inforamtion on a **Resource Server** and the **Key Distribution Center**(KDC).

To provide a more scalable system Kerberos' developers included the concept of a KDC. A KDC is a trusted third party that manages secret keys throughout a network. The KDC shares a secret key with every entity on the network. 



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

2.  **The Authentication Server**

    - Authentication Service(AS)
    This service grants ticket-granting tickets that can then be used by the ticket granting service. The authenticating service ensures that the details supplied are correct and allows the user to use the TGS by offering up the TGT.

    The AS confirms the users credentials. The AS the can grant the user a Ticket this ticket can be then used later by the user. Using the users name and password the AS can authenticate the users, a timestamp is also used to ensure the user's request wasn't tampered.

3. **The TGT Received**

    The server has granted the TGT to a client the Server has encrypted the ticket with a key only known to the server. This allows only the server to see the contents.
    Alice wants to access Bob's files. So she sends her TGT to the KDC. Since the server received a TGT the server knows the user has been authenticated. 

4. **The KDC provides a TST**
    
    The KDC has a request from ALice for Bob's files. The KDC knows Bob's passwords and it can encrypt a Ticket Session Ticket with Bob's secret key and encased this is then enclosed in the TGT again.

5. **Alice Receives TST**
    Alice can now remove the TGT layer from the message and is left with the TST
    Bob can then send this TST to the KDC which is encrypted with Bob's Secret key and the KDC's master key. 
6. **Bob Receives TST**
    Bob which can be sent to the KDC to get decrypt the first layer of encryption on the message. This verifies that the ticket has been sent from the KDC. Then Bob can now decrypt the request and this can then be used to create a shared key between Alice and Bob.

--------

# Goals of The System

In this example the code shows how a server can reduce the number of keys needed on a network if there are fifty clients on a network. For each of these clients to communicate with eachother they would need 2500 keys to comunicate securely. These large cryptographic keys can be taxing on a network and the use of the Kerberos protocol can reduce the number of keys needed to just one for each entity on the network.

_______
# Implementation

The code first creates a user which has a user name and a secret message(BigInteger) these users then create a shared secret key with the server, using diffie-hellman, which stores this key in a `HashMap` which contains the usename and this shared key.

The user can then decide to share their encrypted message with the server these messages are stored in another `HashMap` containing the username and their encrypted message. When another user requests another users 'message' the server then decrypts the stored message(encrypted) and decrypts it with the linked users key, then once again encrypts the data with the user's key, that made the request and sends the encrypted data back to that user.

The time sensitivity of the keys are taken care of by the length the server is running. When the server stops all keys are lost at that point 

## Potential imrpovements

The use of the private message could then be used to create some shared information between the two users. This could open the potential to end-to-end encryption with the two users sharing some inforamtion and this being used to generate another shared key.

The storgae of a timeset for each user could have been added giving the user a set ammount of time they can have their key stored on the server.

Verification of the user could have been added using a password to the user class .This would mean the username and password would have to be stored in a database.

## Limitations of the code

The Diffie hellman exchange that occurs between the user and the server doesn't use a guaranteed generator. This could limit the possible outcomes of the keys produced which would make the system weaker than if a generator was chosen.

