#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main(){
  
  char* s1="Hello";
      int size = 0;
      while(*(s1 + ++size)!='\0');
      char* s2 = (char*) malloc(size * sizeof(char));
      for(int i = 0; i < size; i++) *(s2+i)=*(s1+i);
      printf("%s", s2);
  
 return 0;
}
