/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aomloxygentitrator;

/**
 *
 * @author pedro
 */
public class Points {

    double titrant;
    double current;
    int U;

    public Points(double titrant, double current, int U) {
        this.titrant = titrant;
        this.current = current;
        this.U = U;

    }

    double getCurrent() {
        return current;
    }

    double getTitrant() {
        return titrant;
    }

    int getU() {
        return U;
    }
}
