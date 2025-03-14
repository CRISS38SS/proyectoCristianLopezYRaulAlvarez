/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.itz.proyecto.controles;

import edu.itz.proyecto.vistas.Vista;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        int returnValue = fileChooser.showSaveDialog(v);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            path = fileChooser.getSelectedFile().getAbsolutePath();
            v.getLblRutaArchivo().setText(path);
            this.leerArchivo(path);
        }

        if (path == null) {
            JOptionPane.showMessageDialog(v, "Opereacion cancelada");
            return;
        }
    }

    public void limpiar() {
        v.getTxtContenido().setText("");
        v.getLblRutaArchivo().setText("");
        v.getTxtSalida().setText("");
    }

    public void leerArchivo(String rutaArchivo) {
        String texto = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(rutaArchivo));
            String file;
            while ((file = bufferedReader.readLine()) != null) {
                texto += file + "\n";
                //System.out.println(file);
            }
        } catch (IOException e) {
            System.err.println("error: " + e.getMessage());
        }
        v.getTxtContenido().append(texto + "\n");
    }
    
    public void salir(){
        System.exit(0);
    }

}
