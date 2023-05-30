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

int n = 0;

// You will be locking and unlocking this
pthread_mutex_t global_lock = PTHREAD_MUTEX_INITIALIZER;

// Don't modify these variables directly, use the functions below.
int next_number_to_check = 0;
int primes_found_so_far = 0;

int get_number_to_check()
{
  int ret = next_number_to_check;
  if (next_number_to_check != n)
    next_number_to_check++;
  return ret;
}

void increment_primes()
{
  primes_found_so_far++;
}

void *check_primes(void *arg)
{
    while(1) {
        int res;
        pthread_mutex_lock(&global_lock);
        res = get_number_to_check();
        pthread_mutex_unlock(&global_lock);

        if (res == n){
            return EXIT_SUCCESS;
        }

        if (is_prime(res)) {
            pthread_mutex_lock(&global_lock);
            increment_primes();
            pthread_mutex_unlock(&global_lock);
        }
    }
}

int main(int argc, char *argv[])
{
  int n_threads = atoi(argv[2]);
  n = atoi(argv[1]);


  pthread_t *threads = malloc(n_threads * sizeof(pthread_t));
  for (int i = 0; i < n_threads; i++)
  {
      pthread_t id;

      int err = pthread_create(&id, NULL, check_primes, &n);
      if (err != 0){
          printf("Error of creating thread...");
          return EXIT_FAILURE;
      }

      threads[i] = id;
  }

  for (int i = 0; i < n_threads; i++)
  {
      pthread_join(threads[i], NULL);
  }

  free(threads);

  printf("Total number of prime numbers in a range: %d\n", primes_found_so_far);
  exit(EXIT_SUCCESS);
}
