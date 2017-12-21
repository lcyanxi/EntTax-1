package com.douguo.dc.util;

public interface Serializable {
    byte[] serialize();
    void unserialize(byte[] ss);
}