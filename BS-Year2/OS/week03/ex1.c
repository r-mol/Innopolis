#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int foo(int age){
	int result;

	result = 2022 - age;

	return result;
}

int main(){
    const int x =  10;
    const int * q = &x;

    // 5 contiguous memory cells with value of x
    const int arr[5] = {x,x,x,x,x};
    const int * const p = arr;
    int *const pp = p;

    for (int i = 0; i < 5; i++){
        printf("%p\n", &p[i]);
    }


    // Scanning of ages of students
    for (int i = 0; i< 5; i++){
       int tmp;
       scanf("%d",&tmp);

       pp[i] = tmp;
    }

    for (int i = 0; i< 5; i++){
        pp[i] = foo(p[i]);
    }

    for (int i = 0; i < 5; i++){
        printf("%d _ %p\n", p[i], &p[i]);
    }
}
