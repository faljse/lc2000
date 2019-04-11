package info.faljse;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;

public class Main {
    public static Chain chain=new Chain();
    public static void main(String args[]) {
        try {
            new WebIf();
            crypto();
        } catch (IOException | NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | BadPaddingException | SignatureException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    private static void crypto() throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, SignatureException, NoSuchPaddingException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        Block b1=new Block();
        KeyPair pair1 = keyGen.generateKeyPair();
        b1.init(pair1.getPublic(), pair1.getPrivate(), "erster".getBytes());
        chain.blocks.add(b1);
        Block b2=new Block();
        KeyPair pair2 = keyGen.generateKeyPair();
        b2.init(pair2.getPublic(), pair2.getPrivate(), "zweiter".getBytes());
        chain.blocks.add(b2);
    }
}
