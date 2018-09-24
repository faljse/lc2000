package info.faljse;


import org.nanohttpd.util.ServerRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

public class Main {

    public static Chain chain=new Chain();


    public static void main(String args[]) {
        // System.setProperty("java.net.preferIPv4Stack" , "true");

        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerRunner.run(WebIf.class);
            }
        }).start();
        try {
            new Main().crypto();
        } catch ( Exception e) {
            e.printStackTrace();
        }

        // new Main().bla();
    }

    private void crypto() throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, SignatureException, NoSuchPaddingException {
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
