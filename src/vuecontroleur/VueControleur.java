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

import vuecontroleur.Menu;
import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;
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
    private Image icoPoubelle;
    private Image icoMine;
    private Image icoCutter;
    private Image icoZoneDepot;
    private Image icoRotater;

    private JComponent grilleIP;
    private boolean mousePressed = false; // permet de mémoriser l'état de la souris
    private ImagePanel[][] tabIP; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône background et front, suivant ce qui est présent dans le modèle)

    private JPanel menuOverlay;
    private GridBagConstraints contrainteMenu;


    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        sizeX = plateau.SIZE_X;
        sizeY = plateau.SIZE_Y;
        frame = new JFrame();

        chargerLesIcones();
        placerLesComposantsGraphiques();

        plateau.addObserver(this);

        mettreAJourAffichage();
    }

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

        icoTapisHaut = new ImageIcon("./data/sprites/buildings/belt_top.png").getImage();
        icoTapisBas = rotateIcon(icoTapisHaut,180);
        icoTapisGauche = rotateIcon(icoTapisHaut,270);
        icoTapisDroite = rotateIcon(icoTapisHaut,90);

        icoRouge = new ImageIcon("./data/sprites/colors/blue.png").getImage();
        icoPoubelle = new ImageIcon("./data/sprites/buildings/trash.png").getImage();
        icoMine = new ImageIcon("./data/sprites/buildings/miner.png").getImage();
        icoRotater = new ImageIcon("./data/sprites/buildings/rotater.png").getImage();

        //machine avec extention
        icoCutter = new ImageIcon("./data/sprites/buildings/cutter.png").getImage();
        icoZoneDepot = new ImageIcon("./data/sprites/buildings/hub.png").getImage();

    }

    private Image getIconeMachine(Machine m) {
        if (m instanceof Tapis) {
            switch (((Tapis) m).getDirection()) {
                case North -> {return icoTapisHaut;}
                case South -> {return icoTapisBas;}
                case West -> {return icoTapisGauche;}
                case East -> {return icoTapisDroite;}
            }
        }
        if (m instanceof Mine)      return icoMine;
        if (m instanceof Poubelle)  return icoPoubelle;
        if (m instanceof Rotater)   return icoRotater;

        if (m instanceof Cutter)    return icoCutter;
        if (m instanceof ZoneDepot) return icoZoneDepot;
        return null;
    }

    private void placerLesComposantsGraphiques() {
        setTitle("ShapeCraft");
        setResizable(true);
        setSize(sizeX * pxCase, sizeX * pxCase);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        menu = new Menu();
        menuOverlay = (JPanel) getGlassPane();
        menuOverlay.setLayout(new GridBagLayout());

        barProgression = new JProgressBar(0,100);

        grilleIP = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille
        tabIP = new ImagePanel[sizeX][sizeY];

        //mise en forme du menue de choix de machine
        contrainteMenu = new GridBagConstraints();
        contrainteMenu.anchor = GridBagConstraints.SOUTH;
        contrainteMenu.weighty = 1.0;
        contrainteMenu.fill = GridBagConstraints.HORIZONTAL;
        contrainteMenu.ipadx = 10;
        contrainteMenu.ipady = 10;
        contrainteMenu.insets = new Insets(0, 0, 50, 0);

        //mise en forme de la bar de progression des niveaux
        barProgression.setStringPainted(true);
        barProgression.setPreferredSize(new Dimension(sizeX * pxCase, 30));
        add(barProgression, BorderLayout.NORTH);


        //ajout du menue avec sa mise en forme a l'overlay générale du jeux qui permet une superposition entre les 2
        menuOverlay.add(menu, contrainteMenu);
        menuOverlay.setVisible(true);

        //check le choix de l'utilisateur pour savoir si il change de machine a placer
        menu.getBTapis().addActionListener(e -> jeu.setMachineChoisie(new Tapis()));
        menu.getBMine().addActionListener(e -> jeu.setMachineChoisie(new Mine()));
        menu.getBPoubelle().addActionListener(e -> jeu.setMachineChoisie(new Poubelle()));
        menu.getBRotater().addActionListener(e -> jeu.setMachineChoisie(new Rotater()));

        menu.getBCutter().addActionListener(e -> jeu.setMachineChoisie(new Cutter()));



        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                ImagePanel iP = new ImagePanel();


                tabIP[x][y] = iP; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )


                final int xx = x; // permet de compiler la classe anonyme ci-dessous
                final int yy = y;
                // écouteur de clics
                iP.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            jeu.suppMachineJeu(xx,yy);
                        }
                        else {
                            mousePressed = false;
                            jeu.press(xx, yy);
                            System.out.println(xx + "-" + yy);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (mousePressed) {
                            jeu.slide(xx, yy);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        mousePressed = true;

                        jeu.rotateM(xx,yy);
                        jeu.press(xx, yy);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        mousePressed = false;

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

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                tabIP[x][y].setBackground((Image) null);
                tabIP[x][y].setFront(null);
                tabIP[x][y].setShape(null);
                tabIP[x][y].resetPartie();
            }
        }

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                Case c = plateau.getCases()[x][y];
                Machine m = c.getMachine();

                if (c.isExtention()){
                    continue;
                }

                if (m != null) {

                    Image ico = getIconeMachine(m);

                    if(m.getLargeur() > 1 || m.getHauteur() > 1)
                    {
                        for(int xx = 0;xx < m.getLargeur();xx++) {
                            for (int yy = 0; yy < m.getHauteur(); yy++) {
                                if (x + xx < sizeX && y + yy < sizeY) {
                                    tabIP[x + xx][y + yy].setPartie(xx, m.getLargeur(), yy, m.getHauteur());
                                    tabIP[x + xx][y + yy].setBackground(ico);
                                }
                            }
                        }
                    }else {
                        if(m instanceof Tapis tapis)
                        {
                            tapis.getDirection();
                        }
                        tabIP[x][y].setBackground(ico);
                    }

                    Item current = m.getCurrent();

                    if (current instanceof ItemShape) {
                        tabIP[x][y].setShape((ItemShape) current);
                    } else {
                        tabIP[x][y].supprimeShape();
                    }

                    if (current instanceof ItemColor) {
                        // tabIP[x][y].setFront(); TODO : placer l'icone des couleurs approprié
                    }


                }
                Item gisement = c.getGisement();
                if (gisement != null) {
                    if (gisement instanceof ItemShape) {
                        tabIP[x][y].setShape((ItemShape) gisement);
                    }
                }

                Niveau n = jeu.getNiveauActuel();
                if (n != null) {
                    int progression = n.getProgression() * 100 / n.getObjectif();
                    barProgression.setValue(progression);
                    barProgression.setString("Niveau " + (jeu.getNumeroNiveau() + 1) + " — " + progression + "%" + " | Objectif : " + n.getObjectif());
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
                    }
                }); 

    }
}
