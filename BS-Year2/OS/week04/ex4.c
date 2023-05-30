#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAXB 1024

char buf[MAXB];

int main() {
    while (1) {
        printf("$ ");
        fgets(buf, MAXB, stdin);

        if (strcmp(buf, "quit\n") == 0) {
            return EXIT_SUCCESS;
        } else {
            system(buf);
        }
    }
}

// Example on using system
// https://www.codingunit.com/c-reference-stdlib-h-function-system

// Example on using execve
// https://linuxhint.com/c-execve-function-usage/