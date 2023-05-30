#include <stdio.h>
#include <stdlib.h>
#include <dirent.h>
#include <string.h>

int main (){
    char* filename = "./tmp";
    DIR *dirp = opendir(filename);
    struct dirent **dps = calloc(10, sizeof(struct dirent*));

    FILE* file = fopen("ex4.txt","w");
    if(file == NULL){
        printf("File does not exist!!!");
        return 1;
    }

    if (dirp == NULL) {
        return EXIT_FAILURE;
    }

    int numFiles = 0;
    struct dirent *dp = NULL;
    while ((dp = readdir(dirp)) != NULL) {
        dps[numFiles] = dp;
        numFiles++;
    }

    for(int i = 0; i<numFiles;i++){
        int count = 0;
        for(int j = 0; j<numFiles;j++){
            if (dps[j]->d_ino == dps[i]->d_ino){
                count++;
            }
        }

        if (count >= 2){
            int secondCount = 1;
            fprintf(file,"%s --- ", dps[i]->d_name);

            for(int j = 0; j<numFiles;j++){
                if (dps[j]->d_ino == dps[i]->d_ino){
                    if (secondCount == count){
                        fprintf(file,"%s",dps[j]->d_name);
                    } else{
                        fprintf(file,"%s, ", dps[j]->d_name);
                    }
                    secondCount++;
                }
            }

            fprintf(file,"\n");
        }
    }

    free(dps);
    fclose(file);
    (void)closedir(dirp);
    return EXIT_SUCCESS;
}
