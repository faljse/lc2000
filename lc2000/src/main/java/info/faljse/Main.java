package info.faljse;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.portmapping.upnp.UpnpPortMapperModule;
import bt.runtime.BtClient;
import bt.runtime.Config;
import com.google.inject.Module;
import org.nanohttpd.util.ServerRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String args[]) {
        ServerRunner.run(WebIf.class);

        new Main().bla();
    }

    private void bla() {
        // enable multithreaded verification of torrent data
        Config config = new Config() {
            @Override
            public int getNumOfHashingThreads() {
                return Runtime.getRuntime().availableProcessors() * 2;
            }
        };

// enable bootstrapping from public routers
        Module dhtModule = new DHTModule(new DHTConfig() {
            @Override
            public boolean shouldUseRouterBootstrap() {
                return true;
            }
        });

        Module upnpModule =new UpnpPortMapperModule();

// get download directory
        Path targetDirectory = new File("~/Downloads").toPath();

// create file system based backend for torrent data
        Storage storage = new FileSystemStorage(targetDirectory);

// create client with a private runtime
        BtClient client = Bt.client()
                .config(config)
                .storage(storage)
                .magnet("magnet:?xt=urn:btih:af0d9aa01a9ae123a73802cfa58ccaf355eb19f1")
                .autoLoadModules()
                .module(dhtModule)
                .module(upnpModule)
                .stopWhenDownloaded()
                .build();

// launch
        client.startAsync().join();
    }
}
