package info.faljse;

import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.config.TinkConfig;
import com.google.crypto.tink.signature.SignatureKeyTemplates;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;

public class Main {
    public static Chain chain=new Chain();
    public static void main(String args[]) {
        try {
            TinkConfig.register();
            new WebIf();
            crypto();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private static void crypto() throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, SignatureException, NoSuchPaddingException {

        try {
            KeysetHandle pri = KeysetHandle.generateNew(SignatureKeyTemplates.ECDSA_P256);
            KeysetHandle pub = pri.getPublicKeysetHandle();
            // KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            Block b1=new Block();
            // KeyPair pair1 = keyGen.generateKeyPair();
            KeysetHandle privateKeysetHandle = KeysetHandle.generateNew(
                    SignatureKeyTemplates.ECDSA_P256);
            // 2. Get the primitive.
            //PublicKeySign signer = privateKeysetHandle.getPrimitive(PublicKeySign.class);
            // pai

            // b1.init(pair1.getPublic(), pair1.getPrivate(), "erster".getBytes());
            // chain.blocks.add(b1);
            // Block b2=new Block();
            // KeyPair pair2 = keyGen.generateKeyPair();
            // b2.init(pair2.getPublic(), pair2.getPrivate(), "zweiter".getBytes());
            //  chain.blocks.add(b2);


        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }


    }
}
