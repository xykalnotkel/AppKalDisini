#include "pipeline.hpp"
#include <cmath>
#include <algorithm>
#include <cstring>
#include <cstdlib>
#define CL(x,lo,hi) std::min((float)hi,std::max((float)lo,(float)x))
static inline float lum(uint8_t*p){return 0.299f*p[0]+0.587f*p[1]+0.114f*p[2];}

void blur(uint8_t*s,uint8_t*d,int w,int h,int st,float r){
    int rad=r*2.5f;if(rad<1)rad=1;if(rad>25)rad=25;
    uint8_t*t=new uint8_t[st*h];memcpy(d,s,st*h);
    // horizontal
    for(int y=0;y<h;y++)for(int x=0;x<w;x++){float sum[4]={0};int c=0;
        for(int dx=-rad;dx<=rad;dx++){int sx=x+dx;if(sx<0||sx>=w)continue;uint8_t*p=s+y*st+sx*4;sum[0]+=p[0];sum[1]+=p[1];sum[2]+=p[2];sum[3]+=p[3];c++;}
        uint8_t*o=t+y*st+x*4;o[0]=sum[0]/c;o[1]=sum[1]/c;o[2]=sum[2]/c;o[3]=sum[3]/c;}
    // vertical
    for(int y=0;y<h;y++)for(int x=0;x<w;x++){float sum[4]={0};int c=0;
        for(int dy=-rad;dy<=rad;dy++){int sy=y+dy;if(sy<0||sy>=h)continue;uint8_t*p=t+sy*st+x*4;sum[0]+=p[0];sum[1]+=p[1];sum[2]+=p[2];sum[3]+=p[3];c++;}
        uint8_t*o=d+y*st+x*4;o[0]=sum[0]/c;o[1]=sum[1]/c;o[2]=sum[2]/c;o[3]=sum[3]/c;}
    delete[]t;
}

void glow(uint8_t*s,uint8_t*d,int w,int h,int st,float i,float th){
    uint8_t*br=new uint8_t[st*h],*bl=new uint8_t[st*h];memcpy(d,s,st*h);
    for(int j=0;j<st*h;j+=4){float l=lum(s+j)/255.f,f=CL((l-th)/(1.f-th+0.001f),0,1);
        br[j]=CL(s[j]*f*i,0,255);br[j+1]=CL(s[j+1]*f*i,0,255);br[j+2]=CL(s[j+2]*f*i,0,255);br[j+3]=0;}
    blur(br,bl,w,h,st,3.f*i);
    for(int j=0;j<st*h;j+=4){d[j]=std::min(255,d[j]+bl[j]);d[j+1]=std::min(255,d[j+1]+bl[j+1]);d[j+2]=std::min(255,d[j+2]+bl[j+2]);}
    delete[]br;delete[]bl;
}

void colorCorrect(uint8_t*s,uint8_t*d,int w,int h,int st,float sat,float bri,float con,float temp,float vib,float exp){
    for(int i=0;i<st*h;i+=4){
        float R=s[i]/255.f,G=s[i+1]/255.f,B=s[i+2]/255.f;
        float g=0.299f*R+0.587f*G+0.114f*B;
        R=g+(R-g)*sat;G=g+(G-g)*sat;B=g+(B-g)*sat;
        R+=bri-0.5f;G+=bri-0.5f;B+=bri-0.5f;
        float mx=std::max({R,G,B});float vf=(mx>0.001f)?((mx-std::min({R,G,B}))/mx)*vib:0;
        R+=(R-g)*vf;G+=(G-g)*vf;B+=(B-g)*vf;
        R=(R-0.5f)*con+0.5f;G=(G-0.5f)*con+0.5f;B=(B-0.5f)*con+0.5f;
        R+=exp;G+=exp;B+=exp;
        R+=temp*0.15f;B-=temp*0.15f;
        d[i]=CL(R*255,0,255);d[i+1]=CL(G*255,0,255);d[i+2]=CL(B*255,0,255);d[i+3]=s[i+3];
    }
}

void chromaKey(uint8_t*s,uint8_t*d,int w,int h,int st,float kr,float kg,float kb,float th,float sp){
    for(int i=0;i<st*h;i+=4){
        float dr=s[i]/255.f-kr,dg=s[i+1]/255.f-kg,db=s[i+2]/255.f-kb,dist=sqrtf(dr*dr+dg*dg+db*db);
        float a=dist<th?0:CL((dist-th)/(sp+0.001f),0,1),l=lum(s+i)/255.f,mix=CL((th-dist)/th,0,1)*sp;
        d[i]=CL((s[i]*(1-mix)+l*mix*255),0,255);d[i+1]=CL((s[i+1]*(1-mix*1.5f)+l*mix*127),0,255);
        d[i+2]=CL((s[i+2]*(1-mix)+l*mix*255),0,255);d[i+3]=CL(a*255,0,255);
    }
}

