/*

   Conexion DHCP
   Codigo del servidor

   Returns valid IP address or error msg

   Nombre Archivo: dhcpserver.c
   Archivos relacionados: calculator.h dhcpclient.c
   Fecha: Marzo 2023

   Compilacion: cc dhcpserver.c -lnsl -o dhcpserver
   Ejecución: ./dhcpserver

*/

// server program for udp connection
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <stdlib.h>

#define PORT 15000
#define MAXLINE 1000

// #define MINIP "172.18.2.2"
// #define MAXIP "172.18.2.3"

#define IP "172.18.2."
#define LASTNUM 2
#define RANGE 1

// Driver code
int main()
{
    char ips[16][RANGE];
    char macs[18][RANGE];
    int ipsLeased[RANGE] = {0};

    char buffer[MAXLINE];
    char *message = "Hello Client";
    int listenfd, len;
    struct sockaddr_in servaddr, cliaddr;
    int empezar;
    printf("Listening in port number: %d", PORT);
    // printf("\nPresione cualquier tecla para empezar \n");
    // scanf("%d",&empezar);

    // bzero(&servaddr, sizeof(servaddr));

    // Create a UDP Socket
    listenfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (listenfd == -1)
    {
        perror("socket creation failed");
        exit(EXIT_FAILURE);
    }

    servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    servaddr.sin_port = htons(PORT);
    servaddr.sin_family = AF_INET;

    // bind server address to socket descriptor
    bind(listenfd, (struct sockaddr *)&servaddr, sizeof(servaddr));

    while (1)
    {

        // receive the datagram
        len = sizeof(cliaddr);
        int n = recvfrom(listenfd, buffer, MAXLINE,
                         0, (struct sockaddr *)&cliaddr, &len); // receive message from server
        if (n < 0)
        {
            perror("recvfrom failed");
            exit(EXIT_FAILURE);
        }
        else
        {
            buffer[n] = '\0';
            printf("\nHe recibido del cliente: ");
            printf("%s\n", buffer);
        }
        // send the response
        sendto(listenfd, message, strlen(message), 0,
               (struct sockaddr *)&cliaddr, sizeof(cliaddr));

        // receive the datagram
        len = sizeof(cliaddr);
        n = recvfrom(listenfd, buffer, MAXLINE,
                     0, (struct sockaddr *)&cliaddr, &len); // receive message from server
        if (n < 0)
        {
            perror("recvfrom failed");
            exit(EXIT_FAILURE);
        }
        else
        {
            buffer[n] = '\0';
            printf("\nHe recibido del cliente: ");
            printf("%s\n", buffer);
        }
        // send the response
        sendto(listenfd, buffer, MAXLINE, 0,
               (struct sockaddr *)&cliaddr, sizeof(cliaddr));
    }

    close(listenfd);
    printf("Conexion cerrada\n");
    return 0;
}