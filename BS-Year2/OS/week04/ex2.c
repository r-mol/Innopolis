#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <time.h>

#define LEN_OF_VECTOR 120
#define MAX_VALUE_IN_RANGE 99
#define FILE_PATH "/Users/rmoll/Developer/CLionProjects/OS_Innopolis/week04/temp.txt"

int findDotProduct(int i, int j,int *u,int *v){
    int dotProduct = 0;

    for ( ; i < j; i++){
        dotProduct += u[i]*v[i];
    }

    return dotProduct;
}

void readFromFile(int * line,char filePath[]){
    FILE * file;
    file = fopen(filePath,"r");

    int i =0;

    if (file==NULL){
        printf("Error! opening file");
        exit(EXIT_FAILURE);
    }

    while (!feof (file)){
        int temp;
        fscanf (file, "%d", &temp);
        line[i] = temp;
        i++;
    }

    fclose(file);
}

void writeToFile(char result[], char filePath[],char mode[]){
    FILE * file;

    file = fopen(filePath,mode);

    if (file==NULL){
        printf("Error! opening file");

        exit(EXIT_FAILURE);
    }

    fputs(result,file);

    fclose(file);
}

void fileWriteDotProduct(int begin, int end, int u[],int v[], char filePath[]){
    int dotProduct = findDotProduct(begin, end,u,v);

    char strDotProduct[100];
    sprintf(strDotProduct, "%d", dotProduct);

    strcat(strDotProduct," ");

    writeToFile(strDotProduct, filePath, "a");
}

void aggregateResults(char filePath[], int n){
    int *arr = (int*) calloc(n, sizeof(int));
    int finalSum = 0;

    readFromFile(arr, filePath);

    for (int i = 0; i <  n ;i++){
        finalSum += arr[i];
    }

    free(arr);

//    char strFinalSum[100];
//    sprintf(strFinalSum,"%d", finalSum);
//
//    writeToFile(strFinalSum, filePath, "w");

    printf("Dot product of random vectors: %d", finalSum);
}

void createPoolOfProcesses(int n, int lenOfDotProduct, pid_t *ids, int* begin,int* end){
    for(int i = 0; i < n;i++) {
        pid_t pid = fork();

        if (0 == pid) {
            *begin = i * lenOfDotProduct;
            *end = (i + 1) * lenOfDotProduct;
            break;
        } else {
            ids[i] = pid;
        }
    }
}

void fillByRandom(int *arr){
    for (int i = 0; i < LEN_OF_VECTOR; i++){
        arr[i] = rand() % (MAX_VALUE_IN_RANGE + 1);
    }
}

int main(){
    fclose(fopen("./temp.txt", "w"));
    pid_t mainId = getpid();
    int n, lenOfDotProduct;

    printf("Please enter number of processes:\n");
    scanf("%d",&n);

    lenOfDotProduct = LEN_OF_VECTOR / n;

    pid_t *ids = (pid_t *) calloc(n, sizeof(pid_t));
    int *u = (int *) calloc(LEN_OF_VECTOR, sizeof(int));
    int *v = (int *) calloc(LEN_OF_VECTOR, sizeof(int));
    int begin;
    int end;

    time_t t;

    srand((unsigned) time(&t));

    fillByRandom(u);
    fillByRandom(v);

    createPoolOfProcesses(n,lenOfDotProduct,ids,&begin,&end);

    if (mainId == getpid() ) {
        for(int i = 0; i < n; i++) {
            waitpid(ids[i],0,0);
        }

        aggregateResults("./temp.txt", n);

        free(u);
        free(v);
        free(ids);

        exit(EXIT_SUCCESS);
    } else {
        fileWriteDotProduct(begin, end , u, v, "./temp.txt");

        free(u);
        free(v);
        free(ids);

        exit(EXIT_SUCCESS);
    }
}
