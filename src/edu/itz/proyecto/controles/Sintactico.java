/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.itz.proyecto.controles;

import java.util.ArrayList;

/**
 *
 * @author criss
 */
public class Sintactico {

    char token = '0';

    /*
    K
     */
    public void k() {
        l();
        m();
        System.out.println("e");
        
    }

    /*
    l->null
    l->{i}
     */
    private void l() {
        ArrayList<Character> firstL = new ArrayList();
        firstL.add('q');
        firstL.add('n');
        if (!firstL.contains(token)) {
            return;
        }
        t();
        System.out.println("l");
        i();
    }

    /*
    M->on
    M->null
     */
    private void m() {
        if (token != 'o') {
            return;
        }
        System.out.println("o");
        System.out.println("n");
    }

    /*
    I-> Ma First(M)={o}
    I-> Tan First(T)={q,n}
     */
    private void i() {
        switch (token) {
            case 'o':
                m();
                System.out.println("o");
                break;
            case 'q':
            case 'n':
                t();
                System.out.println("a");
                System.out.println("n");
                break;
            default:
                throw new AssertionError();
        }
    }

    /*
    T->qM
    T->n
     */
    private void t() {
        switch (token) {
            case 'q':
                System.out.println("q");

                //avanza token
                m();
                break;
            case 'n':
                System.out.println("n");

                //avanza token
                break;
            default:
                throw new AssertionError();
        }
    }
}
