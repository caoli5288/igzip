package com.github.caoli5288.igzip;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IGzip implements Closeable {

    public static final int NO_COMPRESS = 0;
    public static final int BEST_SPEED = 1;
    public static final int BEST_COMPRESSION = 3;

    public static final int FMT_DEFLATE = 0;
    public static final int FMT_GZIP = 1;
    public static final int FMT_GZIP_NO_HEADER = 2;
    public static final int FMT_Z = 3;
    public static final int FMT_Z_NO_HEADER = 4;

    public static final int NO_FLUSH = 0;
    public static final int SYNC_FLUSH = 2;
    public static final int FULL_FLUSH = 3;

    static {
        String system = System.getProperties().getProperty("os.name", "");
        if (system.startsWith("Lin")) {
            if (System.getProperties().getProperty("os.arch", "").equals("amd64")) {
                try {
                    loadLibrary("libigzip_jni.so", File.createTempFile("libigzip_jni_", ".so"));
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        if (system.startsWith("Win")) {
            if (System.getProperties().getProperty("os.arch", "").equals("amd64")) {
                try {
                    loadLibrary("igzip_jni.dll", File.createTempFile("igzip_jni_", ".dll"));
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    private long zstream;
    private int level;
    private int gzipFlag;
    private int flushFlag;

    public IGzip() {
        this(BEST_SPEED, FMT_Z, NO_FLUSH);
    }

    public IGzip(int level, int gzipFlag, int flushFlag) {
        zstream = alloc(null, -1);// Use default
        if (zstream == 0) {
            throw new IllegalStateException("Bad zstream return code");
        }
        this.level = level;
        this.gzipFlag = gzipFlag;
        this.flushFlag = flushFlag;
    }

    public void setLevel(int level) {
        this.level = Math.min(Math.max(NO_COMPRESS, level), BEST_COMPRESSION);
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
        ensureOpen();
        if (inOff < 0 || inLen < 0 || inOff > inArray.length - inLen || outOff < 0 || outLen < 0 || outOff > outArray.length - outLen) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return compress(init(zstream, level, gzipFlag, flushFlag), inArray, inOff, inLen, outArray, outOff, outLen);
    }

    public int compress(long inRef, int inAvail, long outRef, int outAvail) {
        ensureOpen();
        return compress(init(zstream, level, gzipFlag, flushFlag), inRef, inAvail, outRef, outAvail);
    }

    public void close() {
        if (zstream != 0) {
            free(zstream);
            zstream = 0;
        }
    }

    @Override
    protected void finalize() {
        close();
    }

    private void ensureOpen() {
        if (zstream == 0) {
            throw new IllegalStateException("closed");
        }
    }

    private static native int compress(long ref, byte[] inArray, int inOff, int inLen, byte[] outArray, int outOff, int outLen);

    private static native long init(long ref, int level, int gzipFlag, int flushFlag);

    private static native long alloc(byte[] deflateBuf, int len);

    private static native void free(long ptr);

    private static native int compress(long ref, long inRef, int inAvail, long outRef, int outAvail);

    private static void loadLibrary(String res, File tmp) throws IOException {
        InputStream lib = IGzip.class.getClassLoader().getResourceAsStream(res);
        if (lib == null) {
            throw new IOException("Library not found: " + res);
        }
        tmp.deleteOnExit();
        FileOutputStream tmpStr = new FileOutputStream(tmp);
        while (lib.available() != 0) {
            tmpStr.write(lib.read());
        }
        tmpStr.flush();
        tmpStr.close();
        lib.close();
        System.load(tmp.getAbsolutePath());
    }
}
