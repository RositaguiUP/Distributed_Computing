/*

   Codigo de lectura de una palabra y devuelve un JSON
   
   Nombre archivo: toJSON.c

*/

#include <stdlib.h> 
#include <stdio.h>
#include <string.h>
#define MAX_LIMIT 2048

void stringToJson(char* word, char* fullAns){
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
  strncat(fullAns, json, size);
}