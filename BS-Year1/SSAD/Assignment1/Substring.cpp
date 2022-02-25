#include <iostream>
#include <vector>
#include <unordered_map>
#include <fstream>

using namespace std;

vector<int> sub(string S, const vector<string> &L) {
    vector<int> result;

    if (L[0].size() * L.size() > S.size()) {
        return result;
    }

    unordered_map<string, int> map;

    for (int i = 0; i < L.size(); i++) {
        map[L[i]]++;
    }

    for (int i = 0; i <= S.size() - L[0].size() * L.size(); i++) {
        unordered_map<string, int> temp_hash_map(map);

        int j = i;
        int count = L.size();

        while (j < L[0].size() * L.size()+i) {

            string word = S.substr(j, L[0].size());

            if (map.find(word) == map.end() || temp_hash_map[word] == 0) {
                break;
            } else {
                temp_hash_map[word]--;
                count--;
            }

            j += L[0].size();
        }

        if (count == 0) {
            result.push_back(i);
        }
    }

    return result;
}

int main() {
    ifstream F("input.txt");
    freopen("output.txt", "w", stdout);
    string S ;
    string temp;
    getline(F,S);
    getline(F,temp);
    getline(F,temp);
    vector<string> L;
    temp +=" ";
    while (!temp.empty()){
        L.push_back(temp.substr(0,temp.find(' ')));
        temp.erase(0,temp.find(' ') +1);
    }

    vector<int> indices = sub(S, L);
    for (int indice : indices)
        cout << indice << " ";
    return 0;
}
