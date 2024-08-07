/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_github_caoli5288_igzip_IGzip */

#ifndef _Included_com_github_caoli5288_igzip_IGzip
#define _Included_com_github_caoli5288_igzip_IGzip
#ifdef __cplusplus
extern "C" {
#endif
#undef com_github_caoli5288_igzip_IGzip_BEST_SPEED
#define com_github_caoli5288_igzip_IGzip_BEST_SPEED 1L
#undef com_github_caoli5288_igzip_IGzip_BEST_COMPRESSION
#define com_github_caoli5288_igzip_IGzip_BEST_COMPRESSION 3L
#undef com_github_caoli5288_igzip_IGzip_FMT_DEFLATE
#define com_github_caoli5288_igzip_IGzip_FMT_DEFLATE 0L
#undef com_github_caoli5288_igzip_IGzip_FMT_GZIP
#define com_github_caoli5288_igzip_IGzip_FMT_GZIP 1L
#undef com_github_caoli5288_igzip_IGzip_FMT_GZIP_NO_HEADER
#define com_github_caoli5288_igzip_IGzip_FMT_GZIP_NO_HEADER 2L
#undef com_github_caoli5288_igzip_IGzip_FMT_Z
#define com_github_caoli5288_igzip_IGzip_FMT_Z 3L
#undef com_github_caoli5288_igzip_IGzip_FMT_Z_NO_HEADER
#define com_github_caoli5288_igzip_IGzip_FMT_Z_NO_HEADER 4L
#undef com_github_caoli5288_igzip_IGzip_NO_FLUSH
#define com_github_caoli5288_igzip_IGzip_NO_FLUSH 0L
#undef com_github_caoli5288_igzip_IGzip_SYNC_FLUSH
#define com_github_caoli5288_igzip_IGzip_SYNC_FLUSH 1L
#undef com_github_caoli5288_igzip_IGzip_FULL_FLUSH
#define com_github_caoli5288_igzip_IGzip_FULL_FLUSH 2L
/*
 * Class:     com_github_caoli5288_igzip_IGzip
 * Method:    compress
 * Signature: (J[BII[BII)I
 */
JNIEXPORT jint JNICALL Java_com_github_caoli5288_igzip_IGzip_compress__J_3BII_3BII
  (JNIEnv *, jclass, jlong, jbyteArray, jint, jint, jbyteArray, jint, jint);

/*
 * Class:     com_github_caoli5288_igzip_IGzip
 * Method:    init
 * Signature: (JIII)J
 */
JNIEXPORT jlong JNICALL Java_com_github_caoli5288_igzip_IGzip_init
  (JNIEnv *, jclass, jlong, jint, jint, jint);

/*
 * Class:     com_github_caoli5288_igzip_IGzip
 * Method:    alloc
 * Signature: ([BI)J
 */
JNIEXPORT jlong JNICALL Java_com_github_caoli5288_igzip_IGzip_alloc
  (JNIEnv *, jclass, jbyteArray, jint);

/*
 * Class:     com_github_caoli5288_igzip_IGzip
 * Method:    free
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_github_caoli5288_igzip_IGzip_free
  (JNIEnv *, jclass, jlong);

/*
 * Class:     com_github_caoli5288_igzip_IGzip
 * Method:    compress
 * Signature: (JJIJI)I
 */
JNIEXPORT jint JNICALL Java_com_github_caoli5288_igzip_IGzip_compress__JJIJI
  (JNIEnv *, jclass, jlong, jlong, jint, jlong, jint);

#ifdef __cplusplus
}
#endif
#endif
