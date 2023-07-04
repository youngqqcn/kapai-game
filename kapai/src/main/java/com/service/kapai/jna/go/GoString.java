package com.service.kapai.jna.go;

import com.sun.jna.Structure;

@Structure.FieldOrder({"p", "n"})
public class GoString extends Structure {
    public String p;
    public long n;

    public static class ByValue extends GoString implements Structure.ByValue {

        public static GoString.ByValue of(String str) {
            GoString.ByValue gs = new GoString.ByValue();
            gs.p = str;
            gs.n = str.length();
            return gs;
        }
    }
}