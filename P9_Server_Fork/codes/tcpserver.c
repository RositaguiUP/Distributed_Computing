/*

   Contesta al cliente con el resultado de la expresión algebraica recibida, pero en formato JSON.
   a la familia TCP, en modo conexion.
   Codigo del servidor

   Nombre Archivo: tcpserver.c   
   Fecha: Abril 2023

   Compilacion: cc tcpserver.c -lnsl -o tcpserver
   Ejecución: ./tcpserver
*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
/* The following headers was required in old or some compilers*/
//#include <sys/types.h>
//#include <sys/socket.h>
//#include <netinet/in.h>
#include <netdb.h>
#include <signal.h>	// it is required to call signal handler functions
#include <unistd.h>  // it is required to close the socket descriptor
#include "calculator.h"

#define  jsonSIZE  10000
#define  msgSIZE   2048      /* longitud maxima parametro entrada/salida */
#define  PUERTO    5005     /* numero puerto arbitrario */

int                  sd, sd_actual;  /* descriptores de sockets */
int                  addrlen;        /* longitud msgecciones */
struct sockaddr_in   sind, pin;      /* msgecciones sockets cliente u servidor */


/*  procedimiento de aborte del servidor, si llega una senal SIGINT */
/* ( <ctrl> <c> ) se cierra el socket y se aborta el programa       */
void aborta_handler(int sig){
   printf("....abortando el proceso servidor %d\n",sig);
   close(sd);  
   close(sd_actual); 
   exit(1);
}


int main(){
  
	char  msg[msgSIZE];	     /* parametro entrada y salida */
	char  json[jsonSIZE];	     /* parametro entrada y salida */

	/*
	When the user presses <Ctrl + C>, the aborta_handler function will be called, 
	and such a message will be printed. 
	Note that the signal function returns SIG_ERR if it is unable to set the 
	signal handler, executing line 54.
	*/	
   if(signal(SIGINT, aborta_handler) == SIG_ERR){
   	perror("Could not set signal handler");
      return 1;
   }
       //signal(SIGINT, aborta);      /* activando la senal SIGINT */

/* obtencion de un socket tipo internet */
	if ((sd = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
		perror("socket");
		exit(1);
	}

/* asignar msgecciones en la estructura de msgecciones */
	sind.sin_family = AF_INET;
	sind.sin_addr.s_addr = INADDR_ANY;   /* INADDR_ANY=0x000000 = yo mismo */
	sind.sin_port = htons(PUERTO);       /*  convirtiendo a formato red */

/* asociando el socket al numero de puerto */
	if (bind(sd, (struct sockaddr *)&sind, sizeof(sind)) == -1) {
		perror("bind");
		exit(1);
	}

/* ponerse a escuchar a traves del socket */
	if (listen(sd, 5) == -1) {
		perror("listen");
		exit(1);
	}

	int max=3;
	pid_t child_pid;
	for (int i=0; i<max; i++) { // Maximun max client connections
		/* esperando que un cliente solicite un servicio */
		if ((sd_actual = accept(sd, (struct sockaddr *)&pin, &addrlen)) == -1) {
			perror("accept");
			exit(1);
		}

		child_pid = fork();
		if (child_pid == 0) {
			// soy el hijo
			break;
        } else {
            // soy el padre
			close(sd_actual);
        }
	}

	if (child_pid==0){
		/* tomar un mensaje del cliente */
		int n = recv(sd_actual, msg, sizeof(msg), 0);
		if (n == -1) {
			perror("recv");
			exit(1);
		}

		strcpy(json," ");		
			
		// msg[n] = '\0';		
		//printf("Client sent: %d caracteres", n);
		printf("Client sent: %s", msg);
		if((strcmp(msg,"finish")==0)){ //it means that the conversation must be closed
			strcpy(json,"finish");
			close(sd_actual);
			close(sd);
			exit(0);
		}else{
			calculate(msg);
			//convert msg received to json format
			strcpy(json,"{'");
			strcat(json,msg);
			strcat(json,"' : '");
			strcat(json,msg);
			strcat(json,"'}\n");
			//----------------------------------
		}

		/* enviando la respuesta del servicio */
		int sent;
		if ( sent = send(sd_actual, json, strlen(json), 0) == -1) {
			perror("send");
			exit(1);
		}
		
		/* cerrar los dos sockets */
		close(sd_actual);
		close(sd);
	}
	else {
		close(sd);
	}
	return 0;
}
