#include <stdio.h>
#include <stdlib.h>
#include <dirent.h>
#include <string.h>

int main (){
    char* filename = "/";
    DIR *dirp = opendir(filename);

    if (dirp == NULL) {
        return EXIT_FAILURE;
    }

    struct dirent *dp = NULL;
    while ((dp = readdir(dirp)) != NULL) {
      printf("%s\n",dp->d_name);
    }

    return EXIT_SUCCESS;
}

