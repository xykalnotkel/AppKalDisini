package com.siputzx.app.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.*
import java.io.IOException

// ─── SCREEN ───────────────────────────────────────────
sealed class S {
    object Home : S()
    data class Category(val tag: String, val name: String) : S()
    data class Endpoint(val tag: String, val name: String, val path: String, val params: List<Pair<String,String>>, val epName: String) : S()
}

// ─── DATA ─────────────────────────────────────────────
data class Cat(val tag: String, val name: String, val count: Int)

data class Ep(
    val path: String,
    val summary: String,
    val params: List<Pair<String,String>>,
    val tag: String
)

data class EpResult(
    val loading: Boolean = false,
    val ok: Boolean = false,
    val body: String? = null,
    val err: String? = null
)

// ─── HTTP CLIENT ─────────────────────────────────────
val httpClient = OkHttpClient.Builder()
    .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
    .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
    .build()

val JSON = "application/json; charset=utf-8".toMediaType()

// ─── MAIN APP ────────────────────────────────────────
@Composable
fun MainApp() {
    var screen by remember { mutableStateOf<S>(S.Home) }
    var specData by remember { mutableStateOf<Map<String, List<Ep>>?>(null) }
    var specError by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val req = Request.Builder()
                    .url("https://api.siputzx.my.id/api/openapi.json")
                    .header("Accept", "application/json")
                    .build()
                val res = httpClient.newCall(req).execute()
                if (!res.isSuccessful) { specError = "HTTP ${res.code}"; loading = false; return@withContext }
                val json = JSONObject(res.body!!.string())
                val paths = json.optJSONObject("paths") ?: JSONObject()
                val map = mutableMapOf<String, MutableList<Ep>>()
                for (key in paths.keys()) {
                    val methods = paths.optJSONObject(key) ?: continue
                    val get = methods.optJSONObject("get") ?: continue
                    val summary = get.optString("summary", key)
                    val tags = get.optJSONArray("tags")
                    val tag = tags?.optString(0) ?: "Other"
                    val paramsArr = get.optJSONArray("parameters")
                    val params = mutableListOf<Pair<String,String>>()
                    if (paramsArr != null) {
                        for (i in 0 until paramsArr.length()) {
                            val p = paramsArr.getJSONObject(i)
                            if (p.optString("in") == "query") {
                                val name = p.optString("name", "")
                                val desc = p.optString("description", "") ?: ""
                                val ex = p.opt("example")
                                val exStr = if (ex != null && ex !is JSONObject) ex.toString() else ""
                                params.add(name to exStr)
                            }
                        }
                    }
                    map.getOrPut(tag) { mutableListOf() }.add(Ep(key, summary, params, tag))
                }
                specData = map.mapValues { it.value.sortedBy { e -> e.summary } }.toSortedMap()
                loading = false
            } catch (e: Exception) {
                specError = e.message ?: "Network error"
                loading = false
            }
        }
    }

    when {
        loading -> LoadingView()
        specError != null -> ErrorView(specError!!) { loading = true; specError = null; specData = null }
        specData != null -> {
            when (val s = screen) {
                is S.Home -> {
                    val cats = specData!!.map { Cat(it.key, formatTag(it.key), it.value.size) }
                    HomeScreen(cats, specData!!.values.sumOf { it.size }) { screen = S.Category(it.tag, it.name) }
                }
                is S.Category -> {
                    val eps = specData!![s.tag] ?: emptyList()
                    CatScreen(s.name, s.tag, eps) { screen = it }
                }
                is S.Endpoint -> EndpointScreen(s) { screen = S.Category(s.tag, s.name) }
            }
        }
    }
}

fun formatTag(tag: String): String = when (tag.lowercase()) {
    "ai" -> "AI"
    "anime" -> "Anime"
    "berita" -> "Berita"
    "canvas" -> "Canvas"
    "downloader" -> "Downloader"
    "games" -> "Games"
    "info" -> "Info"
    "maker" -> "Maker"
    "primbon" -> "Primbon"
    "random" -> "Random"
    "search" -> "Search"
    "stalker" -> "Stalker"
    "sticker" -> "Sticker"
    "tools" -> "Tools"
    else -> tag
}

