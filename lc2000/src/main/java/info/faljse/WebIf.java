package info.faljse;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetWriter;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.signature.SignatureKeyTemplates;
import spark.Redirect;
import spark.Route;

import java.io.*;
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
        staticFiles.externalLocation("C:\\Users\\marti\\IdeaProjects\\lc2000\\lc2000\\webroot\\");
        redirect.get("","/index.html", Redirect.Status.MOVED_PERMANENTLY);
        get("/newKey", ((request, response) -> {
            KeysetHandle privateKeysetHandle = KeysetHandle.generateNew(
                    SignatureKeyTemplates.ECDSA_P256);
            ByteArrayOutputStream bos=new ByteArrayOutputStream();

            CleartextKeysetHandle.write(privateKeysetHandle, JsonKeysetWriter.withOutputStream(bos));
            KeysetHandle publicKeysetHandle =
                    privateKeysetHandle.getPublicKeysetHandle();
            CleartextKeysetHandle.write(publicKeysetHandle, JsonKeysetWriter.withOutputStream(bos));
            return bos.toString();

            // KeysetHandle publicKeysetHandle =
            //        privateKeysetHandle.getPublicKeysetHandle();
        }));
        get("/", (request, response) -> {return "JO!";});
        post("/addBlock", (req, res) -> {
            var json=Json.parse(req.body()).asObject();
            JsonArray blocks = Json.array();
            for (Block b : Main.chain.blocks) {
                JsonObject o = new JsonObject();
                o.add("data", b.payload.toString());
                blocks.add(o);
            }
            return "";
        });
        //staticFiles.location("/static");
    }
}