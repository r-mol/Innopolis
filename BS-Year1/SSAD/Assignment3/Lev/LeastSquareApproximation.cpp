// Created by Rekhlov Lev BS1-07
// l.rekhlov@innopolis.university
#include <iostream>
#include <vector>
#include <fstream>
#include <iomanip>
#include <cmath>
#include <string>

using namespace std;

// Input and output files.
ifstream fin("input.txt");
ofstream fout("output.txt");

template<typename T>
void exchange(T &a, T &b) {
    // Function exchanges two values of variables using references.
    T c = a;
    a = b;
    b = c;
}

void readMatrix(vector<vector<float>> &A, vector<vector<float>> &b) {
    // Function reads initial matrix A and vector b from the input file.
    int n, m;
    fin >> n >> m;
    A.resize(m, vector<float>(n + 1));
    b.resize(m, vector<float>(1));
    for (int i = 0; i < m; ++i) A[i][0] = 1;
    for (int i = 0; i < m; ++i) {
        for (int j = 0; j < n + 1; ++j) {
            if (j == n) {
                fin >> b[i][0];
            } else {
                fin >> A[i][j + 1];
            }
        }
    }
}

vector<vector<float>> transposeMatrix(const vector<vector<float>> &A) {
    vector<vector<float>> A_T = vector<vector<float>>(A[0].size(), vector<float>(A.size()));
    // Function finds transpose of a matrix and returns the result.
    for (int i = 0; i < A[0].size(); ++i) {
        for (int j = 0; j < A.size(); ++j) {
            A_T[i][j] = A[j][i];
        }
    }
    return A_T;
}

vector<vector<float>> multiplyMatrices(const vector<vector<float>> &A, const vector<vector<float>> &B) {
    // Function multiplies two matrices and returns the result.
    vector<vector<float>> R = vector<vector<float>>(A.size(), vector<float>(B[0].size()));
    for (int i = 0; i < A.size(); ++i) {
        for (int j = 0; j < B[0].size(); ++j) {
            float res = 0;
            for (int k = 0; k < A[0].size(); ++k) {
                res += A[i][k] * B[k][j];
            }
            R[i][j] = res;
        }
    }
    return R;
}

void LUP_Decompose(vector<vector<float>> &A, int perms[]) {
    // Function finds PA = LU decomposition for matrix A and writes both L and U in single 2D array.
    unsigned n = A.size();
    int k_prime = 0;
    for (int k = 0; k < n; ++k) {
        float p = 0;
        for (int i = k; i < n; ++i) {
            if (fabs(A[i][k]) > p) {
                p = fabs(A[i][k]);
                k_prime = i;
            }
        }
        exchange(perms[k], perms[k_prime]);
        for (int i = 0; i < n; ++i) {
            exchange(A[k][i], A[k_prime][i]);
        }
        for (int i = k + 1; i < n; ++i) {
            A[i][k] /= A[k][k];
            for (int j = k + 1; j < n; ++j) {
                A[i][j] -= A[i][k] * A[k][j];
            }
        }
    }
}

void findInverse(vector<vector<float>> &A_inverse, const vector<vector<float>> &L, const vector<vector<float>> &U,
                 int perms[]) {
    // Function finds inverse of matrix by solving Ly = Pb and then Ux = y where b is row of identity matrix I.
    unsigned n = L.size();
    vector<vector<float>> I = vector<vector<float>>(n, vector<float>(n, 0.0));
    for (int i = 0; i < I.size(); ++i) {
        I[i][i] = 1.0;
    }

    vector<float> y = vector<float>(n, 0.0);

    for (int c = 0; c < I.size(); ++c) {
        for (int i = 0; i < n; ++i) {
            y[i] = I[c][perms[i]];
            for (int j = 0; j <= i - 1; ++j) {
                y[i] -= L[i][j] * y[j];
            }
        }

        for (int i = n - 1; i > -1; --i) {
            A_inverse[i][c] = 0;
            for (int j = i + 1; j < n; ++j) {
                A_inverse[i][c] += U[i][j] * A_inverse[j][c];
            }
            A_inverse[i][c] = (y[i] - A_inverse[i][c]) / U[i][i];
        }
    }
}

void outputMatrix(vector<vector<float>> &matrix, const string &label) {
    // Function writes the matrix with corresponding label to the output file.
    fout << label << ':' << '\n';
    for (int i = 0; i < matrix.size(); ++i) {
        for (int j = 0; j < matrix[0].size(); ++j) {
            if (matrix[i][j] <= 0 && matrix[i][j] > -1e-7) matrix[i][j] = 0.0;
            fout << fixed << setprecision(2) << matrix[i][j] << ' ';
        }
        fout << '\n';
    }
    fout << '\n';
}

int main() {
    // Main function.

    // Reading input.
    vector<vector<float>> A;
    vector<vector<float>> b;
    readMatrix(A, b);
    outputMatrix(A, "A");
    outputMatrix(b, "b");

    // Calculating A_T and multiplying by A.
    vector<vector<float>> A_T = transposeMatrix(A);

    vector<vector<float>> A_TA = multiplyMatrices(A_T, A);
    outputMatrix(A_TA, "A_T*A");

    // Finding decomposition of A_T*A.
    int perms[A_TA.size()];
    for (int i = 0; i < A_TA.size(); ++i) {
        perms[i] = i;
    }
    LUP_Decompose(A_TA, perms);

    // Splitting A_TA into L and U.
    vector<vector<float>> L = vector<vector<float>>(A_TA.size(), vector<float>(A_TA.size(), 0));
    vector<vector<float>> U = vector<vector<float>>(A_TA.size(), vector<float>(A_TA.size(), 0));
    for (int i = 0; i < A_TA.size(); ++i) {
        for (int j = 0; j < A_TA.size(); ++j) {
            if (i > j) {
                L[i][j] = A_TA[i][j];
            } else {
                U[i][j] = A_TA[i][j];
            }
        }
    }

    // Finding inverse of A_TA.
    vector<vector<float>> A_TA_inverse = vector<vector<float>>(A_TA.size(), vector<float>(A_TA.size(), 0));
    findInverse(A_TA_inverse, L, U, perms);
    outputMatrix(A_TA_inverse, "(A_T*A)_-1");

    // Multiplying A_TA_inverse by A_T.
    vector<vector<float>> A_TA_inverse_A_T = multiplyMatrices(A_TA_inverse, A_T);
    outputMatrix(A_TA_inverse_A_T, "(A_T*A)_-1*A_T");

    // Finding x.
    vector<vector<float>> x = multiplyMatrices(A_TA_inverse_A_T, b);
    outputMatrix(x, "x");

    // Closing file streams.
    fin.close();
    fout.close();
    return 0;
}
