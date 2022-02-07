#include <bits/stdc++.h>

int main() {
    int n;
    int num = 4029, index = -1;
    std::cin >> n;
    int a[n]
    for (int i = 0; i < n; i++) {
        std::cin >> a[i];
    }

    for (int i = 0; i < n; i++) {
        if (a[i] == num) {
            index = i;
            break;
        }
    }

    std::cout << index;

    return 0;
}