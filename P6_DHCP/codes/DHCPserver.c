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
#define MAXLINE 19

// #define MINIP "172.18.2.3"
// #define MAXIP "172.18.2.4"

#define IP "172.18.2."
#define LASTNUM 3
#define RANGE 2

// Driver code
int main()
{
    char ips[RANGE][16];
    char macs[RANGE][MAXLINE] = {"00-00-00-00-00-00", "00-00-00-00-00-00"};
    int ipsLeased[RANGE] = {0};
    int free = 0;
    char current[RANGE];

    char buffer[MAXLINE];
    char *message = malloc(sizeof(char) * MAXLINE);
    int listenfd, len;
    struct sockaddr_in servaddr, cliaddr;
    bzero(&servaddr, sizeof(servaddr));
    printf("Listening in port number: %d", PORT);

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

	char sigue='S';
    while (sigue=='S') {
        //  receive the datagram
        len = sizeof(cliaddr);
        int n = recvfrom(listenfd, buffer, sizeof(buffer),
                        0, (struct sockaddr *)&cliaddr, &len); // receive message from server
        if (n < 0)
        {
            perror("recvfrom failed");
            exit(EXIT_FAILURE);
        }

        if((strcmp(buffer,"close")==0)){ //it means that the conversation must be closed
			sigue='N';
			strcpy(message,"close");
		}else{
            buffer[n] = '\0';
            printf("\nHe recibido del cliente: ");
            printf("%s\n", buffer);

            printf("Holaa\n");
            // GIVE VALID IP ADDRESS
            for (int i = 0; i < RANGE; i++)
            {
                char *tmp = malloc(strlen(macs[i]) + 1);
                strcpy(tmp, macs[i]);
                printf("%s\n", tmp);
                printf("%s\n", buffer);
                if (!strcmp(tmp, buffer))
                {
                    free = i + 1;
                    printf("free: %d\n", free);
                    break;
                }
                printf("DONE\n");
            }
            strcpy(message, "AIUDA");
            printf("Hi %s\n", message);

            if (free != 0)
            {
                strcpy(macs[free - 1],"00-00-00-00-00-00");
                ipsLeased[free - 1] = 0;
                // MAC's IP removed and free
                strcpy(message, "IP Libre");
            }
            else
            {
                strcpy(message, "No se obtuvo");
                printf("Message: %s", message);
                for (int i = 0; i < RANGE; i++)
                {
                    if (ipsLeased[i] == 0)
                    {
                        // Add to array
                        ipsLeased[i] = 1;
                        strcpy(macs[i], buffer);
                        sprintf(current, "%d", LASTNUM + i);
                        printf("CU %s", current);
                        strcpy(message, IP);
                        strcat(message, current);
                        break;
                    }
                }
            }

            printf("Message: %s", message);
            free = 0;
        }

        // send the response
        sendto(listenfd, message, MAXLINE, 0,
            (struct sockaddr *)&cliaddr, sizeof(cliaddr));
    }

    close(listenfd);
    printf("Conexion cerrada\n");
    return 0;
}