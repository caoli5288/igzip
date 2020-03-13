#!/bin/sh

_dir=`pwd`

# prepare build env
apk add --no-cache mingw-w64-gcc build-base autoconf automake libtool nasm yasm openjdk8

# build linux so
# build igzip
cp -a $_dir/isa-l /tmp/isa-l
cp $_dir/igzip.am /tmp/isa-l/Makefile.am
cd /tmp/isa-l
./autogen.sh
CFLAGS="-O3 -fPIC" ./configure
make -j`nproc`

# build igzip-jni
cp -a $_dir/igzip-java /tmp/igzip-java
cd /tmp/igzip-java/src/main/c
gcc -shared -s -O3 -I /tmp/isa-l/include/ -I /usr/lib/jvm/java-1.8-openjdk/include/ -I /usr/lib/jvm/java-1.8-openjdk/include/linux/ igzip_jni.c /tmp/isa-l/.libs/libisal.a -o $_dir/libigzip_jni.so

# clear
rm -rf /tmp/isa-l
rm -rf /tmp/igzip-java

# build windows dll
# build igzip
cp -a $_dir/isa-l /tmp/isa-l
cp $_dir/igzip.am /tmp/isa-l/Makefile.am
cd /tmp/isa-l
./autogen.sh
CFLAGS="-O3 -fPIC" ./configure --host=x86_64-w64-mingw32
make -j`nproc`

# build igzip-jni
cp -a $_dir/igzip-java /tmp/igzip-java
cd /tmp/igzip-java/src/main/c
x86_64-w64-mingw32-gcc -shared -s -O3 -I /tmp/isa-l/include/ -I /usr/lib/jvm/java-1.8-openjdk/include/ -I /usr/lib/jvm/java-1.8-openjdk/include/linux/ igzip_jni.c /tmp/isa-l/.libs/libisal.a -o $_dir/igzip_jni.dll
