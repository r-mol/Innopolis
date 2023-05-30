#include <float.h>
#include <limits.h>
#include <stdio.h>

int main() {
    int x = INT_MAX;
    unsigned short int y = USHRT_MAX;
    signed long int e = LONG_MAX;
    float z = FLT_MAX;
    double h = DBL_MAX;

    printf("x: type int, size %lu bytes, value %d\n", sizeof(x), x);
    printf("y: type unsigned short int, size %lu bytes, value %d\n", sizeof(y), y);
    printf("e: type signed long int, size %lu bytes, value %ld\n", sizeof(e), e);
    printf("z: type float, size %lu bytes, value %f\n", sizeof(z), z);
    printf("h: type double, size %lu bytes, value %lf\n", sizeof(h), h);

    return 0;
}