void distort(uint8_t*s,uint8_t*d,int w,int h,int st,int type,float amt){
    float cx=w/2.f,cy=h/2.f,mr=std::max(w,h)/2.f;memset(d,0,st*h);
    for(int y=0;y<h;y++)for(int x=0;x<w;x++){
        float dx=x-cx,dy=y-cy,dist=sqrtf(dx*dx+dy*dy),sx=x,sy=y;
        if(type==0){float f=1.f+amt*(dist/mr)*(dist/mr);sx=cx+dx*f;sy=cy+dy*f;}
        else if(type==1){float ang=amt*(1.f-dist/mr)*3.f,c=cosf(ang),ss=sinf(ang);sx=cx+dx*c-dy*ss;sy=cy+dx*ss+dy*c;}
        else if(type==2){sx=x+sinf(y*0.05f)*amt*20;sy=y;}
        int px=(int)sx,py=(int)sy;if(px>=0&&px<w&&py>=0&&py<h)memcpy(d+(y*st+x*4),s+(py*st+px*4),4);
    }
}

void noise(uint8_t*s,uint8_t*d,int w,int h,int st,float amt,bool mono){
    for(int i=0;i<st*h;i+=4){
        int n=mono?((int)((rand()%255-128)*amt/100.f)):0;
        d[i]=CL(s[i]+(mono?n:(int)((rand()%255-128)*amt/100.f)),0,255);
        d[i+1]=CL(s[i+1]+(mono?n:(int)((rand()%255-128)*amt/100.f)),0,255);
        d[i+2]=CL(s[i+2]+(mono?n:(int)((rand()%255-128)*amt/100.f)),0,255);d[i+3]=s[i+3];
    }
}

void vignette(uint8_t*s,uint8_t*d,int w,int h,int st,float amt,float feather){
    float cx=w/2.f,cy=h/2.f,mr=sqrtf(cx*cx+cy*cy);
    for(int y=0;y<h;y++)for(int x=0;x<w;x++){
        float dist=sqrtf((x-cx)*(x-cx)+(y-cy)*(y-cy))/mr,vf=CL((1.f-dist-feather)/(1.f-feather+0.001f),0,1)*(amt/100.f);
        uint8_t*o=d+y*st+x*4,*si=s+y*st+x*4;o[0]=CL(si[0]*(1-vf),0,255);o[1]=CL(si[1]*(1-vf),0,255);o[2]=CL(si[2]*(1-vf),0,255);o[3]=si[3];
    }
}

void sharpen(uint8_t*s,uint8_t*d,int w,int h,int st,float amt){
    memcpy(d,s,st*h);float str=amt/50.f;
    for(int y=1;y<h-1;y++)for(int x=1;x<w-1;x++)for(int c=0;c<3;c++){
        float v=5*s[y*st+x*4+c]-s[(y-1)*st+x*4+c]-s[(y+1)*st+x*4+c]-s[y*st+(x-1)*4+c]-s[y*st+(x+1)*4+c];
        d[y*st+x*4+c]=CL(s[y*st+x*4+c]+v*str,0,255);
    }
}

void pixelate(uint8_t*s,uint8_t*d,int w,int h,int st,int bs){
    if(bs<1)bs=1;
    for(int y=0;y<h;y+=bs)for(int x=0;x<w;x+=bs){
        uint8_t avg[4]={0};int cnt=0;
        for(int dy=0;dy<bs&&y+dy<h;dy++)for(int dx=0;dx<bs&&x+dx<w;dx++){int idx=(y+dy)*st+(x+dx)*4;avg[0]+=s[idx];avg[1]+=s[idx+1];avg[2]+=s[idx+2];avg[3]+=s[idx+3];cnt++;}
        avg[0]/=cnt;avg[1]/=cnt;avg[2]/=cnt;avg[3]/=cnt;
        for(int dy=0;dy<bs&&y+dy<h;dy++)for(int dx=0;dx<bs&&x+dx<w;dx++){int idx=(y+dy)*st+(x+dx)*4;d[idx]=avg[0];d[idx+1]=avg[1];d[idx+2]=avg[2];d[idx+3]=avg[3];}
    }
}

