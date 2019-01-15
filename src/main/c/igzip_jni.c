#include <stdlib.h>
#include "igzip_lib.h"
#include "com_github_caoli5288_igzip_IGzip.h"

JNIEXPORT jlong JNICALL Java_com_github_caoli5288_igzip_IGzip_init(JNIEnv *env, jclass clz, jbyteArray buf_arr, jint buf_len, jint level, jint gzip_flag, jint flush_flag) {
    struct isal_zstream* strm = malloc(sizeof(struct isal_zstream));
    isal_deflate_stateless_init(strm);
    strm->level = level;
    strm->gzip_flag = gzip_flag;
    strm->flush = flush_flag;
    unsigned char* buf = (*env)->GetPrimitiveArrayCritical(env, buf_arr, 0);
    strm->level_buf = buf;
    strm->level_buf_size = buf_len;
    (*env)->ReleasePrimitiveArrayCritical(env, buf_arr, buf, 0);
    return (jlong)strm;
}

JNIEXPORT jint JNICALL Java_com_github_caoli5288_igzip_IGzip_compress__J_3BII_3BII(JNIEnv *env, jclass clz, jlong ref, jbyteArray in_arr, jint in_off, jint in_avail, jbyteArray out_arr, jint out_off, jint out_avail) {
    struct isal_zstream* strm = (struct isal_zstream*)ref;
    unsigned char* in = (*env)->GetPrimitiveArrayCritical(env, in_arr, 0);
    strm->next_in = in + in_off;
    strm->avail_in = in_avail;
    unsigned char* out = (*env)->GetPrimitiveArrayCritical(env, out_arr, 0);
    strm->next_out = out + out_off;
    strm->avail_out = out_avail;
    /* compress op */
    isal_deflate_stateless(strm);
    jint ret = strm->total_out;
    /* cleanup */
    (*env)->ReleasePrimitiveArrayCritical(env, in_arr, in, 0);
    (*env)->ReleasePrimitiveArrayCritical(env, out_arr, out, 0);
    free(strm);
    return ret;
}

JNIEXPORT jint JNICALL Java_com_github_caoli5288_igzip_IGzip_compress__JJIJI(JNIEnv *env, jclass clz, jlong ref, jlong in_ref, jint in_avail, jlong out_ref, jint out_avail) {
    struct isal_zstream* strm = (struct isal_zstream*)ref;
    strm->next_in = (unsigned char*)in_ref;
    strm->avail_in = in_avail;
    strm->next_out = (unsigned char*)out_ref;
    strm->avail_out = out_avail;
    /* compress op */
    isal_deflate_stateless(strm);
    jint ret = strm->total_out;
    free(strm);
    return ret;
}
