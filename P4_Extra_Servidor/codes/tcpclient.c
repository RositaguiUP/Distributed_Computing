/*
  Cliente del programa numero 1, de la materia Cómputo Distribuido
  

   Lectura remota de un directorio usando sockets pertenecientes
   a la familia TCP, en modo conexion.
   Codigo del cliente.

   Nombre Archivo: tcpclient.c
   Archivos relacionados: tcpserver.c calculator.h
   Fecha: Febrero 2023

   Compilacion: cc tcpclient.c -lnsl -o tcpclient

   Ejecucion: ./tcpclient <host> <directorio>   
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <winsock2.h>
#include <unistd.h>  // it is required to close the socket descriptor

#pragma comment(lib,"-lws2_32")

#define  DIRSIZE    2048   /* longitud maxima parametro entrada/salida */
#define  PUERTO     5000   /* numero puerto arbitrario */
#define  MSGSIZE    2048   /* longitud de los mensajes */

int main(argc, argv)
   int    argc;  
   char  *argv[];
{
	WSADATA wsa;
	SOCKET sd;

	char                dir[DIRSIZE];  /* parametro entrada y salida */
	//int                 sd;		   /* descriptores de sockets    */
	struct hostent 	   *hp;		   /* estructura del host        */
	struct sockaddr_in sin, pin; 	   /* direcciones socket        */
    int                *status;        /* regreso llamada sistema */
    char               *host;          /* nombre del host */

/* verificando el paso de parametros */

        /*if ( argc != 3) {
           fprintf(stderr,"Error uso: %s <host> <archivo> \n",argv[0]);
           exit(1);
        }*/
        host = "5000"; //argv[1];

/*  iniciando windosrck library */
	printf("\nInitialising Winsock...");
	if (WSAStartup(MAKEWORD(2,2),&wsa) != 0)
	{
		printf("Failed. Error Code : %d",WSAGetLastError());
		return 1;
	}
	
	printf("Initialised.");

/* crear un socket y devulve el socquet descriptor */
/*
 Address Family : AF_INET (this is IP version 4)
 Type : SOCK_STREAM (this means connection oriented TCP protocol)
 Protocol : 0 [ or IPPROTO_TCP , IPPROTO_UDP ]
*/

	/*if((s = socket(AF_INET , SOCK_STREAM , 0 )) == INVALID_SOCKET)
	{
		printf("Could not create socket : %d" , WSAGetLastError());
	}

	
*/

/* obtencion de un socket tipo internet */
	if ( (sd = socket(AF_INET, SOCK_STREAM, 0)) == INVALID_SOCKET) {
		perror("socket");
		exit(1);
	}
	printf("\nSocket created.\n");
		
/* llenar la estructura de direcciones con la informacion del host */
	pin.sin_family = AF_INET;
	pin.sin_addr.s_addr = inet_addr("192.168.0.100"); //192.168.0.100");//((struct in_addr *) (hp->h_addr))->s_addr; 172.25.0.1
	pin.sin_port = htons( 5000 );        //  172.18.2.255          

/* conectandose al PUERTO en el HOST  */
	if ( connect(sd, (struct sockaddr *)&pin, sizeof(pin)) < 0) {
		perror("connect");
		exit(1);
	}
	printf("Connected");
	
	int t = 1;
	while (t) {
		memset(&dir[0], 0, sizeof(dir));
		/* enviar mensaje al PUERTO del  servidor en la maquina HOST */
		printf("\nEscribe la palabra: ");
		scanf("%s",dir);

		if ( send(sd, dir, sizeof(dir), 0) == -1 ) {
			perror("send");
			exit(1);
		}

		/* esperar por la respuesta */
		int received = recv(sd, dir, sizeof(dir), 0);
		if (received == -1) {
			perror("recv");
			exit(1);
		} else if (received == 0) {
			printf("\nEl servidor ha cerrado la conexión\n");
			break;
		}
		
		/* imprimimos el resultado y cerramos la conexion del socket */
		printf("\nEl resultado de la operacion mandada es: %s", dir);
	}

	closesocket(sd);
	WSACleanup();
	
	return 0;
}
