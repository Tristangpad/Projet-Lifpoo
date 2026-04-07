package vuecontroleur;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

import modele.item.Couleur;
import modele.plateau.Painter;
import vuecontroleur.Menu;
import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;
import static modele.item.Couleur.*;
import modele.jeu.Jeu;
import modele.plateau.*;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle
 *
 */
public class VueControleur extends JFrame implements Observer {
    private Plateau plateau; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)
    private Jeu jeu;
    private final int sizeX; // taille de la grille affichée
    private final int sizeY;
    private static final int pxCase = 82; // nombre de pixel par case

    //init de la bar de progression propre a JP
    private JProgressBar barProgression;
    private JFrame frame;
    private Menu menu;


    private double rotationAngle = Math.toRadians(90);

    // icones affichées dans la grille
    private Image icoTapisHaut;
    private Image icoTapisBas;
    private Image icoTapisGauche;
    private Image icoTapisDroite;

    private Image icoRouge;
    private Image icoBleu;
    private Image icoCyan;
    private Image icoVert;
    private Image icoViolet;
    private Image icoGris;
    private Image icoBlanc;
    private Image icoJaune;

    private Image icoPoubelle;

    private Image icoMineHaut;
    private Image icoMineBas;
    private Image icoMineGauche;
    private Image icoMineDroite;

    private Image icoCutter;
    private Image icoPainter;
    private Image icoZoneDepot;

    private Image icoRotaterHaut;
    private Image icoRotaterBas;
    private Image icoRotaterGauche;
    private Image icoRotaterDroite;

    private Image icoTapisHautDroite;
    private Image icoTapisBasDroite;
    private Image icoTapisHautGauche;
    private Image icoTapisBasGauche;

    private Image icoTapisDroiteHaut;
    private Image icoTapisDroiteBas;
    private Image icoTapisGaucheHaut;
    private Image icoTapisGaucheBas;


    private JComponent grilleIP;
    private boolean mousePressed = false; // permet de mémoriser l'état de la souris
    private ImagePanel[][] tabIP; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône background et front, suivant ce qui est présent dans le modèle)

    //creation des differentes interfaces ajouter a la grille de base du jeux
    private JPanel menuOverlay;
    private GridBagConstraints contrainteMenu;
    private JPanel introNiveau;
    private GridBagConstraints contrainteNiveau;
    private JPanel niveauOverlay;
    private GridBagConstraints contrainteNiveauOverlay;
    private ImagePanel niveauOverlayForme;

    //pour calculer les liaison pour les tapis pour les ajuster
    private int casePX = -1; //par defaut
    private int casePY = -1;

    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        sizeX = plateau.SIZE_X;
        sizeY = plateau.SIZE_Y;
        frame = new JFrame();

        chargerLesIcones();//charge les icones
        placerLesComposantsGraphiques();//place les icones
        afficherIntroNiveau(jeu.getNiveauActuel(), jeu.getNumeroNiveau());//crée un pop up temporaire a chaque début de niveau

        plateau.addObserver(this);

        mettreAJourAffichage();
    }
    /*
    Fonction créant une nouvelle image apartir de li'mage passr en paramètre en fonction de la rotation demander
     */
    public static Image rotateIcon(Image img, double angle) {
        //trouver sur internet
        int w = img.getWidth(null);
        int h = img.getHeight(null);

        BufferedImage rotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageR = rotated.createGraphics();

        imageR.rotate(Math.toRadians(angle), w / 2.0, h / 2.0);
        imageR.drawImage(img, 0, 0, null);

        imageR.dispose();

        return rotated;
    }


    private void chargerLesIcones() {

        //tapis droits puis tapis pour liaison en coude
        icoTapisHaut = new ImageIcon("./data/sprites/buildings/belt_top.png").getImage();
        icoTapisBas = rotateIcon(icoTapisHaut,180);
        icoTapisGauche = rotateIcon(icoTapisHaut,270);
        icoTapisDroite = rotateIcon(icoTapisHaut,90);

        icoTapisHautDroite = new ImageIcon("./data/sprites/buildings/belt_right.png").getImage();
        icoTapisHautGauche = new ImageIcon("./data/sprites/buildings/belt_left.png").getImage();
        icoTapisBasDroite = rotateIcon(icoTapisHautGauche,180);
        icoTapisBasGauche = rotateIcon(icoTapisHautDroite,180);

        icoTapisDroiteHaut = rotateIcon(icoTapisHautGauche,90);
        icoTapisDroiteBas  = rotateIcon(icoTapisHautDroite,90);
        icoTapisGaucheHaut  = rotateIcon(icoTapisHautDroite,270);
        icoTapisGaucheBas = rotateIcon(icoTapisHautGauche,270);

        //chargement des couleurs
        icoRouge = new ImageIcon("./data/sprites/colors/red.png").getImage();
        icoBleu = new ImageIcon("./data/sprites/colors/blue.png").getImage();
        icoCyan = new ImageIcon("./data/sprites/colors/cyan.png").getImage();
        icoVert = new ImageIcon("./data/sprites/colors/green.png").getImage();
        icoViolet = new ImageIcon("./data/sprites/colors/purple.png").getImage();
        icoGris = new ImageIcon("./data/sprites/colors/uncolored.png").getImage();
        icoBlanc = new ImageIcon("./data/sprites/colors/white.png").getImage();
        icoJaune = new ImageIcon("./data/sprites/colors/yellow.png").getImage();

        //chargement des machines
        icoPoubelle = new ImageIcon("./data/sprites/buildings/trash.png").getImage();

        icoMineHaut = new ImageIcon("./data/sprites/buildings/miner.png").getImage();
        icoMineBas = rotateIcon(icoMineHaut,180);
        icoMineGauche = rotateIcon(icoMineHaut,270);
        icoMineDroite = rotateIcon(icoMineHaut,90);

        icoRotaterHaut = new ImageIcon("./data/sprites/buildings/rotater.png").getImage();
        icoRotaterBas = rotateIcon(icoRotaterHaut,180);
        icoRotaterGauche = rotateIcon(icoRotaterHaut,270);
        icoRotaterDroite = rotateIcon(icoRotaterBas,90);

        //chargement des machines avec extention
        icoCutter = new ImageIcon("./data/sprites/buildings/cutter.png").getImage();
        icoPainter = new ImageIcon("./data/sprites/buildings/painter.png").getImage();
        icoZoneDepot = new ImageIcon("./data/sprites/buildings/hub.png").getImage();

    }

    private Image getIconeColor(Couleur c) {
        //pour la partie graphique renvoie l'image assossier a chaque couleur
        switch (c) {
            case Red -> {
                return icoRouge;
            }
            case White -> {
                return icoBlanc;
            }
            case Blue -> {
                return icoBleu;
            }
            case Cyan -> {
                return icoCyan;
            }
            case Green -> {
                return icoVert;
            }
            case Purple -> {
                return icoViolet;
            }
            case Yellow -> {
                return icoJaune;
            }
            default -> {
                return icoGris;
            }
        }
    }


    private Image getIconeMachine(Machine m) {
        //pour chaque machine renvoie sont icones approprié
        //pour les tapis cel aest fait en fonction de leurs entré et sorite
        if (m instanceof Tapis) {
            Direction sortie = ((Tapis) m).getDirection();
            Direction entree = ((Tapis) m).getDirInput();

            //System.out.println(entree + "--->" + sortie); //debug
            if (entree == null || entree == sortie) {

                switch (sortie) {
                    case North -> {
                        return icoTapisHaut;
                    }
                    case South -> {
                        return icoTapisBas;
                    }
                    case West -> {
                        return icoTapisGauche;
                    }
                    case East -> {
                        return icoTapisDroite;
                    }
                }
            }
            //double switch pour liée en fonction de l'entree a la sortie
            switch (entree) {
                case North -> {switch (sortie) {
                                case East  -> {return icoTapisHautDroite;}
                                case West  -> {return icoTapisHautGauche;}
                                default    -> {return icoTapisHaut;}}
                }
                case South -> {switch (sortie) {
                                case East  -> {return icoTapisBasDroite;}
                                case West  -> {return icoTapisBasGauche;}
                                default    -> {return icoTapisBas;}}
                }
                case East -> {switch (sortie) {
                                case North -> {return icoTapisDroiteHaut;}
                                case South -> {return icoTapisDroiteBas;}
                                default    -> {return icoTapisDroite;}}
                }
                case West -> {switch (sortie) {
                                case North -> {return icoTapisGaucheHaut;}
                                case South -> {return icoTapisGaucheBas;}
                                default    -> {return icoTapisGauche;}}
                }
            }
        }
        if (m instanceof Mine)
            switch (((Mine) m).getDirection()) {
                case North -> {return icoMineHaut;}
                case South -> {return icoMineBas;}
                case West -> {return icoMineGauche;}
                case East -> {return icoMineDroite;}
            }
        if (m instanceof Poubelle)  return icoPoubelle;

        if (m instanceof Rotater)
            switch (((Rotater) m).getDirection()) {
                case North -> {return icoRotaterHaut;}
                case South -> {return icoRotaterBas;}
                case West -> {return icoRotaterGauche;}
                case East -> {return icoRotaterDroite;}
            }
        if (m instanceof Cutter)    return icoCutter;
        if (m instanceof Painter)    return icoPainter;
        if (m instanceof ZoneDepot) return icoZoneDepot;
        return null;
    }


    public void afficherIntroNiveau(Niveau n,int numNiveau) {
        //definitions de l'intro que l'ont voit a chaque début de niveau
        if(introNiveau != null){
            menuOverlay.remove(introNiveau);
        }
        menu.setVisible(false);//sinon l'ont voix le menue des machins au dessus de l'overlay d'intro

        if (introNiveau != null) introNiveau.setVisible(false);

        introNiveau = new NiveauAfficher(n,numNiveau);

        //occupe toutes la place
        contrainteNiveau = new GridBagConstraints();
        contrainteNiveau.gridx = 0;
        contrainteNiveau.gridy = 0;
        contrainteNiveau.weightx = 1;
        contrainteNiveau.weighty = 1;
        contrainteNiveau.fill = GridBagConstraints.BOTH;

        //ajout de layer a la grille principale
        menuOverlay.add(introNiveau, contrainteNiveau);
        menuOverlay.revalidate();
        menuOverlay.repaint();
    }


    public void mettreAJourNiveauOverlay(Niveau n) {
        //pour tous les niveau après le premier
        if (n != null) {
            niveauOverlayForme.setShape(new ItemShape(n.getFormeDemander()));
            niveauOverlayForme.repaint();
        }
    }

    private void placerLesComposantsGraphiques() {
        setTitle("ShapeCraft");
        setResizable(true);
        setSize(sizeX * pxCase, sizeX * pxCase);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre


        menu = new Menu();
        menuOverlay = (JPanel) getGlassPane();
        menuOverlay.setLayout(new GridBagLayout());

        barProgression = new JProgressBar(0,100);//bar de progression

        grilleIP = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille
        tabIP = new ImagePanel[sizeX][sizeY];
        grilleIP.setBackground(new Color(200, 200, 200));


        //mise en forme du menue de choix de machine
        contrainteMenu = new GridBagConstraints();
        contrainteMenu.anchor = GridBagConstraints.SOUTH;//point d'attache
        contrainteMenu.weighty = 1.0;
        contrainteMenu.fill = GridBagConstraints.HORIZONTAL;
        contrainteMenu.ipadx = 10;
        contrainteMenu.ipady = 10;
        contrainteMenu.insets = new Insets(0, 20, 20, 20);


        //mise en forme de l'affichage des objectifs pour le niveau actuelle
        contrainteNiveauOverlay = new GridBagConstraints();
        contrainteNiveauOverlay.anchor = GridBagConstraints.SOUTH;

        //mise en forme de la bar de progression des niveaux
        barProgression.setPreferredSize(new Dimension(sizeX * pxCase, 30));
        add(barProgression, BorderLayout.NORTH);
        barProgression.setBackground(new Color(30, 30, 30));
        barProgression.setForeground(new Color(100, 180, 100));
        barProgression.setStringPainted(true);


        //ajout du menue avec sa mise en forme a l'overlay générale du jeux qui permet une superposition entre les 2
        menuOverlay.add(menu, contrainteMenu);
        menuOverlay.setVisible(true);

        //check le choix de l'utilisateur pour savoir si il change de machine a placer
        menu.getBTapis().addActionListener(e -> jeu.setMachineChoisie(new Tapis()));
        menu.getBMine().addActionListener(e -> jeu.setMachineChoisie(new Mine()));
        menu.getBPoubelle().addActionListener(e -> jeu.setMachineChoisie(new Poubelle()));
        menu.getBRotater().addActionListener(e -> jeu.setMachineChoisie(new Rotater()));

        menu.getBCutter().addActionListener(e -> jeu.setMachineChoisie(new Cutter()));
        menu.getBPainter().addActionListener(e -> jeu.setMachineChoisie(new Painter()));

        //affichage, création et mise en page de la preview des objectifs des niveau
        niveauOverlay = new JPanel();
        niveauOverlay.setLayout(new BoxLayout(niveauOverlay, BoxLayout.Y_AXIS));
        niveauOverlay.setBackground(new Color(0, 0, 0, 150));
        niveauOverlay.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        //definitions de la preview des formes a données
        JLabel labelObjectif = new JLabel("Objectif :");
        labelObjectif.setForeground(Color.WHITE);
        labelObjectif.setAlignmentX(Component.CENTER_ALIGNMENT);

        //mise en forme de la preview
        niveauOverlayForme = new ImagePanel();
        niveauOverlayForme.setPreferredSize(new Dimension(80, 80));
        niveauOverlayForme.setMaximumSize(new Dimension(80, 80));
        niveauOverlayForme.setAlignmentX(Component.CENTER_ALIGNMENT);

        niveauOverlay.add(labelObjectif);
        niveauOverlay.add(niveauOverlayForme);

        contrainteNiveauOverlay = new GridBagConstraints();
        contrainteNiveauOverlay.anchor = GridBagConstraints.NORTHEAST; // ← coin haut-droite
        contrainteNiveauOverlay.weightx = 1.0;
        contrainteNiveauOverlay.weighty = 1.0;
        contrainteNiveauOverlay.insets = new Insets(10, 0, 0, 10); // ← marge du bord

        menuOverlay.add(niveauOverlay, contrainteNiveauOverlay);

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                ImagePanel iP = new ImagePanel();
                iP.setBackground(new Color(220, 220, 220));

                tabIP[x][y] = iP; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )


                final int xx = x; // permet de compiler la classe anonyme ci-dessous
                final int yy = y;
                // écouteur de clics
                iP.addMouseListener(new MouseAdapter() {
                    /*@Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            jeu.suppMachineJeu(xx,yy);
                        }
                        else {
                            mousePressed = true;
                            jeu.rotateM(xx,yy);
                            jeu.press(xx, yy);
                            System.out.println(xx + "-" + yy);
                        }
                    }*/

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (mousePressed) {

                            if(casePX != -1 && casePY != -1) {
                                System.out.println("dir changer");//debug
                                Direction dir = jeu.calculLiaisonTapis(casePX,casePY,xx,yy);
                                jeu.setDirectionMachine(dir);//rewrite la dir de la machine si il y a une liaison avec cette dernière
                            }
                            //permet en cas de slide d'actualiser les liaisons
                            casePX = xx;
                            casePY = yy;
                            jeu.slide(xx, yy);
                            if (SwingUtilities.isLeftMouseButton(e)) {jeu.slide(xx, yy);}
                            else if (SwingUtilities.isRightMouseButton(e)) {jeu.suppMachineJeu(xx, yy);}
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        mousePressed = true;

                        if (SwingUtilities.isRightMouseButton(e)) {
                            jeu.suppMachineJeu(xx, yy);
                        }
                        else if (SwingUtilities.isLeftMouseButton(e)) {
                            jeu.rotateM(xx, yy);
                            jeu.press(xx, yy);
                            System.out.println(xx + "-" + yy);
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        mousePressed = false;
                        casePX = -1; //la case precedente ne compte plus tant que ce n'est pas pour slide
                        casePY = -1;
                    }
                });


                grilleIP.add(iP);

            }
        }
        add(grilleIP);

    }

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabIP)
     */
    private void mettreAJourAffichage() {
        //redessine la grille vierge
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                tabIP[x][y].setBackground((Image) null);
                tabIP[x][y].setFront(null);
                tabIP[x][y].setShape(null);
                tabIP[x][y].resetPartie();
            }
        }
        //dessine la grille actualiser
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                Case c = plateau.getCases()[x][y];
                Machine m = c.getMachine();

                //si la machine est une extention d'une autre alors ont skip la case
                if (c.isExtention()) {
                    continue;
                }

                if (m != null) {
                    //get l'image pour chaque case
                    Image ico = getIconeMachine(m);
                    //cas pour machine avec extention
                    if (m.getLargeur() > 1 || m.getHauteur() > 1) {
                        for (int xx = 0; xx < m.getLargeur(); xx++) {
                            for (int yy = 0; yy < m.getHauteur(); yy++) {
                                if (x + xx < sizeX && y + yy < sizeY) {
                                    //on dessinr chaque partie de la machine case par case
                                    tabIP[x + xx][y + yy].setPartie(xx, m.getLargeur(), yy, m.getHauteur());
                                    tabIP[x + xx][y + yy].setBackground(ico);
                                }
                            }
                        }
                    } else {
                        if (m instanceof Tapis tapis) {
                            //re set des dir pour les tapis
                            tapis.setDirection(tapis.getDirection());
                        }
                        tabIP[x][y].setBackground(ico);
                    }
                    //draw des items (forme et couleur)
                    Item current = m.getCurrent();

                    if (current instanceof ItemShape) {
                        tabIP[x][y].setShape((ItemShape) current);
                    } else if (current instanceof ItemColor itemCouleur) {
                        // affiche l'icône de couleur en front
                        tabIP[x][y].setFront(getIconeColor(itemCouleur.getColor()));
                    } else {
                        tabIP[x][y].supprimeShape();
                    }


                }
                //gisement de forme ou couleur
                Item gisement = c.getGisement();
                if (gisement != null) {
                    if (gisement instanceof ItemShape) {
                        tabIP[x][y].setShape((ItemShape) gisement);
                    } else if (gisement instanceof ItemColor itemCouleur) {
                        tabIP[x][y].setFront(getIconeColor(itemCouleur.getColor())); // ← affiche la couleur du gisement
                    }

                }
                //affichage de la bar de progression
                Niveau n = jeu.getNiveauActuel();
                if (n != null) {
                    int progression = n.getProgression() * 100 / n.getObjectif();//ratio sur 100
                    barProgression.setValue(progression);
                    barProgression.setString("Niveau " + (jeu.getNumeroNiveau() + 1) + " — " + progression + "%" + " | Objectif : " + n.getObjectif());

                    mettreAJourNiveauOverlay(n);
                }
            }
        }
        grilleIP.repaint();
    }

    @Override
    public void update(Observable o, Object arg) {

        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                        if (arg instanceof Niveau n) {
                            afficherIntroNiveau(n, jeu.getNumeroNiveau());
                        }
                    }
                });


    }
}
