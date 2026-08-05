#pragma once
#include <vector>
#include <string>
class AudioProcessor{
public:
    void load(const char* path);
    std::vector<float> waveform(int n);
    float duration()const{return dur;}
    float amplitude(float t)const;
    void spectrum(std::vector<float>&out,int bands);
    const std::vector<float>& samples()const{return smp;}
private:
    std::vector<float> smp;float dur=0;int sr=44100;
};
