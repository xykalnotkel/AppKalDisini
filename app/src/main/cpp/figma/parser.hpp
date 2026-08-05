#pragma once
#include <string>
#include <vector>
struct FNode{std::string id,name,type;float x,y,w,h,rot,op,r,g,b,a,cr,sw,fs;std::string txt,svg;std::vector<FNode> ch;};
class FigmaParser{public:std::string parse(const std::string&json);std::string toJSON();private:std::vector<FNode> nodes;};
