package com.siputzx.app.data.repository

import com.siputzx.app.data.api.ApiResponse
import com.siputzx.app.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiRepository {
    private val api = RetrofitClient.apiService

    suspend fun callEndpoint(endpointId: String, params: Map<String, String>): Result<ApiResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = when (endpointId) {
                    // AI
                    "duckai" -> api.duckAi(params["text"] ?: "")
                    "bibleai" -> api.bibleAi(params["text"] ?: "")
                    "gptoss120b" -> api.gptOss120b(params["text"] ?: "")
                    "glm47flash" -> api.glm47Flash(params["text"] ?: "")
                    "phi2" -> api.phi2(params["text"] ?: "")
                    "qwq32b" -> api.qwq32b(params["text"] ?: "")
                    "deepseekr1" -> api.deepseekR1(params["text"] ?: "")
                    "metaai" -> api.metaAi(params["text"] ?: "")
                    "duckaiimage" -> api.duckAiImage(params["text"] ?: "")
                    "gemini" -> api.gemini(params["text"] ?: "")
                    "gita" -> api.gita(params["text"] ?: "")

                    // Anime
                    "animequotes" -> api.animeQuotes()
                    "auratail-search" -> api.auratailSearch(params["q"] ?: "")
                    "auratail-latest" -> api.auratailLatest()
                    "auratail-schedule" -> api.auratailSchedule()
                    "auratail-detail" -> api.auratailDetail(params["url"] ?: "")
                    "otakudesu-ongoing" -> api.otakudesuOngoing()
                    "otakudesu-search" -> api.otakudesuSearch(params["q"] ?: "")
                    "otakudesu-download" -> api.otakudesuDownload(params["url"] ?: "")
                    "otakudesu-detail" -> api.otakudesuDetail(params["url"] ?: "")
                    "anichin-episode" -> api.anichinEpisode(params["url"] ?: "")
                    "anichin-search" -> api.anichinSearch(params["q"] ?: "")
                    "anichin-download" -> api.anichinDownload(params["url"] ?: "")
                    "anichin-latest" -> api.anichinLatest()
                    "anichin-popular" -> api.anichinPopular()
                    "anichin-detail" -> api.anichinDetail(params["url"] ?: "")
                    "oploverz-episode" -> api.oploverzEpisode(params["url"] ?: "")
                    "oploverz-ongoing" -> api.oploverzOngoing()
                    "oploverz-search" -> api.oploverzSearch(params["q"] ?: "")
                    "oploverz-download" -> api.oploverzDownload(params["url"] ?: "")
                    "komikindo-search" -> api.komikindoSearch(params["q"] ?: "")
                    "komikindo-detail" -> api.komikindoDetail(params["url"] ?: "")
                    "komikindo-download" -> api.komikindoDownload(params["url"] ?: "")
                    "samehadaku-search" -> api.samehadakuSearch(params["q"] ?: "")
                    "samehadaku-download" -> api.samehadakuDownload(params["url"] ?: "")
                    "samehadaku-latest" -> api.samehadakuLatest()
                    "samehadaku-release" -> api.samehadakuRelease()
                    "samehadaku-detail" -> api.samehadakuDetail(params["url"] ?: "")

                    // Berita
                    "kompas" -> api.beritaKompas()
                    "cnn" -> api.beritaCnn()
                    "tribunnews" -> api.beritaTribunnews()
                    "jkt48" -> api.beritaJkt48()
                    "liputan6" -> api.beritaLiputan6()
                    "suara" -> api.beritaSuara()
                    "sindonews" -> api.beritaSindonews()
                    "cnbcindonesia" -> api.beritaCnbc()
                    "merdeka" -> api.beritaMerdeka()
                    "antara" -> api.beritaAntara()

                    // Downloader
                    "capcut" -> api.dCapcut(params["url"] ?: "")
                    "gdrive" -> api.dGdrive(params["url"] ?: "")
                    "spotify" -> api.dSpotify(params["url"] ?: "")
                    "savefrom" -> api.dSavefrom(params["url"] ?: "")
                    "github" -> api.dGithub(params["url"] ?: "")
                    "douyin" -> api.dDouyin(params["url"] ?: "")
                    "lahelu" -> api.dLahelu(params["url"] ?: "")
                    "soundcloud" -> api.dSoundcloud(params["url"] ?: "")
                    "snackvideo" -> api.dSnackvideo(params["url"] ?: "")
                    "spotifyv2" -> api.dSpotifyV2(params["url"] ?: "")
                    "tiktok" -> api.dTiktok(params["url"] ?: "")
                    "fastdl" -> api.dFastdl(params["url"] ?: "")
                    "igram" -> api.dIgram(params["url"] ?: "")
                    "tiktokv2" -> api.dTiktokV2(params["url"] ?: "")
                    "twitter" -> api.dTwitter(params["url"] ?: "")
                    "ssstwiter" -> api.dSsstwiter(params["url"] ?: "")
                    "facebook" -> api.dFacebook(params["url"] ?: "")
                    "ytpost" -> api.dYtpost(params["url"] ?: "")
                    "sssinstagram" -> api.dSssinstagram(params["url"] ?: "")
                    "ummy" -> api.dUmmy(params["url"] ?: "")
                    "rednote" -> api.dRednote(params["url"] ?: "")

                    // Games
                    "lengkapikalimat" -> api.gameLengkapiKalimat()
                    "kabupaten" -> api.gameKabupaten()
                    "tekateki" -> api.gameTekateki()
                    "tebakjkt" -> api.gameTebakJkt()
                    "tebakkalimat" -> api.gameTebakKalimat()
                    "caklontong" -> api.gameCaklontong()
                    "cc-sd" -> api.gameCcSd()
                    "tebaklogo" -> api.gameTebakLogo()
                    "susunkata" -> api.gameSusunKata()
                    "tebakwarna" -> api.gameTebakWarna()
                    "tebaklagu" -> api.gameTebakLagu()
                    "asahotak" -> api.gameAsahOtak()
                    "tebaklirik" -> api.gameTebakLirik()
                    "maths" -> api.gameMaths()
                    "tebakkata" -> api.gameTebakKata()
                    "tebakkimia" -> api.gameTebakKimia()
                    "surah" -> api.gameSurah()
                    "tebakhewan" -> api.gameTebakHewan()
                    "tebaktebakan" -> api.gameTebakTebakan()
                    "tebakbendera" -> api.gameTebakBendera()
                    "tebakkartun" -> api.gameTebakKartun()
                    "siapakahaku" -> api.gameSiapakahAku()
                    "tebakgame" -> api.gameTebakGame()
                    "karakter-freefire" -> api.gameKarakterFreefire()
                    "tebakheroml" -> api.gameTebakHeroMl()
                    "tebakgambar" -> api.gameTebakGambar()
                    "family100" -> api.gameFamily100()

                    // Info
                    "cuaca" -> api.infoCuaca(params["q"] ?: "")
                    "event-indonesia" -> api.infoEventIndonesia()
                    "bmkg" -> api.infoBmkg()
                    "jadwaltv" -> api.infoJadwalTv()

                    // Search
                    "applemusic" -> api.sAppleMusic(params["q"] ?: "")
                    "spotify-search" -> api.sSpotify(params["q"] ?: "")
                    "gitagram" -> api.sGitagram(params["q"] ?: "")
                    "lahelu-search" -> api.sLahelu(params["q"] ?: "")
                    "duckduckgo" -> api.sDuckduckgo(params["q"] ?: "")
                    "bimg" -> api.sBimg(params["q"] ?: "")
                    "musixmatch" -> api.sMusixmatch(params["q"] ?: "")
                    "brave" -> api.sBrave(params["q"] ?: "")
                    "myinstants" -> api.sMyinstants(params["q"] ?: "")
                    "8font" -> api.s8font(params["q"] ?: "")
                    "seegore" -> api.sSeegore(params["q"] ?: "")
                    "otakotaku" -> api.sOtakotaku(params["q"] ?: "")
                    "mcpedl" -> api.sMcpedl(params["q"] ?: "")
                    "googleimg" -> api.sGoogleImg(params["q"] ?: "")
                    "mangatoon" -> api.sMangatoon(params["q"] ?: "")
                    "gsmarena" -> api.sGsmarena(params["q"] ?: "")
                    "resep" -> api.sResep(params["q"] ?: "")
                    "youtube" -> api.sYoutube(params["q"] ?: "")
                    "kbbi" -> api.sKbbi(params["q"] ?: "")
                    "pinterest" -> api.sPinterest(params["q"] ?: "")
                    "soundcloud-search" -> api.sSoundcloud(params["q"] ?: "")

                    // Stalker
                    "stalk-github" -> api.stalkGithub(params["username"] ?: "")
                    "stalk-instagram" -> api.stalkInstagram(params["username"] ?: "")
                    "stalk-roblox" -> api.stalkRoblox(params["username"] ?: "")
                    "stalk-twitter" -> api.stalkTwitter(params["username"] ?: "")
                    "stalk-threads" -> api.stalkThreads(params["username"] ?: "")
                    "stalk-youtube" -> api.stalkYoutube(params["username"] ?: "")
                    "stalk-pinterest" -> api.stalkPinterest(params["username"] ?: "")
                    "stalk-tiktok" -> api.stalkTiktok(params["username"] ?: "")

                    // Tools
                    "ssweb" -> api.toolsSsweb(params["url"] ?: "")
                    "kodepos" -> api.toolsKodepos(params["q"] ?: "")
                    "translate" -> api.toolsTranslate(params["text"] ?: "", params["from"] ?: "id", params["to"] ?: "en")
                    "countryInfo" -> api.toolsCountryInfo(params["country"] ?: "")
                    "subdomains" -> api.toolsSubdomains(params["domain"] ?: "")
                    "vcc-generator" -> api.toolsVccGenerator()

                    // Canvas
                    "welcomev1" -> api.canvasWelcomeV1(params["name"] ?: "", params["avatar"] ?: "", params["group"] ?: "", params["member"] ?: "")
                    "captcha" -> api.canvasCaptcha(params["text"] ?: "")
                    "goodbyev1" -> api.canvasGoodbyeV1(params["name"] ?: "", params["avatar"] ?: "")
                    "profile" -> api.canvasProfile(params)
                    "blur" -> api.canvasBlur(params["url"] ?: "")
                    "security" -> api.canvasSecurity(params["name"] ?: "", params["avatar"] ?: "")
                    "sertifikat-tolol" -> api.canvasSertifikatTolol(params["name"] ?: "")
                    "facepalm" -> api.canvasFacepalm(params["avatar"] ?: "")
                    "goodbyev4" -> api.canvasGoodbyeV4(params["name"] ?: "", params["avatar"] ?: "")
                    "gay" -> api.canvasGay(params["avatar"] ?: "")
                    "ship" -> api.canvasShip(params["avatar"] ?: "", params["avatar2"] ?: "")
                    "welcomev5" -> api.canvasWelcomeV5(params["name"] ?: "", params["avatar"] ?: "", params["group"] ?: "", params["member"] ?: "", params["bg"] ?: "")
                    "batslap" -> api.canvasBatslap(params["avatar"] ?: "", params["avatar2"] ?: "")
                    "greyscale" -> api.canvasGreyscale(params["url"] ?: "")
                    "darkness" -> api.canvasDarkness(params["url"] ?: "")
                    "kiss" -> api.canvasKiss(params["avatar"] ?: "", params["avatar2"] ?: "")
                    "goodbyev5" -> api.canvasGoodbyeV5(params["name"] ?: "", params["avatar"] ?: "")
                    "circle" -> api.canvasCircle(params["url"] ?: "")
                    "fake-xnxx" -> api.canvasFakeXnxx(params["text"] ?: "", params["text2"] ?: "")
                    "spotify-card" -> api.canvasSpotify(params["title"] ?: "", params["artist"] ?: "", params["album"] ?: "", params["cover"] ?: "", params["duration"] ?: "")
                    "goodbyev3" -> api.canvasGoodbyeV3(params["name"] ?: "", params["avatar"] ?: "")
                    "ektpe" -> api.canvasEktpe(params["nik"] ?: "", params["nama"] ?: "", params["ttl"] ?: "", params["jk"] ?: "", params["alamat"] ?: "", params["agama"] ?: "", params["status"] ?: "", params["pekerjaan"] ?: "", params["foto"] ?: "")
                    "tweet" -> api.canvasTweet(params["name"] ?: "", params["username"] ?: "", params["avatar"] ?: "", params["text"] ?: "", params["reply"] ?: "", params["retweet"] ?: "", params["like"] ?: "", params["theme"] ?: "")
                    "welcomev2" -> api.canvasWelcomeV2(params["name"] ?: "", params["avatar"] ?: "", params["group"] ?: "", params["member"] ?: "")
                    "affect" -> api.canvasAffect(params["avatar"] ?: "")
                    "xnxx" -> api.canvasXnxx(params["avatar"] ?: "", params["avatar2"] ?: "")
                    "beautiful" -> api.canvasBeautiful(params["url"] ?: "")
                    "welcomev3" -> api.canvasWelcomeV3(params["name"] ?: "", params["avatar"] ?: "", params["group"] ?: "", params["member"] ?: "")
                    "invert" -> api.canvasInvert(params["url"] ?: "")
                    "welcomev4" -> api.canvasWelcomeV4(params["name"] ?: "", params["avatar"] ?: "", params["group"] ?: "", params["member"] ?: "")
                    "level-up" -> api.canvasLevelUp(params["avatar"] ?: "", params["name"] ?: "")
                    "goodbyev2" -> api.canvasGoodbyeV2(params["name"] ?: "", params["avatar"] ?: "")
                    "top" -> api.canvasTop(params["top"] ?: "")
                    "delete" -> api.canvasDelete(params["avatar"] ?: "")

                    // Maker
                    "photooxy" -> api.mPhotooxy(params["text"] ?: "", params["theme"] ?: "")
                    "ephoto360" -> api.mEphoto360(params["text"] ?: "", params["theme"] ?: "")
                    "brat" -> api.mBrat(params["text"] ?: "")
                    "textpro" -> api.mTextpro(params["text"] ?: "", params["theme"] ?: "")

                    // Primbon
                    "cek-penyakit" -> api.primbonCekPenyakit(params["nama"] ?: "", params["tgl"] ?: "")
                    "artinama" -> api.primbonArtiNama(params["nama"] ?: "")
                    "nomorhoki" -> api.primbonNomorHoki(params["nomor"] ?: "")
                    "tafsirmimpi" -> api.primbonTafsirMimpi(params["q"] ?: "")
                    "kecocokan-pasangan" -> api.primbonKecocokanPasangan(params["nama1"] ?: "", params["nama2"] ?: "")
                    "ramalanjodohbali" -> api.primbonRamalanJodohBali(params["nama1"] ?: "", params["nama2"] ?: "", params["tgl1"] ?: "", params["tgl2"] ?: "")
                    "rejeki-weton" -> api.primbonRejekiWeton(params["tgl"] ?: "", params["weton"] ?: "")
                    "zodiak" -> api.primbonZodiak(params["zodiak"] ?: "")
                    "sifat-usaha" -> api.primbonSifatUsaha(params["tgl"] ?: "")
                    "ramalanjodoh" -> api.primbonRamalanJodoh(params["nama1"] ?: "", params["nama2"] ?: "", params["tgl1"] ?: "", params["tgl2"] ?: "")

                    // Random
                    "quotesanime" -> api.rQuotesAnime()
                    "lahelu-rand" -> api.rLahelu()
                    "seegore-rand" -> api.rSeegore()
                    "cats" -> api.rCats()
                    "blue-archive" -> api.rBlueArchive()
                    "neko" -> api.rNeko()
                    "cecan-japan" -> api.rCecanJapan()
                    "cecan-indonesia" -> api.rCecanIndonesia()
                    "cecan-vietnam" -> api.rCecanVietnam()
                    "cecan-china" -> api.rCecanChina()
                    "cecan-thailand" -> api.rCecanThailand()
                    "cecan-korea" -> api.rCecanKorea()
                    "waifu" -> api.rWaifu()

                    // Sticker
                    "combot-search" -> api.stickerCombot(params["q"] ?: "")

                    else -> throw IllegalArgumentException("Unknown endpoint: $endpointId")
                }

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
