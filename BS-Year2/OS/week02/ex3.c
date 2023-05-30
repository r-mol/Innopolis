#include <stdio.h>
#include <string.h>

int fromChar(char c) {
    return (int) c - '0';
}

char toChar(int num) {
    return (char) (num + '0');
}

void reverseString(char *str) {
    int len = strlen(str);
    int i;

    for (i = 0; i < len / 2; i++) {
        char temp = str[i];
        str[i] = str[len - i - 1];
        str[len - i - 1] = temp;
    }
}

void fromDecimal(char res[], int base, long int inputNum) {
    int index = 0;

    while (inputNum > 0) {
        res[index++] = toChar(inputNum % base);
        inputNum /= base;
    }

    res[index] = '\0';

    reverseString(res);
}

long long int toDecimal(char *str, int base) {
    int len = strlen(str);
    int power = 1;
    long long int num = 0;
    int i;

    for (i = len - 1; i >= 0; i--) {
        if (fromChar(str[i]) >= base) {
            printf("\"cannot convert!\"");
            return -1;
        }

        num += fromChar(str[i]) * power;
        power = power * base;
    }

    return num;
}

void convert(long long int number, int fromSystem,int toSystem) {
    char str[256];
    char res[256];
    long long int decimal;
    sprintf(str, "%lld", number);

    decimal = toDecimal(str, fromSystem);

    fromDecimal(res, toSystem, decimal);

    puts(res);
}

int main(void) {
    long long int number;
    int fromBase;
    int toBase;

    printf("Please enter number which need to convert:\n");
    scanf("%lld",&number);

    printf("Please enter base from which need to convert:\n");
    scanf("%d",&fromBase);

    printf("Please enter base to which need to convert:\n");
    scanf("%d",&toBase);

    convert(number, fromBase, toBase);

    return 0;
}

