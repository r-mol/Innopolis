#include <stdio.h>
#include <stdlib.h>

typedef struct{
    int id;
    int startTime;
    int completionTime;
    int turnaroundTime;
    int waitingTime;
    int arrivalTime;
    int burstTime;
} Process;

int compareByArrival(const void *s1, const void *s2)
{
    Process *p1 = (Process *)s1;
    Process *p2 = (Process *)s2;

    if ( p1->arrivalTime == p2->arrivalTime )
        return p1->id - p2->id;
    else
        return p1->arrivalTime - p2->arrivalTime;
}

int compareById(const void *s1, const void *s2)
{
    Process *p1 = (Process *)s1;
    Process *p2 = (Process *)s2;

    return p1->id - p2->id;
}

int max(int x, int y){
    if (x < y){
        return y;
    } else {
        return x;
    }
}

int main(void){
    int n;
    float averageTurnaroundTime;
    float averageWaitingTime;
    int totalTurnaroundTime = 0;
    int totalWaitingTime = 0;

    printf("Enter the number of processes: ");
    scanf("%d", &n);

    Process *processes = malloc(n * sizeof (Process));

    printf("Enter the arrival time and burst time of processes:\n");
    for(int i = 0; i < n ; i++){
        int aTmp, bTmp;

        scanf("%d %d", &aTmp,&bTmp);

        processes[i].arrivalTime = aTmp;
        processes[i].burstTime = bTmp;
        processes[i].id = i+1;
    }

    qsort(processes, n, sizeof(Process), compareByArrival);

    for(int i = 0; i < n ; i++){
        processes[i].startTime = (i == 0)?processes[i].arrivalTime:max(processes[i-1].completionTime,processes[i].arrivalTime);
        processes[i].completionTime = processes[i].startTime + processes[i].burstTime;
        processes[i].turnaroundTime = processes[i].completionTime - processes[i].arrivalTime;
        processes[i].waitingTime = processes[i].turnaroundTime - processes[i].burstTime;

        totalTurnaroundTime += processes[i].turnaroundTime;
        totalWaitingTime += processes[i].waitingTime;
    }

    qsort(processes, n, sizeof(Process), compareById);

    for(int i = 0; i < n ; i++){
        printf("Completion time of process %d: %d\n", i,processes[i].completionTime);
        printf("Turnaround time of process %d: %d\n", i,processes[i].turnaroundTime);
        printf("Waiting time of process %d: %d\n\n", i,processes[i].waitingTime);
    }

    averageTurnaroundTime = (float) totalTurnaroundTime / n;
    averageWaitingTime = (float) totalWaitingTime / n;

    printf("Average turnaround time: %f\n", averageTurnaroundTime);
    printf("Average waiting time: %f\n", averageWaitingTime);

    free(processes);

    return EXIT_SUCCESS;
}