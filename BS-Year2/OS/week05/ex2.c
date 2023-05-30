#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <pthread.h>
#include <string.h>

typedef struct{
    pthread_t id;
    int i;
    char  message[257];
} Thread;

void *helloThread(void *vargp){
    Thread *thread = (Thread *)vargp;

    printf("%s", thread -> message);

}

int main() {
    int n;

    printf("Enter the number of threads:\n");
    scanf("%d", &n);

    Thread threads[n];


    for (int i = 0; i < n; i++) {

        threads[i].i = i;

        sprintf(threads[i].message, "Hello from thread %d.\n", i);

        int err = pthread_create(&threads[i].id, NULL, helloThread, &threads[i]);

        if (err != 0) {
            printf("Error of creating thread...");
            return EXIT_FAILURE;
        }
        printf("Thread %d is created.\n", threads[i].i);
        pthread_join(threads[i].id, NULL);

        printf("Thread %d is existed.\n", threads[i].i);
    }

    pthread_exit(NULL);
}
