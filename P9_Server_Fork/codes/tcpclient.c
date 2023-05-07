/*
	Lectura remota de un directorio usando sockets pertenecientes
   a la familia TCP, en modo conexion.
   Codigo del cliente.

   Nombre Archivo: tcpclient.c
   Archivos relacionados: tcpserver.c calculator.h
   Fecha: Abril 2023

   Compilacion: cc tcpclient.c -lnsl -o tcpclient

   Ejecucion: ./tcpclient <host> <directorio>   
*/



#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>  // it is required to close the socket descriptor
#include <netdb.h>

#define  DIRSIZE    2048   /* longitud maxima parametro entrada/salida */
#define  PUERTO     5005   /* numero puerto arbitrario */

int main(argc, argv)
   int    argc;  
   char  *argv[];
{
	char                dir[DIRSIZE];  /* parametro entrada y salida */
	int                 sd;		   /* descriptores de sockets    */
	struct hostent 	   *hp;		   /* estructura del host        */
	struct sockaddr_in sin, pin; 	   /* direcciones socket        */
    int                *status;        /* regreso llamada sistema */
    char               *host;          /* nombre del host */

/* verificando el paso de parametros */

        if ( argc != 3) {
           fprintf(stderr,"Error uso: %s <host> \n",argv[0]);
           exit(1);
        } 
        host = argv[1];

/* encontrando todo lo referente acerca de la maquina host */

	if ( (hp = gethostbyname(host)) == 0) {
		perror("gethosbyname");
		exit(1);
	}
		
/* llenar la estructura de direcciones con la informacion del host */
	pin.sin_family = AF_INET;
	pin.sin_addr.s_addr = ((struct in_addr *) (hp->h_addr))->s_addr;
	pin.sin_port = htons(PUERTO);                    

/* obtencion de un socket tipo internet */
	if ( (sd = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
		perror("socket");
		exit(1);
	}

/* conectandose al PUERTO en el HOST  */
	if ( connect(sd, (struct sockaddr *)&pin, sizeof(pin)) == -1) {
		perror("connect");
		exit(1);
	}

	/* enviar mensaje al PUERTO del  servidor en la maquina HOST */
	strcpy(dir, argv[2]);
	if ( send(sd, dir, sizeof(dir), 0) == -1 ) {
		perror("send");
		exit(1);
	}

	/* esperar por la respuesta */
	int received = recv(sd, dir, sizeof(dir), 0);
	if (received == -1) {
		perror("recv");
		exit(1);
	}
	
	/* imprimimos el resultado y cerramos la conexion del socket */
	printf("\nEl resultado de la operacion mandada es: %s", dir);
	close(sd);
	return 0;
}
