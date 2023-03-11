/*

   Conexion DHCP
   Codigo del cliente

   Input: MAC Address, end lease

   Nombre Archivo: dhcpcliente.c
   Fecha: Marzo 2023

   Compilacion: cc dhcpcliente.c -lnsl -o dhcpcliente
   Ejecución: ./dhcpcliente <directorio>
*/
// udp client driver program
#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <stdlib.h>

// #define PORT 15000
#define MAXLINE 1000

// Driver code
int main(argc, argv)
int argc;
char *argv[];
{
    char buffer[MAXLINE];
    char ip[16];
    char mac[18];
    char *message = "Hello Server";
    int sockfd;
    int port;
    char endLease[9];
    struct sockaddr_in servaddr;

    // clear servaddr
    bzero(&servaddr, sizeof(servaddr));
    printf("Escribe la ip del server: ");
    scanf("%s", ip);
    printf("Escribe el puerto del server: ");
    scanf("%d", &port);
    printf("Escribe tu MAC address: ");
    scanf("%s", mac);

    servaddr.sin_addr.s_addr = inet_addr(ip);
    servaddr.sin_port = htons(port);
    servaddr.sin_family = AF_INET;

    // create datagram socket
    sockfd = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (sockfd == -1)
    {
        perror("socket creation failed");
        exit(EXIT_FAILURE);
    }

    // it is not required to establish a connection
    // directly sending a message
    int r = sendto(sockfd, mac, strlen(mac), 0, (struct sockaddr *)&servaddr, sizeof(servaddr));
    if (r == -1)
    {
        perror("sendto failed");
        exit(EXIT_FAILURE);
    }

    // waiting for response
    int len = sizeof(servaddr);
    int n = recvfrom(sockfd, buffer, MAXLINE, 0, (struct sockaddr *)&servaddr, &len);
    if (n < 0)
    {
        perror("recvfrom failed");
        exit(EXIT_FAILURE);
    }
    else
    {
        buffer[n] = '\0';
        printf("Tu nueva IP es: %s \n", buffer);
        while (1)
        {
            scanf("%s", endLease);
            if (strcmp(endLease, "endLease") == 0)
            {
                int r = sendto(sockfd, "00-00-00-00-00-00", strlen("00-00-00-00-00-00"), 0, (struct sockaddr *)&servaddr, sizeof(servaddr));
                if (r == -1)
                {
                    perror("sendto failed");
                    exit(EXIT_FAILURE);
                }
                break;
            }
        }
    }

    // close the descriptor
    close(sockfd);
    printf("Conexion cerrada\n");
}