package com.alight.motion.editor

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import com.alight.motion.model.*

// ═══ INFO PANEL ═══
@Composable fun InfoPanel(project:Project){
    Text("PROJECT INFO",color=Color(0xFFB0B0B8),fontSize=11.sp,fontWeight=FontWeight.Bold,fontFamily=FontFamily.Monospace,letterSpacing=2.sp)
    Spacer(Modifier.height(10.dp));PR("Name",project.name);PR("Resolution","${project.width}x${project.height}");PR("FPS","${project.fps}")
    PR("Duration","%.2fs".format(project.duration));PR("Tracks","${project.tracks.size}");PR("Layers","${project.tracks.sumOf{it.layers.size}}")
    PR("Audio clips","${project.audioClips.size}")
    Spacer(Modifier.height(10.dp));Text("IMPORT",color=Color(0xFF5C9CFF),fontSize=9.sp,fontFamily=FontFamily.Monospace,letterSpacing=1.sp)
    Text(IMPORT_FORMATS.joinToString(", "),color=Color(0xFF6A6A70),fontSize=9.sp,fontFamily=FontFamily.Monospace)
}

// ═══ PROPERTIES ═══
@Composable fun PropertiesPanel(layer:Layer?){
    Text("PROPERTIES",color=Color(0xFFB0B0B8),fontSize=11.sp,fontWeight=FontWeight.Bold,fontFamily=FontFamily.Monospace,letterSpacing=2.sp)
    Spacer(Modifier.height(10.dp))
    if(layer==null){Text("Select a layer",color=Color(0xFF5A5A60),fontSize=11.sp);return}
    Text(layer.name,color=Color(0xFFD0D0D8),fontSize=14.sp,fontWeight=FontWeight.Medium)
    Text(layer::class.simpleName?:"Layer",color=Color(0xFF6A6A70),fontSize=10.sp,fontFamily=FontFamily.Monospace)
    Spacer(Modifier.height(12.dp))
    PS("Transform"){PR("Position","${layer.transform.posX.toInt()},${layer.transform.posY.toInt()}");PR("Scale","${layer.transform.scaleX}x ${layer.transform.scaleY}x");PR("Rotation","${layer.transform.rotation}deg");PR("Opacity","${(layer.opacity*100).toInt()}%")}
    PS("Timing"){PR("Start","%.2fs".format(layer.startTime));PR("Duration","%.2fs".format(layer.duration));PR("End","%.2fs".format(layer.startTime+layer.duration))}
    PS("Blending"){PR("Mode",layer.blending.label)}
    if(layer is ShapeLayer)PS("Shape"){PR("Type",layer.shapeType.name);PR("Corner","%.1f".format(layer.cornerRadius));PR("Stroke","%.1f".format(layer.strokeWidth))}
    if(layer is TextLayer)PS("Text"){PR("Font","%.0f".format(layer.fontSize));PR("Content",layer.text.take(40))}
    if(layer is VideoLayer)PS("Video"){PR("Speed","${layer.speed}x");PR("Volume","${(layer.volume*100).toInt()}%");PR("Source",layer.sourceUri.take(30))}
    PS("Animation"){PR("Keyframes","${layer.keyframes.size}")};PS("Effects"){PR("Active","${layer.effects.count{it.enabled}}/${layer.effects.size}")}
}

