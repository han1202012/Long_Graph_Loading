#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_kim_hsl_long_1graph_1loading_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
