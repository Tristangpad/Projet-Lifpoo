package modele.plateau;

public class Poubelle extends Machine {
    @Override
    public void send(){

    }
    //supprime n'importe qule item présent dans sa file
    @Override
    public void work(){
        if(!current.isEmpty()){current.removeFirst();}
    }
}
