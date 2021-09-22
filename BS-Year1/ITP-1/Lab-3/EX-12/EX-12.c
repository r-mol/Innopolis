#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main(){

    int begin ;
    int end;
    int arr[200];
    int size =0 ;
    printf("Starting range :");
    scanf("%d",&begin);
    printf("Ending range :");
    scanf("%d",&end);

    for(; begin <= end;begin++){
        int temp = begin;
        int sum = 0;
        while(temp != 0){
            int x = temp %10;
            int mul = 1;
            for(int i = 1; i<= x;i++){
                mul *=i;
            }
            sum+=mul;
            temp /=10;
        }
        if(sum == begin){
            arr[size++] = begin;
        }
    }

    printf("The strong numbers are: ");
    for (int i = 0; i< size;i++ ){
        printf("%d;",arr[i]);
    }
  
  return 0;
}
