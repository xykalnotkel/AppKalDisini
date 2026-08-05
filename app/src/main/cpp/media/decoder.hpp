#pragma once
#include <media/NdkMediaCodec.h>
#include <media/NdkMediaExtractor.h>
#include <media/NdkMediaFormat.h>
#include <media/NdkImage.h>
#include <cstdint>
#include <vector>
#include <string>

struct VideoFrame { uint8_t* data; int w,h,stride; double pts; };
class MediaDecoder {
public:
    bool openVideo(const char* path);
    bool openAudio(const char* path);
    VideoFrame* decodeNextFrame();
    std::vector<float> decodeAudioFrames();
    double getDuration();
    int getWidth(),getHeight();
    void close();
private:
    AMediaExtractor* extr=nullptr;AMediaCodec* codec=nullptr;
    int w=0,h=0;double dur=0;bool eos=false;
};
