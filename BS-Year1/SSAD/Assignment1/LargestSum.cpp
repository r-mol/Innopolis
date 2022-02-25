#include <vector>
#include <iostream>

using namespace std;

int MaxSum(vector<int> a[]){
    int maxFinalSum = a->at(0);
    for (int i = 0; i < a->size(); i++) {
        int maxSum = 0;
        for (int j = i; j < a->size(); j++){
            maxSum += a->at(j);
            if(maxFinalSum< maxSum){
                maxFinalSum = maxSum;
            }
        }
    }
    return maxFinalSum;
}

int main() {

    freopen("input.txt", "r", stdin);
    freopen("output.txt", "w", stdout);
    vector<int> a;
    int num;
    while(cin>>num){
        a.push_back(num);
    }


    int result = MaxSum(&a);
    cout << result;

    return 0;
}


