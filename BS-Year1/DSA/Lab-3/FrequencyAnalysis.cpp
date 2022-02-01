#include <iostream>
#include <map>
#include <string>
#include <vector>
#include <bits/stdc++.h>

bool cmp(std::pair<std::string, int>& a,std::pair<std::string, int>& b)
{
    return a.second < b.second;
}

void sort(std::map<std::string, int>& M)
{
    std::vector<std::pair<std::string, int> > A;

    for (auto& it : M) {
        A.emplace_back(it);
    }

    sort(A.begin(), A.end(), cmp);
    reverse(A.begin(), A.end() );
    for (auto& it : A) {

        std::cout << it.first << ' ' << it.second << std::endl;
    }
}

int main(){
    std::string str;
    getline(std::cin, str);
    std::map<std::string, int> map1;
    std::string tempStr;

    for(int i = 0; i <= str.length(); i++){
        if(str[i]!=' '&& str[i]!= '\0'){
            tempStr+= str[i];
        }
        else{
            auto search = map1.find(tempStr);
            if (search != map1.end()) {
                search->second++;
            } else {
                map1.insert(std::pair<std::string, int>(tempStr,1));
            }
            tempStr = "";
        }
    }
    sort(map1);
}