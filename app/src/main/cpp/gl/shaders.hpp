#pragma once
#define SOLID_VS "attribute vec4 aPos;uniform mat4 uMVP;void main(){gl_Position=uMVP*vec4(aPos.xy,0,1);}"
#define SOLID_FS "precision mediump float;uniform vec4 uColor;void main(){gl_FragColor=uColor;}"
#define TEX_VS "attribute vec4 aPos;attribute vec2 aTex;varying vec2 vTex;uniform mat4 uMVP;void main(){gl_Position=uMVP*vec4(aPos.xy,0,1);vTex=aTex;}"
#define TEX_FS "precision mediump float;varying vec2 vTex;uniform sampler2D uTex;uniform float uOpacity;void main(){gl_FragColor=texture2D(uTex,vTex)*uOpacity;}"
#define SDF_VS TEX_VS
#define SDF_FS "precision mediump float;varying vec2 vTex;uniform sampler2D uTex;uniform vec4 uColor;uniform float uSmooth;void main(){float d=texture2D(uTex,vTex).a;float a=smoothstep(0.5-uSmooth,0.5+uSmooth,d);gl_FragColor=vec4(uColor.rgb,uColor.a*a);}"
#define PATH_VS SOLID_VS
#define PATH_FS "precision mediump float;uniform vec4 uFill;uniform vec4 uStroke;uniform float uStrokeW;uniform float uIsFill;void main(){gl_FragColor=uIsFill>0.5?uFill:uStroke;}"
