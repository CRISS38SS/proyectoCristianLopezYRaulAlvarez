/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.itz.proyecto.controles;

import edu.itz.proyecto.enumerada.Token;
import edu.itz.proyecto.error.ErroresDeSintaxis;
import java.util.*;
import java.util.List;


/**
 *
 * @author criss
 */
public class Sintaxis {
    private final List<Token> tokens;
    private int posicionActual;
    private Token tokenEnTurno;
    private final StringBuilder errores;

    
    
    private static final Set<Token> FIRST_C1 = Set.of(Token.ID);
    private static final Set<Token> FIRST_C2 = Set.of(Token.CONST);
    private static final Set<Token> FIRST_C3 = Set.of(Token.ID);
    private static final Set<Token> FIRST_C4 = Set.of(Token.VAR);
    private static final Set<Token> FIRST_C5 = Set.of(Token.PROCED);
    private static final Set<Token> FIRST_C6 = Set.of(Token.IGUAL, Token.DISTINTO, Token.MENOR,Token.MAYOR, Token.MENOR_IGUAL, Token.MAYOR_IGUAL);
    private static final Set<Token> FIRST_EXPRESION = Set.of(Token.PARENTESIS_ABRE, Token.ID, Token.NUM);
    private static final Set<Token> FIRST_PROPOSICION = Set.of(Token.LLAVE_ABRE, Token.ID, Token.PRINT,Token.INPUT, Token.EXEC, Token.IF,Token.WHILE, Token.FOR);
    
    public Sintaxis(List<Token> tokens) {
        this.tokens = tokens;
        this.posicionActual = 0;
        this.tokenEnTurno = tokens.isEmpty() ? null : tokens.get(0);
        this.errores = new StringBuilder();

    }    
    
    public String getErrores() {
        return errores.toString();
    }
    
    private void avanzar() {
        posicionActual++;
        tokenEnTurno = posicionActual < tokens.size() ? tokens.get(posicionActual) : null;
    }
    
        private boolean coincide(Token tipo) {
        return tokenEnTurno != null && tokenEnTurno == tipo;
    }
        
    private void consumir(Token esperado) throws ErroresDeSintaxis{
        if (!coincide(esperado)) {
            throw new ErroresDeSintaxis("Error en posición " + posicionActual +
                    ": Se esperaba " + esperado.name() +
                    " pero se encontró " +
                    (tokenEnTurno == null ? "fin de archivo" : tokenEnTurno.name()));
        
        }
        avanzar();
    }    
    
    private boolean enConjuntoFIRST(Set<Token> conjunto) {
        return tokenEnTurno != null && conjunto.contains(tokenEnTurno);
    }
    
    
    // <Programa> ::= <Bloque> .
    private void programa() throws ErroresDeSintaxis {
        bloque();
        consumir(Token.PUNTO);
    }

    // <Bloque> ::= <C2> <C4> <C5> <Proposicion>
    private void bloque() throws ErroresDeSintaxis {
        c2();
        c4();
        c5();

        if (!enConjuntoFIRST(FIRST_PROPOSICION)) {
            throw new ErroresDeSintaxis("Se esperaba una proposición (ej: { ... }, print, if, etc.)");
        }
        proposicion();
    }

    // <C1> ::= id <C1zC1>
    private void c1() throws ErroresDeSintaxis {
        consumir(Token.ID);
        c1zC1();
    }

    // <C1zC1> ::= = num | = num , <C1>
    private void c1zC1() throws ErroresDeSintaxis {
        consumir(Token.ASIGNACION);
        consumir(Token.NUM);

        if (coincide(Token.COMA)) {
            consumir(Token.COMA);
            c1();
        }
    }

    // <C2> ::= ε | const <C1> ;
    private void c2() throws ErroresDeSintaxis {
        if (enConjuntoFIRST(FIRST_C2)) {
            consumir(Token.CONST);
            c1();
            consumir(Token.PUNTO_Y_COMA);
        }
        // ε caso
    }

    // <C3> ::= id <C3zC3>
    private void c3() throws ErroresDeSintaxis {
        consumir(Token.ID);
        c3zC3();
    }

    // <C3zC3> ::= , <C3> | ε
    private void c3zC3() throws ErroresDeSintaxis {
        if (coincide(Token.COMA)) {
            consumir(Token.COMA);
            c3();
        }
        // ε caso
    }

    // <C4> ::= ε | var <C3> ;
    private void c4() throws ErroresDeSintaxis {
        if (enConjuntoFIRST(FIRST_C4)) {
            consumir(Token.VAR);
            c3();
            consumir(Token.PUNTO_Y_COMA);
        }
        // ε caso
    }

    // <C5> ::= ε | proced id ; <Bloque> ; <C5>
    private void c5() throws ErroresDeSintaxis {
        if (enConjuntoFIRST(FIRST_C5)) {
            consumir(Token.PROCED);
            consumir(Token.ID);
            consumir(Token.PUNTO_Y_COMA);
            bloque();
            consumir(Token.PUNTO_Y_COMA);
            c5();
        }
        // ε caso
    }


