#include "stringToJson.h"

int main() {
    char dir[5] = "hola";
    char palabras[3][5]; //= "hola";
    
    strcpy(palabras[0], dir);

    stringToJson(palabras[0], dir);
    printf("%s", dir);
}