#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv){
    if(argc <= 1){
        printf("Please, enter number of pages in to arguments!");
        return 1;
    }
    int pageNum = atoi(argv[1]);

    FILE* file = fopen("Lab 09 input.txt","r");
    if(file == NULL){
        printf("File does not exist!!!");
        return 1;
    }

    int num_elements =0;
    int page;
    int arr[10000];
    int max_value = 0;
    while(fscanf(file, "%d", &page) == 1) {
        arr[num_elements] = page;
        if (page > max_value) {
            max_value = page;
        }
        num_elements++;
    }

    max_value+=1;

    int hits = 0;
    int miss = 0;
    unsigned int *pageCounter = calloc(max_value,sizeof(unsigned int));
    int* table = calloc(pageNum, sizeof(int));

    for(int j =0; j < num_elements; j++){
        page = arr[j];
        for (int i = 0; i < max_value; i++){
            pageCounter[i] >>= 1;
        }

        pageCounter[page] |= (1 << 31);

        int referenced = 0;
        for(int i = 0; i < pageNum; i++){
            if(table[i] == page){
                hits++;
                referenced = 1;
                break;
            }
        }

        if(!referenced) {
            for (int i = 0; i < pageNum; i++) {
                if(table[i] == 0){
                    miss++;
                    table[i] = page;
                    referenced = 1;
                    break;
                }
            }
        }

        if(!referenced){
            miss++;
            int swapIndex= 0;
            unsigned int swapCounter= UINT32_MAX;
            for(int i = 0; i < pageNum; i++){
                unsigned int curCounter = pageCounter[table[i]];
                if(curCounter < swapCounter){
                    swapCounter = curCounter;
                    swapIndex = i;
                }
            }
            table[swapIndex] = page;
        }
    }

    printf("Total pages: %d\n", hits + miss);
    printf("Hits pages: %d\n", hits);
    printf("Misses pages: %d\n", miss);
    printf("Hit/Miss ratio: %.3f\n", 1.0 * hits / miss);

    free(pageCounter);
    free(table);

    fclose(file);
    return 0;
}