// ═══ EFFECTS ═══
@Composable fun EffectsPanel(layer:Layer?){
    var exp by remember{mutableStateOf<EffectType?>(null)}
    Text("EFFECTS",color=Color(0xFFB0B0B8),fontSize=11.sp,fontWeight=FontWeight.Bold,fontFamily=FontFamily.Monospace,letterSpacing=2.sp)
    Spacer(Modifier.height(10.dp))
    if(layer==null){Text("Select a layer",color=Color(0xFF5A5A60),fontSize=11.sp);return}
    EffectCat.entries.forEach{cat->val eps=EffectType.entries.filter{it.cat==cat}
        if(eps.isNotEmpty()){Text(cat.name,color=Color(0xFF5A5A60),fontSize=9.sp,fontFamily=FontFamily.Monospace,letterSpacing=1.sp,modifier=Modifier.padding(top=4.dp))
            eps.forEach{et->val active=layer.effects.any{it.type==et};val eff=layer.effects.find{it.type==et}
                Surface(color=if(active)Color(0xFF252535)else Color(0xFF1C1C22),shape=RoundedCornerShape(4.dp),modifier=Modifier.fillMaxWidth().padding(vertical=1.dp).clickable{if(active)exp=if(exp==et)null else et else{layer.effects.add(Effect(type=et));exp=et}}){
                    Column{Row(Modifier.padding(8.dp),verticalAlignment=Alignment.CenterVertically){Icon(if(active)Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank,null,tint=if(active)Color(0xFF5C9CFF)else Color(0xFF4A4A50),modifier=Modifier.size(14.dp));Spacer(Modifier.width(6.dp));Text(et.label,color=if(active)Color.White else Color(0xFF7A7A80),fontSize=11.sp,modifier=Modifier.weight(1f));if(active)Text("v",color=Color(0xFF5A5A60),fontSize=8.sp)}
                    AnimatedVisibility(exp==et&&active){Column(Modifier.padding(horizontal=12.dp,vertical=4.dp)){val p=eff?.params?: mutableMapOf()
                        when(et){EffectType.BLUR->{EF("Radius",p,"radius",0f,100f,5f);EF("Quality",p,"quality",0f,2f,1f)}
                            EffectType.GLOW->{EF("Intensity",p,"intensity",0f,100f,50f);EF("Threshold",p,"threshold",0f,100f,30f)}
                            EffectType.DROP_SHADOW->{EF("Distance",p,"dist",0f,50f,5f);EF("Angle",p,"angle",0f,360f,135f);EF("Blur",p,"blur",0f,50f,5f);EF("Opacity",p,"opacity",0f,100f,50f)}
                            EffectType.CHROMA_KEY->{EF("Threshold",p,"th",0f,100f,30f);EF("Feather",p,"feather",0f,100f,10f);EF("Spill",p,"spill",0f,100f,0f)}
                            EffectType.NOISE->{EF("Amount",p,"amt",0f,100f,10f);ET("Monochrome",p,"mono")}
                            EffectType.VIGNETTE->{EF("Amount",p,"amt",0f,100f,50f);EF("Feather",p,"feather",0f,100f,30f)}
                            EffectType.SHARPEN->EF("Amount",p,"amt",0f,100f,20f)
                            EffectType.PIXELATE->EF("Size",p,"size",1f,50f,5f)
                            EffectType.INVERT->ET("On",p,"on")
                            EffectType.POSTERIZE->EF("Levels",p,"lv",2f,32f,8f)
                            EffectType.HALFTONE->{EF("Size",p,"sz",1f,20f,5f);EF("Angle",p,"ang",0f,360f,45f)}
                            EffectType.MOTION_BLUR->{EF("Intensity",p,"i",0f,100f,30f);EF("Angle",p,"ang",0f,360f,0f)}
                            EffectType.EDGE_GLOW->{EF("Intensity",p,"i",0f,100f,50f);EF("Threshold",p,"th",0f,100f,20f)}
                            EffectType.CRYSTALLIZE->EF("Size",p,"sz",2f,50f,10f)
                            else->Text("Settings...",color=Color(0xFF5A5A60),fontSize=10.sp)
                        }}}}}}}}
}

