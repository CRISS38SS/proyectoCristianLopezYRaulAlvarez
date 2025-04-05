/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.itz.proyecto.controles;

import edu.itz.proyecto.vistas.Vista;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author criss
 */
public class Control {

    Vista v;

    public Control(Vista v) {
        this.v = v;
    }

    public void abrirArchivo() {
        this.limpiar();
        String path = null;
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(v);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            path = fileChooser.getSelectedFile().getAbsolutePath();
            this.leerArchivo(path);
        }

        if (path == null) {
            JOptionPane.showMessageDialog(v, "Opereacion cancelada");
            return;
        }
    }

    public void limpiar() {
        v.getTxtContenido().setText("");
        v.getTxtSalida().setText("");
    }

    public void leerArchivo(String rutaArchivo) {
        String texto = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(rutaArchivo));
            String file;
            while ((file = bufferedReader.readLine()) != null) {
                texto += file + "\n";
            }
        } catch (IOException e) {
            System.err.println("error: " + e.getMessage());
        }
        v.getTxtContenido().append(texto);
    }

    public void salir() {
        System.exit(0);
    }

    public void separarPalabras() {
        String texto = v.getTxtContenido().getText();
        v.getTxtSalida().setText("");

        if (texto == null || texto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(v, "El área de contenido está vacía. No hay texto para analizar.");
            return;
        }

        String regex = "\\bconst\\b|\\bvar\\b|\\bproced\\b|\\bprint\\b|\\binput\\b|\\bexec\\b|\\bif\\b|\\bwhile\\b|\\bfor\\b|[a-zA-Z_][a-zA-Z0-9_]*|\\d+|==|<>|<=|>=|->|<-|[\\+\\-*/=.,;:<>{}()]";

        // Construir el resultado
        StringBuilder resultado = new StringBuilder();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(texto);

        while (matcher.find()) {
            String token = matcher.group();

            Token tipoToken = Token.obtenerTokenPara(token);
            if (tipoToken != null) {
                resultado.append(token).append("     ").append(tipoToken.getValor()).append("\n");
            } else {
                resultado.append(token).append("     ").append("No reconocido").append("\n");
            }
        }
        v.getTxtSalida().setText(resultado.toString());
    }

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
        NUM("\\d+", 18);

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
}
