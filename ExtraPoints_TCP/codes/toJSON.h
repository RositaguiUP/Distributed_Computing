/*

   Codigo de lectura de una palabra y devuelve un JSON
   
   Nombre archivo: toJSON.c

*/

#include <stdlib.h> 
#include <stdio.h>
#include <string.h>
#define MAX_LIMIT 20

struct json {
    char text[MAX_LIMIT*2 + 4];
};

void stringToJson(char* word, struct json* prueba){
  char json[MAX_LIMIT*2 + 5];
  char add = ':';
  int size = strlen (word);

  //Concatenates opening key
  json[0] = '{';
  json[1] = '\0';

  //Concatenates word
  strncat(json, word, size - 1);
  
  //Concatenates two points
  strncat(json, &add, 1);
  
  //concatenates space
  add = ' ';
  strncat(json, &add, 1);

  //concatenates value
  strncat(json, word, size);

  //Concatenates closing key
  size = strlen (json);
  json[size - 1] = '}';

  //Sets json value
  strcpy(prueba -> text, json);
}

//Esta funcion recibe como parametros una palabra y devuelve cuantas vocales contiene.
void toJSON(dir)
  char *dir; //palabra nueva
{
  char line[MAX_LIMIT];
  char final[1024 * 10];
  int value = 0;
  int numPalabras = 0;
  struct json prueba;

  //Prueba gora
  char palabras[1024][MAX_LIMIT];
    for(int x = 0; x < numPalabras; x++){
        printf("En %d el arreglo tiene el valor %s\n", x, palabras[x]);
    }
  //EndPruebaGora

  while(value != 10){
    fgets(line, MAX_LIMIT, stdin);
    //scanf("%s", line);
    value = strcmp(line,"exit"); 
    
    if(value != 0){
      //Agrega valor
      strcpy(palabras[numPalabras], line);
      numPalabras++;
      
      //
      for(int i = 0; i < numPalabras; i++){
        stringToJson(palabras[i], &prueba);
        puts(prueba.text);
        if(numPalabras != 1){
          printf("%s", ", \0");
        }
      }
      
    }
  }
  printf(json);
  strcpy(dir,json);
  return 0;
}

