/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.itz.proyecto.controles;

import edu.itz.proyecto.enumerada.Token;
import java.util.*;
import java.util.List;


/**
 *
 * @author criss
 */
public class SintacticoProcesador {
    private final List<Token> tokens;
    private int posicionActual;
    private Token tokenEnTurno;
    
    
    private static final Set<Token> FIRST_C1 = Set.of(Token.ID);
    private static final Set<Token> FIRST_C2 = Set.of(Token.CONST);
    private static final Set<Token> FIRST_C3 = Set.of(Token.ID);
    private static final Set<Token> FIRST_C4 = Set.of(Token.VAR);
    private static final Set<Token> FIRST_C5 = Set.of(Token.PROCED);
    private static final Set<Token> FIRST_C6 = Set.of(Token.IGUAL, Token.DISTINTO, Token.MENOR,
            Token.MAYOR, Token.MENOR_IGUAL, Token.MAYOR_IGUAL);
    private static final Set<Token> FIRST_EXPRESION = Set.of(Token.PARENTESIS_ABRE, Token.ID, Token.NUM);
    private static final Set<Token> FIRST_PROPOSICION = Set.of(Token.LLAVE_ABRE, Token.ID, Token.PRINT,
            Token.INPUT, Token.EXEC, Token.IF,
            Token.WHILE, Token.FOR);
    
    public SintacticoProcesador(List<Token> tokens) {
        this.tokens = tokens;
        this.posicionActual = 0;
        this.tokenEnTurno = tokens.isEmpty() ? null : tokens.get(0);
    }    
    
    
    private void avanzar() {
        posicionActual++;
        tokenEnTurno = posicionActual < tokens.size() ? tokens.get(posicionActual) : null;
    }
    
        private boolean coincide(Token tipo) {
        return tokenEnTurno != null && tokenEnTurno == tipo;
    }
        
    private void consumir(Token esperado){
        if (!coincide(esperado)) {
            System.out.println("error");
        }
        avanzar();
    }    
    
    private boolean enConjuntoFIRST(Set<Token> conjunto) {
        return tokenEnTurno != null && conjunto.contains(tokenEnTurno);
    }
    
}
