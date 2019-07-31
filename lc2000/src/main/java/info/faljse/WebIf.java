package info.faljse;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
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
    }
}