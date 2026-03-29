package modele.plateau;

import modele.item.Item;

public class Poubelle extends Machine {
    @Override
    public void send(){

    }

    @Override
    public void work(){
        if(!current.isEmpty()){current.removeFirst();}
    }
}
