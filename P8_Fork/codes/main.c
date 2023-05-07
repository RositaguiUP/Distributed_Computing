#include <stdio.h>
#include <unistd.h>

int main() {
    pid_t child_pid;
    int x = 5;
    child_pid = fork();
    printf("The value of child_pid variable is %d\n", (int) child_pid);
    printf(" El valor de x es %d\n", x);
    return 0;
}
