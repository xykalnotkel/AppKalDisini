#include "renderer.hpp"
#include "shaders.hpp"
#include <cstring>
#include <cstdlib>
#include <android/log.h>
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,"GL",__VA_ARGS__)
static const float PI=3.14159265359f, D2R=PI/180.0f;

GLRenderer gRenderer;

void GLRenderer::init(int w,int h){mW=w;mH=h;
    mProgSolid=buildProgram(SOLID_VS,SOLID_FS);mProgTex=buildProgram(TEX_VS,TEX_FS);
    mProgSDF=buildProgram(SDF_VS,SDF_FS);mProgPath=buildProgram(PATH_VS,PATH_FS);
    glGenBuffers(1,&mVBO);glEnable(GL_BLEND);glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
    matProj();mCC[0]=mCC[1]=mCC[2]=0;mCC[3]=1;
}
void GLRenderer::destroy(){glDeleteProgram(mProgSolid);glDeleteProgram(mProgTex);glDeleteProgram(mProgSDF);glDeleteProgram(mProgPath);glDeleteBuffers(1,&mVBO);}
void GLRenderer::resize(int w,int h){mW=w;mH=h;matProj();}
void GLRenderer::beginFrame(){glViewport(0,0,mW,mH);glClearColor(mCC[0],mCC[1],mCC[2],mCC[3]);glClear(GL_COLOR_BUFFER_BIT);mVC=0;}
void GLRenderer::setClearColor(float r,float g,float b,float a){mCC[0]=r;mCC[1]=g;mCC[2]=b;mCC[3]=a;}

void GLRenderer::flush(){
    if(mVC==0||mVC%6!=0)return;
    glBindBuffer(GL_ARRAY_BUFFER,mVBO);glBufferData(GL_ARRAY_BUFFER,mVC*4,mV,GL_DYNAMIC_DRAW);
    glEnableVertexAttribArray(0);glVertexAttribPointer(0,2,GL_FLOAT,GL_FALSE,24,(void*)0);
    glEnableVertexAttribArray(1);glVertexAttribPointer(1,2,GL_FLOAT,GL_FALSE,24,(void*)8);
    glEnableVertexAttribArray(2);glVertexAttribPointer(2,2,GL_FLOAT,GL_FALSE,24,(void*)16);
    glDrawArrays(GL_TRIANGLES,0,mVC/6);mVC=0;
}

void GLRenderer::emitQuad(float u0,float v0,float u1,float v1,float alpha){
    if(mVC+36>=MAXV*6)flush();
    // triangle 1
    mV[mVC++]=-0.5f;mV[mVC++]=-0.5f;mV[mVC++]=u0;mV[mVC++]=v0;mV[mVC++]=0;mV[mVC++]=alpha;
    mV[mVC++]=0.5f;mV[mVC++]=-0.5f;mV[mVC++]=u1;mV[mVC++]=v0;mV[mVC++]=1;mV[mVC++]=alpha;
    mV[mVC++]=-0.5f;mV[mVC++]=0.5f;mV[mVC++]=u0;mV[mVC++]=v1;mV[mVC++]=0;mV[mVC++]=alpha;
    // triangle 2
    mV[mVC++]=0.5f;mV[mVC++]=-0.5f;mV[mVC++]=u1;mV[mVC++]=v0;mV[mVC++]=1;mV[mVC++]=alpha;
    mV[mVC++]=0.5f;mV[mVC++]=0.5f;mV[mVC++]=u1;mV[mVC++]=v1;mV[mVC++]=1;mV[mVC++]=alpha;
    mV[mVC++]=-0.5f;mV[mVC++]=0.5f;mV[mVC++]=u0;mV[mVC++]=v1;mV[mVC++]=0;mV[mVC++]=alpha;
}

void GLRenderer::drawRect(float x,float y,float w,float h,float r,float g,float b,float a,float rot,float sx,float sy,float cr,float op){
    float m[16];matModel(x,y,w,h,rot,sx,sy,m);float mvp[16];matMul(mvp,mPM,m);
    glUseProgram(mProgSolid);glUniformMatrix4fv(glGetUniformLocation(mProgSolid,"uMVP"),1,false,mvp);
    glUniform4f(glGetUniformLocation(mProgSolid,"uColor"),r,g,b,a*op);
    emitQuad(0,0,1,1,a*op);
}

