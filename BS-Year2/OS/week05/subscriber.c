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
    int n = atoi(argv[1]);
    int i = atoi(argv[2]);
    char str[MAX_BUF];
    char * myfifo = "/tmp/ex1";

    printf("Subscriber %d is here..\n", i);

    mkfifo(myfifo, 0666);

    while (1) {
        fd = open(myfifo,O_RDONLY);
        read(fd, str, MAX_BUF);

        printf("Published Message: %s\n", str);
        sleep(n + 2);
        close(fd);
    }
}

// mkfifo reference
// https://man7.org/linux/man-pages/man3/mkfifo.3.html
