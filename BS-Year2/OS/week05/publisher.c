#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <string.h>

#define MAX_BUF 1024

int main(int argc, char *argv[]) {
    int fd;
    char * myfifo = "/tmp/ex1";
    char str[MAX_BUF];
    int n = atoi(argv[1]);

    printf("Publisher is here...\n");

    mkfifo(myfifo, 0666);

    while (1) {
        fd = open(myfifo, O_WRONLY);

        fgets(str, MAX_BUF, stdin);

        for(int i = 0; i < n; i++) {
            write(fd, str, strlen(str) + 1);
            sleep(1);
        }
        close(fd);
    }
}

// mkfifo reference
// https://man7.org/linux/man-pages/man3/mkfifo.3.html
