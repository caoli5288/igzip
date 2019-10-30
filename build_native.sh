#!/bin/sh
emake --ini=emake.ini igzip.mak
mv -v libigzip_jni.so igzip-linux-amd64/src/main/resources

emake --ini=emake_win64.ini igzip.mak
mv -v igzip_jni.dll igzip-windows-amd64/src/main/resources

rm igzip.p

