#include <stdio.h>

void printTriangle1 (int n, int side){
    if (side == 0){
    for(int i = 0; i < n; i++){
        for (int j =0; j<= i; j++){
            putchar('*');
        }
        putchar('\n');
    }
    } else {

    for(int i = 0; i < n; i++){
        for (int j = n - 1; j>= i; j--){
            putchar('*');
        }
        putchar('\n');
    }
    }
}

void printRectangle(int a, int b){
    for (int i =0; i < a; i++){
        for(int j = 0; j < b; j++){
            putchar('*');
        }
        putchar('\n');
    }
}

void printTriangle2(int n){
    printTriangle1(n,0);
    printTriangle1(n-1,1);
}


int main (){
    int height;

    printf("Please enter height of right triangle: ");
    scanf("%d",&height);

    printTriangle1(height,0);

    printf("\nPlease enter height of triangle: ");
    scanf("%d",&height);

    printTriangle2(height);

    int a,b;

    printf("\nPlease enter height of rectangle: ");
    scanf("%d",&a);

    printf("\nPlease enter width of rectangle: ");
    scanf("%d",&b);

    printRectangle(a,b);

    return 0;
}
