package info.faljse;

import javax.crypto.*;
import java.security.*;


public class Block {

    public int id;
    public String nextPubKey;
    public String nestRSAEncryptedAesKey;
    public String nextAesEncryptedPrivateKey;
    public final static int AES_KEYSIZE=256;

    public void init(PublicKey nextParticipant) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privKey = keyPair.getPrivate();
        PublicKey pubKey = keyPair.getPublic();

        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(AES_KEYSIZE); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] aesEncryptedPrivateKey = aesCipher.doFinal(privKey.getEncoded());


        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, nextParticipant);
        byte[] encryptedKey = rsaCipher.doFinal(secKey.getEncoded());

        this.nextAesEncryptedPrivateKey=aesEncryptedPrivateKey.toString();
        this.nestRSAEncryptedAesKey=encryptedKey.toString();
        this.nextPubKey=pubKey.toString();
    }

}
