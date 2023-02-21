/*

   Codigo de lectura de una palabra y devuelve cuantas vocales contiene
   
   Nombre archivo: num_vocales.c

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


//Esta funcion recibe como parametros una palabra y devuelve cuantas vocales contiene.
void num_vocales(dir)
  char *dir;
{
  char letra; //variable temporal que almacena cada letra
  int longitud_palabra; //tamanio de las cadena
  int i=0; //contador
  int num_voc; //numero de vocales
  printf("Me mandaste la palabra: %s\n",dir);
  longitud_palabra=strlen(dir);
  num_voc=0;
  while (i<longitud_palabra)
  {
    letra=dir[i];
    switch (letra)
    { //si en caso es cualquier vocal aumenta el contador
      case 'a':num_voc++;break;
      case 'e':num_voc++;break;
      case 'i':num_voc++;break;
      case 'o':num_voc++;break;
      case 'u':num_voc++;break;
      case 'A':num_voc++;break;
      case 'E':num_voc++;break;
      case 'I':num_voc++;break;
      case 'O':num_voc++;break;
      case 'U':num_voc++;break;
    }
    i++;
  }
  printf("Tiene %d vocales \n",num_voc);
  strcpy(dir,itoa(num_voc));  
}