void posterize(uint8_t*s,uint8_t*d,int w,int h,int st,int lv){float step=256.f/lv;for(int i=0;i<st*h;i++)d[i]=(int)(s[i]/step)*step;}
void invert(uint8_t*s,uint8_t*d,int w,int h,int st){for(int i=0;i<st*h;i+=4){d[i]=255-s[i];d[i+1]=255-s[i+1];d[i+2]=255-s[i+2];d[i+3]=s[i+3];}}

void halftone(uint8_t*s,uint8_t*d,int w,int h,int st,float sz,float ang){
    float ra=ang*3.14159265f/180.f;
    for(int y=0;y<h;y++)for(int x=0;x<w;x++){
        float rx=x*cosf(ra)-y*sinf(ra),ry=x*sinf(ra)+y*cosf(ra);
        float fx=rx/sz+2,fy=ry/sz+2;int ix=(int)fx,iy=(int)fy;
        float dx=fx-ix-0.5f,dy=fy-iy-0.5f,dist=sqrtf(dx*dx+dy*dy),l=lum(s+y*st+x*4)/255.f;
        float dv=dist<(1.f-l)*0.5f?0.f:1.f;d[y*st+x*4]=d[y*st+x*4+1]=d[y*st+x*4+2]=dv*255;d[y*st+x*4+3]=s[y*st+x*4+3];
    }
}

void threshold(uint8_t*s,uint8_t*d,int w,int h,int st,float lv){for(int i=0;i<st*h;i+=4){int v=lum(s+i)>lv?255:0;d[i]=d[i+1]=d[i+2]=v;d[i+3]=s[i+3];}}

void motionBlur(uint8_t*s,uint8_t*d,int w,int h,int st,float i,float ang){
    float ra=ang*3.14159265f/180.f,dx=cosf(ra)*i*0.5f,dy=sinf(ra)*i*0.5f;memcpy(d,s,st*h);
    for(int y=0;y<h;y++)for(int x=0;x<w;x++){
        float sum[4]={0};int cnt=0;
        for(float t=-1;t<=1;t+=0.5f){int sx=x+dx*t,sy=y+dy*t;if(sx<0||sx>=w||sy<0||sy>=h)continue;
            uint8_t*p=s+sy*st+sx*4;sum[0]+=p[0];sum[1]+=p[1];sum[2]+=p[2];sum[3]+=p[3];cnt++;}
        uint8_t*o=d+y*st+x*4;o[0]=sum[0]/cnt;o[1]=sum[1]/cnt;o[2]=sum[2]/cnt;o[3]=sum[3]/cnt;
    }
}

void edgeGlow(uint8_t*s,uint8_t*d,int w,int h,int st,float i,float th){
    uint8_t*e=new uint8_t[st*h];memcpy(d,s,st*h);
    for(int y=1;y<h-1;y++)for(int x=1;x<w-1;x++){float v=0;
        for(int dy=-1;dy<=1;dy++)for(int dx=-1;dx<=1;dx++)if(dx||dy)v+=lum(s+(y+dy)*st+(x+dx)*4);
        v/=8.f;float diff=fabsf(lum(s+y*st+x*4)-v)/255.f;float f=diff>th?CL((diff-th)/(1.f-th+0.001f)*i,0,1):0;
        e[y*st+x*4]=f*255;e[y*st+x*4+1]=e[y*st+x*4+2]=e[y*st+x*4];e[y*st+x*4+3]=255;}
    for(int i2=0;i2<st*h;i2+=4){d[i2]=std::min(255,d[i2]+e[i2]);d[i2+1]=std::min(255,d[i2+1]+e[i2+1]);d[i2+2]=std::min(255,d[i2+2]+e[i2+2]);}
    delete[]e;
}

void crystallize(uint8_t*s,uint8_t*d,int w,int h,int st,float sz){
    int cs=sz;if(cs<2)cs=2;memset(d,0,st*h);
    for(int y=0;y<h;y+=cs)for(int x=0;x<w;x+=cs){
        int cx=x+rand()%cs,cy=y+rand()%cs;if(cx>=w)cx=w-1;if(cy>=h)cy=h-1;
        uint8_t clr[4]={s[cy*st+cx*4],s[cy*st+cx*4+1],s[cy*st+cx*4+2],s[cy*st+cx*4+3]};
        for(int dy=0;dy<cs&&y+dy<h;dy++)for(int dx=0;dx<cs&&x+dx<w;dx++)memcpy(d+(y+dy)*st+(x+dx)*4,clr,4);
    }
}
