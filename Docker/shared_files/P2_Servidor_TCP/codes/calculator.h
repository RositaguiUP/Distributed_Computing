#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>

#define SIZE 21
char *inOP[SIZE] = {0};
int k = 0;

typedef struct stack
{
	int data;
	struct stack *next;
} stack;

typedef struct exp {
    char op;
    char *term;
    struct exp *left;
    struct exp *right;
} Exp;

Exp *make_exp2(char *str){
    if(!str || !*str) return NULL;//*str == '\0' is format error.
    char *mul = strrchr(str, '*');
    char *div = strrchr(str, '/');
    Exp *node = malloc(sizeof(*node));
    if(mul == NULL && div == NULL){
        node->op = '\0';
        node->term = str;
        node->left = node->right = NULL;
        return node;
    }
    char *op;
    op = mul < div ? div : mul;
    node->op = *op;
    *op = '\0';
    node->left  = make_exp2(str );
    node->right = make_exp2(op+1);
    return node;
}

Exp *make_exp(char *str){
    if(!str || !*str) return NULL;//*str == '\0' is format error.
    char *minus = strrchr(str, '-');
    char *plus  = strrchr(str, '+');
    if(minus == NULL && plus == NULL)
        return make_exp2(str);
    char *op;
    Exp *node = malloc(sizeof(*node));
    op = minus < plus ? plus : minus;
    node->op = *op;
    *op = '\0';
    node->left  = make_exp(str );
    node->right = make_exp(op+1);
    return node;
}


void print(Exp *exp, int level){
    int i;
    if(exp->op){
        inOP[k] = &exp->op;
        k = k+1;
        print(exp->right, level+1);
        print(exp->left, level+1);
    } else {
        inOP[k] = exp->term;
        k = k+1;
    }
}


int calculate2(int a, char *bC, char op){
  int b = (int)strtol(bC, (char **)NULL, 10);
  switch(op){
    case '+':
      return a+b;
    case '-':
      return a-b;
    case '*':
      return a*b;
    case '/':
      return a/b;
  }
  return 0;
}

void push( stack **head, int value )
{
	stack* node = malloc( sizeof(stack) );

	if( node == NULL ) {
		fputs( "Error: Out of memory\n", stderr );
		exit( 1 );
	} else {
		node->data = value;
		node->next = *head;
		*head = node;
	}
}

int pop( stack **head )
{
	if( *head == NULL ) {
		fputs( "Error: bottom of stack!\n", stderr );
		exit( 1 );
	} else {
		stack* top = *head;
		int value = top->data;
		*head = top->next;
		free( top );
		return value;
	}
}

int eval( char op, stack** head )
{
	int temp;
	switch( op ) {
		case '+': return pop(head) + pop(head);
		case '*': return pop(head) * pop(head);
		case '-': temp = pop(head); return pop(head) - temp;
		case '/': temp = pop(head); return pop(head) / temp;
	}
}

int need( char op )
{
	switch( op ) {
		case '+':
		case '*':
		case '-':
		case '/':
			return 2;
		default:
			fputs( "Invalid operand!", stderr );
			exit( 1 );
	}
}

int checknr( char* number )
{
	for( ; *number; number++ )
		if( *number < '0' || *number > '9' )
			return 0;

	return 1;
}

//Se creo la funcion itoa() porque esta funcion no viene incorporada dentro de las funciones que
//maneja linux, nos sirve para convertir un entero a una cadena.
char *itoa(int n)
{
  static char   buf[32];
  sprintf(buf,"%d ",n);
  return  buf;
}

void calculate(char *e){
  int i, temp, stacksize = 0;
	stack* head = NULL;

  //char str[] = "3+1-4*6-7";
  char str[strlen(e) + 1]; 
  strcpy(str, e);
  
  int length = strlen(str);
  Exp *exp = make_exp(str);
  print(exp, 0);

	for( i = k-1; i >= 0; i--) {
		char* token = inOP[i];
		char* endptr;
		char op;

		if( checknr( token ) ) {
			/* We have a valid number. */
			temp = atoi( token );
			push( &head, temp );
			++stacksize;
		} else {
			/* We have an operand (hopefully) */
			if( strlen( token ) != 1 ) {
				fprintf( stderr, "Error: Token '%s' too large.\n", token );
				exit( 1 );
			}

			op = token[0];

			if( stacksize < need( op ) ) {
				fputs( "Too few arguments on stack.\n", stderr );
				exit( 1 );
			}

			push( &head, eval( op, &head ) );
			stacksize -= need( op ) - 1;
		}
	}

	if( stacksize != 1 ) {
		fputs( "Too many arguments on stack.\n", stderr );
		exit( 1 );
	}

	printf( "Result: %i\n", head->data );
    strcpy(e,itoa(head->data));
	//return head->data;
}