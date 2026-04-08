package modele.plateau;

import modele.item.ItemShape;

import java.util.Objects;

public class Niveau {

    private String formeDemander;
    private int objectif;
    private int progression;

    //constructeur du niveau
    public Niveau(String formeDemander, int objectif) {
        this.formeDemander = formeDemander;
        this.objectif = objectif;
        this.progression = 0;//set la progression a 0
    }

    public Niveau genereNiveauRandom(Niveau n){
        return n;
    }
    public String getFormeDemander() {return formeDemander;}

    //si la forme demander ne correspond pas a l'item passer en paramètre return false
    //teste aussi les différentes rotations possible en cas d'utilisation du rotater
    public boolean verifierItem(ItemShape item) {
        return Objects.equals(item.getConfigItem(), formeDemander);
    }

    public boolean niveauFinis() {
        return progression >= objectif;
    }

    //getter et setter
    public int getProgression() {
        return progression;
    }

    public void setProgression(int progression) {
        this.progression = progression;
    }

    public int getObjectif() {
        return objectif;
    }

    public void setObjectif(String forme,int objectif) {
        this.objectif = objectif;
        this.formeDemander = forme;
    }

    public void incrementer() { progression++; }

}

