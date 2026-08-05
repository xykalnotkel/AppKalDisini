package com.siputzx.app.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiService {

    // ==================== AI ====================
    @GET("api/ai/duckai")
    suspend fun duckAi(@Query("text") text: String): Response<ApiResponse>

    @GET("api/ai/bibleai")
    suspend fun bibleAi(@Query("text") text: String): Response<ApiResponse>

    @GET("api/ai/gptoss120b")
    suspend fun gptOss120b(@Query("text") text: String): Response<ApiResponse>

    @GET("api/ai/glm47flash")
    suspend fun glm47Flash(@Query("text") text: String): Response<ApiResponse>

    @GET("api/ai/phi2")
    suspend fun phi2(@Query("text") text: String): Response<ApiResponse>

    @GET("api/ai/qwq32b")
    suspend fun qwq32b(@Query("text") text: String): Response<ApiResponse>

    @GET("api/ai/deepseekr1")
    suspend fun deepseekR1(@Query("text") text: String): Response<ApiResponse>

    @GET("api/ai/metaai")
    suspend fun metaAi(@Query("text") text: String): Response<ApiResponse>

    @GET("api/ai/duckaiimage")
    suspend fun duckAiImage(@Query("text") text: String): Response<ApiResponse>

    @GET("api/ai/gemini")
    suspend fun gemini(@Query("text") text: String): Response<ApiResponse>

    @GET("api/ai/gita")
    suspend fun gita(@Query("text") text: String): Response<ApiResponse>

    // ==================== ANIME ====================
    @GET("api/s/animequotes")
    suspend fun animeQuotes(): Response<ApiResponse>

    @GET("api/anime/auratail-search")
    suspend fun auratailSearch(@Query("q") q: String): Response<ApiResponse>

    @GET("api/anime/auratail-latest")
    suspend fun auratailLatest(): Response<ApiResponse>

    @GET("api/anime/auratail-schedule")
    suspend fun auratailSchedule(): Response<ApiResponse>

    @GET("api/anime/auratail-detail")
    suspend fun auratailDetail(@Query("url") url: String): Response<ApiResponse>

    @GET("api/anime/otakudesu/ongoing")
    suspend fun otakudesuOngoing(): Response<ApiResponse>

    @GET("api/anime/otakudesu/search")
    suspend fun otakudesuSearch(@Query("q") q: String): Response<ApiResponse>

    @GET("api/anime/otakudesu/download")
    suspend fun otakudesuDownload(@Query("url") url: String): Response<ApiResponse>

    @GET("api/anime/otakudesu/detail")
    suspend fun otakudesuDetail(@Query("url") url: String): Response<ApiResponse>

    @GET("api/anime/anichin-episode")
    suspend fun anichinEpisode(@Query("url") url: String): Response<ApiResponse>

    @GET("api/anime/anichin-search")
    suspend fun anichinSearch(@Query("q") q: String): Response<ApiResponse>

    @GET("api/anime/anichin-download")
    suspend fun anichinDownload(@Query("url") url: String): Response<ApiResponse>

    @GET("api/anime/anichin-latest")
    suspend fun anichinLatest(): Response<ApiResponse>

    @GET("api/anime/anichin-popular")
    suspend fun anichinPopular(): Response<ApiResponse>

    @GET("api/anime/anichin-detail")
    suspend fun anichinDetail(@Query("url") url: String): Response<ApiResponse>

    @GET("api/anime/oploverz-episode")
    suspend fun oploverzEpisode(@Query("url") url: String): Response<ApiResponse>

    @GET("api/anime/oploverz-ongoing")
    suspend fun oploverzOngoing(): Response<ApiResponse>

    @GET("api/anime/oploverz-search")
    suspend fun oploverzSearch(@Query("q") q: String): Response<ApiResponse>

    @GET("api/anime/oploverz-download")
    suspend fun oploverzDownload(@Query("url") url: String): Response<ApiResponse>

    @GET("api/anime/komikindo-search")
    suspend fun komikindoSearch(@Query("q") q: String): Response<ApiResponse>

    @GET("api/anime/komikindo-detail")
    suspend fun komikindoDetail(@Query("url") url: String): Response<ApiResponse>

    @GET("api/anime/komikindo-download")
    suspend fun komikindoDownload(@Query("url") url: String): Response<ApiResponse>

    @GET("api/anime/samehadaku/search")
    suspend fun samehadakuSearch(@Query("q") q: String): Response<ApiResponse>

    @GET("api/anime/samehadaku/download")
    suspend fun samehadakuDownload(@Query("url") url: String): Response<ApiResponse>

    @GET("api/anime/samehadaku/latest")
    suspend fun samehadakuLatest(): Response<ApiResponse>

    @GET("api/anime/samehadaku/release")
    suspend fun samehadakuRelease(): Response<ApiResponse>

    @GET("api/anime/samehadaku/detail")
    suspend fun samehadakuDetail(@Query("url") url: String): Response<ApiResponse>

    // ==================== BERITA ====================
    @GET("api/berita/kompas")
    suspend fun beritaKompas(): Response<ApiResponse>

    @GET("api/berita/cnn")
    suspend fun beritaCnn(): Response<ApiResponse>

    @GET("api/berita/tribunnews")
    suspend fun beritaTribunnews(): Response<ApiResponse>

    @GET("api/berita/jkt48")
    suspend fun beritaJkt48(): Response<ApiResponse>

    @GET("api/berita/liputan6")
    suspend fun beritaLiputan6(): Response<ApiResponse>

    @GET("api/berita/suara")
    suspend fun beritaSuara(): Response<ApiResponse>

    @GET("api/berita/sindonews")
    suspend fun beritaSindonews(): Response<ApiResponse>

    @GET("api/berita/cnbcindonesia")
    suspend fun beritaCnbc(): Response<ApiResponse>

    @GET("api/berita/merdeka")
    suspend fun beritaMerdeka(): Response<ApiResponse>

    @GET("api/berita/antara")
    suspend fun beritaAntara(): Response<ApiResponse>

    // ==================== CANVAS ====================
    @GET("api/canvas/welcomev1")
    suspend fun canvasWelcomeV1(
        @Query("name") name: String,
        @Query("avatar") avatar: String,
        @Query("group") group: String,
        @Query("member") member: String
    ): Response<ApiResponse>

    @GET("api/canvas/captcha")
    suspend fun canvasCaptcha(@Query("text") text: String): Response<ApiResponse>

    @GET("api/canvas/goodbyev1")
    suspend fun canvasGoodbyeV1(@Query("name") name: String, @Query("avatar") avatar: String): Response<ApiResponse>

    @GET("api/canvas/profile")
    suspend fun canvasProfile(@QueryMap params: Map<String, String>): Response<ApiResponse>

    @GET("api/canvas/blur")
    suspend fun canvasBlur(@Query("url") url: String): Response<ApiResponse>

    @GET("api/canvas/security")
    suspend fun canvasSecurity(@Query("name") name: String, @Query("avatar") avatar: String): Response<ApiResponse>

    @GET("api/canvas/sertifikat-tolol")
    suspend fun canvasSertifikatTolol(@Query("name") name: String): Response<ApiResponse>

    @GET("api/canvas/facepalm")
    suspend fun canvasFacepalm(@Query("avatar") avatar: String): Response<ApiResponse>

    @GET("api/canvas/goodbyev4")
    suspend fun canvasGoodbyeV4(@Query("name") name: String, @Query("avatar") avatar: String): Response<ApiResponse>

    @GET("api/canvas/gay")
    suspend fun canvasGay(@Query("avatar") avatar: String): Response<ApiResponse>

    @GET("api/canvas/ship")
    suspend fun canvasShip(@Query("avatar") avatar: String, @Query("avatar2") avatar2: String): Response<ApiResponse>

    @GET("api/canvas/welcomev5")
    suspend fun canvasWelcomeV5(
        @Query("name") name: String, @Query("avatar") avatar: String,
        @Query("group") group: String, @Query("member") member: String,
        @Query("bg") bg: String
    ): Response<ApiResponse>

    @GET("api/canvas/batslap")
    suspend fun canvasBatslap(@Query("avatar") avatar: String, @Query("avatar2") avatar2: String): Response<ApiResponse>

    @GET("api/canvas/greyscale")
    suspend fun canvasGreyscale(@Query("url") url: String): Response<ApiResponse>

    @GET("api/canvas/darkness")
    suspend fun canvasDarkness(@Query("url") url: String): Response<ApiResponse>

    @GET("api/canvas/kiss")
    suspend fun canvasKiss(@Query("avatar") avatar: String, @Query("avatar2") avatar2: String): Response<ApiResponse>

    @GET("api/canvas/goodbyev5")
    suspend fun canvasGoodbyeV5(@Query("name") name: String, @Query("avatar") avatar: String): Response<ApiResponse>

    @GET("api/canvas/circle")
    suspend fun canvasCircle(@Query("url") url: String): Response<ApiResponse>

    @GET("api/canvas/fake-xnxx")
    suspend fun canvasFakeXnxx(@Query("text") text: String, @Query("text2") text2: String): Response<ApiResponse>

    @GET("api/canvas/spotify")
    suspend fun canvasSpotify(
        @Query("title") title: String, @Query("artist") artist: String,
        @Query("album") album: String, @Query("cover") cover: String,
        @Query("duration") duration: String
    ): Response<ApiResponse>

    @GET("api/canvas/goodbyev3")
    suspend fun canvasGoodbyeV3(@Query("name") name: String, @Query("avatar") avatar: String): Response<ApiResponse>

    @GET("api/canvas/ektpe")
    suspend fun canvasEktpe(
        @Query("nik") nik: String, @Query("nama") nama: String,
        @Query("ttl") ttl: String, @Query("jk") jk: String,
        @Query("alamat") alamat: String, @Query("agama") agama: String,
        @Query("status") status: String, @Query("pekerjaan") pekerjaan: String,
        @Query("foto") foto: String
    ): Response<ApiResponse>

    @GET("api/canvas/tweet")
    suspend fun canvasTweet(
        @Query("name") name: String, @Query("username") username: String,
        @Query("avatar") avatar: String, @Query("text") text: String,
        @Query("reply") reply: String, @Query("retweet") retweet: String,
        @Query("like") like: String, @Query("theme") theme: String
    ): Response<ApiResponse>

    @GET("api/canvas/welcomev2")
    suspend fun canvasWelcomeV2(
        @Query("name") name: String, @Query("avatar") avatar: String,
        @Query("group") group: String, @Query("member") member: String
    ): Response<ApiResponse>

    @GET("api/canvas/affect")
    suspend fun canvasAffect(@Query("avatar") avatar: String): Response<ApiResponse>

    @GET("api/canvas/xnxx")
    suspend fun canvasXnxx(@Query("avatar") avatar: String, @Query("avatar2") avatar2: String): Response<ApiResponse>

    @GET("api/canvas/beautiful")
    suspend fun canvasBeautiful(@Query("url") url: String): Response<ApiResponse>

    @GET("api/canvas/welcomev3")
    suspend fun canvasWelcomeV3(
        @Query("name") name: String, @Query("avatar") avatar: String,
        @Query("group") group: String, @Query("member") member: String
    ): Response<ApiResponse>

    @GET("api/canvas/invert")
    suspend fun canvasInvert(@Query("url") url: String): Response<ApiResponse>

    @GET("api/canvas/welcomev4")
    suspend fun canvasWelcomeV4(
        @Query("name") name: String, @Query("avatar") avatar: String,
        @Query("group") group: String, @Query("member") member: String
    ): Response<ApiResponse>

    @GET("api/canvas/level-up")
    suspend fun canvasLevelUp(@Query("avatar") avatar: String, @Query("name") name: String): Response<ApiResponse>

    @GET("api/canvas/goodbyev2")
    suspend fun canvasGoodbyeV2(@Query("name") name: String, @Query("avatar") avatar: String): Response<ApiResponse>

    @GET("api/canvas/top")
    suspend fun canvasTop(@Query("top") top: String): Response<ApiResponse>

    @GET("api/canvas/delete")
    suspend fun canvasDelete(@Query("avatar") avatar: String): Response<ApiResponse>

    // ==================== DOWNLOADER ====================
    @GET("api/d/capcut")
    suspend fun dCapcut(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/gdrive")
    suspend fun dGdrive(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/spotify")
    suspend fun dSpotify(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/savefrom")
    suspend fun dSavefrom(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/github")
    suspend fun dGithub(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/douyin")
    suspend fun dDouyin(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/lahelu")
    suspend fun dLahelu(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/soundcloud")
    suspend fun dSoundcloud(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/snackvideo")
    suspend fun dSnackvideo(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/spotifyv2")
    suspend fun dSpotifyV2(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/tiktok")
    suspend fun dTiktok(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/fastdl")
    suspend fun dFastdl(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/igram")
    suspend fun dIgram(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/tiktok/v2")
    suspend fun dTiktokV2(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/twitter")
    suspend fun dTwitter(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/ssstwiter")
    suspend fun dSsstwiter(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/facebook")
    suspend fun dFacebook(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/ytpost")
    suspend fun dYtpost(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/sssinstagram")
    suspend fun dSssinstagram(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/ummy")
    suspend fun dUmmy(@Query("url") url: String): Response<ApiResponse>

    @GET("api/d/rednote")
    suspend fun dRednote(@Query("url") url: String): Response<ApiResponse>

    // ==================== GAMES ====================
    @GET("api/games/lengkapikalimat")
    suspend fun gameLengkapiKalimat(): Response<ApiResponse>

    @GET("api/games/kabupaten")
    suspend fun gameKabupaten(): Response<ApiResponse>

    @GET("api/games/tekateki")
    suspend fun gameTekateki(): Response<ApiResponse>

    @GET("api/games/tebakjkt")
    suspend fun gameTebakJkt(): Response<ApiResponse>

    @GET("api/games/tebakkalimat")
    suspend fun gameTebakKalimat(): Response<ApiResponse>

    @GET("api/games/caklontong")
    suspend fun gameCaklontong(): Response<ApiResponse>

    @GET("api/games/cc-sd")
    suspend fun gameCcSd(): Response<ApiResponse>

    @GET("api/games/tebaklogo")
    suspend fun gameTebakLogo(): Response<ApiResponse>

    @GET("api/games/susunkata")
    suspend fun gameSusunKata(): Response<ApiResponse>

    @GET("api/games/tebakwarna")
    suspend fun gameTebakWarna(): Response<ApiResponse>

    @GET("api/games/tebaklagu")
    suspend fun gameTebakLagu(): Response<ApiResponse>

    @GET("api/games/asahotak")
    suspend fun gameAsahOtak(): Response<ApiResponse>

    @GET("api/games/tebaklirik")
    suspend fun gameTebakLirik(): Response<ApiResponse>

    @GET("api/games/maths")
    suspend fun gameMaths(): Response<ApiResponse>

    @GET("api/games/tebakkata")
    suspend fun gameTebakKata(): Response<ApiResponse>

    @GET("api/games/tebakkimia")
    suspend fun gameTebakKimia(): Response<ApiResponse>

    @GET("api/games/surah")
    suspend fun gameSurah(): Response<ApiResponse>

    @GET("api/games/tebakhewan")
    suspend fun gameTebakHewan(): Response<ApiResponse>

    @GET("api/games/tebaktebakan")
    suspend fun gameTebakTebakan(): Response<ApiResponse>

    @GET("api/games/tebakbendera")
    suspend fun gameTebakBendera(): Response<ApiResponse>

    @GET("api/games/tebakkartun")
    suspend fun gameTebakKartun(): Response<ApiResponse>

    @GET("api/games/siapakahaku")
    suspend fun gameSiapakahAku(): Response<ApiResponse>

    @GET("api/games/tebakgame")
    suspend fun gameTebakGame(): Response<ApiResponse>

    @GET("api/games/karakter-freefire")
    suspend fun gameKarakterFreefire(): Response<ApiResponse>

    @GET("api/games/tebakheroml")
    suspend fun gameTebakHeroMl(): Response<ApiResponse>

    @GET("api/games/tebakgambar")
    suspend fun gameTebakGambar(): Response<ApiResponse>

    @GET("api/games/family100")
    suspend fun gameFamily100(): Response<ApiResponse>

    // ==================== INFO ====================
    @GET("api/info/cuaca")
    suspend fun infoCuaca(@Query("q") q: String): Response<ApiResponse>

    @GET("api/info/event-indonesia")
    suspend fun infoEventIndonesia(): Response<ApiResponse>

    @GET("api/info/bmkg")
    suspend fun infoBmkg(): Response<ApiResponse>

    @GET("api/info/jadwaltv")
    suspend fun infoJadwalTv(): Response<ApiResponse>

    // ==================== MAKER ====================
    @GET("api/m/photooxy")
    suspend fun mPhotooxy(@Query("text") text: String, @Query("theme") theme: String): Response<ApiResponse>

    @GET("api/m/ephoto360")
    suspend fun mEphoto360(@Query("text") text: String, @Query("theme") theme: String): Response<ApiResponse>

    @GET("api/m/brat")
    suspend fun mBrat(@Query("text") text: String): Response<ApiResponse>

    @GET("api/m/textpro")
    suspend fun mTextpro(@Query("text") text: String, @Query("theme") theme: String): Response<ApiResponse>

    // ==================== PRIMBON ====================
    @GET("api/primbon/cek_potensi_penyakit")
    suspend fun primbonCekPenyakit(@Query("nama") nama: String, @Query("tgl") tgl: String): Response<ApiResponse>

    @GET("api/primbon/artinama")
    suspend fun primbonArtiNama(@Query("nama") nama: String): Response<ApiResponse>

    @GET("api/primbon/nomorhoki")
    suspend fun primbonNomorHoki(@Query("nomor") nomor: String): Response<ApiResponse>

    @GET("api/primbon/tafsirmimpi")
    suspend fun primbonTafsirMimpi(@Query("q") q: String): Response<ApiResponse>

    @GET("api/primbon/kecocokan_nama_pasangan")
    suspend fun primbonKecocokanPasangan(
        @Query("nama1") nama1: String,
        @Query("nama2") nama2: String
    ): Response<ApiResponse>

    @GET("api/primbon/ramalanjodohbali")
    suspend fun primbonRamalanJodohBali(
        @Query("nama1") nama1: String, @Query("nama2") nama2: String,
        @Query("tgl1") tgl1: String, @Query("tgl2") tgl2: String
    ): Response<ApiResponse>

    @GET("api/primbon/rejeki_hoki_weton")
    suspend fun primbonRejekiWeton(@Query("tgl") tgl: String, @Query("weton") weton: String): Response<ApiResponse>

    @GET("api/primbon/zodiak")
    suspend fun primbonZodiak(@Query("zodiak") zodiak: String): Response<ApiResponse>

    @GET("api/primbon/sifat_usaha_bisnis")
    suspend fun primbonSifatUsaha(@Query("tgl") tgl: String): Response<ApiResponse>

    @GET("api/primbon/ramalanjodoh")
    suspend fun primbonRamalanJodoh(
        @Query("nama1") nama1: String, @Query("nama2") nama2: String,
        @Query("tgl1") tgl1: String, @Query("tgl2") tgl2: String
    ): Response<ApiResponse>

    // ==================== RANDOM ====================
    @GET("api/r/quotesanime")
    suspend fun rQuotesAnime(): Response<ApiResponse>

    @GET("api/r/lahelu")
    suspend fun rLahelu(): Response<ApiResponse>

    @GET("api/r/seegore")
    suspend fun rSeegore(): Response<ApiResponse>

    @GET("api/r/cats")
    suspend fun rCats(): Response<ApiResponse>

    @GET("api/r/blue-archive")
    suspend fun rBlueArchive(): Response<ApiResponse>

    @GET("api/r/neko")
    suspend fun rNeko(): Response<ApiResponse>

    @GET("api/r/cecan/japan")
    suspend fun rCecanJapan(): Response<ApiResponse>

    @GET("api/r/cecan/indonesia")
    suspend fun rCecanIndonesia(): Response<ApiResponse>

    @GET("api/r/cecan/vietnam")
    suspend fun rCecanVietnam(): Response<ApiResponse>

    @GET("api/r/cecan/china")
    suspend fun rCecanChina(): Response<ApiResponse>

    @GET("api/r/cecan/thailand")
    suspend fun rCecanThailand(): Response<ApiResponse>

    @GET("api/r/cecan/korea")
    suspend fun rCecanKorea(): Response<ApiResponse>

    @GET("api/r/waifu")
    suspend fun rWaifu(): Response<ApiResponse>

    // ==================== SEARCH ====================
    @GET("api/s/applemusic")
    suspend fun sAppleMusic(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/spotify")
    suspend fun sSpotify(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/gitagram")
    suspend fun sGitagram(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/lahelu")
    suspend fun sLahelu(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/duckduckgo")
    suspend fun sDuckduckgo(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/bimg")
    suspend fun sBimg(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/musixmatch")
    suspend fun sMusixmatch(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/brave")
    suspend fun sBrave(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/myinstants")
    suspend fun sMyinstants(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/8font")
    suspend fun s8font(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/seegore")
    suspend fun sSeegore(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/otakotaku")
    suspend fun sOtakotaku(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/mcpedl")
    suspend fun sMcpedl(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/googleimg")
    suspend fun sGoogleImg(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/mangatoon")
    suspend fun sMangatoon(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/gsmarena")
    suspend fun sGsmarena(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/resep")
    suspend fun sResep(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/youtube")
    suspend fun sYoutube(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/kbbi")
    suspend fun sKbbi(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/pinterest")
    suspend fun sPinterest(@Query("q") q: String): Response<ApiResponse>

    @GET("api/s/soundcloud")
    suspend fun sSoundcloud(@Query("q") q: String): Response<ApiResponse>

    // ==================== STALKER ====================
    @GET("api/stalk/github")
    suspend fun stalkGithub(@Query("username") username: String): Response<ApiResponse>

    @GET("api/stalk/instagram")
    suspend fun stalkInstagram(@Query("username") username: String): Response<ApiResponse>

    @GET("api/stalk/roblox")
    suspend fun stalkRoblox(@Query("username") username: String): Response<ApiResponse>

    @GET("api/stalk/twitter")
    suspend fun stalkTwitter(@Query("username") username: String): Response<ApiResponse>

    @GET("api/stalk/threads")
    suspend fun stalkThreads(@Query("username") username: String): Response<ApiResponse>

    @GET("api/stalk/youtube")
    suspend fun stalkYoutube(@Query("username") username: String): Response<ApiResponse>

    @GET("api/stalk/pinterest")
    suspend fun stalkPinterest(@Query("username") username: String): Response<ApiResponse>

    @GET("api/stalk/tiktok")
    suspend fun stalkTiktok(@Query("username") username: String): Response<ApiResponse>

    // ==================== STICKER ====================
    @GET("api/sticker/combot-search")
    suspend fun stickerCombot(@Query("q") q: String): Response<ApiResponse>

    // ==================== TOOLS ====================
    @GET("api/tools/ssweb")
    suspend fun toolsSsweb(@Query("url") url: String): Response<ApiResponse>

    @GET("api/tools/kodepos")
    suspend fun toolsKodepos(@Query("q") q: String): Response<ApiResponse>

    @GET("api/tools/translate")
    suspend fun toolsTranslate(@Query("text") text: String, @Query("from") from: String, @Query("to") to: String): Response<ApiResponse>

    @GET("api/tools/countryInfo")
    suspend fun toolsCountryInfo(@Query("country") country: String): Response<ApiResponse>

    @GET("api/tools/subdomains")
    suspend fun toolsSubdomains(@Query("domain") domain: String): Response<ApiResponse>

    @GET("api/tools/vcc-generator")
    suspend fun toolsVccGenerator(): Response<ApiResponse>
}
