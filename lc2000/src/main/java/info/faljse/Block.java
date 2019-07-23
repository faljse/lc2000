package info.faljse;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.hybrid.HybridKeyTemplates;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Base64;

public class Block {
    public int id;
    public byte[] payload;
    public byte[] signature;
    public KeysetHandle publicHandle;
    public PublicKey nextRSAEncryptedAesKey;
    public byte[] nextAesEncryptedPrivateKey;



    public void asd() {
        try {
            KeysetHandle keysetHandle = KeysetHandle.generateNew(
                    HybridKeyTemplates.ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM);
            publicHandle=keysetHandle.getPublicKeysetHandle();
            KeysetHandle nextPair=KeysetHandle.generateNew(HybridKeyTemplates.ECIES_P256_HKDF_HMAC_SHA256_AES128_GCM);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

    }

    public String toJSON() {
        Base64.Encoder encoder = Base64.getEncoder().withoutPadding();
        JsonObject jo=new JsonObject();
        jo.add("id", id);
        jo.add("data", encoder.encodeToString(payload));
        jo.add("signature", encoder.encodeToString(signature));
        // jo.add("nextPubKey", encoder.encodeToString(nextPubKey.getEncoded()));
      //  jo.add("nextRSAEncryptedAesKey", encoder.encodeToString(nextRSAEncryptedAesKey));
        jo.add("nextAesEncryptedPrivateKey", encoder.encodeToString(nextAesEncryptedPrivateKey));
        return jo.toString();
    }

    public void fromJSON(String json){
        Base64.Decoder decoder = Base64.getDecoder();
        JsonObject jo = Json.parse(json).asObject();
        id=jo.getInt("id",0);
//        data=decoder.decode(jo.getString("data",""));
//        signature=decoder.decode(jo.getString("signature",""));
//        X509EncodedKeySpec spec =
//                new X509EncodedKeySpec(decoder.decode(jo.getString("nextPubKey","")));
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        this.nextPubKey=kf.generatePublic(spec);
    }

}
