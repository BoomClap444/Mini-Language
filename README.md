# Mini-Language

A small interpreted programming language built from scratch in Java.

Mini-Language was created as a personal project to learn how programming languages work from the ground up. The project includes a lexer, parser, AST, interpreter, runtime environments, functions, recursion, arrays, control flow, and basic error handling.

## Features

* Lexical analysis
* Parsing and AST generation
* Variables with `let`
* Arithmetic expressions
* Comparisons
* Boolean expressions
* `if / else`
* `while` loops
* `for` loops
* Functions
* Parameters and function calls
* `return`
* Recursion
* Arrays
* Array indexing and assignment
* Strings
* Runtime and syntax errors
* `print`
* Execution from `.mini` source files

## Example

```text
func factorial(n) {
    if n == 0 {
        return 1;
    }

    return n * factorial(n - 1);
}

func sumArray(numbers) {
    let total = 0;
    let i = 0;

    while i < 5 {
        total = total + numbers[i];
        i = i + 1;
    }

    return total;
}

let numbers = [10, 20, 30, 40, 50];

numbers[0] = 100;

let total = sumArray(numbers);
let result = factorial(5);

print(numbers);
print(total);
print(result);
```

Example output:

```text
[100, 20, 30, 40, 50]
600
120
```

## How It Works

Mini-Language follows the basic structure of an interpreter:

```text
Source Code
    ↓
Lexer
    ↓
Tokens
    ↓
Parser
    ↓
Abstract Syntax Tree
    ↓
Interpreter
    ↓
Runtime Environment
```

### Lexer

The lexer reads the source code and converts it into tokens such as:

```text
LET
IDENTIFIER
NUMBER
PLUS
EQUALS
LEFT_PAREN
RIGHT_PAREN
...
```

It also recognizes keywords, strings, comparison operators, logical operators, and punctuation.

### Parser

The parser consumes the tokens and builds an Abstract Syntax Tree (AST).

Expression parsing uses different precedence levels so expressions such as:

```text
5 + 10 * 2
```

are interpreted as:

```text
5 + (10 * 2)
```

rather than:

```text
(5 + 10) * 2
```

### Interpreter

The interpreter walks the AST and evaluates each statement and expression.

Runtime values are represented using Java objects, including:

* `Integer`
* `Boolean`
* `String`
* `List<Object>`

### Environments

Variables are stored in an `Environment`.

Functions create child environments so parameters and local variables have their own scope while still being able to access values from parent scopes.

This also allows recursive functions to have separate environments for each call.

## Source Files

Programs are written in `.mini` files.

Example:

```text
hello.mini
```

```text
let x = 5 + 10;
print(x);
```

The interpreter reads the file and executes it.

## Project Structure

```text
src/
├── ast/
├── lexer/
├── parser/
├── interpreter/
└── Main.java
```

The exact classes are organized by their role in the language implementation.

## Running a Program

From the project directory:

```
java src/Main.java RunMe.mini
```

RunMe.mini contains example code, you can replace it with what you want.

## Error Handling

Mini-Language includes basic error handling for invalid programs and runtime problems, including cases such as:

```text
ERROR: UNDEFINED VARIABLE
ERROR: UNDEFINED FUNCTION
ERROR: WRONG NUMBER OF ARGUMENTS
ERROR: ARRAY INDEX OUT OF BOUNDS
ERROR: ARRAY INDEX MUST BE AN INTEGER
ERROR: DIVISION BY ZERO
ERROR: RETURN OUTSIDE FUNCTION
```

Syntax errors are also reported when the parser encounters an unexpected token or a required token is missing.

## Project Goal

The goal of this project was not to build a production-ready programming language. The goal was to understand how a programming language works by implementing one from scratch.

This project covers the core pipeline of a simple language:

**lexing → parsing → AST construction → interpretation → runtime execution**

## Status

Version 1.0 — Complete

The original feature goals for the project have been implemented.

## Future Ideas

Possible future additions include:

* More string operations
* Additional built-in functions
* More data structures
* Better error messages and source locations
* A standard library
* A REPL
* A bytecode compiler or virtual machine
* Editor syntax highlighting
