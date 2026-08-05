cmake_minimum_required(VERSION 3.22.1)
project("alightmotion")
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
find_library(log-lib log)
find_library(android-lib android)
find_library(GLESv2-lib GLESv2)
find_library(EGL-lib EGL)
find_library(OpenSLES-lib OpenSLES)
find_library(jnigraphics-lib jnigraphics)
find_library(mediandk-lib mediandk)

set(CORE_SOURCES
    jni_bridge.cpp
    gl/renderer.cpp
    gl/shaders.cpp
    effects/pipeline.cpp
    particles/engine.cpp
    figma/parser.cpp
    audio/processor.cpp
    media/decoder.cpp
    font/sdf_renderer.cpp
)

add_library(alightmotion SHARED ${CORE_SOURCES})

target_include_directories(alightmotion PRIVATE
    ${CMAKE_CURRENT_SOURCE_DIR}
    ${CMAKE_CURRENT_SOURCE_DIR}/gl
    ${CMAKE_CURRENT_SOURCE_DIR}/effects
    ${CMAKE_CURRENT_SOURCE_DIR}/particles
    ${CMAKE_CURRENT_SOURCE_DIR}/figma
    ${CMAKE_CURRENT_SOURCE_DIR}/audio
    ${CMAKE_CURRENT_SOURCE_DIR}/media
    ${CMAKE_CURRENT_SOURCE_DIR}/font
)

target_link_libraries(alightmotion
    ${log-lib} ${android-lib} ${GLESv2-lib} ${EGL-lib}
    ${OpenSLES-lib} ${jnigraphics-lib} ${mediandk-lib}
)

target_compile_options(alightmotion PRIVATE -O3 -ffast-math -funroll-loops -fomit-frame-pointer)
