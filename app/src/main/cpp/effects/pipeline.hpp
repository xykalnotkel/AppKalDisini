#pragma once
#include <cstdint>
void blur(uint8_t*s,uint8_t*d,int w,int h,int st,float r);
void glow(uint8_t*s,uint8_t*d,int w,int h,int st,float i,float th);
void colorCorrect(uint8_t*s,uint8_t*d,int w,int h,int st,float sat,float bri,float con,float temp,float vib,float exp);
void chromaKey(uint8_t*s,uint8_t*d,int w,int h,int st,float kr,float kg,float kb,float th,float sp);
void distort(uint8_t*s,uint8_t*d,int w,int h,int st,int type,float amt);
void noise(uint8_t*s,uint8_t*d,int w,int h,int st,float amt,bool mono);
void vignette(uint8_t*s,uint8_t*d,int w,int h,int st,float amt,float feather);
void sharpen(uint8_t*s,uint8_t*d,int w,int h,int st,float amt);
void pixelate(uint8_t*s,uint8_t*d,int w,int h,int st,int bs);
void posterize(uint8_t*s,uint8_t*d,int w,int h,int st,int lv);
void invert(uint8_t*s,uint8_t*d,int w,int h,int st);
void halftone(uint8_t*s,uint8_t*d,int w,int h,int st,float sz,float ang);
void threshold(uint8_t*s,uint8_t*d,int w,int h,int st,float lv);
void motionBlur(uint8_t*s,uint8_t*d,int w,int h,int st,float i,float ang);
void edgeGlow(uint8_t*s,uint8_t*d,int w,int h,int st,float i,float th);
void crystallize(uint8_t*s,uint8_t*d,int w,int h,int st,float sz);
