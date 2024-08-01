package com.github.caoli5288.igzip;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

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
    public static final int SYNC_FLUSH = 1;
    public static final int FULL_FLUSH = 2;

    static {
        try {
            String libName = System.mapLibraryName("igzip_jni");
            InputStream s = IGzip.class.getClassLoader().getResourceAsStream(libName);
            Objects.requireNonNull(s, String.format("Can't found library %s", libName));
            try {
                File tmp = File.createTempFile("igzip_jni", ".tmp");
                tmp.deleteOnExit();
                Files.copy(s, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.load(tmp.getAbsolutePath());
            } finally {
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long igZip;
    private int level;
    private int gzipFlag;
    private int flushFlag;
    private boolean flagModify;

    public IGzip() {
        this(BEST_SPEED, FMT_Z, NO_FLUSH);
    }

    public IGzip(int level, int gzipFlag, int flushFlag) {
        igZip = alloc(null, -1);// Use default
        if (igZip == 0) {
            throw new IllegalStateException("Native allocation failed");
        }
        this.level = level;
        this.gzipFlag = gzipFlag;
        this.flushFlag = flushFlag;
        init(igZip, level, gzipFlag, flushFlag);
    }

    public void setLevel(int level) {
        if (this.level != level) {
            this.level = Math.min(Math.max(NO_COMPRESS, level), BEST_COMPRESSION);
            flagModify = true;
        }
    }

    public void setGzipFlag(int gzipFlag) {
        if (this.gzipFlag != gzipFlag) {
            this.gzipFlag = gzipFlag;
            flagModify = true;
        }
    }

    public void setFlushFlag(int flushFlag) {
        if (this.flushFlag != flushFlag) {
            this.flushFlag = flushFlag;
            flagModify = true;
        }
    }

    public int compress(byte[] inArray, byte[] outArray) {
        return compress(inArray, 0, inArray.length, outArray, 0, outArray.length);
    }

    public int compress(byte[] inArray, int inOff, int inLen, byte[] outArray, int outOff, int outLen) {
        validate();
        if (inOff < 0 || inLen < 0 || inOff > inArray.length - inLen || outOff < 0 || outLen < 0 || outOff > outArray.length - outLen) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (flagModify) {
            flagModify = false;
            init(igZip, level, gzipFlag, flushFlag);
        }
        return compress(igZip, inArray, inOff, inLen, outArray, outOff, outLen);
    }

    public int compress(long inRef, int inAvail, long outRef, int outAvail) {
        validate();
        if (flagModify) {
            init(igZip, level, gzipFlag, flushFlag);
            flagModify = false;
        }
        return compress(igZip, inRef, inAvail, outRef, outAvail);
    }

    public void close() {
        if (igZip != 0) {
            free(igZip);
            igZip = 0;
        }
    }

    private void validate() {
        if (igZip == 0) {
            throw new IllegalStateException("closed");
        }
    }

    private static native int compress(long ref, byte[] inArray, int inOff, int inLen, byte[] outArray, int outOff, int outLen);

    private static native long init(long ref, int level, int gzipFlag, int flushFlag);

    private static native long alloc(byte[] deflateBuf, int len);

    private static native void free(long ptr);

    private static native int compress(long ref, long inRef, int inAvail, long outRef, int outAvail);
}
