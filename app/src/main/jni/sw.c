#include <jni.h>
#include <string.h>
#include <stdlib.h>
#include <android/log.h>
#include <com_example_cn09876_myapplication_MainActivity.h>

static const char *TAG="sw_debug";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)


JNIEXPORT jstring JNICALL Java_com_example_cn09876_myapplication_MainActivity_get_1pwd(JNIEnv *env, jobject obj, jint idx)
{
	LOGE("hello i am jni123哈哈");
	return (*env)->NewStringUTF(env, "48156327");
}