// ═══ FIGMA ═══
@Composable fun FigmaPanel(project:Project){
    var fid by remember{mutableStateOf("")};var tok by remember{mutableStateOf("")}
    Text("FIGMA IMPORT",color=Color(0xFFB0B0B8),fontSize=11.sp,fontWeight=FontWeight.Bold,fontFamily=FontFamily.Monospace,letterSpacing=2.sp)
    Spacer(Modifier.height(10.dp));Text("Import Figma designs as layers",color=Color(0xFF6A6A70),fontSize=10.sp);Spacer(Modifier.height(8.dp))
    OutlinedTextField(fid,{fid=it},label={Text("File ID")},modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(6.dp),colors=fldColors(),singleLine=true)
    Spacer(Modifier.height(4.dp));OutlinedTextField(tok,{tok=it},label={Text("Access Token")},modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(6.dp),colors=fldColors(),singleLine=true)
    Spacer(Modifier.height(10.dp));Button({},Modifier.fillMaxWidth().height(38.dp),shape=RoundedCornerShape(6.dp),colors=ButtonDefaults.buttonColors(containerColor=Color(0xFF5C9CFF))){Text("IMPORT",fontSize=10.sp,fontFamily=FontFamily.Monospace,letterSpacing=2.sp)}
}

// ═══ EXPORT CONFIG ═══
@Composable fun ExportConfigPanel(project:Project,onExport:()->Unit){
    var fmt by remember{mutableStateOf(ExportFormat.MP4)};var w by remember{mutableIntStateOf(project.width)};var h by remember{mutableIntStateOf(project.height)}
    var fps by remember{mutableIntStateOf(project.fps)};var br by remember{mutableFloatStateOf(16f)};var q by remember{mutableIntStateOf(90)}
    Text("EXPORT",color=Color(0xFFB0B0B8),fontSize=11.sp,fontWeight=FontWeight.Bold,fontFamily=FontFamily.Monospace,letterSpacing=2.sp);Spacer(Modifier.height(10.dp))
    Text("Format",color=Color(0xFF5A5A60),fontSize=9.sp,fontFamily=FontFamily.Monospace);Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(4.dp)){ExportFormat.entries.forEach{f->FilterChip(fmt==f,{fmt=f},{Text(f.name,fontSize=10.sp,fontFamily=FontFamily.Monospace)},colors=FilterChipDefaults.filterChipColors(selectedContainerColor=Color(0xFF3A3A50)))}}
    Spacer(Modifier.height(6.dp));Text("Resolution",color=Color(0xFF5A5A60),fontSize=9.sp,fontFamily=FontFamily.Monospace)
    LazyColumn(Modifier.height(80.dp)){items(ResolutionPreset.ALL){r->Text("${r.name} (${r.w}x${r.h})",color=if(w==r.w&&h==r.h)Color.White else Color(0xFF6A6A70),fontSize=10.sp,modifier=Modifier.clickable{w=r.w;h=r.h}.padding(vertical=2.dp))}}
    Text("FPS",color=Color(0xFF5A5A60),fontSize=9.sp,fontFamily=FontFamily.Monospace);Row(Modifier.fillMaxWidth()){ALL_FPS.take(7).forEach{f->FilterChip(fps==f,{fps=f},{Text("$f",fontSize=9.sp,fontFamily=FontFamily.Monospace)},colors=FilterChipDefaults.filterChipColors(selectedContainerColor=Color(0xFF3A3A50)),modifier=Modifier.padding(1.dp))}}
    PR("Bitrate","${br.toInt()} Mbps");PR("Quality","$q%");Slider(br/50f,{br=it*50f},modifier=Modifier.fillMaxWidth().height(20.dp),colors=SliderDefaults.colors(thumbColor=Color(0xFF5C9CFF),activeTrackColor=Color(0xFF5C9CFF)))
    Spacer(Modifier.height(10.dp));Button(onExport,Modifier.fillMaxWidth().height(40.dp),shape=RoundedCornerShape(6.dp),colors=ButtonDefaults.buttonColors(containerColor=Color(0xFF5C9CFF))){Icon(Icons.Filled.FileDownload,null,modifier=Modifier.size(14.dp));Spacer(Modifier.width(4.dp));Text("EXPORT ${fmt.name}",fontSize=11.sp,fontFamily=FontFamily.Monospace,letterSpacing=2.sp)}
}

