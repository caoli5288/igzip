#include <stdlib.h>
#include "igzip_lib.h"
#include "com_github_caoli5288_igzip_IGzip.h"

JNIEXPORT jlong JNICALL Java_com_github_caoli5288_igzip_IGzip_init(JNIEnv *env, jclass clz, jlong ref, jint level, jint gzip_flag, jint flush_flag) {
    struct isal_zstream* strm = (struct isal_zstream*)ref;
    strm->level = level;
    strm->gzip_flag = gzip_flag;
    strm->flush = flush_flag;
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
    int status = isal_deflate_stateless(strm);
    jint total_out = strm->total_out;
    /* cleanup */
    isal_deflate_reset(strm);
    strm->next_in = NULL;
    strm->next_out = NULL;
    (*env)->ReleasePrimitiveArrayCritical(env, out_arr, out, 0);
    (*env)->ReleasePrimitiveArrayCritical(env, in_arr, in, 0);
    if (status == COMP_OK) {
        return total_out;
    }
    return (jint)status;
}

JNIEXPORT jint JNICALL Java_com_github_caoli5288_igzip_IGzip_compress__JJIJI(JNIEnv *env, jclass clz, jlong ref, jlong in_ref, jint in_avail, jlong out_ref, jint out_avail) {
    struct isal_zstream* strm = (struct isal_zstream*)ref;
    strm->next_in = (unsigned char*)in_ref;
    strm->avail_in = in_avail;
    strm->next_out = (unsigned char*)out_ref;
    strm->avail_out = out_avail;
    /* compress op */
    int status = isal_deflate_stateless(strm);
    jint total_out = strm->total_out;
    isal_deflate_reset(strm);
    strm->next_in = NULL;
    strm->next_out = NULL;
    if (status == COMP_OK) {
        return total_out;
    }
    return (jint)status;
}

JNIEXPORT jlong JNICALL Java_com_github_caoli5288_igzip_IGzip_alloc(JNIEnv *env, jclass clz, jbyteArray arr, jint arr_len) {
    struct isal_zstream* strm = malloc(sizeof(struct isal_zstream));
    if (strm == NULL) {
        return 0;
    }
    isal_deflate_stateless_init(strm);
    if (arr_len < 0) {
        arr_len = ISAL_DEF_LVL3_EXTRA_LARGE;
    } else if (arr_len < ISAL_DEF_LVL3_MIN) {
        arr_len = ISAL_DEF_LVL3_MIN;
    } else if (arr_len > ISAL_DEF_LVL3_EXTRA_LARGE) {
        arr_len = ISAL_DEF_LVL3_EXTRA_LARGE;
    }
    unsigned char* buf = malloc(arr_len);
    if (buf == NULL) {
        free(strm);
        return 0;
    }
    strm->level_buf = buf;
    strm->level_buf_size = arr_len;
    return (jlong)strm;
}

JNIEXPORT void JNICALL Java_com_github_caoli5288_igzip_IGzip_free(JNIEnv *env, jclass clz, jlong ref) {
    struct isal_zstream* strm = (struct isal_zstream*)ref;
    free(strm->level_buf);
    free(strm);
}
