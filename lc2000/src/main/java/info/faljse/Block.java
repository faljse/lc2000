package info.faljse;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import javafx.beans.binding.IntegerBinding;

import javax.crypto.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class Block {

    public int id;
    public byte[] data;
    public byte[] signature;
    public PublicKey nextPubKey;
    public byte[] nextRSAEncryptedAesKey;
    public byte[] nextAesEncryptedPrivateKey;
    public final static int AES_KEYSIZE=256;

    public void init(PublicKey nextParticipant, PrivateKey signKey, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, SignatureException {
        this.data=data;
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
        this.nextRSAEncryptedAesKey = rsaCipher.doFinal(secKey.getEncoded());

        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(signKey);
        privateSignature.update(data); //TODO: sign whole block
        this.signature = privateSignature.sign();
    }

    public String toJSON() {
        Base64.Encoder encoder = Base64.getEncoder().withoutPadding();
        JsonObject jo=new JsonObject();
        jo.add("id", id);
        jo.add("data",encoder.encodeToString(data));
        jo.add("signature", encoder.encodeToString(signature));
        jo.add("nextPubKey",encoder.encodeToString(nextPubKey.getEncoded()));
        jo.add("nextRSAEncryptedAesKey", encoder.encodeToString(nextRSAEncryptedAesKey));
        jo.add("nextAesEncryptedPrivateKey", encoder.encodeToString(nextAesEncryptedPrivateKey));
        return jo.toString();
    }

    public void fromJSON(String json) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Base64.Decoder decoder = Base64.getDecoder();
        JsonObject jo = Json.parse(json).asObject();
        id=jo.getInt("id",0);
        data=decoder.decode(jo.getString("data",""));
        signature=decoder.decode(jo.getString("signature",""));
        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(decoder.decode(jo.getString("nextPubKey","")));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        this.nextPubKey=kf.generatePublic(spec);
    }

}
