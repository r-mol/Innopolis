#include <stdlib.h>
#include <stdio.h>

struct exam_day{
    int day;
    char month[20];
    int year;
} ;

struct student{
    char Name[20];
    char Surname[20];
    int groupNo;
    struct exam_day exam;
} std[10];

int main() {
    int n;
    scanf("%d", &n);
    for (int i = 0; i < n; i++) {
        printf("Plz enter your fullname:");
        scanf("%s", std[i].Name);
        printf("Plz enter your Surname:");
        scanf("%s", std[i].Surname);
        printf("Plz enter your groupNo:");
        scanf("%d", &std[i].groupNo);
        printf("Plz enter your exam_day:");
        scanf("%d", &std[i].exam.day);
        printf("Plz enter your exam_month:");
        scanf("%s", std[i].exam.month);
        printf("Plz enter your exam_year:");
        scanf("%d", &std[i].exam.year);
    }


    int w;
    printf("Which student do u want to see?");
    scanf("%d", &w);

    printf("%s\n", std[w].Name);
    printf("%s\n", std[w].Surname);
    printf("%d\n", std[w].groupNo);
    printf("%d\n", std[w].exam.day);
    printf("%s\n", std[w].exam.month);
    printf("%d\n", std[w].exam.year);

    return 0;
}