#include <stdio.h>
#include <string.h>

// structure for items information
struct item{
    char name_item[100];
    double size;
    int amount;
    char measurement_unit[6];
};

// structure for tenant information
struct Forms{
    char fullname[30];
    char date[11];
    char time[9];
    struct item equipment[100];
};


int main() {
    FILE *fin = fopen("input.txt", "r");
    FILE *fout = fopen("output.txt", "w");

    struct Forms tenant[100];

    int CountRenters = 0;
    int CountEquipment[100];
    char s[2] = {'\000','\n'};

    // Input data from file (fin)
    while(!feof(fin)) {
        fscanf(fin, "%[^\n]s", tenant[CountRenters].fullname);
        fscanf(fin, "%s", tenant[CountRenters].date);
        fscanf(fin, "%s", tenant[CountRenters].time);

        for(int i = 0; i < 100; i++) {
            if((s[0] = (char)fgetc(fin)) == '\n' && (s[1] = (char)fgetc(fin)) == '\n'){
                break;
            }else{
                if(s[0] == ' '){
                    fscanf(fin, "%[^0-9]s", tenant[CountRenters].equipment[CountEquipment[CountRenters]].name_item);
                    for(int j = 0; j < strlen(tenant[CountRenters].equipment[CountEquipment[CountRenters]].name_item) ; j++){
                        tenant[CountRenters].equipment[CountEquipment[CountRenters]].name_item[j] = tenant[CountRenters].equipment[CountEquipment[CountRenters]].name_item[j+1];
                    }

                }
                else {
                    fscanf(fin, "%[^0-9]s", tenant[CountRenters].equipment[CountEquipment[CountRenters]].name_item);
                    for(int j = strlen(tenant[CountRenters].equipment[CountEquipment[CountRenters]].name_item) - 1; j >= 0 ; j--){
                        tenant[CountRenters].equipment[CountEquipment[CountRenters]].name_item[j + 1] = tenant[CountRenters].equipment[CountEquipment[CountRenters]].name_item[j];
                    }
                    tenant[CountRenters].equipment[CountEquipment[CountRenters]].name_item[0] = s[1];
                }

                s[1] = '\000';
            }


            fscanf(fin, "%lf", &tenant[CountRenters].equipment[CountEquipment[CountRenters]].size);
            fscanf(fin, "%d", &tenant[CountRenters].equipment[CountEquipment[CountRenters]].amount);
            fscanf(fin, "%s", tenant[CountRenters].equipment[CountEquipment[CountRenters]].measurement_unit);
            if(tenant[CountRenters].equipment[CountEquipment[CountRenters]].name_item[0] != '\000') {
                CountEquipment[CountRenters]++;
            }
            else{
                break;
            }
        }
        CountRenters++;
    }

    int check = 0;
    //Check for correct input
    for(int i = 0; i < CountRenters; i++) {
        //Check Name
        for(int j = 0; j < strlen(tenant[i].fullname); j++) {
            if(('A' > tenant[i].fullname[j] || tenant[i].fullname[j] > 'Z') && ('a' > tenant[i].fullname[j] || tenant[i].fullname[j] > 'z') && tenant[i].fullname[j] != ' '){
                fprintf(fout,"Invalid input!");
                check = 1;
                break;
            }
        }
        if(check == 1){
            break;
        }

        //Check date format
        for(int q = 0; q < strlen(tenant[i].date) - 2; q++){
            if((tenant[i].date[q] < '/' || tenant[i].date[q] > '9')&& tenant[i].date[q] != '\000'){
                fprintf(fout,"Invalid input!");
                check = 1;
                break;
            }
        }
        if(check == 1){
            break;
        }

        int day = (int)(tenant[i].date[0] - 48) * 10 + (int)(tenant[i].date[1] - 48);
        int month = (int)(tenant[i].date[3] - 48) * 10 + (int)(tenant[i].date[4] - 48);
        int year = (int)(tenant[i].date[6] - 48) * 1000 + (int)(tenant[i].date[7] - 48) * 100 + (int)(tenant[i].date[8] - 48) * 10 + (int)(tenant[i].date[9] - 48);
        int checkYear = 0;

        if(year < 2022) {
            if(month == 0 || month > 12){
                fprintf(fout,"Invalid input!");
                check = 1;
                break;
            }

            if (year % 400 == 0 || year % 4 == 0) {
                checkYear = 1;
            }

            if (checkYear == 0) {
                if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 ||
                     month == 12) && day > 31) {
                    fprintf(fout,"Invalid input!");
                    check = 1;
                    break;
                } else if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
                    fprintf(fout,"Invalid input!");
                    check = 1;
                    break;
                } else if (month == 2 && day > 28) {
                    fprintf(fout,"Invalid input!");
                    check = 1;
                    break;
                }
            }
            else {
                if ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day > 31) {
                    fprintf(fout,"Invalid input!");
                    check = 1;
                    break;
                } else if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
                    fprintf(fout,"Invalid input!");
                    check = 1;
                    break;
                } else if (month == 2 && day > 29) {
                    fprintf(fout,"Invalid input!");
                    check = 1;
                    break;
                }
            }
        }
        else{
            fprintf(fout,"Invalid input!");
            check = 1;
            break;
        }


        // Check time format
        int hours = (int)(tenant[i].time[0] - 48) * 10 + (int)(tenant[i].time[1] - 48);
        int minutes = (int)(tenant[i].time[3] - 48) * 10 + (int)(tenant[i].time[4] - 48);
        int seconds = (int)(tenant[i].time[6] - 48) * 10 + (int)(tenant[i].time[7] - 48);

        if(hours > 23 || minutes > 59 || seconds > 59){
            fprintf(fout,"Invalid input!");
            check = 1;
            break;
        }


        for(int j = 0; j < CountEquipment[i]; j++) {
            // Check Item_name
            if(strlen(tenant[i].equipment[j].name_item)< 4 || strlen(tenant[i].equipment[j].name_item) > 15){
                fprintf(fout,"Invalid input!");
                check = 1;
                break;
            }
            for(int q = 0; q < strlen(tenant[i].equipment[j].name_item); q++) {
                if((64 > tenant[i].equipment[j].name_item[q] || tenant[i].equipment[j].name_item[q] > 90) && (97 > tenant[i].equipment[j].name_item[q] || tenant[i].equipment[j].name_item[q] > 122) && tenant[i].equipment[j].name_item[q] != 32){
                    fprintf(fout,"Invalid input!");
                    check = 1;
                    break;
                }
            }
            if(check == 1){
                break;
            }

            // Check size
            if(tenant[i].equipment[j].size <= 0 || tenant[i].equipment[j].size > 200){
                fprintf(fout,"Invalid input!");
                check = 1;
                break;
            }

            // Check amount
            if(tenant[i].equipment[j].amount <= 0 || tenant[i].equipment[j].amount > 30){
                fprintf(fout,"Invalid input!");
                check = 1;
                break;
            }

            // Check measurement_unit
            if(strcmp(tenant[i].equipment[j].measurement_unit, "pcs") !=0 && strcmp(tenant[i].equipment[j].measurement_unit, "pair") !=0  ){
                fprintf(fout,"Invalid input!");
                check = 1;
                break;
            }
            if(tenant[i].equipment[j].amount > 1 && strcmp(tenant[i].equipment[j].measurement_unit, "pair") == 0 ){
                strcpy(tenant[i].equipment[j].measurement_unit, "pairs");
            }



        }
    }


    if(check != 1) {
        for (int i = 0; i < CountRenters; i++) {
            fprintf(fout, "%s has rented ", tenant[i].fullname);

            for (int j = 0; j < CountEquipment[i]; j++) {
                fprintf(fout, "%d ", tenant[i].equipment[j].amount);
                fprintf(fout, "%s of ", tenant[i].equipment[j].measurement_unit);
                fprintf(fout, "%sof size ", tenant[i].equipment[j].name_item);
                if(j+2 == CountEquipment[i]){
                    fprintf(fout, "%g and ", tenant[i].equipment[j].size);
                }
                else if(j+1 == CountEquipment[i]){
                    fprintf(fout, "%g on ", tenant[i].equipment[j].size);
                }
                else {
                    fprintf(fout, "%g, ", tenant[i].equipment[j].size);
                }
            }
            fprintf(fout, "%s at ", tenant[i].date);
            fprintf(fout, "%s.", tenant[i].time);
            if(i+1!=CountRenters){
                fprintf(fout,"\n");
            }
        }
    }

    fclose(fin);
    fclose(fout);

    return 0;
}
