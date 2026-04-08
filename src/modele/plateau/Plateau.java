/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;



import modele.item.*;

import java.util.HashMap;
import java.util.Observable;
import java.util.Random;


public class Plateau extends Observable implements Runnable {

    public static final int SIZE_X = 50;
    public static final int SIZE_Y = 50;


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
        //init du plateau sans rien
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                grilleCases[x][y] = new Case(this);
                map.put(grilleCases[x][y], new Point(x, y));
            }
        }
    }

    public void setMachine(int x, int y, Machine m) {
        //prends une machine en paramètre

        //err : cas ou l'ont essaie de placer une mine ailleurs qu'un gisement
        if( m instanceof Mine && (grilleCases[x][y].getGisement() == null) ) return;

        //err : cas pour machine impossible a placer car déborde ou superpossé a une autre
        for (int xx = 0; xx < m.getLargeur(); xx++) {
            for (int yy = 0; yy < m.getHauteur(); yy++) {
                Case c = grilleCases[x+ xx][y + yy];
                if(c.getMachine() != null || c.isExtention())
                    {return;}//ne set pas de machine si case obstruer par une autre machine
            }

        }
        //setter
        grilleCases[x][y].setMachine(m);

        //setter si machine dim > 2
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
        //err : cas si la case est vide alors l'ont ne vas pas plus loins
        if (m == null) return;

        //quelque soit la taille de la machine l'ont supprime sa partie principale et ses extentions si elle en possède
        for (int dx = 0; dx < m.getLargeur(); dx++) {
            for (int dy = 0; dy < m.getHauteur(); dy++) {
                grilleCases[x + dx][y + dy].suppMachineCase();
                grilleCases[x + dx][y + dy].suppMachinePrincipale();
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
        if (m instanceof Rotater rotater){
            rotater.tourner();
        }
        if (m instanceof Rotater rotaterInverser){
            rotaterInverser.tourner();
        }

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

    public void genererGisementsAleatoires(int n) {
        int count = 0;

        while (count < n) {
            int x = new Random().nextInt(SIZE_X);
            int y = new Random().nextInt(SIZE_Y);

            // vérifie que la case est vide
            if (grilleCases[x][y].getGisement() == null) {
                grilleCases[x][y].setGisement(genererItemAleatoire(new Random()));
                count++;
            }
        }
        setChanged();
        notifyObservers();
    }

    private Item genererItemAleatoire(Random random) {
        //80% chance de forme et 20% chance de couleur
        if (random.nextInt(10) < 8) {
            return new ItemShape(genererFormeAleatoire(random));
        } else {
            Couleur[] couleurs = {Couleur.Red, Couleur.Green, Couleur.Blue, Couleur.Yellow, Couleur.Purple, Couleur.Cyan, Couleur.White};
            return new ItemColor(couleurs[random.nextInt(couleurs.length)]);
        }
    }

    private String genererFormeAleatoire(Random random) {
        StringBuilder forme = new StringBuilder();
        SubShape[] shapes = {SubShape.Carre, SubShape.Circle, SubShape.Fan, SubShape.Star, SubShape.None};
        Couleur[] couleurs = {Couleur.Red, Couleur.Green, Couleur.Blue, Couleur.Yellow, Couleur.Purple, Couleur.Cyan, Couleur.White};

        for (int i = 0; i < 4; i++) {
            SubShape shape = shapes[random.nextInt(shapes.length)];
            if (shape == SubShape.None) {
                forme.append("--");
            } else {
                Couleur couleur = couleurs[random.nextInt(couleurs.length)];
                forme.append(shape.adapterEnString()).append(couleur.adapterEnString());
            }
        }
        return forme.toString();
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
        //si il y a un dépot sur le plateau de jeux alors les objectifs et les informations
        //du niveau actuelle lui sont init
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

    public Point getPosition(Case c) {
        return map.get(c);
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
