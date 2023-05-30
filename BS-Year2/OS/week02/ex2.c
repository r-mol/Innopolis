#include <stdio.h>

int main(void) {
    char c;
    char word[256];
    int i = 0;
    char key = '\n';

    printf("Enter text. Include a dot ('.') in a sentence to exit or finished with ENTER key:\n");

    do {
        c = getchar();
        word[i] = c;
        i++;
    } while (c != '.' && c != key);

    i -= 2;

    putchar('\"');

    for (; i >= 0; i--) {
        putchar(word[i]);
    }

    puts("\"\n");

    return 0;
}
