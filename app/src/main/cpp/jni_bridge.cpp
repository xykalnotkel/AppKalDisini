#include <jni.h>
#include <android/log.h>
#include <android/bitmap.h>
#include <cstring>
#include <cmath>
#include <GLES2/gl2.h>
#include <EGL/egl.h>
#include "gl/renderer.hpp"
#include "effects/pipeline.hpp"
#define TAG "AlightNative"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

extern "C" {
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeInit(JNIEnv*,jobject,jint w,jint h){gRenderer.init(w,h);}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeRelease(JNIEnv*,jobject){gRenderer.destroy();}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeResize(JNIEnv*,jobject,jint w,jint h){gRenderer.resize(w,h);}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeRenderFrame(JNIEnv*,jobject){gRenderer.beginFrame();}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeClearColor(JNIEnv*,jobject,jfloat r,jfloat g,jfloat b,jfloat a){gRenderer.setClearColor(r,g,b,a);}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeFlush(JNIEnv*,jobject){gRenderer.flush();}

JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeDrawRect(JNIEnv*,jobject,jfloat x,jfloat y,jfloat w,jfloat h,jfloat r,jfloat g,jfloat b,jfloat a,jfloat rot,jfloat sx,jfloat sy,jfloat cr,jfloat op){gRenderer.drawRect(x,y,w,h,r,g,b,a,rot,sx,sy,cr,op);}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeDrawCircle(JNIEnv*,jobject,jfloat cx,jfloat cy,jfloat rad,jfloat r,jfloat g,jfloat b,jfloat a,jfloat rot,jfloat sx,jfloat sy,jfloat op){gRenderer.drawCircle(cx,cy,rad,r,g,b,a,rot,sx,sy,op);}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeDrawTriangle(JNIEnv*,jobject,jfloat x1,jfloat y1,jfloat x2,jfloat y2,jfloat x3,jfloat y3,jfloat r,jfloat g,jfloat b,jfloat a,jfloat op){gRenderer.drawTriangle(x1,y1,x2,y2,x3,y3,r,g,b,a,op);}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeDrawStar(JNIEnv*,jobject,jfloat cx,jfloat cy,jfloat ir,jfloat orad,jint pts,jfloat r,jfloat g,jfloat b,jfloat a,jfloat rot,jfloat op){gRenderer.drawStar(cx,cy,ir,orad,pts,r,g,b,a,rot,op);}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeDrawText(JNIEnv* e,jobject,jstring t_,jfloat x,jfloat y,jfloat fs,jfloat r,jfloat g,jfloat b,jfloat a,jfloat rot,jfloat sx,jfloat sy,jfloat op){const char*t=e->GetStringUTFChars(t_,0);gRenderer.drawText(t,x,y,fs,r,g,b,a,rot,sx,sy,op);e->ReleaseStringUTFChars(t_,t);}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeDrawImage(JNIEnv* e,jobject,jobject bmp,jfloat x,jfloat y,jfloat w,jfloat h,jfloat rot,jfloat sx,jfloat sy,jfloat op){AndroidBitmapInfo i;void*p;if(AndroidBitmap_getInfo(e,bmp,&i)<0||AndroidBitmap_lockPixels(e,bmp,&p)<0)return;gRenderer.drawImage((uint8_t*)p,i.width,i.height,i.stride,i.format,x,y,w,h,rot,sx,sy,op);AndroidBitmap_unlockPixels(e,bmp);}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeDrawSVGPath(JNIEnv* e,jobject,jstring d_,jfloat x,jfloat y,jfloat sc,jfloat r,jfloat g,jfloat b,jfloat a,jfloat sw,jfloat sr,jfloat sg,jfloat sb,jfloat sa,jfloat op,jboolean fill){const char*d=e->GetStringUTFChars(d_,0);gRenderer.drawSVG(d,x,y,sc,r,g,b,a,sw,sr,sg,sb,sa,op,fill);e->ReleaseStringUTFChars(d_,d);}

JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeApplyBlur(JNIEnv* e,jobject,jobject src,jobject dst,jfloat r){AndroidBitmapInfo si,di;void*sp;void*dp;if(AndroidBitmap_getInfo(e,src,&si)<0||AndroidBitmap_getInfo(e,dst,&di)<0)return;if(AndroidBitmap_lockPixels(e,src,&sp)<0||AndroidBitmap_lockPixels(e,dst,&dp)<0){AndroidBitmap_unlockPixels(e,src);return;}blur((uint8_t*)sp,(uint8_t*)dp,si.width,si.height,si.stride,r);AndroidBitmap_unlockPixels(e,src);AndroidBitmap_unlockPixels(e,dst);}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeApplyGlow(JNIEnv* e,jobject,jobject src,jobject dst,jfloat i,jfloat th){AndroidBitmapInfo si,di;void*sp;void*dp;if(AndroidBitmap_getInfo(e,src,&si)<0||AndroidBitmap_getInfo(e,dst,&di)<0)return;if(AndroidBitmap_lockPixels(e,src,&sp)<0||AndroidBitmap_lockPixels(e,dst,&dp)<0){AndroidBitmap_unlockPixels(e,src);return;}glow((uint8_t*)sp,(uint8_t*)dp,si.width,si.height,si.stride,i,th);AndroidBitmap_unlockPixels(e,src);AndroidBitmap_unlockPixels(e,dst);}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeApplyColorCorrection(JNIEnv* e,jobject,jobject src,jobject dst,jfloat sat,jfloat bri,jfloat con,jfloat temp,jfloat vib,jfloat exp){AndroidBitmapInfo si,di;void*sp;void*dp;if(AndroidBitmap_getInfo(e,src,&si)<0||AndroidBitmap_getInfo(e,dst,&di)<0)return;if(AndroidBitmap_lockPixels(e,src,&sp)<0||AndroidBitmap_lockPixels(e,dst,&dp)<0){AndroidBitmap_unlockPixels(e,src);return;}colorCorrect((uint8_t*)sp,(uint8_t*)dp,si.width,si.height,si.stride,sat,bri,con,temp,vib,exp);AndroidBitmap_unlockPixels(e,src);AndroidBitmap_unlockPixels(e,dst);}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeApplyChromaKey(JNIEnv* e,jobject,jobject src,jobject dst,jfloat kr,jfloat kg,jfloat kb,jfloat th,jfloat sp){AndroidBitmapInfo si,di;void*sp_;void*dp_;if(AndroidBitmap_getInfo(e,src,&si)<0||AndroidBitmap_getInfo(e,dst,&di)<0)return;if(AndroidBitmap_lockPixels(e,src,&sp_)<0||AndroidBitmap_lockPixels(e,dst,&dp_)<0){AndroidBitmap_unlockPixels(e,src);return;}chromaKey((uint8_t*)sp_,(uint8_t*)dp_,si.width,si.height,si.stride,kr,kg,kb,th,sp);AndroidBitmap_unlockPixels(e,src);AndroidBitmap_unlockPixels(e,dst);}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeApplyDistortion(JNIEnv* e,jobject,jobject src,jobject dst,jint t,jfloat a){AndroidBitmapInfo si,di;void*sp;void*dp;if(AndroidBitmap_getInfo(e,src,&si)<0||AndroidBitmap_getInfo(e,dst,&di)<0)return;if(AndroidBitmap_lockPixels(e,src,&sp)<0||AndroidBitmap_lockPixels(e,dst,&dp)<0){AndroidBitmap_unlockPixels(e,src);return;}distort((uint8_t*)sp,(uint8_t*)dp,si.width,si.height,si.stride,t,a);AndroidBitmap_unlockPixels(e,src);AndroidBitmap_unlockPixels(e,dst);}

JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeParticleUpdate(JNIEnv*,jobject,jfloat){}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeParticleRender(JNIEnv*,jobject){}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeParticleEmit(JNIEnv*,jobject,jfloat,jfloat,jint,jfloat,jfloat,jfloat,jfloat,jfloat,jfloat,jfloat,jfloat){}
JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeParticleSetEmitter(JNIEnv*,jobject,jfloat,jfloat,jfloat,jboolean){}

JNIEXPORT jstring JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeParseFigmaJSON(JNIEnv* e,jobject,jstring){return e->NewStringUTF("{}");}

JNIEXPORT void JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeAudioLoadFile(JNIEnv*,jobject,jstring){}
JNIEXPORT jfloatArray JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeAudioGetWaveform(JNIEnv* e,jobject,jint n){return e->NewFloatArray(n);}
JNIEXPORT jfloat JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeAudioGetDuration(JNIEnv*,jobject){return 0.0f;}
JNIEXPORT jfloat JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeAudioGetAmplitude(JNIEnv*,jobject,jfloat){return 0.0f;}

JNIEXPORT jboolean JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeOpenVideo(JNIEnv*,jobject,jstring){return false;}
JNIEXPORT jboolean JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeOpenAudio(JNIEnv*,jobject,jstring){return false;}
JNIEXPORT jint JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeGetVideoWidth(JNIEnv*,jobject){return 0;}
JNIEXPORT jint JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeGetVideoHeight(JNIEnv*,jobject){return 0;}
JNIEXPORT jdouble JNICALL Java_com_alight_motion_engine_native_NativeBridge_nativeGetVideoDuration(JNIEnv*,jobject){return 0.0;}
}
