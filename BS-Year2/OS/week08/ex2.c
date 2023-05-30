#include <stdlib.h>
#include <math.h>
#include <stdio.h>
#include <time.h>
#include<unistd.h>
#include <string.h>

// Ex 2: In macOs system I do not get changing in swapins and swapouts columns even with allocating 10GB instead of 10MB.
// Ex 3: By top again I do not get swapping of memory even with allocating 10GB instead of 10MB.
//
// But system start to compress memory in both exercises than I allocating 10GB instead of 10MB.
// I use array of pointers for making free() in the end and because system clean
// memory than I allocate it and do not use it.

int main(){
    int *arr[20];
    int i = 0;
    long seconds = time(NULL), timer = seconds + 10;
    while(seconds < timer){
        arr[i] = malloc(pow(2,30)*10);
        memset(arr[i],0, pow(2,30)*10);
        seconds = time(NULL);
        sleep(1);
        i++;
    }

    for(int j = 0; j < i; j++){
        free(arr[j]);
    }

    return EXIT_SUCCESS;
}
