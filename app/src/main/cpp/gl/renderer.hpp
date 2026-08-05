#pragma once
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <cstdint>
#include <cmath>
struct PathCmd { char c; float x,y,x2,y2; };
class GLRenderer {
public:
    void init(int w,int h); void destroy(); void resize(int w,int h);
    void beginFrame(); void setClearColor(float r,float g,float b,float a);
    void drawRect(float x,float y,float w,float h,float r,float g,float b,float a,float rot,float sx,float sy,float cr,float op);
    void drawCircle(float cx,float cy,float r,float cr,float cg,float cb,float ca,float rot,float sx,float sy,float op);
    void drawTriangle(float x1,float y1,float x2,float y2,float x3,float y3,float r,float g,float b,float a,float op);
    void drawStar(float cx,float cy,float ir,float orad,int pts,float r,float g,float b,float a,float rot,float op);
    void drawText(const char* s,float x,float y,float fs,float r,float g,float b,float a,float rot,float sx,float sy,float op);
    void drawImage(uint8_t* pix,int iw,int ih,int st,int fmt,float x,float y,float w,float h,float rot,float sx,float sy,float op);
    void drawSVG(const char* d,float x,float y,float sc,float r,float g,float b,float a,float sw,float sr,float sg,float sb,float sa,float op,bool fill);
    int  createTexture(uint8_t* p,int w,int h,int st,int fmt);
    void bindTexture(int id); void deleteTexture(int id); void flush();
private:
    int mW,mH; float mCC[4]; float mPM[16];
    int mProgSolid,mProgTex,mProgSDF,mProgPath;
    GLuint mVBO; static constexpr int MAXV=65536; float mV[MAXV*6]; int mVC;
    void matId(float* m); void matProj(); void matModel(float x,float y,float w,float h,float r,float sx,float sy,float* o);
    void matMul(float* res,float* a,float* b); int buildProgram(const char*vs,const char*fs);
    void emitQuad(float u0,float v0,float u1,float v1,float alpha);
};
extern GLRenderer gRenderer;
