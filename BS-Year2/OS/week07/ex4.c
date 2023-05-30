#include <stdio.h>
#include <stdlib.h>
#include <string.h>

size_t min(size_t x, size_t y){
    if (x < y){
        return x;
    } else {
        return y;
    }
}

void *realloc2(void *ptr, size_t oldSize, size_t newSize) {
     void *newPtr = malloc(newSize);
     if(newPtr == NULL){
         return  NULL;
     }

     if (ptr == NULL || oldSize == 0){
         return newPtr;
     }

     memcpy(newPtr, ptr,  min(newSize, oldSize));
     free(ptr);
     return newPtr;
 }

int main() {
   return 0;
}
