/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.itz.proyecto.controles;

import edu.itz.proyecto.vistas.Vista;
import enumerada.Token;
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

        String regex = "\\bconst\\b|\\bvar\\b|\\bproced\\b|\\bprint\\b|\\binput\\b|\\bexec\\b|\\bif\\b|\\bwhile\\b|\\bfor\\b|[a-zA-Z_][a-zA-Z0-9_]*|0|[1-9][0-9]*|==|<>|<=|>=|->|<-|[\\+\\-*/=.,;:<>{}()]";

        // Construir el resultado
        StringBuilder resultado = new StringBuilder();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(texto);

        while (matcher.find()) {
            String token = matcher.group();

            Token tipoToken = Token.obtenerTokenPara(token);
            if (tipoToken != null) {
                resultado.append(token).append("          ").append(tipoToken.getValor()).append("\n");
            } else {
                resultado.append(token).append("          ").append("No reconocido").append("\n");
            }
        }
        v.getTxtSalida().setText(resultado.toString());
    }
}
