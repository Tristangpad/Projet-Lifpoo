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
    //a voir
    public void createGisement(Item g) {
        ItemShape gisement = (ItemShape) g; // cast
    }


    public Machine getMachine() {
        return machine;
    }

    public Case(Plateau _plateau) {

        plateau = _plateau;
    }

}