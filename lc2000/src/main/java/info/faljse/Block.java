package info.faljse;

import javax.crypto.*;
import java.security.*;


public class Block {

    public int id;
    public byte[] data;
    public byte[] signature;
    public PublicKey nextPubKey;
    public byte[] nestRSAEncryptedAesKey;
    public byte[] nextAesEncryptedPrivateKey;
    public final static int AES_KEYSIZE=256;

    public void init(PublicKey nextParticipant, PrivateKey signKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, SignatureException {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privKey = keyPair.getPrivate();
        this.nextPubKey=keyPair.getPublic();


        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(AES_KEYSIZE); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        this.nextAesEncryptedPrivateKey = aesCipher.doFinal(privKey.getEncoded());

        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, nextParticipant);
        this.nestRSAEncryptedAesKey = rsaCipher.doFinal(secKey.getEncoded());

        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(signKey);
        privateSignature.update(data); //TODO: sign whole block
        this.signature = privateSignature.sign();
    }

}
