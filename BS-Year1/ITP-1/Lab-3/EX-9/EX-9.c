#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int main(){

  char arr[1000];
    int size = 0;
    char x;
    while (x != '\n') {
        if ((x = (char) getchar()) != ' ') {
            *(arr + size) = x;
            ++size;
        }
    }

    int a[10] = {1,1,1,1,1,1,1,1,1,1};

    for(int i = 0; i < size; i++) {
        for (int j = i + 1; j < size; j++) {
            if(arr[i] == arr[j] && arr[i] !='*'){
                arr[j] = '*';
                a[i]++;
            }
        }
    }
    size = size - 1;

    for(int i = 0; i < size;i++){
        if(arr[i]!='*'){
            for(int j=i+1 ; j < size;j++){
                if(a[i]<a[j]){
                    char temp = arr[j];
                    arr[j] = arr[i];
                    arr[i] = temp;
                    int tem = a[j];
                    a[j] = a[i];
                    a[i] = tem;
                }
                else{
                    if(a[i]==a[j] && arr[i]>arr[j]){
                        char temp = arr[j];
                        arr[j] = arr[i];
                        arr[i] = temp;
                    }
                }
            }
        }
    }

    for(int i = 0; i < size; i++) {
        if(arr[i]!='*'){
            printf("%c", arr[i]);
            for(int j = 0; j < a[i]; j++){
                printf(".");
            }
            printf("\n");
        }
    }
  
 return 0; 
}
