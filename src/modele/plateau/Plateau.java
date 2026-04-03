/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;



import modele.item.Item;

import java.util.HashMap;
import java.util.Observable;


public class Plateau extends Observable implements Runnable {

    public static final int SIZE_X = 16;
    public static final int SIZE_Y = 16;


    private HashMap<Case, Point> map = new HashMap<Case, Point>(); // permet de récupérer la position d'une case à partir de sa référence
    private Case[][] grilleCases = new Case[SIZE_X][SIZE_Y]; // permet de récupérer une case à partir de ses coordonnées

    private Niveau niveauActuel;

    public Plateau() {
        initPlateauVide();
    }

    public Case[][] getCases() {
        return grilleCases;
    }

    public Case getCase(Case source, Direction d) {
        Point p = map.get(source);
        return caseALaPosition(new Point(p.x+d.dx, p.y+d.dy));
    }

    public Case getCase(int x, int y) {
        return  grilleCases[x][y];
    }

    private void initPlateauVide() {

        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                grilleCases[x][y] = new Case(this);
                map.put(grilleCases[x][y], new Point(x, y));
            }
        }
    }

    public void setMachine(int x, int y, Machine m) {

        if( m instanceof Mine && (grilleCases[x][y].getGisement() == null) ) return;

        for (int xx = 0; xx < m.getLargeur(); xx++) {
            for (int yy = 0; yy < m.getHauteur(); yy++) {
                Case c = grilleCases[x+ xx][y + yy];
                if(c.getMachine() != null || c.isExtention())
                    {return;}//ne set pas de machine si case obstruer par une autre machine
            }

        }
        grilleCases[x][y].setMachine(m);

        //extention si machine dim > 2
        for (int xx = 0; xx < m.getLargeur(); xx++) {
            for (int yy = 0; yy < m.getHauteur(); yy++) {
                if (xx == 0 && yy == 0){
                    continue;
                }
                grilleCases[x + xx][y + yy].setMachinePrincipale(grilleCases[x][y]); //place les autre case en fonction de la case principale
            }
        }
        setChanged();
        notifyObservers();
    }

    public void suppMachinePlateau(int x, int y) {

        Machine m = grilleCases[x][y].getMachine();
        if(m != null) {
            if (m.getHauteur() == 1 || m.getLargeur() == 1) {
                grilleCases[x][y].suppMachineCase();
            } else {
                for (int dx = 0; dx < m.getLargeur(); dx++) {
                    for (int dy = 0; dy < m.getHauteur(); dy++) {
                        grilleCases[x + dx][y + dy].suppMachineCase();
                        grilleCases[x + dx][y + dy].suppMachinePrincipale();
                    }
                }
            }
        }
        setChanged();
        notifyObservers();
    }
    public void changerDirMachine(int x, int y) {
        Machine m = grilleCases[x][y].getMachine();

        if (m instanceof Tapis tapis) {
            tapis.tourner();
        }
        if (m instanceof Mine mine) {
            mine.tourner();
        }

        /*if (m instanceof Rotater rotater)
            rotater.tourner();
        }*/

        setChanged();
        notifyObservers();
    }
    /**
     * transforme une case en gisement
     */
    public void transformeCaseEnGisement(int x, int y, Item forme)  {
        grilleCases[x][y].setGisement(forme);
        setChanged();
        notifyObservers();
    }


    /**
     * Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }

    private Case caseALaPosition(Point p) {
        Case retour = null;

        if (contenuDansGrille(p)) {
            retour = grilleCases[p.x][p.y];
        }
        return retour;
    }



    public void setNiveauActuel(Niveau n) {
        this.niveauActuel = n;
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                Machine m = grilleCases[x][y].getMachine();
                if (m instanceof ZoneDepot) {
                    ((ZoneDepot) m).setNiveau(n);
                }
            }
        }
        setChanged();
        notifyObservers(n);
    }


    @Override
    public void run() {
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                Machine m = grilleCases[x][y].getMachine();
                if (m != null) {
                    m.reset_ARecuCeTick();
                }
            }
        }

        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                Machine m = grilleCases[x][y].getMachine();
                if (m != null) {
                    m.run();
                }
            }
        }
        setChanged();
        notifyObservers();
    }
}