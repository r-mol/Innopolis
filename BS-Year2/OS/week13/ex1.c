#include <stdio.h>
#include <stdlib.h>

int main() {
  int num_of_processes;
  int num_of_resources;

  printf("Enter number of processes: ");
  scanf("%d", &num_of_processes);
  printf("Enter number of resources: ");
  scanf("%d", &num_of_resources);

  int *existing_resources = (int *)malloc(num_of_resources * sizeof(int));
  int *available_resources = (int *)malloc(num_of_resources * sizeof(int));

  int **allocated_resources = (int **)malloc(num_of_processes * sizeof(int *));
  for (int i = 0; i < num_of_processes; i++)
    allocated_resources[i] = (int *)malloc(num_of_resources * sizeof(int));

  int **requested_resources = (int **)malloc(num_of_processes * sizeof(int *));
  for (int i = 0; i < num_of_processes; i++)
    requested_resources[i] = (int *)malloc(num_of_resources * sizeof(int));

  FILE *fp = fopen("input.txt", "r");

  if (fp == NULL) {
    printf("Error opening file");
    return EXIT_FAILURE;
  }

  for (int i = 0; i < num_of_resources; i++)
    fscanf(fp, "%d", &existing_resources[i]);
  for (int i = 0; i < num_of_resources; i++)
    fscanf(fp, "%d", &available_resources[i]);

  for (int i = 0; i < num_of_processes; i++)
    for (int j = 0; j < num_of_resources; j++)
      fscanf(fp, "%d", &allocated_resources[i][j]);

  for (int i = 0; i < num_of_processes; i++)
    for (int j = 0; j < num_of_resources; j++)
      fscanf(fp, "%d", &requested_resources[i][j]);

  for (int i = 0; i < num_of_resources; i++) {
    int sum = 0;
    for (int j = 0; j < num_of_processes; j++)
      sum += allocated_resources[j][i];
    if (sum + available_resources[i] != existing_resources[i]) {
      printf("Error: allocated resources plus available resources are "
             "grater than existing resources.\n");
      return EXIT_FAILURE;
    }
  }

  int do_not_have_available = 0;
  int all_processes_terminated = 0;
  int *terminated_processes = calloc(num_of_processes, sizeof(int));
  while (!all_processes_terminated && !do_not_have_available) {
    all_processes_terminated = 1;
    do_not_have_available = 1;
    for (int i = 0; i < num_of_processes; i++) {
      if (terminated_processes[i])
        continue;

      all_processes_terminated = 0;
      int can_be_terminated = 1;
      for (int j = 0; j < num_of_resources; j++) {
        if (requested_resources[i][j] > available_resources[j]) {
          can_be_terminated = 0;
          break;
        }
      }
      if (can_be_terminated) {
        for (int j = 0; j < num_of_resources; j++) {
          available_resources[j] += allocated_resources[i][j];
        }
        terminated_processes[i] = 1;
        do_not_have_available = 0;
      }
    }
  }

  if (all_processes_terminated) {
    printf("All processes terminated successfully\n");
  } else {
    printf("Processes that are deadlocked: ");
    for (int i = 0; i < num_of_processes; i++) {
      if (!terminated_processes[i])
        printf("p%d ", i + 1);
    }
    printf("\n");
  }

  free(existing_resources);
  free(available_resources);
  free(terminated_processes);

  for (int i = 0; i < num_of_processes; i++)
    free(allocated_resources[i]);
  free(allocated_resources);

  for (int i = 0; i < num_of_processes; i++)
    free(requested_resources[i]);
  free(requested_resources);

  fclose(fp);
  return EXIT_SUCCESS;
}
