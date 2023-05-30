#include <stdlib.h>
#include <stdio.h>

int main(){
    printf("Enter original array size:");
    int n=0;
    scanf("%d",&n);

    int *arr = (int *)malloc(n * sizeof(int));
    if (arr == NULL) {
        printf("Memory allocation error\n");
        return 1;
    }

    for(int i=0; i<n; i++){
        arr[i] = i;
    }

    for(int i=0; i<n;i++){
        printf("%d ", arr[i]);
    }
    printf("\n");

    free(arr);

    return 0;
}
