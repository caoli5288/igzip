#!/bin/sh
emake --ini=emake.ini igzip.mak
strip libigzip_jni.so
mv -v libigzip_jni.so igzip-linux-amd64/src/main/resources

emake --ini=emake_win64.ini igzip.mak
strip igzip_jni.dll
mv -v igzip_jni.dll igzip-windows-amd64/src/main/resources

rm igzip.p

