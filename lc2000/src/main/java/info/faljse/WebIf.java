package info.faljse;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import org.nanohttpd.protocols.http.IHTTPSession;
import org.nanohttpd.protocols.http.response.IStatus;
import org.nanohttpd.protocols.http.response.Response;
import org.nanohttpd.protocols.http.response.Status;
import org.nanohttpd.router.RouterNanoHTTPD;

import java.io.*;
import java.util.Map;


public class WebIf extends RouterNanoHTTPD {
    private static final int PORT = 9090;

    public WebIf() throws IOException {
        super(PORT);
        addMappings();
        System.out.println("\nRunning! Point your browers to http://localhost:" + PORT + "/ \n");
    }

    @Override
    public void addMappings() {
        super.addMappings();


        addRoute("/blocks", BlockHandler.class);
        addRoute("/user/help", BlockHandler.class);
        addRoute("/user/:id", BlockHandler.class);
        addRoute("/general/:param1/:param2", GeneralHandler.class);
        addRoute("/photos/:customer_id/:photo_id", null);
        addRoute("/test", String.class);
        addRoute("/interface", UriResponder.class); // this will cause an error
        // when called
        addRoute("/toBeDeleted", String.class);
        removeRoute("/toBeDeleted");
        addRoute("/stream", StreamUrl.class);

        addRoute("/static(.)+", StaticPageTestHandler.class, new File("webroot/").getAbsoluteFile());
        addRoute("/", StaticPageTestHandler.class, new File("webroot/index.html").getAbsoluteFile());
    }

    static public class StreamUrl extends DefaultStreamHandler {

        @Override
        public String getMimeType() {
            return "text/plain";
        }

        @Override
        public IStatus getStatus() {
            return Status.OK;
        }

        @Override
        public InputStream getData() {
            return new ByteArrayInputStream("a stream of data ;-)".getBytes());
        }

    }

    public static class StaticPageTestHandler extends StaticPageHandler {


        @Override
        protected BufferedInputStream fileToInputStream(File fileOrdirectory) throws IOException {
            if ("exception.html".equals(fileOrdirectory.getName())) {
                throw new IOException("trigger something wrong");
            }
            return super.fileToInputStream(fileOrdirectory);
        }
    }

    public static class BlockHandler extends DefaultHandler {

        @Override
        public String getMimeType() {
            return MIME_PLAINTEXT;
        }

        @Override
        public String getText() {
            return "not implemented";
        }

        @Override
        public IStatus getStatus() {
            return Status.OK;
        }

        @Override
        public Response get(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {

            JsonArray blocks = Json.array();
            for(Block b: Main.chain.blocks) {
                JsonObject o = new JsonObject();
                o.add("data", b.data.toString());
                blocks.add(o);
            }

            return Response.newFixedLengthResponse(blocks.toString());
        }
    }
}