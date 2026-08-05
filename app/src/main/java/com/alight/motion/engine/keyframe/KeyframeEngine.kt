package com.alight.motion.engine.keyframe
import com.alight.motion.model.*
import kotlin.math.*

object KeyframeEngine {
    data class TR(val px:Float,val py:Float,val sx:Float,val sy:Float,val rot:Float)
    
    fun evalTransform(kfs:List<Keyframe>,time:Float,def:Transform):TR{
        return TR(
            px=eval(kfs,KeyframeProperty.POS_X,time,def.posX),
            py=eval(kfs,KeyframeProperty.POS_Y,time,def.posY),
            sx=eval(kfs,KeyframeProperty.SCALE_X,time,def.scaleX),
            sy=eval(kfs,KeyframeProperty.SCALE_Y,time,def.scaleY),
            rot=eval(kfs,KeyframeProperty.ROTATION,time,def.rotation))
    }

    fun evalOpacity(kfs:List<Keyframe>,time:Float,def:Float):Float{
        return eval(kfs,KeyframeProperty.OPACITY,time,def)
    }

    fun eval(kfs:List<Keyframe>,prop:KeyframeProperty,time:Float,def:Float):Float{
        val rel=kfs.filter{it.property==prop}.sortedBy{it.time}
        if(rel.isEmpty())return def
        if(rel.size==1)return rel.first().value
        if(time<=rel.first().time)return rel.first().value
        if(time>=rel.last().time)return rel.last().value
        for(i in 0 until rel.size-1){
            val a=rel[i];val b=rel[i+1]
            if(time in a.time..b.time){
                val t=((time-a.time)/(b.time-a.time)).coerceIn(0f,1f)
                val eased:Float=when(a.easing){
                    EasingType.LINEAR->t
                    EasingType.EASE_IN->t*t
                    EasingType.EASE_OUT->t*(2f-t)
                    EasingType.EASE_IN_OUT->if(t<0.5f)2f*t*t else(-1f+(4f-2f*t)*t)
                    EasingType.BOUNCE->{val n=7.5625f;val d=2.75f;when{t<1f/d->n*t*t;t<2f/d->n*(t-1.5f/d)*(t-1.5f/d)+0.75f;t<2.5f/d->n*(t-2.25f/d)*(t-2.25f/d)+0.9375f;else->n*(t-2.625f/d)*(t-2.625f/d)+0.984375f}}
                    EasingType.ELASTIC->if(t==0f||t==1f)t else (2.0f.pow(-10f*t)* sin((t-1f)*2f* PI/0.3f)+1f).toFloat()
                    EasingType.BACK_IN->{val c=1.70158f;(c+1f)*t*t*t-c*t*t}
                    EasingType.BACK_OUT->{val c=1.70158f;1f+(c+1f)*(t-1f)*(t-1f)*(t-1f)+c*(t-1f)*(t-1f)}
                }
                return a.value+(b.value-a.value)*eased
            }
        }
        return def
    }
}
