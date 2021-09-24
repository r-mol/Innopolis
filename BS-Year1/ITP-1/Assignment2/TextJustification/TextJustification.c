#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int main() {
    FILE *fin = fopen("../input.txt", "r");
    FILE *fout = fopen("../output.txt", "w");

    char text[300];
    int maxWidth;

    //Reading From File
    fgets(text, 1000, fin);
    fscanf(fin, "%d", &maxWidth);

    int size = strlen(text) - 1;    //Initialise len of input string
    int rows = size / maxWidth;     //Initialise count of rows

    if (size % maxWidth != 0) {
        rows += 1;
    }

    for (int i = 1; i < rows; i++) {
        int spaces = 0;
        int count_spaces = 0;

        // Delete space in beginning of text
        if (text[i * maxWidth - maxWidth] == ' ') {
            for (int j = i * maxWidth - maxWidth; j < size; j++) {
                text[j] = text[j + 1];
            }
            size--;
        }

        // Delete spaces in end of text
        if (text[i * maxWidth] == ' ') {
            for (int j = i * maxWidth - maxWidth; j < size; j++) {
                text[j] = text[j + 1];
            }
            size--;
        }

        if (text[i * maxWidth] != ' ') {
            int all;
            int last;


            // Count how many values program should move
            for (int j = i * maxWidth; text[j] != ' '; j--) {
                spaces++;
            }

            for (int j = (i * maxWidth) - maxWidth; j < i * maxWidth; j++) {
                if (text[j] == ' ') {
                    count_spaces++;
                }
            }
            if (count_spaces != 1) {
                count_spaces -= 1;
                spaces += count_spaces;
            }

            // Count how long space should be in give row
            if (spaces % count_spaces != 0) {
                all = spaces / count_spaces;
                last = all + (spaces - (all * count_spaces));
            } else {
                all = spaces / count_spaces;
                last = spaces / count_spaces;
            }

            //Add additional spaces
            for (int j = 0; j < count_spaces - 1; j++) {
                char temp;
                int count = 0;
                for (int q = (i * maxWidth) - maxWidth; q < i * maxWidth; q++) {
                    if (text[q] == ' ' && text[q - 1] != ' ' && text[q + 1] != ' ' && count + 1 != count_spaces) {
                        for (int w = 1; w < last; w++) {
                            temp = text[q];
                            text[q] = ' ';
                            size++;
                            for (int e = q + 1; e < size; e++) {
                                char m = text[e];
                                text[e] = temp;
                                temp = m;
                            }
                        }
                        count++;
                    }
                }
            }
            char temp;
            for (int q = i * maxWidth - all; q >= (i * maxWidth) - maxWidth; q--) {
                if (text[q] == ' ' && text[q - 1] != ' ' && text[q + 1] != ' ') {
                    for (int w = 1; w < all; w++) {
                        temp = text[q];
                        text[q] = ' ';
                        size++;
                        for (int e = q + 1; e < size; e++) {
                            char m = text[e];
                            text[e] = temp;
                            temp = m;
                        }
                    }
                    break;
                }
            }
        }
        // Output to file fout
        for (int j = (i * maxWidth) - maxWidth; j < i * maxWidth; j++) {
            fprintf(fout, "%c", text[j]);
        }
        fprintf(fout, "\n");
    }
    if (text[rows * maxWidth - maxWidth] == ' ') {
        for (int j = rows * maxWidth - maxWidth; j < size; j++) {
            text[j] = text[j + 1];
        }
        size--;
    }

    if (text[rows * maxWidth] == ' ') {
        for (int j = rows * maxWidth - maxWidth; j < size; j++) {
            text[j] = text[j + 1];
        }
        size--;
    }

    for (int j = (rows * maxWidth) - maxWidth; j < size; j++) {
        fprintf(fout, "%c", text[j]);
    }

    fclose(fin);
    fclose(fout);

    return 0;
}
