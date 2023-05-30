#include<stdio.h>
#include<stdlib.h>
#include <string.h>


struct Directory;
struct File;

struct File{
    int id;
    char name[64];
    unsigned long int size;
    char data[1024];
    struct Directory *directory;// The parent directory
};

struct Directory{
    int nf;
    int nd;
    char name[64];
    struct File *files[256];
    struct Directory *directories[8];
    char path[2048];
};

typedef struct Directory Directory;
typedef struct File File;


// Operations on files
void add_to_file(File *file, const char * content);
void append_to_file(File *file, const char * content);
void pwd_file(File * file);


// Operations on directories
void add_file(File* file, Directory *dir);
void add_dir(Directory *dir1, Directory *dir2); // given to you

// Helper functions
void show_dir(Directory *dir);
void show_file(File *file);
void show_file_detailed(File *file);

Directory create_directory(const char * name);
File create_file(const char * name);

int main() {
    char content1[] = "int printf(const char * format, ...);";
    char content2[] = "int main(){printf('Hello World');}";
    char content3[] = "//This is a comment in C language";
    char content4[] = "Bourne Again Shell!";

    Directory home, bin, root;

    root = create_directory("root");
    strcpy(root.path, "/");
    bin = create_directory("bin");
    home = create_directory("home");

    // Example: the path of the folder home is /home

    // Add subdirectories
    add_dir(&home, &root);
    add_dir(&bin, &root);

    File bash, ex31, ex32;

    bash = create_file("bash");
    ex31 = create_file("ex31.c");
    ex32 = create_file("ex32.sh");

    add_file(&bash, &bin);
    add_file(&ex31, &home);
    add_file(&ex32, &home);

    add_to_file(&bash, content4);
    add_to_file(&ex31, content1);
    add_to_file(&ex32, content3);


    append_to_file(&ex31, content2);

    show_dir(&root);
    show_file_detailed(&bash);
    show_file_detailed(&ex31);
    show_file_detailed(&ex32);

    pwd_file(&bash);
    pwd_file(&ex31);
    pwd_file(&ex32);

    return EXIT_SUCCESS;
}

Directory create_directory(const char * name){
    Directory d;

    strcpy(d.name, name);
    d.nf = 0;
    d.nd = 0;
    strcpy(d.path, "");

    return d;
}

File create_file(const char * name){
    File f;

    strcpy(f.data, "");
    strcpy(f.name, name);
    f.id = 0;
    f.size = 0;
    f.directory = NULL;

    return f;
}

// Helper functions

// Displays the content of the Directory dir
void show_dir(Directory *dir){
    printf("\nDIRECTORY\n");
    printf(" name: %s\n", dir->name);
    printf(" path: %s\n", dir->path);
    printf(" files:\n");
    printf("    [ ");
    for (int i = 0; i < dir->nf; i++){
        show_file(dir->files[i]);
    }
    printf("]\n");
    printf(" directories:\n");
    printf("    { ");

    for (int i = 0; i < dir->nd; i++){
        show_dir(dir->directories[i]);
    }
    printf("}\n");
}

// Prints the name of the File file
void show_file(File *file){
    printf("%s ", file->name);
}

// Shows details of the File file
void show_file_detailed(File *file){
    printf("\nFILE\n");
    printf(" id: %d\n", file->id);
    printf(" name: %s\n", file->name);
    printf(" size: %lu\n", file->size);
    printf(" data:\n");
    printf("    [ %s ]\n", file->data);
}

// Implementation: Operations on files

// Adds the content to the File file
void add_to_file(File *file, const char * content) {
    file -> size = strlen(content) + 1;
	strcpy(file->data, content);
}

// Appends the content to the File file
void append_to_file(File *file, const char * content) {
    file -> size += strlen(content);
	strcat(file->data, content);
}

// Prints the path of the File file
void pwd_file(File * file) {
    printf("Path of file %s:\n%s%s\n",file -> name, file -> directory -> path,file -> name);
}


// Implementation: Operations on directories

// Adds the File file to the Directory dir
void add_file(File* file, Directory *dir) {
    if (dir -> nf > 256) {
        return;
    }
    file -> directory = dir;
    dir -> files[dir -> nf] = file;
    dir -> nf++;
}

// Given to you
// Adds the subdirectory dir1 to the directory dir2
void add_dir(Directory *dir1, Directory *dir2){
	if (dir1 && dir2){
        dir2->directories[dir2->nd] = dir1;
        dir2->nd++;
        strcpy(dir1 -> path , dir2->path);
        strcat(dir1 -> path, dir1 -> name);
        strcat(dir1 -> path,"/");
	}
}



