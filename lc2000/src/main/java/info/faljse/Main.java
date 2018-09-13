package info.faljse;

import bt.Bt;
import bt.BtClientBuilder;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.portmapping.upnp.UpnpPortMapperModule;
import bt.protocol.crypto.EncryptionPolicy;
import bt.runtime.BtClient;
import bt.runtime.BtRuntime;
import bt.runtime.Config;
import bt.torrent.selector.PieceSelector;
import bt.torrent.selector.SequentialSelector;
import com.google.inject.Module;
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
    private BtRuntime runtime;
    private BtClient client;

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
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        // new Main().bla();
    }

    private void crypto() throws GeneralSecurityException, IOException {
       Block b=new Block();
       KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        KeyPair pair = keyGen.generateKeyPair();
        //b.init(pair.getPublic());


    }



    private void bla() {
        // enable multithreaded verification of torrent data
        Config config = new Config() {
            @Override
            public int getNumOfHashingThreads() {
                return Runtime.getRuntime().availableProcessors() * 2;
            }



            @Override
            public EncryptionPolicy getEncryptionPolicy() {
                return EncryptionPolicy.PREFER_PLAINTEXT;
            }

//            @Override
//            public InetAddress getAcceptorAddress() {
//
//                InetAddress a = null;
//                try {
//                    a = InetAddress.getByName("10.16.119.131");
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                }
//                return a;
//            }
        };

// enable bootstrapping from public routers
        Module dhtModule = new DHTModule(new DHTConfig() {
            @Override
            public boolean shouldUseRouterBootstrap() {
                return true;
            }



        });
        Module upnpModule = new UpnpPortMapperModule();


// get download directory
        Path targetDirectory = new File(".").toPath();

// create file system based backend for torrent data
        Storage storage = new FileSystemStorage(targetDirectory);
        Module m = binder -> binder.bind(OverridenServiceModule.class).to(OverridenServiceModule.class);

        this.runtime = BtRuntime.builder(config)
                .module(upnpModule)
                .module(dhtModule)
                .autoLoadModules()
                .module(new OverridenServiceModule())
                .disableAutomaticShutdown()
                .build();
        PieceSelector selector = SequentialSelector.sequential();

        BtClientBuilder clientBuilder = Bt.client(runtime)
                .storage(storage)
                .selector(selector)


//                .selector(new PieceSelector() {
//                    int i=1;
//                    @Override
//                    public Stream<Integer> getNextPieces(PieceStatistics pieceStatistics) {
//
//                        return Stream.of(i++);
//
//                    }
//                })
 .magnet("magnet:?xt=urn:btih:c792fb28a1af64fffce387d5baa0d604d1261cbc&dn=Two.Point.Hospital.v1.0.20828.Update-SKIDROW&tr=http%3A%2F%2Ftracker.trackerfix.com%3A80%2Fannounce&tr=udp%3A%2F%2F9.rarbg.me%3A2710&tr=udp%3A%2F%2F9.rarbg.to%3A2710")
//                .torrent(() -> {
//                    try {
//                        return runtime.service(IMetadataService.class).fromInputStream(new FileInputStream("Fiat Punto 176 FEDERBEIN A. U. E..pdf.torrent"));
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                        return null;
//                    }
//                })

                ;



        //magnet:?xt=urn:btih:850c0faf2225cdffb06abf204bba05f0489a445b&dn=Fiat%20Punto%20176%20FEDERBEIN%20A.%20U.%20E..pdf&tr=udp%3a%2f%2ftracker.coppersurfer.tk%3a6969%2fannounce&tr=udp%3a%2f%2ftracker.open-internet.nl%3a6969%2fannounce&tr=udp%3a%2f%2fexodus.desync.com%3a6969%2fannounce&tr=udp%3a%2f%2ftracker.opentrackr.org%3a1337%2fannounce&tr=udp%3a%2f%2ftracker.internetwarriors.net%3a1337%2fannounce&tr=udp%3a%2f%2f9.rarbg.to%3a2710%2fannounce&tr=udp%3a%2f%2fpublic.popcorn-tracker.org%3a6969%2fannounce&tr=udp%3a%2f%2ftracker.vanitycore.co%3a6969%2fannounce&tr=udp%3a%2f%2ftracker.mg64.net%3a6969%2fannounce&tr=udp%3a%2f%2ftracker.tiny-vps.com%3a6969%2fannounce&tr=udp%3a%2f%2ftracker.cypherpunks.ru%3a6969%2fannounce&tr=udp%3a%2f%2fbt.xxx-tracker.com%3a2710%2fannounce&tr=udp%3a%2f%2ftracker.torrent.eu.org%3a451%2fannounce&tr=udp%3a%2f%2fthetracker.org%3a80%2fannounce&tr=udp%3a%2f%2fretracker.lanta-net.ru%3a2710%2fannounce&tr=udp%3a%2f%2fopen.stealth.si%3a80%2fannounce&tr=http%3a%2f%2fretracker.telecom.by%3a80%2fannounce&tr=udp%3a%2f%2ftracker1.itzmx.com%3a8080%2fannounce&tr=udp%3a%2f%2ftracker.uw0.xyz%3a6969%2fannounce&tr=udp%3a%2f%2ftracker.iamhansen.xyz%3a2000%2fannounce

        this.client = clientBuilder.build();
        //.magnet("magnet:?xt=urn:btih:a53e3226a59f4032eefa26cefe6a9bdee17aa970&dn=KMSpico+10.1.8+FINAL+%2B+Portable+%28Office+and+Windows+10+Activator&tr=udp%3A%2F%2Ftracker.leechers-paradise.org%3A6969&tr=udp%3A%2F%2Fzer0day.ch%3A1337&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=udp%3A%2F%2Ftracker.coppersurfer.tk%3A6969&tr=udp%3A%2F%2Fexodus.desync.com%3A6969");


// launch
        client.startAsync(state -> {
            System.out.println(state.getDownloaded());
            System.out.println(state.getPiecesComplete());
            System.out.println("s:" +state.getConnectedPeers().size());
        }, 1000).join();
    }
}
