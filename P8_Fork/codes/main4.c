#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>

int main() {
    pid_t child_pid;
    int n = 0;
    printf("Enter a number:\n");
    scanf("%d", &n);
    for (int i = 0; i < n; i++) {
        child_pid = fork();
        if (child_pid == 0) {
            if (getpid() % 2 ==0) {
                printf("The child's process ID %d is even (par)\n", (int) getpid());
            } else {
                printf("The child's process ID %d is odd (impar)\n", (int) getpid());
            }
            exit(0);
        }
    }
    return 0;
}