// ─── LOADING ─────────────────────────────────────────
@Composable
fun LoadingView() {
    Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(32.dp))
            Spacer(Modifier.height(16.dp))
            Text("Connecting...", color = Gray500, fontSize = 13.sp, fontFamily = FontFamily.Monospace)
        }
    }
}

@Composable
fun ErrorView(err: String, retry: () -> Unit) {
    Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Text("!", color = Gray500, fontSize = 48.sp, fontWeight = FontWeight.Thin)
            Spacer(Modifier.height(16.dp))
            Text("Connection Failed", color = White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Text(err, color = Gray500, fontSize = 12.sp, fontFamily = FontFamily.Monospace, modifier = Modifier.padding(top = 8.dp))
            Spacer(Modifier.height(24.dp))
            OutlinedButton(onClick = retry, shape = RoundedCornerShape(0.dp),
                border = BorderStroke(1.dp, Gray600),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = White)) {
                Text("Retry", fontFamily = FontFamily.Monospace)
            }
        }
    }
}

// ─── HOME ────────────────────────────────────────────
@Composable
fun HomeScreen(cats: List<Cat>, totalEp: Int, onCat: (Cat) -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.Black)) {
        // HEADER
        Column(Modifier.padding(24.dp)) {
            Spacer(Modifier.height(16.dp))
            Text("SIPUTZX", color = White, fontSize = 32.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text("API CLIENT", color = Gray500, fontSize = 14.sp, fontFamily = FontFamily.Monospace, letterSpacing = 4.sp)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                MiniStat("${cats.size}", "CATEGORIES", Modifier.weight(1f))
                MiniStat("$totalEp", "ENDPOINTS", Modifier.weight(1f))
                MiniStat("FREE", "NO KEY", Modifier.weight(1f))
            }
        }

        Divider(color = Gray800, thickness = 1.dp)

        Text("CATEGORIES", color = Gray500, fontSize = 11.sp, fontFamily = FontFamily.Monospace,
            letterSpacing = 3.sp, modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(cats) { cat ->
                Row(
                    Modifier.fillMaxWidth().clickable { onCat(cat) }.padding(horizontal = 24.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(cat.name, color = White, fontSize = 15.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                    Text("${cat.count}", color = Gray600, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                    Spacer(Modifier.width(12.dp))
                    Text("→", color = Gray700, fontSize = 16.sp)
                }
            }
            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun MiniStat(val_: String, label: String, mod: Modifier) {
    Column(mod.background(Gray900).padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(val_, color = White, fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        Text(label, color = Gray500, fontSize = 9.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
    }
}

// ─── CATEGORY SCREEN ─────────────────────────────────
@Composable
fun CatScreen(name: String, tag: String, eps: List<Ep>, onEp: (S) -> Unit) {
    Column(Modifier.fillMaxSize().background(Color.Black)) {
        // Header
        Row(Modifier.padding(horizontal = 8.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onEp(S.Home) }) { Icon(Icons.Filled.ArrowBack, "Back", tint = White) }
            Text(name, color = White, fontSize = 22.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(Modifier.width(8.dp))
            Text("${eps.size}", color = Gray500, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        }
        Divider(color = Gray800, thickness = 1.dp)

        LazyColumn {
            items(eps) { ep ->
                Row(
                    Modifier.fillMaxWidth().clickable {
                        onEp(S.Endpoint(tag, name, ep.path, ep.params, ep.summary))
                    }.padding(horizontal = 20.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(ep.summary, color = White, fontSize = 14.sp, fontWeight = FontWeight.Medium, maxLines = 1)
                        Text(ep.path, color = Gray600, fontSize = 11.sp, fontFamily = FontFamily.Monospace, maxLines = 1)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text("GET", color = Gray600, fontSize = 9.sp, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp)
                }
            }
            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}

// ─── ENDPOINT SCREEN ─────────────────────────────────
@Composable
fun EndpointScreen(s: S.Endpoint, back: () -> Unit) {
    var params by remember { mutableStateOf(s.params.associate { it.first to it.second }) }
    var result by remember { mutableStateOf<EpResult?>(null) }
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize().background(Color.Black)) {
        // Header
        Row(Modifier.padding(horizontal = 4.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = back) { Icon(Icons.Filled.ArrowBack, "Back", tint = White) }
            Text(s.epName, color = White, fontSize = 18.sp, fontWeight = FontWeight.Bold, maxLines = 1, modifier = Modifier.weight(1f))
        }
        Divider(color = Gray800, thickness = 1.dp)

        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp)) {
            // Path
            Surface(color = Gray900, shape = RoundedCornerShape(4.dp)) {
                Text(s.path, color = Gray400, fontSize = 12.sp, fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(12.dp))
            }
            Spacer(Modifier.height(20.dp))

            // Params
            if (s.params.isNotEmpty()) {
                Text("PARAMETERS", color = Gray500, fontSize = 11.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
                Spacer(Modifier.height(12.dp))
                s.params.forEach { (name, example) ->
                    OutlinedTextField(
                        value = params[name] ?: "",
                        onValueChange = { params = params + (name to it) },
                        label = { Text(name, color = Gray500, fontFamily = FontFamily.Monospace) },
                        placeholder = { Text(example.ifEmpty { "..." }, color = Gray700, fontFamily = FontFamily.Monospace) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(0.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Gray500, unfocusedBorderColor = Gray800,
                            focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent,
                            cursorColor = White, focusedTextColor = White, unfocusedTextColor = White
                        ),
                        singleLine = true
                    )
                    Spacer(Modifier.height(10.dp))
                }
            } else {
                Text("No parameters required", color = Gray600, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                Spacer(Modifier.height(16.dp))
            }

            // Execute
            Button(
                onClick = {
                    val filteredParams = params.filter { it.value.isNotEmpty() }
                    result = EpResult(loading = true)
                    scope.launch(Dispatchers.IO) {
                        try {
                            val urlBuilder = StringBuilder(s.path)
                            if (filteredParams.isNotEmpty()) {
                                urlBuilder.append("?")
                                filteredParams.forEach { (k, v) ->
                                    urlBuilder.append("$k=${java.net.URLEncoder.encode(v, "UTF-8")}&")
                                }
                            }
                            val url = "https://api.siputzx.my.id${urlBuilder.toString().removeSuffix("&")}"
                            val req = Request.Builder().url(url).header("Accept", "application/json").build()
                            val res = httpClient.newCall(req).execute()
                            val raw = res.body?.string() ?: ""
                            val pretty = try { JSONObject(raw).toString(2) } catch (_: Exception) {
                                try { JSONArray(raw).toString(2) } catch (_: Exception) { raw }
                            }
                            result = EpResult(ok = res.isSuccessful, body = pretty,
                                err = if (!res.isSuccessful) "HTTP ${res.code}" else null)
                        } catch (e: Exception) {
                            result = EpResult(err = e.message ?: "Error")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = result?.loading != true,
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White, contentColor = Black,
                    disabledContainerColor = Gray800, disabledContentColor = Gray600
                )
            ) {
                if (result?.loading == true) {
                    CircularProgressIndicator(color = Black, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                }
                Text("EXECUTE", fontSize = 13.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }

            // Response
            if (result?.body != null || result?.err != null) {
                Spacer(Modifier.height(20.dp))
                Text("RESPONSE", color = Gray500, fontSize = 11.sp, fontFamily = FontFamily.Monospace, letterSpacing = 2.sp)
                Spacer(Modifier.height(10.dp))
                Surface(
                    color = if (result!!.ok) Gray900 else Color(0xFF1A0000),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(
                            if (result!!.ok) "200 OK" else result!!.err ?: "ERROR",
                            color = if (result!!.ok) Gray500 else Color(0xFFFF4444),
                            fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(10.dp))
                        SelectionContainer {
                            Text(
                                text = result!!.body ?: result!!.err ?: "",
                                color = if (result!!.ok) Gray300 else Color(0xFFFF6666),
                                fontSize = 12.sp, fontFamily = FontFamily.Monospace, lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}
