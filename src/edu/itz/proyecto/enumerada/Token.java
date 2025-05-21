/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enumerada;

/**
 *
 * @author criss
 */
public enum Token {
    //palabras reservadas
    CONST("const", 2),
    VAR("var", 7),
    PROCED("proced", 8),
    PRINT("print", 17),
    INPUT("input", 19),
    EXEC("exec", 20),
    IF("if", 21),
    WHILE("while", 23),
    FOR("for", 24),
    // tienen dos caracteres
    IGUAL("==", 9),
    DISTINTO("<>", 10),
    MENOR_IGUAL("<=", 13),
    MAYOR_IGUAL(">=", 14),
    FLECHA_DER("->", 25),
    FLECHA_IZQ("<-", 26),
    //tienen un caracter
    MENOR("<", 11),
    MAYOR(">", 12),
    ASIGNACION("=", 4),
    SUMA("\\+", 27),
    RESTA("-", 28),
    MULT("\\*", 31),
    DIV("/", 32),
    PUNTO("\\.", 1),
    COMA(",", 5),
    PUNTO_Y_COMA(";", 6),
    DOS_PUNTOS(":", 22),
    LLAVE_ABRE("\\{", 15),
    LLAVE_CIERRA("\\}", 16),
    PARENTESIS_ABRE("\\(", 29),
    PARENTESIS_CIERRA("\\)", 30),
    //no son reservadas
    ID("[a-zA-Z_][a-zA-Z0-9_]*", 3),
    CERO("0", 18),
    NUM("[1-9][0-9]*", 18);

    private final String patron;
    private final int valor;

    Token(String patron, int valor) {
        this.patron = patron;
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public boolean coincideCon(String elemento) {
        if (patron == null) {
            return true;
        }
        return elemento.matches(patron);
    }

    public static Token obtenerTokenPara(String elemento) {
        for (Token token : values()) {
            if (token.coincideCon(elemento)) {
                return token;
            }
        }
        return null;
    }
}
