package modele.plateau;

import modele.item.Couleur;
import modele.item.ItemShape;
import modele.item.SubShape;

import java.util.Objects;
import java.util.Random;

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

    public static Niveau genereNiveauRandom(int objectif){
        StringBuilder forme = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            SubShape[] formeDisponibles = {SubShape.Carre, SubShape.Circle, SubShape.Fan, SubShape.Star, SubShape.None};
            SubShape shape = formeDisponibles[new Random().nextInt(formeDisponibles.length)];

            if (shape == SubShape.None) {
                forme.append("--");
            } else {
                Couleur[] couleursDisponibles = {Couleur.Red, Couleur.Green, Couleur.Blue, Couleur.Yellow, Couleur.Purple, Couleur.Cyan, Couleur.White};
                Couleur couleur = couleursDisponibles[new Random().nextInt(couleursDisponibles.length)];
                forme.append(shape.adapterEnString()).append(couleur.adapterEnString());
            }
        }
        return new Niveau(forme.toString(), objectif);
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

