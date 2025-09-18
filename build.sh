#!/bin/bash

SRC=igzip-java/src/main/c/igzip_jni.c
WORKDIR=$(pwd)
BUILD="isa-l"
JNI_INCLUDE="/usr/lib/jvm/java-11-openjdk-amd64/include"
CROSS_COMPILE=x86_64-w64-mingw32

cd $BUILD
# build linux
./autogen.sh
CFLAGS="-O3 -fPIC -flto" ./configure
make -j`nproc`
gcc -shared -s -O3 -fPIC -flto -I include/ -I $JNI_INCLUDE/ -I $JNI_INCLUDE/linux/ $SRC .libs/libisal.a -o $WORKDIR/libigzip_jni.so
make clean
# build windows
CFLAGS="-O3 -fPIC" ./configure --host=$CROSS_COMPILE
make -j`nproc`
$CROSS_COMPILE-gcc -shared -s -O3 -fPIC -flto -I include/ -I $JNI_INCLUDE/ -I $JNI_INCLUDE/linux/ $SRC .libs/libisal.a -o $WORKDIR/igzip_jni.dll