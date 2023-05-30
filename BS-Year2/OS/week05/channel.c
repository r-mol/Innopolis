#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

int main()
{
    int fd[2];

    if (pipe(fd) == -1) {
        fprintf(stderr, "Pipe Failed");
        return 1;
    }

    pid_t p = fork();

    if (p != 0) {
        char concat_str[1024];

        while(1) {
            read(fd[0], concat_str, 1024);
            printf("\nConcatenated string %s\n", concat_str);
        }
    } else {
        char input_str[1024];

        while(1) {
            scanf("%s", input_str);
            write(fd[1], input_str, strlen(input_str) + 1);

        }
    }
}

