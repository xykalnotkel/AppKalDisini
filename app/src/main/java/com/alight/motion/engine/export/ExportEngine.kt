package com.alight.motion.engine.export

import android.media.*
import android.media.MediaCodecInfo.CodecCapabilities
import com.alight.motion.engine.keyframe.KeyframeEngine
import com.alight.motion.model.*
import kotlinx.coroutines.*

data class ExpProg(val pct:Float=0f,val frame:Int=0,val total:Int=0,val done:Boolean=false,val err:String?=null)

class ExportEngine(private val p:Project,private val s:ExportSettings,private val cb:(ExpProg)->Unit){
    suspend fun run()=withContext(Dispatchers.IO){
        try{val tf=(p.duration*s.fps).toInt();val br=(s.width*s.height*s.fps*(s.bitrate*100000).toInt()).coerceIn(1_000_000,200_000_000)
            val mime=when(s.codec){VideoCodec.H264->MediaFormat.MIME_TYPE_AVC;VideoCodec.H265->MediaFormat.MIME_TYPE_HEVC;VideoCodec.VP8->MediaFormat.MIME_TYPE_VP8;else->MediaFormat.MIME_TYPE_AVC}
            val fmt=MediaFormat.createVideoFormat(mime,s.width,s.height).apply{setInteger(MediaFormat.KEY_BIT_RATE,br);setInteger(MediaFormat.KEY_FRAME_RATE,s.fps);setInteger(MediaFormat.KEY_I_FRAME_INTERVAL,1);setInteger(MediaFormat.KEY_COLOR_FORMAT,CodecCapabilities.COLOR_FormatSurface)}
            val enc=MediaCodec.createEncoderByType(mime).apply{configure(fmt,null,null,MediaCodec.CONFIGURE_FLAG_ENCODE)}
            val mux=MediaMuxer(s.outputPath,MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            val surf=enc.createInputSurface();enc.start();var ti=-1;var ms=false
            for(f in 0 until tf){if(!isActive)break
                val c=surf.lockCanvas(null);if(c!=null){c.drawColor(p.bgColor.toInt());drawFrame(c,p,f.toFloat()/s.fps);surf.unlockCanvasAndPost(c)}
                drain(enc,mux,ms,false){ti=it;ms=true};cb(ExpProg(f.toFloat()/tf,f,tf))}
            drain(enc,mux,ms,true){ti=it;ms=true};cb(ExpProg(1f,tf,tf,true))
            enc.stop();enc.release();mux.stop();mux.release()}catch(e:Exception){cb(ExpProg(err=e.message))}
    }
    private fun drawFrame(c:android.graphics.Canvas,p:Project,t:Float){p.tracks.filter{it.isVisible}.forEach{track->track.layers.filter{it.isVisible&&t in it.startTime..it.startTime+it.duration}.forEach{drawLayer(c,it,t-it.startTime)}}}
    private fun drawLayer(c:android.graphics.Canvas,l:Layer,lt:Float){c.save()
        try{val tf=KeyframeEngine.evalTransform(l.keyframes,lt,l.transform);val op=KeyframeEngine.evalOpacity(l.keyframes,lt,l.opacity)
            c.translate(tf.px,tf.py);c.scale(tf.sx,tf.sy);c.rotate(tf.rot);c.saveLayerAlpha(null,(op*255).toInt().coerceIn(0,255))
            when(l){is ShapeLayer->{val p=android.graphics.Paint().apply{color=l.fillColor.toInt();style=android.graphics.Paint.Style.FILL;isAntiAlias=true}
                when(l.shapeType){ShapeType.RECT->c.drawRoundRect(-50f,-50f,50f,50f,l.cornerRadius,l.cornerRadius,p);ShapeType.CIRCLE->c.drawCircle(0f,0f,50f,p);else->c.drawRect(-50f,-50f,50f,50f,p)}}
                is TextLayer->{val p=android.graphics.Paint().apply{color=l.fontColor.toInt();textSize=l.fontSize;isAntiAlias=true;textAlign=android.graphics.Paint.Align.CENTER};c.drawText(l.text,0f,0f,p)}}
            c.restore()}finally{c.restore()}}
    private fun drain(e:MediaCodec,m:MediaMuxer,started:Boolean,eos:Boolean=false,cb:(Int)->Unit){
        val bi=MediaCodec.BufferInfo()
        while(true){val i=e.dequeueOutputBuffer(bi,10000)
            if(i==MediaCodec.INFO_TRY_AGAIN_LATER){if(!eos)break}
            else if(i==MediaCodec.INFO_OUTPUT_FORMAT_CHANGED){if(!started)cb(m.addTrack(e.outputFormat))}
            else if(i>=0){val b=e.getOutputBuffer(i)?:continue
                if(bi.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG!=0)bi.size=0
                if(bi.size>0&&started){b.position(bi.offset);b.limit(bi.offset+bi.size);m.writeSampleData(0,b,bi)}
                e.releaseOutputBuffer(i,false);if(bi.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM!=0)break}}
    }
}
