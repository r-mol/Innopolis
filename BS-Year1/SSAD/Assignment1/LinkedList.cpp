#include <iostream>

using namespace std;

int main(){
    freopen("input.txt", "r", stdin);
    freopen("output.txt", "w", stdout);
    int arr[10000];

    int t =0;
    while(cin >> arr[t++]);
    t-=1;
    int brr[t];

    for(int i = 0; i<t ;i++){
        brr[i] =i;
    }

    if((t)%2==0){
        for(int i = 0; i< t;i+=2){
            swap(brr[i],brr[i+1]);
        }
    }else {
        for (int i = 0; i < t - 1 ;i+=2){
            swap(brr[i],brr[i+1]);
        }
    }

    for(int i = 0; i < t;i++){
        cout << arr[brr[i]] << " ";
    }

}

