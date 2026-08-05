plugins {
    id("com.android.application");id("org.jetbrains.kotlin.android");id("org.jetbrains.kotlin.plugin.compose")
}
android {
    namespace="com.alight.motion";compileSdk=35;ndkVersion="27.0.12077973"
    defaultConfig {
        applicationId="com.alight.motion.editor";minSdk=26;targetSdk=35;versionCode=2;versionName="2.0.0"
        ndk{abiFilters+=listOf("arm64-v8a","armeabi-v7a","x86_64")}
        externalNativeBuild{cmake{cppFlags+="-std=c++17 -O3 -ffast-math";arguments+=listOf("-DANDROID_STL=c++_shared")}}
    }
    buildTypes{
        release{isMinifyEnabled=true;proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro");ndk{debugSymbolLevel="none"}}
        debug{isDebuggable=true;isJniDebuggable=true;ndk{debugSymbolLevel="full"}}
    }
    compileOptions{sourceCompatibility=JavaVersion.VERSION_17;targetCompatibility=JavaVersion.VERSION_17}
    kotlinOptions{jvmTarget="17"};buildFeatures{compose=true}
    externalNativeBuild{cmake{path=file("src/main/cpp/CMakeLists.txt");version="3.22.1"}}
}
dependencies {
    val composeBom=platform("androidx.compose:compose-bom:2024.06.00");implementation(composeBom)
    implementation("androidx.core:core-ktx:1.13.1");implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0");implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
    implementation("androidx.compose.ui:ui");implementation("androidx.compose.ui:ui-graphics");implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended");implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.foundation:foundation");implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-transformer:1.3.1");implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1");implementation("com.squareup.okhttp3:okhttp:4.12.0");implementation("org.json:json:20240303")
}
