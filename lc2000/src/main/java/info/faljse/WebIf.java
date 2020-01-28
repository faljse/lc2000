package info.faljse;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.google.crypto.tink.*;
import com.google.crypto.tink.integration.awskms.AwsKmsClient;
import com.google.crypto.tink.signature.PublicKeyVerifyFactory;

import java.io.*;
import java.security.GeneralSecurityException;

import static spark.Spark.*;

public class WebIf {
    private static final int PORT = 9090;

    public WebIf() throws IOException {
        port(PORT);
        addMappings();
        awaitInitialization();
        System.out.println("\nRunning! Point your browers to http://localhost:" + PORT + "/ \n");
    }

    public void addMappings() {
        get("/", (request, response) -> {return "JO!";});
        post("/addBlock", (req, res) -> {
            var json=Json.parse(req.body()).asObject();
            var payload = json.getString("payload","");
            var signature = json.getString("signature","");
            var nextPublicKey = json.getString("nextPublicKey","");

            BlockV1 b=new BlockV1();
            b.payload=payload.getBytes();
            b.signature=signature.getBytes();
            b.nextPublicKey= CleartextKeysetHandle.read(
                    JsonKeysetReader.withBytes(nextPublicKey.getBytes()));
            addBlock(b);


            return "";
        });
    }

    private void addBlock(BlockV1 b) {
        if(Main.chain.blocks.size()==0) {
            Main.chain.blocks.add(b);
            return;
        }
        int bc=Main.chain.blocks.size();
        BlockV1 prev = Main.chain.blocks.get(bc-1);

        try {
            PublicKeyVerify verifier = PublicKeyVerifyFactory.getPrimitive(prev.nextPublicKey);
            verifier.verify(b.signature, b.payload);
            System.out.println("verified");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }
}