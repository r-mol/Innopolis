#include <iostream>
#include <string>
#include <cmath>
#include <algorithm>

using namespace std;

int matrix[10][10];

int input(){
    freopen("input.txt", "r", stdin);
    freopen("output.txt", "w", stdout);
    string s;
    getline(cin, s);
    int position = s.find_first_of('}')/2;
    for (int i = 0; i < s.length(); i++) {
        if (s[i] == '{') {
            s.erase(remove(s.begin(), s.end(), '{'), s.end());
        } else if (s[i] == '}') {
            s.erase(remove(s.begin(), s.end(), '}'), s.end());
        }
    }

    int c = 0;
    int r = 0;
    for (char i : s) {
        if(c != position){
            if (i != ',') {
                matrix[r][c++] = i - '0';
            }
        }
        else {
            c = 0;
            r++;
            matrix[r][c] = i - '0';
        }
    }
    return position;
}

int determinant( int matrixx[10][10], int n) {
    int det = 0;
    int submatrix[10][10];
    if (n == 2)
        return ((matrixx[0][0] * matrixx[1][1]) - (matrixx[1][0] * matrixx[0][1]));
    else {
        for (int x = 0; x < n; x++) {
            int subi = 0;
            for (int i = 1; i < n; i++) {
                int subj = 0;
                for (int j = 0; j < n; j++) {
                    if (j == x)
                        continue;
                    submatrix[subi][subj] = matrixx[i][j];
                    subj++;
                }
                subi++;
            }
            det += (pow(-1, x) * matrixx[0][x] * determinant( submatrix, n - 1 ));
        }
    }
    return det;
}

int main() {

    int position  = input();
    cout << determinant(matrix, position);

    return 0;
}