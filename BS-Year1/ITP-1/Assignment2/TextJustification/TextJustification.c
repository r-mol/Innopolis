#include <stdio.h>
#include <string.h>
#include <stdlib.h>

struct text{
    char word[100];
};

int main() {
    FILE *fin = fopen("input.txt", "r");
    FILE *fout = fopen("output.txt", "w");

    int count_words = 0;
    struct text ptr[100];
    int maxWidth;

    // Input from FILE
    while(!feof(fin)){
        fscanf(fin, "%s", ptr[count_words].word);
        count_words++;
    }
    // Inotialize from last element of ptr
    maxWidth = atoi(ptr[count_words-1].word);
    count_words--;
    int index = 0, count = 0, len = 0, spaces[100], s[100];
    char res[300][300];
    int rSize = 0;

    // Inotialize NULL to all elements of  array spaces
    for (int i = 0; i < 100; i++) {
        spaces[i] = 0;
    }

    while(index < count_words) {
        // find count of words for first row
        len += strlen(ptr[index].word);
        if(len + count > maxWidth){
            int i = 0;
            len -= strlen(ptr[index].word);
            while(len < maxWidth){
                // count how much additional spaces I need
                if(count == 1){
                    spaces[0]++;
                    len++;
                }
                else{
                    spaces[i % (count - 1)]++;
                    len++;
                    i++;
                }
            }
            res[rSize][0] = '\0';

            // Add to array's res[i] words and spaces
            for(i = 0; i < count; i++){
                strcat(res[rSize], ptr[s[i]].word);
                for(int j = 0; j < spaces[i]; j++){
                    strcat(res[rSize], " ");
                }
            }

            // Again zeroing the variable
            for (i = 0; i < 100; i++) {
                spaces[i] = 0;
            }
            count = 0;
            len = strlen(ptr[index].word);
            rSize++;
        }

        s[count] = index;
        count++;
        index++;
    }

    // The same algorithm but for last element
    int i = 0;
    for(int i = 0; i < count - 1; i++){
        spaces[i]++;
        len++;
    }
    while(len < maxWidth){
        spaces[count - 1]++;
        len++;
    }
    res[rSize][0] = '\0';
    for(i = 0; i < count; i++){
        strcat(res[rSize], ptr[s[i]].word);
        for(int j = 0; j < spaces[i]; j++) {
            strcat(res[rSize], " ");
        }
    }

    // Output to file fout
    for(int w = 0;w <= rSize; w++) {
        if(rSize == w){
            int e = 0;
            do{
                fprintf(fout,"%c",res[w][e]);
                e++;
            }while(res[rSize][e] != '\000' && res[rSize][e] != '.');

            if(res[rSize][e] == '.'){
                fprintf(fout,".");
            }
        }
        else{
            fprintf(fout,"%s\n",res[w]);
        }
    }

    fclose(fin);
    fclose(fout);

    return 0;
}
