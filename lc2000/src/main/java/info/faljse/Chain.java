package info.faljse;

import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class Chain {

    public List<BlockV1> blocks=new ArrayList<>();

    public void add(BlockV1 b) {
        if(blocks.isEmpty()) {
            blocks.add(b);
            return;
        }
        BlockV1 last=blocks.get(blocks.size()-1);
        if(b.id!=last.id+1){
            throw new RuntimeException("wrong block id");
        }

    }

}
