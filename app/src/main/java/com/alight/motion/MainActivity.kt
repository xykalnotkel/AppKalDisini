package com.alight.motion

import android.os.Bundle; import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent; import androidx.activity.enableEdgeToEdge
import com.alight.motion.editor.EditorScreen; import com.alight.motion.model.*

class MainActivity:ComponentActivity(){
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState);enableEdgeToEdge();setContent{EditorScreen(createDemo())}
    }
    private fun createDemo()=Project(name="Alight Motion Demo",width=1920,height=1080,fps=30,duration=10f,
        tracks=mutableListOf(
            Track(name="Video",layers=mutableListOf(VideoLayer(name="Background",duration=10f,transform=Transform(960f,540f)))),
            Track(name="Text",layers=mutableListOf(
                TextLayer(name="Title",text="ALIGHT MOTION",fontSize=96f,fontColor=0xFFFFFFFF,duration=5f,transform=Transform(960f,400f),
                    keyframes=mutableListOf(Keyframe(time=0f,property=KeyframeProperty.SCALE_X,value=0f,easing=EasingType.BOUNCE),Keyframe(time=1f,property=KeyframeProperty.SCALE_X,value=1f))),
                TextLayer(name="Subtitle",text="Motion Graphics Editor",fontSize=42f,fontColor=0xFF5C9CFF,startTime=2f,duration=6f,transform=Transform(960f,540f)))),
            Track(name="Shapes",layers=mutableListOf(
                ShapeLayer(name="Circle",shapeType=ShapeType.CIRCLE,fillColor=0xFFFF6644,startTime=1f,duration=4f,transform=Transform(960f,700f),keyframes=mutableListOf(Keyframe(time=0f,property=KeyframeProperty.ROTATION,value=0f),Keyframe(time=4f,property=KeyframeProperty.ROTATION,value=360f)),effects=mutableListOf(Effect(type=EffectType.GLOW,params=mutableMapOf("intensity" to 60f,"threshold" to 25f)))),
                ShapeLayer(name="Star",shapeType=ShapeType.STAR,fillColor=0xFFFFDD44,startTime=5f,duration=4f,transform=Transform(1520f,700f)),
                NullLayer(name="Controller",duration=10f,transform=Transform(200f,200f)),
                CameraLayer(name="Main Camera",duration=10f,transform=Transform(960f,540f)))
        ))
}
