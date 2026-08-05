package com.alight.motion.figma
import com.alight.motion.model.*;import kotlinx.coroutines.*;import okhttp3.*;import org.json.JSONObject
class FigmaImporter(private val token:String){private val cl=OkHttpClient()
    suspend fun import(fileId:String)=withContext(Dispatchers.IO){val res=mutableListOf<Track>()
        try{val r=cl.newCall(Request.Builder().url("https://api.figma.com/v1/files/$fileId").header("X-Figma-Token",token).build()).execute()
            if(!r.isSuccessful)return@withContext res
            val doc=JSONObject(r.body!!.string()).optJSONObject("document")?:return@withContext res
            val t=Track(name="Figma:"+fileId);parseChildren(doc,t.layers);if(t.layers.isNotEmpty())res.add(t)}catch(_:Exception){}
        res}
    private fun parseChildren(n:JSONObject,layers:MutableList<Layer>){n.optJSONArray("children")?.let{a->for(i in 0 until a.length()){val c=a.getJSONObject(i)
        val type=c.optString("type");val abs=c.optJSONObject("absoluteBoundingBox")
        when{if(abs!=null&&type in listOf("RECTANGLE","ELLIPSE","POLYGON","STAR","VECTOR")){
            val clr=parseColor(c.optJSONArray("fills"));val sw=c.optDouble("strokeWeight",0.0).toFloat();val cr=c.optDouble("cornerRadius",0.0).toFloat()
            layers.add(ShapeLayer(name=c.optString("name",type),duration=5f,transform=Transform((abs.optDouble("x")+abs.optDouble("width")/2).toFloat(),(abs.optDouble("y")+abs.optDouble("height")/2).toFloat()),shapeType=when(type){"ELLIPSE"->ShapeType.CIRCLE;"POLYGON"->ShapeType.POLYGON;"STAR"->ShapeType.STAR;"VECTOR"->ShapeType.SVG_PATH;else->ShapeType.RECT},fillColor=clr,strokeWidth=sw,cornerRadius=cr))}
        if(abs!=null&&type=="TEXT"){val style=c.optJSONObject("style")?:continue
            layers.add(TextLayer(name=c.optString("name","Text"),duration=5f,transform=Transform((abs.optDouble("x")+abs.optDouble("width")/2).toFloat(),(abs.optDouble("y")+abs.optDouble("height")/2).toFloat()),text=c.optString("characters",""),fontSize=style.optDouble("fontSize",24.0).toFloat(),fontColor=parseColor(c.optJSONArray("fills")),fontFamily=style.optString("fontFamily","default")))}
        if(type in listOf("FRAME","GROUP","COMPONENT","INSTANCE"))parseChildren(c,layers)}}}
    private fun parseColor(fills:Any?):Long=try{val a=(fills as? org.json.JSONArray)?.optJSONObject(0);if(a!=null){val c=a.optJSONObject("color")?:return 0xFFFFFFFF;return (((a.optDouble("opacity",1.0)*255).toInt().toLong() shl 24)or((c.optDouble("r")*255).toInt().toLong() shl 16)or((c.optDouble("g")*255).toInt().toLong() shl 8)or(c.optDouble("b")*255).toInt().toLong())}}catch(_:Exception){};0xFFFFFFFF
}
