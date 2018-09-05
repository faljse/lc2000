package info.faljse;

import bt.data.Bitfield;
import bt.data.ChunkDescriptor;
import bt.data.ChunkVerifier;
import bt.data.DataRange;
import bt.data.digest.Digester;

import java.util.List;

public class MyChunkVerifier implements ChunkVerifier {
    public MyChunkVerifier(Digester digester, int numOfHashingThreads) {
        System.out.println("mychunk");
    }



    @Override
    public boolean verify(List<ChunkDescriptor> chunks, Bitfield bitfield) {
        ChunkDescriptor[] arr = chunks.toArray(new ChunkDescriptor[chunks.size()]);

        return false;
    }

    @Override
    public boolean verify(ChunkDescriptor chunk) {
        DataRange data = chunk.getData();
        byte[] bytes = data.getBytes();

        return false;
    }
}
