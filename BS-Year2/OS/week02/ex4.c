#include <stdio.h>
#include <string.h>

int count(char str[], char letter) {
    int count = 0;

    for (int i = 0; i < strlen(str) && i < 256; i++) {
        if (str[i] == letter || str[i] == letter - 32) {
            count++;
        }
    }

    return count;
}

void countAll(char str[]) {
    for (int i = 0; i < strlen(str); i++) {
        char letter = str[i];

        if (90 >= letter && letter >= 65) {
            letter += 32;
        }

        int num = count(str, letter);
        printf("%c:%d", letter, num);

        if (i != strlen(str) - 1) {
            printf(", ");
        } else {
            printf("\n");
        }
    }
}

int main(int arg, char **args) {
    //char word[256];

    //printf("Please enter the string to count letters:\n");

    //scanf("%s",word);

    countAll(args[1]);

    return 0;
}
