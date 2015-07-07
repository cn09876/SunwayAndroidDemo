#include <jni.h>
#include <string.h>
#include <stdlib.h>
#include <android/log.h>
#include <sww_util.h>

static const char *TAG="sw_debug";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)


JNIEXPORT jint JNICALL Java_sww_util_get_1enc(JNIEnv *env, jclass ii)
{
	LOGE("hello i am jni123哈哈");
	return 998;
}