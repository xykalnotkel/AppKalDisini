#pragma once
#include <vector>
#include "../gl/renderer.hpp"
struct Particle{float x,y,vx,vy,life,ml,r,g,b,sz,rot,rs;};
class ParticleEngine{
public:
    ParticleEngine(int m):max(m){parts.reserve(m);}
    void update(float dt);void render();
    void emit(float x,float y,int n,float spd,float life,float r,float g,float b,float sz,float spread,float grav);
    void setEmitter(float x,float y,float rate,bool a){emX=x;emY=y;emR=rate;emA=a;}
private:
    std::vector<Particle> parts;int max;float emX=0,emY=0,emR=10,emAcc=0;bool emA=false;
};
