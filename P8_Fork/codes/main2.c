#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>

int main() {
    pid_t child_pid;
    printf("The main program process ID is  %d\n", (int) getpid());
    child_pid = fork();
    printf("The value of child_pid variable is %d\n", (int) child_pid);
    printf("The process ID is %d\n", getpid());
    return 0;
}
