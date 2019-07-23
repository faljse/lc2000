package info.faljse;

import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class Chain {

    public List<Block> blocks=new ArrayList<>();

    public void add(Block b) {
        if(blocks.isEmpty()) {
            blocks.add(b);
            return;
        }
        Block last=blocks.get(blocks.size()-1);
        if(b.id!=last.id+1){
            throw new RuntimeException("wrong block id");
        }
        try {
            if (verify(b.payload, b.signature, last.nextRSAEncryptedAesKey)) {
                return;
            }
            else throw new RuntimeException("Signature doesnt match");

        } catch (SignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static boolean verify(byte[] data, byte[] signature, PublicKey publicKey) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(data);
        return publicSignature.verify(signature);
    }
}