    // <Proposicion> ::= { <E1> } | id = <Expresion> | print <E2> | input id | Exec id |
    //                   if <Condicion> : <Proposicion> | while <Condicion> : <Proposicion> |
    //                   for id = <Expresion> <E3> <Expresion> : <Proposicion>
    private void proposicion() throws ErroresDeSintaxis {
        System.out.println("Token actual en proposicion(): " + tokenEnTurno); // Debug
        if (coincide(Token.LLAVE_ABRE)) {
            consumir(Token.LLAVE_ABRE);
            e1();
            consumir(Token.LLAVE_CIERRA);
        } else if (coincide(Token.ID)) {
            consumir(Token.ID);
            consumir(Token.ASIGNACION);
            expresion();
        } else if (coincide(Token.PRINT)) {
            consumir(Token.PRINT);
            e2();
        } else if (coincide(Token.INPUT)) {
            consumir(Token.INPUT);
            consumir(Token.ID);
        } else if (coincide(Token.EXEC)) {
            consumir(Token.EXEC);
            consumir(Token.ID);
        } else if (coincide(Token.IF)) {
            consumir(Token.IF);
            condicion();
            consumir(Token.DOS_PUNTOS);
            proposicion();
        } else if (coincide(Token.WHILE)) {
            consumir(Token.WHILE);
            condicion();
            consumir(Token.DOS_PUNTOS);
            proposicion();
        } else if (coincide(Token.FOR)) {
            consumir(Token.FOR);
            consumir(Token.ID);
            consumir(Token.ASIGNACION);
            expresion();
            e3();
            expresion();
            consumir(Token.DOS_PUNTOS);
            proposicion();
        } else {
            throw new ErroresDeSintaxis("Proposición no válida");
        }
    }

    // <E1> ::= <Proposicion> <E1zE1>
    private void e1() throws ErroresDeSintaxis {
        proposicion();
        e1zE1();
    }

    // <E1zE1> ::= ; <E1> | ε
    private void e1zE1() throws ErroresDeSintaxis {
        if (coincide(Token.PUNTO_Y_COMA)) {
            consumir(Token.PUNTO_Y_COMA);
            e1();
        }
        // ε caso
    }

    // <E2> ::= id | num
    private void e2() throws ErroresDeSintaxis {
        if (coincide(Token.ID)) {
            consumir(Token.ID);
        } else {
            consumir(Token.NUM);
        }
    }

    // <E3> ::= -> | <-
    private void e3() throws ErroresDeSintaxis {
        if (coincide(Token.FLECHA_DER)) {
            consumir(Token.FLECHA_DER);
        } else {
            consumir(Token.FLECHA_IZQ);
        }
    }

    // <Condicion> ::= <Expresion> <C6> <Expresion>
    private void condicion() throws ErroresDeSintaxis {
        expresion();
        c6();
        expresion();
    }

    // <C6> ::= == | <> | < | > | <= | >=
    private void c6() throws ErroresDeSintaxis {
        if (!enConjuntoFIRST(FIRST_C6)) {
            throw new ErroresDeSintaxis("Operador de comparación esperado");
        }
        avanzar(); 
    }


    // <Expresion> ::= <Termino> <D2zD2>
    private void expresion() throws ErroresDeSintaxis {
        if (!enConjuntoFIRST(FIRST_EXPRESION)) {
            throw new ErroresDeSintaxis("Expresión no válida");
        }
        termino();
        d2zD2();
    }

    // <D2zD2> ::= <D1> <D2> | ε
    private void d2zD2() throws ErroresDeSintaxis {
        if (coincide(Token.SUMA) || coincide(Token.RESTA)) {
            d1();
            expresion();
        }
        // ε caso
    }

    // <D1> ::= + | -
    private void d1() throws ErroresDeSintaxis {
        if (coincide(Token.SUMA)) {
            consumir(Token.SUMA);
        } else {
            consumir(Token.RESTA);
        }
    }

    // <Termino> ::= <Factor> <D4zD4>
    private void termino() throws ErroresDeSintaxis {
        factor();
        d4zD4();
    }

    // <D4zD4> ::= <D3> <D4> | ε
    private void d4zD4() throws ErroresDeSintaxis {
        if (coincide(Token.MULT) || coincide(Token.DIV)) {
            d3();
            termino();
        }
        // ε caso
    }

    // <D3> ::= * | /
    private void d3() throws ErroresDeSintaxis {
        if (coincide(Token.MULT)) {
            consumir(Token.MULT);
        } else {
            consumir(Token.DIV);
        }
    }

    // <Factor> ::= ( <Expresion> ) | id | num
    private void factor() throws ErroresDeSintaxis {
        if (coincide(Token.PARENTESIS_ABRE)) {
            consumir(Token.PARENTESIS_ABRE);
            expresion();
            consumir(Token.PARENTESIS_CIERRA);
        } else if (coincide(Token.ID)) {
            consumir(Token.ID);
        } else if (coincide(Token.NUM)){
            consumir(Token.NUM);
        }
    }
    
    public boolean analizar() {
        try {
            programa();
            return true;
        } catch (ErroresDeSintaxis e) {
            errores.append(e.getMessage());
            return false;
        }
    }    
}
