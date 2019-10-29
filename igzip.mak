import: java8
mode: dll
flag: -O3

src: igzip-java/src/main/c/igzip_jni.c

linux64/link: isa-l/linux64/lib/libisal.a
win64/link: isa-l/win64/lib/libisal.a

linux64/out: libigzip_jni.so
win64/out: igzip_jni.dll
