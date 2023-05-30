#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <time.h>

int main(){

    clock_t firstTime, secondTime;
    pid_t pid = fork();
    firstTime = clock();

    if (0 == pid){
        printf("2. My id is %d, my parent's id is %d, my execution time is %f\n\n", getpid(), getppid(), ((float)(clock() - firstTime)) * 1000/CLOCKS_PER_SEC);
        exit(EXIT_SUCCESS);
    }

    pid = fork();
    secondTime = clock();

    if (0 == pid){
        printf("3. My id is %d, my parent's id is %d, my execution time is %f\n\n", getpid(), getppid(), ((float)(clock() - secondTime)) * 1000/CLOCKS_PER_SEC);
        exit(EXIT_SUCCESS);
    } else {
        wait(NULL);
        printf("1. My id is %d, my parent's id is %d, my execution time is %f\n\n", getpid(), getppid(), ((float)(clock() - firstTime)) * 1000/CLOCKS_PER_SEC);
        exit(EXIT_SUCCESS);
    }
}
