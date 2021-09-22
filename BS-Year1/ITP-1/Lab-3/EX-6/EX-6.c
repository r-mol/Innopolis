#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main(){

char* s = "Hello";
    int size = 0;

    while(*(s + ++size) != '\0');
    printf("%d",size);
  
  return 0;
}
