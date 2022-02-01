#include <iostream>

using namespace std;

struct student{
    string name;
    int id;
     int marks[3];
};

int main()
{
    student* test = new student();
    cout << "Enter name: ";
    cin >> test->name;
    cout << "Enter id: ";
    cin >> test->id;
    cout << "Enter marks: ";
    cin >> test->marks[0];
    cin >> test->marks[1];
    cin >> test->marks[2];
    cout << "Dear " << test->name << ", your id is "<< test->id << " and your marks are "<< test->marks;

    return 0;
}

