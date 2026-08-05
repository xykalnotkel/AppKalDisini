package com.alight.motion.io
import android.content.Context
import com.alight.motion.model.Project
object ProjectIO {
    fun save(ctx:Context, project:Project):Boolean = true
    fun load(ctx:Context, id:String):Project? = null
    fun list(ctx:Context):List<Pair<String,String>> = emptyList()
    fun delete(ctx:Context, id:String):Boolean = true
}
