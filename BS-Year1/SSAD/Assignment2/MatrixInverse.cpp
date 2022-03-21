#include <iostream>
#include <string>
#include <algorithm>
#include <iomanip>

using namespace std;

int matrix[3][3];

void input() {
    freopen("input.txt", "r", stdin);
    freopen("output.txt", "w", stdout);
    string s;
    getline(cin, s);
    int position = s.find_first_of('}') / 2;
    for (int i = 0; i < s.length(); i++) {
        if (s[i] == '{') {
            s.erase(remove(s.begin(), s.end(), '{'), s.end());
        } else if (s[i] == '}') {
            s.erase(remove(s.begin(), s.end(), '}'), s.end());
        }
    }

    int c = 0;
    int r = 0;
    for (char i: s) {
        if (c != position) {
            if (i != ',') {
                matrix[r][c++] = i - '0';
            }
        } else {
            c = 0;
            r++;
            matrix[r][c] = i - '0';
        }
    }
}

void inverse(double determinant){

}

int main() {

    input();

    double determinant = matrix[0][0] * (matrix[1][1] * matrix[2][2] - matrix[2][1] * matrix[1][2]) -
                         matrix[0][1] * (matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0]) +
                         matrix[0][2] * (matrix[1][0] * matrix[2][1] - matrix[1][1] * matrix[2][0]);


    double invdet = 1 / determinant;

    if((matrix[1][1] * matrix[2][2] - matrix[2][1] * matrix[1][2]) * invdet == -0) {
        cout << "0.00 ";
    }else{
        cout << fixed << setprecision(2) << (matrix[1][1] * matrix[2][2] - matrix[2][1] * matrix[1][2]) * invdet << " ";
    }

    if((matrix[0][1] * matrix[2][2] - matrix[0][2] * matrix[2][1]) * invdet == 0) {
        cout << "0.00 ";
    }else{
        cout << fixed << setprecision(2) << -(matrix[0][1] * matrix[2][2] - matrix[0][2] * matrix[2][1]) * invdet << " ";
    }

    if((matrix[0][1] * matrix[1][2] - matrix[0][2] * matrix[1][1]) * invdet == -0) {
        cout << "0.00 ";
    }else{
        cout << fixed<< setprecision(2) << (matrix[0][1] * matrix[1][2] - matrix[0][2] * matrix[1][1]) * invdet << endl;
    }

    //
    if(-(matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0]) * invdet == -0) {
        cout << "0.00 ";
    }else{
        cout << fixed << setprecision(2) << -(matrix[1][0] * matrix[2][2] - matrix[1][2] * matrix[2][0]) * invdet << " ";
    }

    if((matrix[0][0] * matrix[2][2] - matrix[0][2] * matrix[2][0]) * invdet == 0) {
        cout << "0.00 ";
    }else{
        cout  << fixed << setprecision(2) << (matrix[0][0] * matrix[2][2] - matrix[0][2] * matrix[2][0]) * invdet << " " ;
    }

    if(-(matrix[0][0] * matrix[1][2] - matrix[1][0] * matrix[0][2]) * invdet == -0) {
        cout << "0.00 ";
    }else{
        cout  << fixed << setprecision(2) << -(matrix[0][0] * matrix[1][2] - matrix[1][0] * matrix[0][2]) * invdet << endl;
    }

    //
    if((matrix[1][0] * matrix[2][1] - matrix[2][0] * matrix[1][1]) * invdet == -0) {
        cout << "0.00 ";
    }else{
        cout << fixed << setprecision(2) << (matrix[1][0] * matrix[2][1] - matrix[2][0] * matrix[1][1]) * invdet << " ";
    }

    if(-(matrix[0][0] * matrix[2][1] - matrix[2][0] * matrix[0][1]) * invdet == 0) {
        cout << "0.00 ";
    }else{
        cout  << fixed << setprecision(2) << -(matrix[0][0] * matrix[2][1] - matrix[2][0] * matrix[0][1]) * invdet << " " ;
    }

    if((matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1]) * invdet == -0) {
        cout << "0.00 ";
    }else{
        cout << fixed << setprecision(2) << (matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1]) * invdet << endl;
    }

    return 0;
}