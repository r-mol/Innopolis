#include <stdlib.h>
#include <math.h>
#include <stdio.h>
#include <time.h>
#include<unistd.h>
#include<string.h>
#include <sys/resource.h>

// Ex 4: System also start to compress data in the memory than I allocating 1GB instead of 10MB.
//
// I use array of pointers for making free() in the end and because system clean
// memory than I allocate it and do not use it.

int main(){
    int i = 0;
    int *arr[20];
    struct rusage usage;
    long seconds = time(NULL), timer = seconds + 10;
    while(seconds < timer){
        arr[i] = malloc(pow(2,30));
        memset(arr[i],0, pow(2,30));
        seconds = time(NULL);
        getrusage(RUSAGE_SELF,&usage);
        printf("Memory usage: %ld\n",usage.ru_maxrss);
        sleep(1);
        i++;
    }

    for(int j = 0; j < i; j++){
        free(arr[j]);
    }
    return EXIT_SUCCESS;
}
