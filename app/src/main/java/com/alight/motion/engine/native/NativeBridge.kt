package com.alight.motion.engine.native
import android.graphics.Bitmap
object NativeBridge {
    init { System.loadLibrary("alightmotion") }
    external fun nativeInit(w:Int,h:Int);external fun nativeRelease();external fun nativeResize(w:Int,h:Int)
    external fun nativeRenderFrame();external fun nativeClearColor(r:Float,g:Float,b:Float,a:Float);external fun nativeFlush()
    external fun nativeDrawRect(x:Float,y:Float,w:Float,h:Float,r:Float,g:Float,b:Float,a:Float,rot:Float,sx:Float,sy:Float,cr:Float,op:Float)
    external fun nativeDrawCircle(cx:Float,cy:Float,rad:Float,r:Float,g:Float,b:Float,a:Float,rot:Float,sx:Float,sy:Float,op:Float)
    external fun nativeDrawTriangle(x1:Float,y1:Float,x2:Float,y2:Float,x3:Float,y3:Float,r:Float,g:Float,b:Float,a:Float,op:Float)
    external fun nativeDrawStar(cx:Float,cy:Float,ir:Float,orad:Float,pts:Int,r:Float,g:Float,b:Float,a:Float,rot:Float,op:Float)
    external fun nativeDrawText(text:String,x:Float,y:Float,fs:Float,r:Float,g:Float,b:Float,a:Float,rot:Float,sx:Float,sy:Float,op:Float)
    external fun nativeDrawImage(bmp:Bitmap,x:Float,y:Float,w:Float,h:Float,rot:Float,sx:Float,sy:Float,op:Float)
    external fun nativeDrawSVGPath(d:String,x:Float,y:Float,sc:Float,r:Float,g:Float,b:Float,a:Float,sw:Float,sr:Float,sg:Float,sb:Float,sa:Float,op:Float,fill:Boolean)
    external fun nativeApplyBlur(src:Bitmap,dst:Bitmap,r:Float)
    external fun nativeApplyGlow(src:Bitmap,dst:Bitmap,i:Float,th:Float)
    external fun nativeApplyColorCorrection(src:Bitmap,dst:Bitmap,sat:Float,bri:Float,con:Float,temp:Float,vib:Float,exp:Float)
    external fun nativeApplyChromaKey(src:Bitmap,dst:Bitmap,kr:Float,kg:Float,kb:Float,th:Float,sp:Float)
    external fun nativeApplyDistortion(src:Bitmap,dst:Bitmap,type:Int,amt:Float)
    external fun nativeParticleUpdate(dt:Float);external fun nativeParticleRender()
    external fun nativeParticleEmit(x:Float,y:Float,n:Int,spd:Float,lf:Float,r:Float,g:Float,b:Float,sz:Float,spread:Float,grav:Float)
    external fun nativeParticleSetEmitter(x:Float,y:Float,rate:Float,active:Boolean)
    external fun nativeParseFigmaJSON(json:String):String
    external fun nativeAudioLoadFile(path:String);external fun nativeAudioGetWaveform(n:Int):FloatArray?
    external fun nativeAudioGetDuration():Float;external fun nativeAudioGetAmplitude(time:Float):Float
    external fun nativeOpenVideo(path:String):Boolean;external fun nativeOpenAudio(path:String):Boolean
    external fun nativeGetVideoWidth():Int;external fun nativeGetVideoHeight():Int;external fun nativeGetVideoDuration():Double
}