void GLRenderer::drawCircle(float cx,float cy,float rad,float cr,float cg,float cb,float ca,float rot,float sx,float sy,float op){
    float m[16];matModel(cx,cy,rad*2,rad*2,rot,sx,sy,m);float mvp[16];matMul(mvp,mPM,m);
    glUseProgram(mProgSolid);glUniformMatrix4fv(glGetUniformLocation(mProgSolid,"uMVP"),1,false,mvp);
    glUniform4f(glGetUniformLocation(mProgSolid,"uColor"),cr,cg,cb,ca*op);
    const int S=64;if(mVC+(S+3)*6>=MAXV*6)flush();
    mV[mVC++]=0;mV[mVC++]=0;mV[mVC++]=0.5f;mV[mVC++]=0.5f;mV[mVC++]=0;mV[mVC++]=ca*op;
    for(int i=0;i<=S;i++){float ang=2*PI*i/S;float cx2=cosf(ang)*0.5f,cy2=sinf(ang)*0.5f;
        mV[mVC++]=cx2;mV[mVC++]=cy2;mV[mVC++]=cx2+0.5f;mV[mVC++]=cy2+0.5f;mV[mVC++]=1;mV[mVC++]=ca*op;
        if(i>0&&i<S){mV[mVC++]=0;mV[mVC++]=0;mV[mVC++]=0.5f;mV[mVC++]=0.5f;mV[mVC++]=0;mV[mVC++]=ca*op;
            float a2=2*PI*(i+1)/S;mV[mVC++]=cosf(a2)*0.5f;mV[mVC++]=sinf(a2)*0.5f;mV[mVC++]=cosf(a2)*0.5f+0.5f;mV[mVC++]=sinf(a2)*0.5f+0.5f;mV[mVC++]=1;mV[mVC++]=ca*op;
            mV[mVC++]=cx2;mV[mVC++]=cy2;mV[mVC++]=cx2+0.5f;mV[mVC++]=cy2+0.5f;mV[mVC++]=1;mV[mVC++]=ca*op;
        }
    }
}

void GLRenderer::drawTriangle(float x1,float y1,float x2,float y2,float x3,float y3,float r,float g,float b,float a,float op){
    float m[16];matId(m);float mvp[16];matMul(mvp,mPM,m);
    glUseProgram(mProgSolid);glUniformMatrix4fv(glGetUniformLocation(mProgSolid,"uMVP"),1,false,mvp);
    glUniform4f(glGetUniformLocation(mProgSolid,"uColor"),r,g,b,a*op);
    if(mVC+18>=MAXV*6)flush();
    mV[mVC++]=x1;mV[mVC++]=y1;mV[mVC++]=0;mV[mVC++]=0;mV[mVC++]=0;mV[mVC++]=a*op;
    mV[mVC++]=x2;mV[mVC++]=y2;mV[mVC++]=1;mV[mVC++]=0;mV[mVC++]=1;mV[mVC++]=a*op;
    mV[mVC++]=x3;mV[mVC++]=y3;mV[mVC++]=0;mV[mVC++]=1;mV[mVC++]=0;mV[mVC++]=a*op;
}

void GLRenderer::drawStar(float cx,float cy,float ir,float orad,int pts,float r,float g,float b,float a,float rot,float op){
    float m[16];matModel(cx,cy,orad*2,orad*2,rot,1,1,m);float mvp[16];matMul(mvp,mPM,m);
    glUseProgram(mProgSolid);glUniformMatrix4fv(glGetUniformLocation(mProgSolid,"uMVP"),1,false,mvp);
    glUniform4f(glGetUniformLocation(mProgSolid,"uColor"),r,g,b,a*op);
    if(mVC+(pts*2+4)*6>=MAXV*6)flush();
    mV[mVC++]=0;mV[mVC++]=0;mV[mVC++]=0.5f;mV[mVC++]=0.5f;mV[mVC++]=0;mV[mVC++]=a*op;
    float ratio=ir/orad;
    for(int i=0;i<=pts*2;i++){float ang=PI*i/pts-PI/2.0f;float rad=(i%2==0)?0.5f:ratio*0.5f;
        float px=cosf(ang)*rad,py=sinf(ang)*rad;
        mV[mVC++]=px;mV[mVC++]=py;mV[mVC++]=px+0.5f;mV[mVC++]=py+0.5f;mV[mVC++]=1;mV[mVC++]=a*op;
    }
}

