package info.faljse;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.hybrid.HybridKeyTemplates;

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Base64;

public class BlockV1 {
    public int id;
    public byte[] payload;
    public byte[] signature;
    public KeysetHandle publicHandle;
}