// ═══ TIMELINE ═══
@Composable fun TimelinePanel(tracks:List<Track>,dur:Float,time:Float,selId:String?,modifier:Modifier,onLayerClick:(String)->Unit){
    val pps=60f;val tw=(dur*pps).dp;val px=(time*pps).dp
    Surface(color=Color(0xFF18181E),modifier=modifier){Column(Modifier.fillMaxSize()){
        Box(Modifier.fillMaxWidth().height(22.dp).background(Color(0xFF202028))){Row(Modifier.horizontalScroll(rememberScrollState())){for(i in 0..dur.toInt())Text("${i}s",color=Color(0xFF4A4A50),fontSize=7.sp,fontFamily=FontFamily.Monospace,modifier=Modifier.width(pps.dp).padding(start=1.dp))};Box(Modifier.offset(x=px.dp).width(2.dp).fillMaxHeight().background(Color(0xFFFF4444)))}
        LazyColumn{items(tracks.size){i->val t=tracks[i];Box(Modifier.fillMaxWidth().height(48.dp).background(if(i%2==0)Color(0xFF14141A)else Color(0xFF1A1A20))){
            Text(t.name,color=Color(0xFF4A4A50),fontSize=8.sp,fontFamily=FontFamily.Monospace,modifier=Modifier.align(Alignment.CenterStart).padding(start=4.dp))
            Row(Modifier.horizontalScroll(rememberScrollState())){t.layers.forEach{l->val clr=when(l){is VideoLayer->Color(0xFF4455FF);is ImageLayer->Color(0xFF44AA44);is TextLayer->Color(0xFFDDDD44);is ShapeLayer->Color(0xFFDD4444);is NullLayer->Color(0xFF888888);is CameraLayer->Color(0xFF44FFFF);is AdjustmentLayer->Color(0xFFAA44AA);else->Color.Gray}
                Box(Modifier.offset(x=(l.startTime*pps).dp).width((l.duration*pps).dp).height(36.dp).padding(vertical=3.dp).clip(RoundedCornerShape(3.dp)).background(clr.copy(alpha=if(l.id==selId)0.9f else 0.45f)).border(1.dp,if(l.id==selId)Color.White else Color.Transparent,RoundedCornerShape(3.dp)).clickable{onLayerClick(l.id)}.padding(horizontal=5.dp),contentAlignment=Alignment.CenterStart){Text(l.name,color=Color.White,fontSize=9.sp,maxLines=1,fontFamily=FontFamily.Monospace)}};Spacer(Modifier.width(tw.dp))}
            Box(Modifier.offset(x=px.dp).width(2.dp).fillMaxHeight().background(Color(0xFFFF4444)))
        }}}
    }}
}

// ═══ PLAYBACK BAR ═══
@Composable fun PlaybackBar(time:Float,dur:Float,playing:Boolean,onPlayPause:()->Unit){
    Surface(color=Color(0xFF1A1A20),shadowElevation=8.dp){Row(Modifier.fillMaxWidth().height(38.dp).padding(horizontal=8.dp),verticalAlignment=Alignment.CenterVertically){
        IconButton({},Modifier.size(24.dp)){Icon(Icons.Filled.SkipPrevious,null,tint=Color(0xFFAAAAAA),modifier=Modifier.size(13.dp))}
        IconButton(onPlayPause,Modifier.size(32.dp)){Icon(if(playing)Icons.Filled.Pause else Icons.Filled.PlayArrow,null,tint=Color.White,modifier=Modifier.size(18.dp))}
        IconButton({},Modifier.size(24.dp)){Icon(Icons.Filled.SkipNext,null,tint=Color(0xFFAAAAAA),modifier=Modifier.size(13.dp))}
        Text(formatTime(time),color=Color(0xFFAAAAAA),fontSize=9.sp,fontFamily=FontFamily.Monospace,modifier=Modifier.padding(horizontal=4.dp))
        LinearProgressIndicator(progress={time/dur},modifier=Modifier.weight(1f).height(3.dp).clip(RoundedCornerShape(2.dp)),color=Color(0xFF5C9CFF),trackColor=Color(0xFF2A2A30))
        Text(formatTime(dur),color=Color(0xFFAAAAAA),fontSize=9.sp,fontFamily=FontFamily.Monospace,modifier=Modifier.padding(horizontal=4.dp))
    }}
}

