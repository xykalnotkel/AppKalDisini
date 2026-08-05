package com.alight.motion.editor

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.alight.motion.engine.keyframe.KeyframeEngine
import com.alight.motion.engine.export.ExportEngine
import com.alight.motion.engine.export.ExpProg
import com.alight.motion.io.ProjectIO
import com.alight.motion.model.*
import kotlinx.coroutines.*


enum class EditorPanel(val label:String){TIMELINE("Timeline"),PROPERTIES("Properties"),EFFECTS("Effects"),FIGMA("Figma"),EXPORT("Export")}
enum class Tool{SELECT,PEN,SHAPE,TEXT,HAND}

@Composable fun EditorScreen(project:Project){
    val ctx= LocalContext.current
    var time by remember{mutableFloatStateOf(0f)};var playing by remember{mutableStateOf(false)}
    var panel by remember{mutableStateOf(EditorPanel.TIMELINE)};var selId by remember{mutableStateOf<String?>(null)}
    var tool by remember{mutableStateOf(Tool.SELECT)};var showResDialog by remember{mutableStateOf(false)}
    var showExportDialog by remember{mutableStateOf(false)};var exportProg by remember{mutableStateOf<ExpProg?>(null)}
    var showImport by remember{mutableStateOf(false)};var showSave by remember{mutableStateOf(false)}

    LaunchedEffect(playing){if(playing)while(playing&&time<project.duration){delay(16L);time=(time+0.016f).coerceAtMost(project.duration)};if(time>=project.duration){playing=false;time=0f}}
    val layer=remember(selId){project.tracks.flatMap{it.layers}.find{it.id==selId}}

    if(showResDialog)ResolutionDialog(project){showResDialog=false}
    if(showExportDialog)ExportDialog(project,exportProg,{showExportDialog=false}){s,out->
        val e=ExportEngine(project,s){exportProg=it}
        CoroutineScope(Dispatchers.Main).launch{e.run()}
    }
    if(showImport)ImportDialog{showImport=false}
    if(showSave){ProjectIO.save(ctx,project);showSave=false}

    Column(Modifier.fillMaxSize().background(Color(0xFF151518)).systemBarsPadding()){
        // TOOLBAR
        Surface(color=Color(0xFF202025),shadowElevation=4.dp){Row(Modifier.fillMaxWidth().height(44.dp).padding(horizontal=6.dp),verticalAlignment=Alignment.CenterVertically){
            IconButton({},Modifier.size(32.dp)){Icon(Icons.Filled.Menu,null,tint=Color(0xFFAAAAAA),modifier=Modifier.size(18.dp))}
            Column(Modifier.padding(horizontal=4.dp).weight(1f)){Text(project.name,color=Color(0xFFE0E0E0),fontSize=13.sp,fontWeight=FontWeight.SemiBold);Text("${project.width}x${project.height} ${project.fps}fps",color=Color(0xFF666670),fontSize=10.sp,fontFamily=FontFamily.Monospace)}
            Tool.entries.forEach{t->IconButton({tool=t},Modifier.size(30.dp)){Icon(when(t){Tool.SELECT->Icons.Filled.NearMe;Tool.PEN->Icons.Filled.Draw;Tool.SHAPE->Icons.Filled.Shapes;Tool.TEXT->Icons.Filled.TextFields;Tool.HAND->Icons.Filled.PanTool},t.name,tint=if(tool==t)Color(0xFF5C9CFF)else Color(0xFF6A6A72),modifier=Modifier.size(15.dp))}}
            VerticalDivider(Modifier.height(20.dp).padding(horizontal=4.dp),color=Color(0xFF2A2A30))
            EditorPanel.entries.forEach{p->TextButton({panel=p},Modifier.height(28.dp),contentPadding=PaddingValues(horizontal=6.dp)){Text(p.label,fontSize=9.sp,fontFamily=FontFamily.Monospace,letterSpacing=1.sp,color=if(panel==p)Color.White else Color(0xFF555560))}}
            IconButton({showImport=true},Modifier.size(28.dp)){Icon(Icons.Filled.Add,null,tint=Color(0xFF5C9CFF),modifier=Modifier.size(18.dp))}
            IconButton({showResDialog=true},Modifier.size(28.dp)){Icon(Icons.Filled.Settings,null,tint=Color(0xFF777780),modifier=Modifier.size(16.dp))}
            IconButton({showSave=true},Modifier.size(28.dp)){Icon(Icons.Filled.Save,null,tint=Color(0xFF777780),modifier=Modifier.size(16.dp))}
        }}

        Row(Modifier.weight(1f).fillMaxWidth()){
            // LEFT: Layer Tree
            Surface(color=Color(0xFF18181D),modifier=Modifier.width(220.dp)){Column(Modifier.fillMaxSize()){
                Row(Modifier.fillMaxWidth().padding(8.dp)){Text("LAYERS",color=Color(0xFF5A5A60),fontSize=10.sp,fontFamily=FontFamily.Monospace,letterSpacing=2.sp);Spacer(Modifier.weight(1f));IconButton({project.tracks.add(Track(name="Track ${project.tracks.size+1}"))},Modifier.size(22.dp)){Icon(Icons.Filled.Add,null,tint=Color(0xFF777780),modifier=Modifier.size(14.dp))}}
                Divider(color=Color(0xFF25252A));LazyColumn{
                    project.tracks.forEach{track->item{Column(Modifier.fillMaxWidth()){
                        Row(Modifier.fillMaxWidth().background(Color(0xFF1C1C24)).clickable{track.layers.firstOrNull()?.let{selId=it.id}}.padding(horizontal=8.dp,vertical=5.dp),verticalAlignment=Alignment.CenterVertically){
                            Icon(if(track.isVisible)Icons.Filled.Visibility else Icons.Filled.VisibilityOff,null,tint=Color(0xFF5A5A60),modifier=Modifier.size(11.dp));Spacer(Modifier.width(4.dp))
                            Text(track.name,color=Color(0xFFB0B0B8),fontSize=11.sp,fontWeight=FontWeight.Medium,modifier=Modifier.weight(1f))
                            Icon(if(track.isLocked)Icons.Filled.Lock else Icons.Filled.LockOpen,null,tint=Color(0xFF5A5A60),modifier=Modifier.size(10.dp))}
                        track.layers.forEach{l->Row(Modifier.fillMaxWidth().padding(start=20.dp,end=4.dp).clip(RoundedCornerShape(3.dp)).background(if(l.id==selId)Color(0xFF2A2A44)else Color.Transparent).clickable{selId=l.id}.padding(horizontal=6.dp,vertical=4.dp),verticalAlignment=Alignment.CenterVertically){
                            Icon(when(l){is VideoLayer->Icons.Filled.Videocam;is ImageLayer->Icons.Filled.Image;is TextLayer->Icons.Filled.TextFields;is ShapeLayer->Icons.Filled.Shapes;is NullLayer->Icons.Filled.Block;is CameraLayer->Icons.Filled.Videocam;is AdjustmentLayer->Icons.Filled.Tune;is GroupLayer->Icons.Filled.Folder;is FigmaLayer->Icons.Filled.DesignServices;else->Icons.Filled.Layers},null,tint=if(l.id==selId)Color(0xFF8899FF)else Color(0xFF7A7A80),modifier=Modifier.size(10.dp));Spacer(Modifier.width(4.dp))
                            Text(l.name,color=if(l.id==selId)Color.White else Color(0xFF999999),fontSize=10.sp,maxLines=1,modifier=Modifier.weight(1f));Text("%.1fs".format(l.duration),color=Color(0xFF4A4A50),fontSize=7.sp,fontFamily=FontFamily.Monospace)}}}}
                    item{Spacer(Modifier.height(8.dp))}}
            }}

            // CENTER: Preview Canvas
            Box(Modifier.weight(1f).fillMaxHeight().background(Color(0xFF0A0A0D)),contentAlignment=Alignment.Center){
                Text(formatTime(time),color=Color(0x99FFFFFF),fontSize=15.sp,fontFamily=FontFamily.Monospace,modifier=Modifier.align(Alignment.TopEnd).padding(12.dp))
                Text("${project.width}x${project.height} @ ${project.fps}fps",color=Color(0xFF3A3A40),fontSize=9.sp,fontFamily=FontFamily.Monospace,modifier=Modifier.align(Alignment.BottomCenter).padding(6.dp))
                Column(Modifier.horizontalScroll(rememberScrollState())){project.tracks.filter{it.isVisible}.forEach{track->track.layers.filter{it.isVisible&&time in it.startTime..it.startTime+it.duration}.forEach{l->
                    val tf=KeyframeEngine.evalTransform(l.keyframes,time-l.startTime,l.transform);val op=KeyframeEngine.evalOpacity(l.keyframes,time-l.startTime,l.opacity)
                    val clr=when(l){is TextLayer->Color(l.fontColor).copy(alpha=op);is ShapeLayer->Color(l.fillColor).copy(alpha=op);is VideoLayer->Color(0xFF4488FF).copy(alpha=op);is ImageLayer->Color(0xFF44CC44).copy(alpha=op);is NullLayer->Color(0xFF888888).copy(alpha=0.3f);is CameraLayer->Color(0xFF44FFFF).copy(alpha=0.5f);is AdjustmentLayer->Color(0xFFAA44AA).copy(alpha=0.4f);else->Color.Gray.copy(alpha=op)}
                    Surface(color=clr,shape=RoundedCornerShape(4.dp),modifier=Modifier.padding(2.dp)){Text("${l.name} [${tf.px.toInt()},${tf.py.toInt()}]",color=Color.White,fontSize=9.sp,fontFamily=FontFamily.Monospace,modifier=Modifier.padding(6.dp))}}}}
            }

            // RIGHT: Active panel
            AnimatedContent(panel,Modifier.width(260.dp)){p->Surface(color=Color(0xFF18181D)){Column(Modifier.fillMaxSize().padding(10.dp)){
                when(p){
                    EditorPanel.TIMELINE->InfoPanel(project)
                    EditorPanel.PROPERTIES->PropertiesPanel(layer)
                    EditorPanel.EFFECTS->EffectsPanel(layer)
                    EditorPanel.FIGMA->FigmaPanel(project)
                    EditorPanel.EXPORT->ExportConfigPanel(project){showExportDialog=true}
                }
            }}}
        }

        // TIMELINE BOTTOM
        if(panel==EditorPanel.TIMELINE)TimelinePanel(project.tracks,project.duration,time,selId,Modifier.height(140.dp)){selId=it}
        PlaybackBar(time,project.duration,playing){playing=!playing}
    }
}
fun formatTime(s:Float)="%02d:%02d.%02d".format((s/60).toInt(),(s%60).toInt(),((s%1)*100).toInt())
