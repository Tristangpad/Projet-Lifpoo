package modele.plateau;

import modele.item.ItemShape;

import java.util.Objects;

public class Niveau {

    private String formeDemander;
    private int objectif;
    private int progression;


    public Niveau(String formeDemander, int objectif) {
        this.formeDemander = formeDemander;
        this.objectif = objectif;
        this.progression = 0;
    }

    public boolean verifierItem(ItemShape item) {
        return Objects.equals(item.getConfigItem(), formeDemander);
    }

    public boolean niveauFinis() {
        return progression >= objectif;
    }

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

