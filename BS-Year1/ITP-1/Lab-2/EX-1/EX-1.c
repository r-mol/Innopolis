#include <stdio.h>

void inputMatrix(int arr[5][5]){
    for(int i = 0; i < 5; i++){
        for(int j = 0; j < 5; j++){
            scanf("%d", (*(arr + i) + j));
        }
    }
}

void printMatrix(int (*arr)[5]){
    for (int i = 0; i < 5; i++){
        for (int j = 0; j < 5; j++){
            printf("%d ", *(*(arr + i) + j));
        }
        printf("\n");
    }
}

int main(){
    int arr[5][5];

    inputMatrix(arr);
    printMatrix(arr);
    return 0;
}
