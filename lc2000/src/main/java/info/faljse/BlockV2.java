package info.faljse;

import com.google.crypto.tink.KeysetHandle;

public class BlockV2 {
    public int id;
    public byte[] payload;
    public byte[] signature;
    public KeysetHandle publicHandle;
}
