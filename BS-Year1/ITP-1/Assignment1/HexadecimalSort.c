#include <stdio.h>

int main() {
    FILE *fin = fopen("input.txt","rb");
    FILE *fout = fopen("output.txt","w");

    int arr[1000];
    int i = 0;

    //Input from file
    while (!feof(fin))
    {
        fscanf(fin, "%X", &arr[i]);
        i++;
    }

    int n = i;

    for(i = 0;i < n - 1; i++){
        for(int j = i +1; j < n; j++){
            if(arr[i]> arr[j]){
                int temp = arr[i];
                arr[i]= arr[j];
                arr[j] = temp;
            }
        }
    }

    for(i = 0; i < n;i++) {
        fprintf(fout,"%X ",arr[i]);

    }

    fclose(fin);
    fclose(fout);
    return 0;
}