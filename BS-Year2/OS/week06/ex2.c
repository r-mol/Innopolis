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

int compareByArrivalAndBurst(const void *s1, const void *s2)
{
    Process *p1 = (Process *)s1;
    Process *p2 = (Process *)s2;

    if ( p1->arrivalTime == p2->arrivalTime )
        return p1->burstTime - p2->burstTime;
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
    int *used = calloc(n, n * sizeof (int));

    printf("Enter the arrival time and burst time of processes:\n");
    for(int i = 0; i < n ; i++){
        int aTmp, bTmp;

        scanf("%d %d", &aTmp,&bTmp);

        processes[i].arrivalTime = aTmp;
        processes[i].burstTime = bTmp;
        processes[i].id = i;
    }

    qsort(processes, n, sizeof( Process), compareByArrivalAndBurst);

    int processCompletionTime = 0;

    for(int i = 0; i < n ;){
        int id = i;
        if(used[processes[i].id] == 1){
            i++;
            continue;
        }

        Process process = processes[i];
        used[process.id] = 1;

        for (int j = i + 1; j < n; j++){
            if (processes[j].arrivalTime > processCompletionTime){
                break;
            }

            if(used[processes[j].id] == 1){
                continue;
            }

            if(processes[j].burstTime < process.burstTime){
                used[process.id] = 0;
                process = processes[j];
                used[process.id] = 1;
                id = j;
            }
        }

        process.startTime = (process.id == 0)?process.arrivalTime:max(processCompletionTime,process.arrivalTime);
        process.completionTime = process.startTime + process.burstTime;
        process.turnaroundTime = process.completionTime - process.arrivalTime;
        process.waitingTime = process.turnaroundTime - process.burstTime;
        processCompletionTime = process.completionTime;

        processes[id] = process;

        totalTurnaroundTime += process.turnaroundTime;
        totalWaitingTime += process.waitingTime;

        if (id == i){
            i++;
        }
    }

    qsort(processes, n, sizeof( Process), compareById);

    for(int i = 0; i < n ; i++){
        printf("Completion time of process %d: %d\n", processes[i].id + 1,processes[i].completionTime);
        printf("Turnaround time of process %d: %d\n", processes[i].id + 1,processes[i].turnaroundTime);
        printf("Waiting time of process %d: %d\n\n", processes[i].id +1 ,processes[i].waitingTime);
    }

    averageTurnaroundTime = (float) totalTurnaroundTime / n;
    averageWaitingTime = (float) totalWaitingTime / n;

    printf("Average turnaround time: %f\n", averageTurnaroundTime);
    printf("Average waiting time: %f\n", averageWaitingTime);

    free(processes);
    free(used);

    return EXIT_SUCCESS;
}