// ═══ DIALOGS ═══
@Composable fun ResolutionDialog(project:Project,onDismiss:()->Unit){
    AlertDialog(onDismissRequest=onDismiss,title={Text("Project Settings",color=Color.White)},text={Column{
        Text("Resolution:",color=Color(0xFF5A5A60),fontSize=9.sp,fontFamily=FontFamily.Monospace);Spacer(Modifier.height(4.dp))
        LazyColumn(Modifier.height(120.dp)){items(ResolutionPreset.ALL){r->Text("${r.name} (${r.w}x${r.h})",color=if(project.width==r.w&&project.height==r.h)Color(0xFF5C9CFF)else Color(0xFF777780),fontSize=10.sp,modifier=Modifier.clickable{project.width=r.w;project.height=r.h;onDismiss()}.padding(vertical=2.dp))}}
        Spacer(Modifier.height(8.dp));Text("FPS: ${project.fps}",color=Color(0xFF777780),fontSize=10.sp,fontFamily=FontFamily.Monospace)
        Row(Modifier.fillMaxWidth()){ALL_FPS.forEach{f->FilterChip(project.fps==f,{project.fps=f},{Text("$f",fontSize=9.sp,fontFamily=FontFamily.Monospace)},colors=FilterChipDefaults.filterChipColors(selectedContainerColor=Color(0xFF3A3A50)),modifier=Modifier.padding(1.dp))}}
    }},confirmButton={TextButton(onDismiss){Text("OK")}},containerColor=Color(0xFF202025))
}
@Composable fun ExportDialog(project:Project,prog:ExpProg?,onDismiss:()->Unit,onStart:(ExportSettings,String)->Unit){
    var fmt by remember{mutableStateOf(ExportFormat.MP4)};var q by remember{mutableIntStateOf(90)}
    val outPath=// will use app external dir
    AlertDialog(onDismissRequest=onDismiss,title={Text("Export Video",color=Color.White)},text={Column{
        if(prog!=null){LinearProgressIndicator(progress={prog.pct},modifier=Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)));Spacer(Modifier.height(8.dp));Text("${(prog.pct*100).toInt()}% Frame ${prog.frame}/${prog.total}",color=Color(0xFF777780),fontSize=10.sp,fontFamily=FontFamily.Monospace)
            if(prog.err!=null)Text(prog.err,color=Color(0xFFFF4444),fontSize=10.sp);if(prog.done)Text("Export Complete!",color=Color(0xFF44CC44),fontSize=12.sp)}
        else{Text("Format:",color=Color(0xFF5A5A60),fontSize=9.sp,fontFamily=FontFamily.Monospace);Row(Modifier.fillMaxWidth()){ExportFormat.entries.forEach{f->FilterChip(fmt==f,{fmt=f},{Text(f.name,fontSize=10.sp,fontFamily=FontFamily.Monospace)})}}
            Spacer(Modifier.height(6.dp));Text("Quality: $q%",color=Color(0xFF777780),fontSize=10.sp,fontFamily=FontFamily.Monospace);Slider(q.toFloat()/100f,{q=(it*100).toInt()},colors=SliderDefaults.colors(thumbColor=Color(0xFF5C9CFF),activeTrackColor=Color(0xFF5C9CFF)))}
    }},confirmButton={TextButton({if(prog==null||prog.done||prog.err!=null)onDismiss()}){Text("CLOSE")}},containerColor=Color(0xFF202025))
    LaunchedEffect(Unit){if(prog==null){val s=ExportSettings(format=fmt,width=project.width,height=project.height,fps=project.fps,bitrate=16f,codec=VideoCodec.H264,quality=q,outputPath=outPath);onStart(s,outPath)}}
}
@Composable fun ImportDialog(onDismiss:()->Unit){
    AlertDialog(onDismissRequest=onDismiss,title={Text("Import Media",color=Color.White)},text={Text("Supported: PNG, JPG, MP4, WebM, SVG, GIF, WebP, HEIC, TIFF, EPS, AI, PSD, MOV, MKV, AVI\nAudio: WAV, MP3, AAC, OGG, FLAC\n\nTap Browse to select files",color=Color(0xFF6A6A70),fontSize=10.sp,fontFamily=FontFamily.Monospace)},confirmButton={TextButton(onDismiss){Text("Browse")}},containerColor=Color(0xFF202025))
}

