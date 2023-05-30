#include <stdio.h>
#include <limits.h>
#include <stdlib.h>

struct Queue {
    int front, rear, size;
    unsigned capacity;
    int* array;
};

struct Queue* createQueue(unsigned capacity)
{
    struct Queue* queue = (struct Queue*)malloc(
            sizeof(struct Queue));
    queue->capacity = capacity;
    queue->front = queue->size = 0;

    queue->rear = capacity - 1;
    queue->array = (int*)malloc(
            queue->capacity * sizeof(int));
    return queue;
}

int isFull(struct Queue* queue)
{
    return (queue->size == queue->capacity);
}

int isEmpty(struct Queue* queue)
{
    return (queue->size == 0);
}

void enqueue(struct Queue* queue, int item)
{
    if (isFull(queue))
        return;
    queue->rear = (queue->rear + 1) % queue->capacity;
    queue->array[queue->rear] = item;
    queue->size = queue->size + 1;
}

int dequeue(struct Queue* queue)
{
    if (isEmpty(queue))
        return INT_MIN;
    int item = queue->array[queue->front];
    queue->front = (queue->front + 1)
                   % queue->capacity;
    queue->size = queue->size - 1;
    return item;
}

typedef struct{
    int id;
    int startTime;
    int completionTime;
    int turnaroundTime;
    int waitingTime;
    int arrivalTime;
    int burstTime;
    int burstTimeInit;
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

int main(void){
    int n;
    int quantum = 0;
    float averageTurnaroundTime;
    float averageWaitingTime;
    int totalTurnaroundTime = 0;
    int totalWaitingTime = 0;

    printf("Enter the number of processes: ");
    scanf("%d", &n);

    printf("Enter the quantum for processes: ");
    scanf("%d",&quantum);

    struct Queue* queue = createQueue(n + 1);
    Process *processes = malloc(n * sizeof (Process));
    int *completed = calloc(n, n * sizeof (int));
    int *used = calloc(n, n * sizeof (int));
    int *started = calloc(n, n * sizeof (int));

    printf("Enter the arrival time and burst time of processes:\n");
    for(int i = 0; i < n ; i++){
        int aTmp, bTmp;

        scanf("%d %d", &aTmp,&bTmp);

        processes[i].arrivalTime = aTmp;
        processes[i].burstTimeInit = bTmp;
        processes[i].burstTime = bTmp;
        processes[i].id = i;
    }

    qsort(processes, n, sizeof(Process), compareByArrival);

    int processCompletionTime = processes[0].arrivalTime;

    used[processes[0].id] = 1;

    enqueue(queue, 0);

    while(!isEmpty(queue)) {

        int id = dequeue(queue);
        Process process = processes[id];

        if(!started[process.id]){
            process.startTime = processCompletionTime;
            started[process.id] = 1;
            processes[id] = process;
        }

        if (process.burstTime <= quantum) {
            processCompletionTime += process.burstTime;
            process.completionTime = processCompletionTime;
            process.turnaroundTime = process.completionTime - process.arrivalTime;
            process.waitingTime = process.turnaroundTime - process.burstTimeInit;

            completed[process.id] = 1;
            used[process.id]=1;

            for (int i = 0; i < n; i++) {
                if (completed[processes[i].id]) {
                    continue;
                }

                if (!used[processes[i].id] && processes[i].arrivalTime <= processCompletionTime) {
                    used[processes[i].id] = 1;

                    enqueue(queue, i);
                }
            }

            totalTurnaroundTime += process.turnaroundTime;
            totalWaitingTime += process.waitingTime;

            processes[id] = process;
        } else {
            processCompletionTime += quantum;
            process.burstTime -= quantum;

            for (int i = 0; i < n; i++) {
                if (completed[processes[i].id]) {
                    continue;
                }

                if (!used[processes[i].id] && processes[i].arrivalTime <= processCompletionTime) {
                    used[processes[i].id] = 1;

                    enqueue(queue, i);
                }
            }

            enqueue(queue, id);

            processes[id] = process;
        }
    }

    qsort(processes, n, sizeof( Process), compareById);

    for(int i = 0; i < n ; i++){
        printf("Completion time of process %d: %d\n", i,processes[i].completionTime);
        printf("Turnaround time of process %d: %d\n", i,processes[i].turnaroundTime);
        printf("Waiting time of process %d: %d\n\n", i,processes[i].waitingTime);
    }

    averageTurnaroundTime = (float) totalTurnaroundTime / n;
    averageWaitingTime = (float) totalWaitingTime / n;

    printf("Average turnaround time: %f\n", averageTurnaroundTime);
    printf("Average waiting time: %f\n", averageWaitingTime);

    free(queue);
    free(processes);
    free(completed);
    free(used);
    free(started);

    return EXIT_SUCCESS;
}