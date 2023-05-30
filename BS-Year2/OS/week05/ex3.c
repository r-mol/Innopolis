#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>

int is_prime(int n)
{
  if (n <= 1)
    return 0;
  for (int d = 2; d * d <= n; d++)
    if (n % d == 0)
      return 0;
  return 1;
}

int primes_count_in_interval(int start, int finish)
{
  int ret = 0;
  for (int i = start; i < finish; i++)
    if (is_prime(i) != 0)
      ret++;
  return ret;
}

// The structure that will be passed to the threads, corresponding to an interval to count the number of primes in.
typedef struct prime_counter_request
{
  int start, finish;
} prime_counter_request;

void *prime_counter(void *arg)
{
    int *res = malloc(sizeof(int));

    prime_counter_request * pcr = (prime_counter_request *) arg;
    *res = primes_count_in_interval(pcr->start, pcr->finish);

    return res;
}

int main(int argc, char *argv[]) {
    int n = atoi(argv[1]), n_threads = atoi(argv[2]);
    int segment_size = n / n_threads;

    pthread_t *threads = malloc(n_threads * sizeof(pthread_t));

    prime_counter_request *requests = malloc(n_threads * sizeof(prime_counter_request));

    void **results = malloc(n_threads * sizeof(void *));

    for (int i = 0; i < n_threads; i++) {
        pthread_t id;

        prime_counter_request pcr = {.start = i * segment_size, .finish = (i + 1) * segment_size};
        requests[i] = pcr;

        int err = pthread_create(&id, NULL, prime_counter, &requests[i]);
        if (err != 0){
            printf("Error of creating thread...");
            return EXIT_FAILURE;
        }

        threads[i] = id;
    }

    for (int i = 0; i < n_threads; i++) {
        pthread_join(threads[i], &results[i]);
    }

    int total_result = 0;
    for (int i = 0; i < n_threads; i++)
        total_result += *(int *) results[i];

    free(requests);
    free(threads);

    printf("Total number of prime numbers in a range: %d\n", total_result);

    exit(EXIT_SUCCESS);
}
