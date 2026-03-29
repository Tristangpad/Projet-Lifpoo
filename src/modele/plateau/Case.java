/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

public class Case {


    protected Plateau plateau;
    protected Machine machine;
    protected Item gisement; // certaines cases sont des gisements, pour placer des mines

    protected Case machinePrincipale; //si jamais l'on pose une machine qui est a des dimentions > a 1 par 1, permet d'en avoir la base

    public void initCase(){
        this.machine = null;
        this.gisement = null;

    }

    public void setMachine(Machine m) {
        if(this.getMachine() == null){
            machine = m;
            m.setCase(this);
        }
    }

    public void suppMachineCase(){
        this.machine = null;
    }
    //a voir
    public void setGisement(Item g) {
        this.gisement = g; // cast
    }


    public Machine getMachine() {
        return machine;
    }

    public Item getGisement() { return gisement; }

    public Case(Plateau _plateau) {
        plateau = _plateau;
        initCase();
    }

    //getter et setter ainsi que le destructeur en cas de machine avec des dimention != a 1 par 1
    public boolean isExtention() {
        return machinePrincipale != null;
    }

    public Case getMachinePrincipale(){
        return machinePrincipale;
    }

    public void setMachinePrincipale(Case c){
        machinePrincipale = c;
    }

    public void suppMachinePrincipale() {
        machinePrincipale = null;
    }
}
