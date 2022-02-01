#include <iostream>
#include <string>
#include <stack>

using namespace std;

int prior(char ch) {
    if (ch == '*' || ch == '/') return 2;
    else if (ch == '+' || ch == '-') return 1;
    else return -1;
}

string manipulate(string str) {
    string result;
    stack<char> st;
    st.push('A');
    for (int i = 0; i < str.length(); i++) {
        switch (str[i]) {
            case '0'...'9':
                result.push_back(str[i]);
                break;
            case '(':
                st.push('(');
            case ')':
                while (st.top() != 'A' && st.top() != '(') {
                    char tempChar = st.top();
                    st.pop();
                    result += tempChar;
                }
                if (st.top() == '(') {
                    char tempChar = st.top();
                    st.pop();
                }
                break;
            default:
                while (st.top() != 'A' && prior(str[i]) <= prior(st.top())) {
                    char tempChar = st.top();
                    st.pop();
                    result += tempChar;
                }
                st.push(str[i]);
        }
    }
    while (st.top() != 'A') {
        char tempChar = st.top();
        st.pop();
        result += tempChar;
    }
    return result;
}

string removeDoubleSpaces(string str) {
    for (int i = 0; i < str.length(); i++) {
        if (str[i] == ' ' && str[i + 1] == ' ') {
            for (int j = i; j < str.length(); j++) {
                str[j] = str[j + 1];
            }
        }
    }
    return str;
}

int main() {
    string str;

    getline(cin, str);
    string result = manipulate(str);
    result = removeDoubleSpaces(result);

    result.erase(str.length() - 4);
    cout << result;
    return 0;
}