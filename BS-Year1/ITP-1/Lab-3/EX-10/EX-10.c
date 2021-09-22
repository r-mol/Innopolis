#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int main(){
  
  int arr[1000];
    int size = 0;

    for (size  = 0; size < 13; size++){
        scanf("%d", &arr[size]);
    }


    for(int i = 0; i < size - 1; i++){
        for(int j = i + 1; j < size;){
            if(arr[i] == arr[j]){
                for(int q = j; q < size; q++)
                {
                    arr[q] = arr[q + 1];
                }
                size--;
            }
            else{
                j++;
            }
        }
    }
    for(int i = 0; i < size; i++) {
        printf("%d ", arr[i]);
    }
  
 return 0; 
}
