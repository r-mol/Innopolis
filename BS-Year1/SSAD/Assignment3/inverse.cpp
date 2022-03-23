#include<iostream>
#include<iomanip>

#define SIZE 30
using namespace std;

float inv[SIZE][SIZE];

void inverse(int n){
    float ratio;
    for(int i=1;i<=n;i++)
    {
        for(int j=1;j<=n;j++)
        {
            if(i==j)
            {
                inv[i][j + n] = 1;
            }
            else
            {
                inv[i][j + n] = 0;
            }
        }
    }

    for(int i=1;i<=n;i++){
        for(int j=1;j<=n;j++){
            if(i!=j){
                ratio = inv[j][i] / inv[i][i];
                for(int q=1;q<=2*n;q++){
                    inv[j][q] = inv[j][q] - ratio * inv[i][q];
                }
            }
        }
    }

    for(int i=1;i<=n;i++)
    {
        for(int j=n+1;j<=2*n;j++)
        {
            inv[i][j] = inv[i][j] / inv[i][i];
        }
    }
}

int main() {
    freopen("input.txt", "r", stdin);
    freopen("output.txt", "w", stdout);
    int k;
    int n;

    cout << setprecision(2) << fixed;

    cin >> n;
    cin >> k;

    float arr[k][n+1];
    float b[k][1];
    float arr_T[n+1][k];
    float mult1[n + 1][n + 1];
    float mult2[n + 1][k];
    float mult3[1][n + 1];
    float inv_normal[n + 1][n + 1];

    for (int i = 0; i < k; i++) {
        for (int j = 0; j <= n + 1; j++) {
            if(j == 0){
                arr[i][j] = 1;
            }else if(j == n+1){
                cin >> b[i][0];
            }else{
                cin >> arr[i][j];
            }
        }
    }

    cout << "A:" << endl;
    for (int i = 0; i < k; ++i) {
        for (int j = 0; j <=n; ++j) {
            cout <<  arr[i][j]<<" ";
        }
        cout<<endl;
    }

    cout << endl << "b:" << endl;
    for (int i = 0; i < k; i++) {
        cout <<  b[i][0] << endl;
    }
    cout << endl;

    for (int i = 0; i < k; i++) {
        for (int j = 0; j <= n; j++) {
            arr_T[j][i] = arr[i][j];
        }
    }

    for(int i = 0; i <= n; ++i) {
        for (int j = 0; j <= n; ++j) {
            mult1[i][j] = 0;
        }
    }

    for(int i = 0; i <= n; ++i) {
        for (int j = 0; j <= n; ++j) {
            float res = 0;
            for (int q = 0; q < k; ++q) {
                res += arr_T[i][q] * arr[q][j];
            }
            mult1[i][j] = res;
        }
    }

    cout << "A_T*A:"<< endl;
    for (int i = 0; i <= n; i++) {
        for (int j = 0; j <= n; j++) {
            cout << mult1[i][j] << " ";
        }
        cout<<endl;
    }

    for (int i = 0; i <= n; i++) {
        for (int j = 0; j <= n; j++) {
            inv[i+1][j+1] = mult1[i][j];
        }
    }

    inverse(n+1);

    cout << endl << "(A_T*A)_-1:"<< endl;
    for(int i=1;i<=n+1;i++)
    {
        for(int j=n+2;j<=2*(n+1);j++)
        {
            cout << inv[i][j] << " ";
        }
        cout<< endl;
    }

    for(int i = 0; i <= n; i++) {
        for (int j = 0; j < k; j++) {
            mult2[i][j] = 0;
        }
    }

    for(int i=1;i<=n+1;i++)
    {
        for(int j=n+2;j<=2*(n+1);j++)
        {
            inv_normal[i - 1][j - n - 2] = inv[i][j];

        }
    }

    for(int i = 0; i <= n; i++) {
        for (int j = 0; j < k; j++) {
            float res = 0;
            for (int q = 0; q <= n; q++) {
                res += inv_normal[i][q] * arr_T[q][j];
            }
            mult2[i][j] = res;
        }
    }

    cout << endl << "(A_T*A)_-1*A_T:"<< endl;
    for (int i = 0; i <=n; i++) {
        for (int j = 0; j < k; j++) {
            cout << mult2[i][j] << " ";
        }
        cout<<endl;
    }

    for(int i = 0; i <=n; i++) {
        mult3[0][i] = 0;
    }

    for(int i = 0; i <= n; i++) {
        float res = 0;
        for (int j = 0; j <k; j++) {
                 res += mult2[i][j] * b[j][0];
        }
        mult3[0][i] = res;
    }

    cout << endl << "x:" << endl;
    for(int i = 0; i <= n; i++) {
        cout << mult3[0][i] << endl;
    }

    return 0 ;
}