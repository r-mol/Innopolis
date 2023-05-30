#include <errno.h>
#include <fcntl.h>
#include <linux/input.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

int main() {
  const char *dev = "/dev/input/by-path/platform-i8042-serio-0-event-kbd";
  struct input_event ev;
  ssize_t n;
  int fd;

  printf("Shorcuts and their messages:\n");
  printf("P+E -> I passed the Exam!\n");
  printf("C+A+P -> Get some cappuccino!\n");
  printf("W+G -> Where are my grades?\n");
  printf("I+M -> In Moodle broo...\n");

  fd = open(dev, O_RDONLY);
  if (fd == -1) {
    fprintf(stderr, "Cannot open %s: %s.\n", dev, strerror(errno));
    return EXIT_FAILURE;
  }
  int button_P = 0;
  int button_E = 0;
  int button_C = 0;
  int button_A = 0;
  int button_W = 0;
  int button_G = 0;
  int button_I = 0;
  int button_M = 0;
  int button_Other = 0;
  int countOfOthers = 0;

  while (1) {
    n = read(fd, &ev, sizeof ev);
    if (n == (ssize_t)-1) {
      if (errno == EINTR)
        continue;
      else
        break;
    } else if (n != sizeof ev) {
      errno = EIO;
      break;
    }

    if (ev.type == EV_KEY && ev.value >= 0 && ev.value <= 2) {
      if (ev.code == KEY_P) {
        button_P = ev.value;
      } else if (ev.code == KEY_E) {
        button_E = ev.value;
      } else if (ev.code == KEY_C) {
        button_C = ev.value;
      } else if (ev.code == KEY_A) {
        button_A = ev.value;
      } else if (ev.code == KEY_W) {
        button_W = ev.value;
      } else if (ev.code == KEY_G) {
        button_G = ev.value;
      } else if (ev.code == KEY_I) {
        button_I = ev.value;
      } else if (ev.code == KEY_M) {
        button_M = ev.value;
      } else {
        if (ev.value == 1) {
          button_Other = 1;
          countOfOthers++;
        } else if (ev.value == 0) {
          if (countOfOthers == 1) {
            button_Other = 0;
            countOfOthers = 0;
          } else {
            countOfOthers--;
          }
        }
      }

      if (button_P && button_E && !button_Other && !button_C && !button_A &&
          !button_W && !button_G && !button_I && !button_M) {
        printf("I passed the Exam!\n");
      } else if (button_C && button_A && button_P && !button_E && !button_M &&
                 !button_Other && !button_W && !button_G && !button_I) {
        printf("Get some cappuccino!\n");
      } else if (button_W && button_G && !button_C && !button_A && !button_P &&
                 !button_E && !button_Other && !button_I && !button_M) {
        printf("Where are my grades?\n");
      } else if (button_I && button_M && !button_C && !button_A && !button_P &&
                 !button_E && !button_Other && !button_W && !button_G) {
        printf("In Moodle broo...\n");
      }
    }
  }

  fflush(stdout);
  fprintf(stderr, "%s.\n", strerror(errno));
  return EXIT_SUCCESS;
}