void GLRenderer::drawText(const char* s,float x,float y,float fs,float r,float g,float b,float a,float rot,float sx,float sy,float op){
    float len=strlen(s),w=len*fs*0.6f;
    float m[16];matModel(x,y,w,fs,rot,sx,sy,m);float mvp[16];matMul(mvp,mPM,m);
    glUseProgram(mProgSDF);glUniformMatrix4fv(glGetUniformLocation(mProgSDF,"uMVP"),1,false,mvp);
    glUniform4f(glGetUniformLocation(mProgSDF,"uColor"),r,g,b,a*op);
    glUniform1f(glGetUniformLocation(mProgSDF,"uSmooth"),0.08f);
    emitQuad(0,0,len,1,a*op);
}

void GLRenderer::drawImage(uint8_t* pix,int iw,int ih,int st,int fmt,float x,float y,float w,float h,float rot,float sx,float sy,float op){
    int tid=createTexture(pix,iw,ih,st,fmt);bindTexture(tid);
    float m[16];matModel(x,y,w,h,rot,sx,sy,m);float mvp[16];matMul(mvp,mPM,m);
    glUseProgram(mProgTex);glUniformMatrix4fv(glGetUniformLocation(mProgTex,"uMVP"),1,false,mvp);
    glUniform1f(glGetUniformLocation(mProgTex,"uOpacity"),op);
    emitQuad(0,0,1,1,op);flush();deleteTexture(tid);
}

void GLRenderer::drawSVG(const char* d,float x,float y,float sc,float r,float g,float b,float a,float sw,float sr,float sg,float sb,float sa,float op,bool fill){
    float m[16];matId(m);m[0]=sc;m[5]=sc;m[12]=x;m[13]=y;float mvp[16];matMul(mvp,mPM,m);
    glUseProgram(mProgPath);glUniformMatrix4fv(glGetUniformLocation(mProgPath,"uMVP"),1,false,mvp);
    glUniform4f(glGetUniformLocation(mProgPath,"uFill"),r,g,b,a*op);
    glUniform4f(glGetUniformLocation(mProgPath,"uStroke"),sr,sg,sb,sa*op);
    glUniform1f(glGetUniformLocation(mProgPath,"uStrokeW"),sw/sc);
    glUniform1f(glGetUniformLocation(mProgPath,"uIsFill"),fill?1.0f:0.0f);
    emitQuad(0,0,1,1,op);
}

int GLRenderer::createTexture(uint8_t* p,int w,int h,int st,int fmt){
    GLuint t;glGenTextures(1,&t);glBindTexture(GL_TEXTURE_2D,t);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
    GLenum f=(fmt==1)?GL_RGBA:GL_RGB;
    glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,w,h,0,f,GL_UNSIGNED_BYTE,p);return t;
}
void GLRenderer::bindTexture(int id){glBindTexture(GL_TEXTURE_2D,id);}
void GLRenderer::deleteTexture(int id){GLuint t=id;glDeleteTextures(1,&t);}

void GLRenderer::matId(float* m){memset(m,0,64);m[0]=m[5]=m[10]=m[15]=1;}
void GLRenderer::matProj(){matId(mPM);mPM[0]=2.0f/mW;mPM[5]=-2.0f/mH;mPM[12]=-1;mPM[13]=1;}
void GLRenderer::matModel(float x,float y,float w,float h,float r,float sx,float sy,float* o){
    matId(o);o[0]=w*sx;o[5]=h*sy;o[12]=x;o[13]=y;
    if(r!=0){float c=cosf(r*D2R),s=sinf(r*D2R);float r0=o[0]*c-o[1]*s,r1=o[0]*s+o[1]*c;o[0]=r0;o[1]=r1;r0=o[4]*c-o[5]*s;r1=o[4]*s+o[5]*c;o[4]=r0;o[5]=r1;}
}
void GLRenderer::matMul(float* res,float* a,float* b){for(int i=0;i<4;i++)for(int j=0;j<4;j++)res[i*4+j]=a[i*4]*b[j]+a[i*4+1]*b[4+j]+a[i*4+2]*b[8+j]+a[i*4+3]*b[12+j];}
int GLRenderer::buildProgram(const char*vs,const char*fs){
    auto compile=[](int t,const char*src){int sh=glCreateShader(t);glShaderSource(sh,1,&src,0);glCompileShader(sh);return sh;};
    int v=compile(GL_VERTEX_SHADER,vs),f=compile(GL_FRAGMENT_SHADER,fs),p=glCreateProgram();
    glAttachShader(p,v);glAttachShader(p,f);glLinkProgram(p);glDeleteShader(v);glDeleteShader(f);return p;
}
