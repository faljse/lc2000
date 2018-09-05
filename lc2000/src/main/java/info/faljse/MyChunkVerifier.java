package info.faljse;

import bt.data.Bitfield;
import bt.data.ChunkDescriptor;
import bt.data.ChunkVerifier;
import bt.data.digest.Digester;

import java.util.List;

public class MyChunkVerifier implements ChunkVerifier {
    public MyChunkVerifier(Digester digester, int numOfHashingThreads) {
    }

    @Override
    public boolean verify(List<ChunkDescriptor> chunks, Bitfield bitfield) {
        return false;
    }

    @Override
    public boolean verify(ChunkDescriptor chunk) {
        return false;
    }
}
