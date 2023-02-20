/*

   Codigo de lectura de un número, el cual devuelve sumándole 1
   
   Nombre archivo: calculator.h

*/

#include <stdlib.h> 
#include <string.h>


//Se creo la funcion itoa() porque esta funcion no viene incorporada dentro de las funciones que
//maneja linux, nos sirve para convertir un entero a una cadena.
char *itoa(int n)
{
  static char   buf[32];
  sprintf(buf,"%d ",n);
  return  buf;
}


//Esta funcion recibe como parametros un número y devuelve el resultado de ese número mas 1
void calculate(dir)
  char *dir;
{
  int num = dir[0] - '0'; // Convierte de char a int
  int res = num + 1;
  
  printf("Me mandaste el número: %s\n",dir);

  printf("Operación realizada = %d\n",res);
  strcpy(dir,itoa(res));  
}

