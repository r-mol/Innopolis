#include <iostream>
#include <stack>

using namespace std;

bool letterOrDigit(char c) {
    if (isdigit(c)) {
        return true;
    } else {
        return false;
    }
}

int getPrecedence(char ch) {
    if (ch == '+' || ch == '-') {
        return 1;
    } else if (ch == '*' || ch == '/') {
        return 2;
    } else {
        return -1;
    }
}

bool hasLeftAssociativity(char ch) {
    if (ch == '+' || ch == '-' || ch == '/' || ch == '*') {
        return true;
    } else {
        return false;
    }
}

string infixToRpn(const string &str) {
    stack<char> stack;
    string output;

    for (int i = 0; i < str.length(); i++) {
        if (letterOrDigit(str[i])) {
            if (str[i + 1] != ' ') {
                output += str[i];
            } else {
                output += str[i];
                output += ' ';
            }
        } else if (str[i] == '(') {
            stack.push(str[i]);
        } else if (str[i] == ')') {
            while (!stack.empty() && stack.top() != '(') {
                output += stack.top();
                output += ' ';
                stack.pop();
            }
            stack.pop();
        }else {
            while (!stack.empty() && getPrecedence(str[i]) <= getPrecedence(stack.top()) && hasLeftAssociativity(str[i])) {
                output += stack.top();
                output += ' ';
                stack.pop();
            }
            stack.push(str[i]);
        }
    }

    while (!stack.empty()) {
        if (stack.top() == '(')
            return "This expression is invalid";
        output += stack.top();
        output += ' ';
        stack.pop();
    }
    return output;
}

string removeAdditionalASpaces(string str) {
    for (int i = 0; i < str.length(); i++) {
        if (str[i] == ' ' && str[i + 1] == ' ') {
            for (int j = i; j < str.length(); j++) {
                str[j] = str[j + 1];
            }
            --i;
        }
    }
    return str;
}

int main() {
    string str;
    getline(cin, str);
    string result = removeAdditionalASpaces(infixToRpn(str));
    result.erase(result.find_last_of(' '));
    cout << result;
    return 0;
}
