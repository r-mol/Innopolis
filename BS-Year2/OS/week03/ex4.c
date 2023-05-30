#include <stdio.h>
#include <stdlib.h>


void* aggregate(void* base, size_t size, int n, void* initial_value, void* (*opr)(const void*, const void*));

void * addInt(const void* a, const void* b){
    int *res = malloc(sizeof(int));

    if (a == NULL) return  b;

    *res = *(int *) a + *(int *) b;

    return (void *) res;
}

void* addDouble(const void* a, const void* b){
    double *res = malloc(sizeof(double));

    if (a == NULL) return  b;

    *res = *(double *) a + *(double *) b;

    return (void *) res;
        
}

void* mulInt(const void* a, const void* b){
    int *res = malloc(sizeof(int));

    if (a == NULL) return  b;

    *res = *(int *) a * *(int *) b;

    return (void *) res;
    
}

void* mulDouble(const void* a, const void* b){
    double *res = malloc(sizeof(double));

    if (a == NULL) return  b;

    *res = *(double *) a * *(double *) b;

    return (void *) res;

}

void* meanInt(const void* a, const void* b){

    double *res = malloc(sizeof(double));

    if (a == NULL){
        *res = (*(int *) b / 5.0);
        return (void *) res;
    }

   * res = *(double *) a + (*(int *) b / 5.0);

    return (void *) res;
    
}

void* meanDouble(const void* a, const void* b){

    double *res = malloc(sizeof(double));

    if (a == NULL){
        *res = (*(double *) b / 5);
        return (void *) &res;
    }

    *res = *(double *) a + (*(double *) b / 5);

    return (void *) res;
}

void* aggregate(void* base, size_t size, int n, void* initial_value, void* (*opr)(const void*, const void*)){

	void* output;

	if (size==sizeof(int)){ // base is a pointer to an integer

        output = initial_value;

        for (int i = 0; i < n; ++i) {

            output = opr(output, base + size * i);

        }


    }else{ // base is a pointer to a double

        output = initial_value;

        for (int i = 0; i < n; ++i) {

            output = opr(output, base + size * i);

        }


    }

	return output;
}




int main() {

    int *ints = malloc(sizeof(int) * 5);
    double *doubles = malloc(sizeof(double) * 5);

    int *int_zero = calloc(1, sizeof(int));
    int *int_one = malloc(sizeof(int));
    *int_one = 1;

    double *double_zero = calloc(1, sizeof(double));
    double *double_one = malloc(sizeof(double));
    *double_one = 1.0;


    for (int i = 0; i < 5; i++) {
        scanf("%d", &ints[i]);
    }

    for (int i = 0; i < 5; i++) {
        scanf("%lf", &doubles[i]);
    }


    // Addition

    int *result1a;

    for (int i = 0; i < 5; i++) {
        result1a = aggregate(ints, sizeof(int), 5, int_zero, addInt);
    }

    printf("%d\n", *result1a);

    double *result2a;

    for (int i = 0; i < 5; i++) {
        result2a = aggregate(doubles, sizeof(double), 5, double_zero, addDouble);
    }

    printf("%f\n", *result2a);


    // Multiplication

    int *result1m;

    for (int i = 0; i < 5; i++) {
        result1m = aggregate(ints, sizeof(int), 5, int_one, mulInt);
    }

    printf("%d\n", *result1m);

    double *result2m;

    for (int i = 0; i < 5; i++) {
        result2m = aggregate(doubles, sizeof(double), 5, double_one, mulDouble);
    }

    printf("%f\n", *result2m);

    // Mean

    double *result1mean;

    for (int i = 0; i < 5; i++) {
        result1mean = aggregate(ints, sizeof(int), 5, int_zero, meanInt);
    }

    printf("%lf\n", *result1mean);

    double *result2mean;

    for (int i = 0; i < 5; i++) {
        result2mean = aggregate(doubles, sizeof(double), 5, double_zero, meanDouble);
    }

    printf("%f\n", *result2mean);


    // free the pointers
    free(ints);
    free(doubles);
    free(int_zero);
    free(int_one);
    free(double_zero);
    free(double_one);

    return EXIT_SUCCESS;
}

