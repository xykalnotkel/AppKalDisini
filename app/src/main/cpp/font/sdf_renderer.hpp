#pragma once
#include <string>
#include <cstdint>
class SDFRenderer{public:void init();void renderText(const char*t,float x,float y,float fs,uint8_t*out,int w,int h);};
