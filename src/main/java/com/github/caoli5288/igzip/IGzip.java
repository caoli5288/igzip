package com.github.caoli5288.igzip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IGzip {

    public static final boolean LIBRARY_READY;

    public static final int BEST_SPEED = 1;
    public static final int BEST_COMPRESSION = 9;

    public static final int FMT_DEFLATE = 0;
    public static final int FMT_GZIP = 1;
    public static final int FMT_GZIP_NO_HEADER = 2;
    public static final int FMT_Z = 3;
    public static final int FMT_Z_NO_HEADER = 4;

    public static final int NO_FLUSH = 0;
    public static final int SYNC_FLUSH = 2;
    public static final int FULL_FLUSH = 3;

    static {
        boolean b = false;
        if (System.getProperties().getProperty("os.name", "").equals("Linux") && System.getProperties().getProperty("os.arch", "").equals("amd64")) {
            try {
                loadLibrary();
                b = true;
            } catch (IOException ignore) {
            }
        }
        LIBRARY_READY = b;
    }

    private final byte[] deflateBuf;
    private int level;
    private int gzipFlag;
    private int flushFlag;

    public IGzip() {
        this(BEST_SPEED, FMT_Z, NO_FLUSH);
    }

    public IGzip(int level, int gzipFlag, int flushFlag) {
        if (!LIBRARY_READY) throw new IllegalStateException("library not ready");
        this.deflateBuf = new byte[256 * 1024];
        this.level = level;
        this.gzipFlag = gzipFlag;
        this.flushFlag = flushFlag;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setGzipFlag(int gzipFlag) {
        this.gzipFlag = gzipFlag;
    }

    public void setFlushFlag(int flushFlag) {
        this.flushFlag = flushFlag;
    }

    public int compress(byte[] inArray, byte[] outArray) {
        return compress(inArray, 0, inArray.length, outArray, 0, outArray.length);
    }

    public int compress(byte[] inArray, int inOff, int inLen, byte[] outArray, int outOff, int outLen) {
        if (inOff < 0 || inLen < 0 || inOff > inArray.length - inLen || outOff < 0 || outLen < 0 || outOff > outArray.length - outLen) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return compress(init(deflateBuf, deflateBuf.length, level, gzipFlag, flushFlag), inArray, inOff, inLen, outArray, outOff, outLen);
    }

    private static native int compress(long ref, byte[] inArray, int inOff, int inLen, byte[] outArray, int outOff, int outLen);

    private static native long init(byte[] deflateBuf, int len, int level, int gzipFlag, int flushFlag);

    public int compress(long inRef, int inAvail, long outRef, int outAvail) {
        return compress(init(deflateBuf, deflateBuf.length, level, gzipFlag, flushFlag), inRef, inAvail, outRef, outAvail);
    }

    private static native int compress(long ref, long inRef, int inAvail, long outRef, int outAvail);

    private static void loadLibrary() throws IOException {
        File tmp = File.createTempFile("libigzip_jni_", ".so");
        tmp.deleteOnExit();
        InputStream lib = IGzip.class.getClassLoader().getResourceAsStream("libigzip_jni.so");
        FileOutputStream tmpStr = new FileOutputStream(tmp);
        while (lib.available() != 0) {
            tmpStr.write(lib.read());
        }
        tmpStr.flush();
        tmpStr.close();
        lib.close();
        System.load(String.valueOf(tmp));
    }
}
