#include<stdio.h>

void findTribonacciSequenceOf(int number) {
    int n0 = 0, n1 = 1, n2 = 1, n3, i;

    for (i = 3; i <= number; i++) {
        n3 = n0 + n1 + n2;

        n0 = n1;
        n1 = n2;
        n2 = n3;
    }

    printf(" %d\n",n3);
}

int main() {
    printf("Tribonacci sequence of number 4:");
    findTribonacciSequenceOf(4);

    printf("\nTribonacci sequence of number 36:");
    findTribonacciSequenceOf(36);

    return 0;
}
