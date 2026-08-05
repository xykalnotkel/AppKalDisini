package com.alight.motion.io
import android.content.Context
import com.alight.motion.model.Project
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.io.File

object ProjectIO {
    private val json=Json{prettyPrint=true;ignoreUnknownKeys=true}
    fun save(ctx:Context,project:Project):Boolean=try{val data=json.encodeToString(project);val dir=File(ctx.filesDir,"projects");dir.mkdirs();File(dir,"${project.id}.alight").writeText(data);true}catch(_:Exception){false}
    fun load(ctx:Context,id:String):Project?=try{val dir=File(ctx.filesDir,"projects");val f=File(dir,"$id.alight");if(!f.exists())null else json.decodeFromString(f.readText())}catch(_:Exception){null}
    fun list(ctx:Context):List<Pair<String,String>>=try{File(ctx.filesDir,"projects").listFiles()?.map{it.nameWithoutExtension to it.nameWithoutExtension}?.filter{it.first.isNotEmpty()}?:emptyList()}catch(_:Exception){emptyList()}
    fun delete(ctx:Context,id:String):Boolean=try{File(ctx.filesDir,"projects/$id.alight").delete()}catch(_:Exception){false}
}