// ═══ HELPERS ═══
@Composable fun PR(label:String,value:String){Row(Modifier.fillMaxWidth().padding(vertical=2.dp),horizontalArrangement=Arrangement.SpaceBetween){Text(label,color=Color(0xFF7A7A80),fontSize=10.sp,fontFamily=FontFamily.Monospace);Text(value,color=Color(0xFFCCCCCC),fontSize=10.sp,fontFamily=FontFamily.Monospace)}}
@Composable fun PS(title:String,content:@Composable ColumnScope.()->Unit){Text(title.uppercase(),color=Color(0xFF5C9CFF),fontSize=9.sp,fontFamily=FontFamily.Monospace,letterSpacing=2.sp,modifier=Modifier.padding(top=10.dp,bottom=4.dp));Column(Modifier.fillMaxWidth(),content=content)}
@Composable fun EF(label:String,pm:MutableMap<String,Float>,key:String,min:Float,max:Float,def:Float){val v=pm.getOrPut(key){def}
    Row(Modifier.fillMaxWidth().padding(vertical=1.dp),verticalAlignment=Alignment.CenterVertically){Text(label,color=Color(0xFF7A7A80),fontSize=9.sp,fontFamily=FontFamily.Monospace,modifier=Modifier.width(50.dp));Slider(v,{pm[key]=it},valueRange=min..max,modifier=Modifier.weight(1f).height(18.dp),colors=SliderDefaults.colors(thumbColor=Color(0xFF5C9CFF),activeTrackColor=Color(0xFF5C9CFF),inactiveTrackColor=Color(0xFF2A2A30)));Text("%.0f".format(v),color=Color(0xFF888888),fontSize=8.sp,fontFamily=FontFamily.Monospace,modifier=Modifier.width(26.dp))}}
@Composable fun ET(label:String,pm:MutableMap<String,Float>,key:String){val v=pm.getOrPut(key){0f};Row(Modifier.fillMaxWidth().padding(vertical=2.dp),verticalAlignment=Alignment.CenterVertically){Text(label,color=Color(0xFF7A7A80),fontSize=9.sp,modifier=Modifier.weight(1f));Switch(checked=v>0f,onCheckedChange={pm[key]=if(it)1f else 0f},colors=SwitchDefaults.colors(checkedThumbColor=Color(0xFF5C9CFF),checkedTrackColor=Color(0xFF3A3A60)))}}
@Composable fun fldColors()=OutlinedTextFieldDefaults.colors(focusedBorderColor=Color(0xFF5C9CFF),unfocusedBorderColor=Color(0xFF2A2A30),focusedTextColor=Color.White,unfocusedTextColor=Color.White,cursorColor=Color(0xFF5C9CFF))
@Composable 
