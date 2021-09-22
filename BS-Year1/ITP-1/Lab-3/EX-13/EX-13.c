#include <stdlib.h>
#include <stdio.h>
#include <string.h>

int main(){

   char str[3];
    int find = 0;
    int attempts = 1;

    again:

    printf("Password =");
    gets(str);


    for(int i = 0 ; i < 3 ; i++){
        if(str[i] >= 32 && str[i] <= 126){
            find = 0;
            break;
        }
        else{
            find = 1;
            break;
        }
    }

    if(find == 0){
        printf("found = %s\n", str);
        printf("number of attempts = %d", attempts);
    }
    else{
        printf("Password didn't find, please try again :");
        goto again;
        attempts++;
    }
  
 return 0;
}
