package vuecontroleur;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

import java.awt.image.BufferedImage;

import modele.item.Couleur;
import modele.plateau.Painter;
import modele.plateau.Cutter;
import modele.item.Item;
import modele.item.ItemShape;
import modele.jeu.Jeu;
import modele.plateau.*;
import modele.plateau.Machine;

public class VueControleur extends JFrame implements Observer {

    private Plateau plateau;
    private Jeu jeu;
    private final int plateauSizeX;
    private final int plateauSizeY;
    private Camera camera = new Camera();

    private int viewSizeX;
    private int viewSizeY;

    //init de la bar de progression propre a JP
    private JProgressBar barProgression;
    private Menu menu;

    private Image icoTapisHaut, icoTapisBas, icoTapisGauche, icoTapisDroite;
    private Image icoTapisHautDroite, icoTapisBasDroite, icoTapisHautGauche, icoTapisBasGauche;
    private Image icoTapisDroiteHaut, icoTapisDroiteBas, icoTapisGaucheHaut, icoTapisGaucheBas;

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
    private Image icoMineHaut, icoMineBas, icoMineGauche, icoMineDroite;
    private Image icoRotaterHaut, icoRotaterBas, icoRotaterGauche, icoRotaterDroite;
    private Image icoCutter, icoZoneDepot;

    private Image icoMineHaut;
    private Image icoMineBas;
    private Image icoMineGauche;
    private Image icoMineDroite;

    //machine +++
    private Image icoCutter;
    private Image icoPainter;
    private Image icoZoneDepot;
    private Image icoStacker;
    private Image icoMixer;

    //version dans toutes les dir
    private Image icoRotaterHaut;
    private Image icoRotaterBas;
    private Image icoRotaterGauche;
    private Image icoRotaterDroite;

    private Image icoRotaterInvHaut;
    private Image icoRotaterInvBas;
    private Image icoRotaterInvGauche;
    private Image icoRotaterInvDroite;

    private Image icoTapisHautDroite;
    private Image icoTapisBasDroite;
    private Image icoTapisHautGauche;
    private Image icoTapisBasGauche;

    private Image icoTapisDroiteHaut;
    private Image icoTapisDroiteBas;
    private Image icoTapisGaucheHaut;
    private Image icoTapisGaucheBas;


    private JComponent grilleIP;
    private JPanel conteneurCamera;
    private boolean mousePressed = false;
    private ImagePanel[][] tabIP;

    //creation des differentes interfaces ajouter a la grille de base du jeux
    private JPanel menuOverlay;
    private GridBagConstraints contrainteMenu;
    private JPanel introNiveau;
    private GridBagConstraints contrainteNiveau;
    private JPanel niveauOverlay;
    private GridBagConstraints contrainteNiveauOverlay;
    private ImagePanel niveauOverlayForme;

    private int casePX = -1;
    //pour calculer les liaison pour les tapis pour les ajuster
    private int casePX = -1; //par defaut
    private int casePY = -1;

    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        plateauSizeX = plateau.SIZE_X;
        plateauSizeY = plateau.SIZE_Y;

        chargerLesIcones();//charge les icones
        placerLesComposantsGraphiques();//place les icones
        afficherIntroNiveau(jeu.getNiveauActuel(), jeu.getNumeroNiveau());//crée un pop up temporaire a chaque début de niveau

        plateau.addObserver(this);

        // NOUVEAU : J'ai supprimé appliquerZoom() d'ici, il est maintenant
        // géré par le ComponentListener plus bas pour s'activer à la bonne taille d'écran !
        mettreAJourAffichage();
    }
    /*
    Fonction créant une nouvelle image apartir de li'mage passr en paramètre en fonction de la rotation demander
     */
    public static Image rotateIcon(Image img, double angle) {
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage rotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = rotated.createGraphics();
        g2.rotate(Math.toRadians(angle), w / 2.0, h / 2.0);
        g2.drawImage(img, 0, 0, null);
        g2.dispose();
        return rotated;
    }

    private void chargerLesIcones() {
        //tapis droits puis tapis pour liaison en coude
        icoTapisHaut = new ImageIcon("./data/sprites/buildings/belt_top.png").getImage();
        icoTapisBas = rotateIcon(icoTapisHaut,180);
        icoTapisGauche = rotateIcon(icoTapisHaut,270);
        icoTapisDroite = rotateIcon(icoTapisHaut,90);

        icoTapisHautDroite  = new ImageIcon("./data/sprites/buildings/belt_right.png").getImage();
        icoTapisHautGauche  = new ImageIcon("./data/sprites/buildings/belt_left.png").getImage();
        icoTapisBasDroite   = rotateIcon(icoTapisHautGauche, 180);
        icoTapisBasGauche   = rotateIcon(icoTapisHautDroite, 180);
        icoTapisDroiteHaut  = rotateIcon(icoTapisHautGauche, 90);
        icoTapisDroiteBas   = rotateIcon(icoTapisHautDroite, 90);
        icoTapisGaucheHaut  = rotateIcon(icoTapisHautDroite, 270);
        icoTapisGaucheBas   = rotateIcon(icoTapisHautGauche, 270);

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

        icoMineHaut   = new ImageIcon("./data/sprites/buildings/miner.png").getImage();
        icoMineBas    = rotateIcon(icoMineHaut, 180);
        icoMineGauche = rotateIcon(icoMineHaut, 270);
        icoMineDroite = rotateIcon(icoMineHaut, 90);

        icoRotaterHaut   = new ImageIcon("./data/sprites/buildings/rotater.png").getImage();
        icoRotaterBas    = rotateIcon(icoRotaterHaut, 180);
        icoRotaterGauche = rotateIcon(icoRotaterHaut, 270);
        icoRotaterDroite = rotateIcon(icoRotaterHaut, 90);

        icoCutter    = new ImageIcon("./data/sprites/buildings/cutter.png").getImage();
        icoRotaterInvHaut = new ImageIcon("./data/sprites/buildings/rotater-ccw.png").getImage();
        icoRotaterInvBas = rotateIcon(icoRotaterInvHaut,180);
        icoRotaterInvGauche = rotateIcon(icoRotaterInvHaut,270);
        icoRotaterInvDroite = rotateIcon(icoRotaterInvBas,90);

        //chargement des machines avec extention
        icoCutter = new ImageIcon("./data/sprites/buildings/cutter.png").getImage();
        icoPainter = new ImageIcon("./data/sprites/buildings/painter.png").getImage();
        icoStacker = new ImageIcon("./data/sprites/buildings/stacker.png").getImage();
        icoMixer = new ImageIcon("./data/sprites/buildings/mixer.png").getImage();
        icoZoneDepot = new ImageIcon("./data/sprites/buildings/hub.png").getImage();
    }

    private void appliquerZoom() {
        int px = camera.getPxCase();

        int winWidth = conteneurCamera.getWidth() > 0 ? conteneurCamera.getWidth() : 1000;
        int winHeight = conteneurCamera.getHeight() > 0 ? conteneurCamera.getHeight() : 800;

        viewSizeX = winWidth / px;
        viewSizeY = winHeight / px;

        if (viewSizeX > plateauSizeX) viewSizeX = plateauSizeX;
        if (viewSizeY > plateauSizeY) viewSizeY = plateauSizeY;

        construireGrille();
        mettreAJourAffichage();
    }

    private void construireGrille() {
        grilleIP.removeAll();
        grilleIP.setLayout(new GridLayout(viewSizeY, viewSizeX));

        int px = camera.getPxCase();
        grilleIP.setPreferredSize(new Dimension(viewSizeX * px, viewSizeY * px));

        tabIP = new ImagePanel[viewSizeX][viewSizeY];

        for (int y = 0; y < viewSizeY; y++) {
            for (int x = 0; x < viewSizeX; x++) {
                ImagePanel iP = new ImagePanel();
                tabIP[x][y] = iP;

                final int vx = x;
                final int vy = y;

                iP.addMouseListener(new MouseAdapter() {
                    private int getPlateauX() { return vx + camera.offsetX; }
                    private int getPlateauY() { return vy + camera.offsetY; }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (mousePressed) {
                            int ppx = getPlateauX(), ppy = getPlateauY();
                            if (casePX != -1 && casePY != -1) {
                                Direction dir = jeu.calculLiaisonTapis(casePX, casePY, ppx, ppy);
                                jeu.setDirectionMachine(dir);
                            }
                            casePX = ppx;
                            casePY = ppy;
                            if (SwingUtilities.isLeftMouseButton(e))       jeu.slide(ppx, ppy);
                            else if (SwingUtilities.isRightMouseButton(e)) jeu.suppMachineJeu(ppx, ppy);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        mousePressed = true;
                        int ppx = getPlateauX(), ppy = getPlateauY();
                        if (SwingUtilities.isRightMouseButton(e)) {
                            jeu.suppMachineJeu(ppx, ppy);
                        } else if (SwingUtilities.isLeftMouseButton(e)) {
                            jeu.rotateM(ppx, ppy);
                            jeu.press(ppx, ppy);
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        mousePressed = false;
                        casePX = -1;
                        casePY = -1;
                    }
                });

                grilleIP.add(iP);
            }
        }

        grilleIP.revalidate();
        grilleIP.repaint();
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
        if (m instanceof Tapis t) {
            Direction sortie = t.getDirection();
            Direction entree = t.getDirInput();
        //pour chaque machine renvoie sont icones approprié
        //pour les tapis cel aest fait en fonction de leurs entré et sorite
        if (m instanceof Tapis) {
            Direction sortie = ((Tapis) m).getDirection();
            Direction entree = ((Tapis) m).getDirInput();

            //System.out.println(entree + "--->" + sortie); //debug
            if (entree == null || entree == sortie) {
                return switch (sortie) {
                    case North -> icoTapisHaut;
                    case South -> icoTapisBas;
                    case West  -> icoTapisGauche;
                    case East  -> icoTapisDroite;
                };
            }
            return switch (entree) {
                case North -> switch (sortie) {
                    case East  -> icoTapisHautDroite;
                    case West  -> icoTapisHautGauche;
                    default    -> icoTapisHaut;
                };
                case South -> switch (sortie) {
                    case East  -> icoTapisBasDroite;
                    case West  -> icoTapisBasGauche;
                    default    -> icoTapisBas;
                };
                case East -> switch (sortie) {
                    case North -> icoTapisDroiteHaut;
                    case South -> icoTapisDroiteBas;
                    default    -> icoTapisDroite;
                };
                case West -> switch (sortie) {
                    case North -> icoTapisGaucheHaut;
                    case South -> icoTapisGaucheBas;
                    default    -> icoTapisGauche;
                };
            };
        }
        if (m instanceof Mine mine) {
            return switch (mine.getDirection()) {
                case North -> icoMineHaut;
                case South -> icoMineBas;
                case West  -> icoMineGauche;
                case East  -> icoMineDroite;
            };
        }

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
        if (m instanceof Rotater r) {
            return switch (r.getDirection()) {
                case North -> icoRotaterHaut;
                case South -> icoRotaterBas;
                case West  -> icoRotaterGauche;
                case East  -> icoRotaterDroite;
            };
        }

        if (m instanceof Rotater)
            switch (((Rotater) m).getDirection()) {
                case North -> {return icoRotaterHaut;}
                case South -> {return icoRotaterBas;}
                case West -> {return icoRotaterGauche;}
                case East -> {return icoRotaterDroite;}
            }
        if (m instanceof RotaterInverser)
            switch (((RotaterInverser) m).getDirection()) {
                case North -> {return icoRotaterInvHaut;}
                case South -> {return icoRotaterInvBas;}
                case West -> {return icoRotaterInvGauche;}
                case East -> {return icoRotaterInvDroite;}
            }
        if (m instanceof Cutter)    return icoCutter;
        if (m instanceof Painter)    return icoPainter;
        if (m instanceof ZoneDepot) return icoZoneDepot;
        if (m instanceof Mixer)    return icoMixer;
        if (m instanceof Stacker)    return icoStacker;
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
        introNiveau = new NiveauAfficher(n, numNiveau);

        GridBagConstraints contrainteNiveau = new GridBagConstraints();
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
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        menu = new Menu();
        menuOverlay = (JPanel) getGlassPane();
        menuOverlay.setLayout(new GridBagLayout());

        barProgression = new JProgressBar(0, 100);
        barProgression.setStringPainted(true);
        add(barProgression, BorderLayout.NORTH);

        grilleIP = new JPanel();
        barProgression = new JProgressBar(0,100);//bar de progression

        grilleIP = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille
        tabIP = new ImagePanel[sizeX][sizeY];
        grilleIP.setBackground(new Color(200, 200, 200));

        conteneurCamera = new JPanel(new GridBagLayout());
        conteneurCamera.setBackground(Color.DARK_GRAY);
        conteneurCamera.add(grilleIP);

        add(conteneurCamera, BorderLayout.CENTER);

        // NOUVEAU : On dit à la caméra de s'adapter automatiquement dès que la fenêtre s'ouvre ou se redimensionne !
        conteneurCamera.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                appliquerZoom();
            }
        });

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

        menu.getBTapis().addActionListener(e -> jeu.setMachineChoisie(new Tapis()));
        menu.getBTapis().setFocusable(false);

        menu.getBMine().addActionListener(e -> jeu.setMachineChoisie(new Mine()));
        menu.getBMine().setFocusable(false);

        menu.getBPoubelle().addActionListener(e -> jeu.setMachineChoisie(new Poubelle()));
        menu.getBPoubelle().setFocusable(false);

        menu.getBRotater().addActionListener(e -> jeu.setMachineChoisie(new Rotater()));
        menu.getBRotater().setFocusable(false);
        menu.getBRotaterD().addActionListener(e -> jeu.setMachineChoisie(new Rotater()));
        menu.getBRotaterG().addActionListener(e -> jeu.setMachineChoisie(new RotaterInverser()));

        menu.getBCutter().addActionListener(e -> jeu.setMachineChoisie(new Cutter()));
        menu.getBCutter().setFocusable(false);
        menu.getBPainter().addActionListener(e -> jeu.setMachineChoisie(new Painter()));
        menu.getBStacker().addActionListener(e -> jeu.setMachineChoisie(new Stacker()));
        menu.getBMixer().addActionListener(e -> jeu.setMachineChoisie(new Mixer()));

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

        setFocusable(true);
        requestFocusInWindow();
        contrainteNiveauOverlay = new GridBagConstraints();
        contrainteNiveauOverlay.anchor = GridBagConstraints.NORTHEAST; // ← coin haut-droite
        contrainteNiveauOverlay.weightx = 1.0;
        contrainteNiveauOverlay.weighty = 1.0;
        contrainteNiveauOverlay.insets = new Insets(10, 0, 0, 10); // ← marge du bord

        menuOverlay.add(niveauOverlay, contrainteNiveauOverlay);

        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "gauche");
        am.put("gauche", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                camera.deplacer(-1, 0, plateauSizeX, plateauSizeY, viewSizeX, viewSizeY);
                mettreAJourAffichage();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "droite");
        am.put("droite", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                camera.deplacer(1, 0, plateauSizeX, plateauSizeY, viewSizeX, viewSizeY);
                mettreAJourAffichage();
            }
        });
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
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                if (casePX != -1 && casePY != -1) {
                                    System.out.println("dir changer");//debug
                                    Direction dir = jeu.calculLiaisonTapis(casePX, casePY, xx, yy);
                                    jeu.setDirectionMachine(dir);//rewrite la dir de la machine si il y a une liaison avec cette dernière
                                }
                                //permet en cas de slide d'actualiser les liaisons
                                casePX = xx;
                                casePY = yy;
                                jeu.slide(xx, yy);
                            }else if (SwingUtilities.isRightMouseButton(e)) {jeu.suppMachineJeu(xx, yy);}
                        }
                    }

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "haut");
        am.put("haut", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                camera.deplacer(0, -1, plateauSizeX, plateauSizeY, viewSizeX, viewSizeY);
                mettreAJourAffichage();
            }
        });
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

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "bas");
        am.put("bas", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                camera.deplacer(0, 1, plateauSizeX, plateauSizeY, viewSizeX, viewSizeY);
                mettreAJourAffichage();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, 0), "zoomInNum");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), "zoomIn");
        AbstractAction actionZoomIn = new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                camera.zoomer();
                appliquerZoom();
            }
        };
        am.put("zoomInNum", actionZoomIn);
        am.put("zoomIn", actionZoomIn);

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "zoomOutNum");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "zoomOut");
        AbstractAction actionZoomOut = new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                camera.dezoomer();
                appliquerZoom();
            }
        };
        am.put("zoomOutNum", actionZoomOut);
        am.put("zoomOut", actionZoomOut);
    }

    private void mettreAJourAffichage() {
        //redessine la grille vierge
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
        if (tabIP == null) return;

        // Reset
        for (int x = 0; x < viewSizeX; x++) {
            for (int y = 0; y < viewSizeY; y++) {
                tabIP[x][y].setBackground((Image) null);
                tabIP[x][y].setFront(null);
                tabIP[x][y].setShape(null);
                tabIP[x][y].resetPartie();
                tabIP[x][y].repaint(); // NOUVEAU : Force le nettoyage visuel !
            }
        }
        //dessine la grille actualiser
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

        for (int vx = 0; vx < viewSizeX; vx++) {
            for (int vy = 0; vy < viewSizeY; vy++) {
                int px = vx + camera.offsetX;
                int py = vy + camera.offsetY;

                if (px >= plateauSizeX || py >= plateauSizeY) continue;

                Case c = plateau.getCases()[px][py];
                Machine m = c.getMachine();

                //si la machine est une extention d'une autre alors ont skip la case
                if (c.isExtention()) {
                    continue;
                }
                if (c.isExtention()) continue;

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

                    if (m.getLargeur() > 1 || m.getHauteur() > 1) {
                        for (int dx = 0; dx < m.getLargeur(); dx++) {
                            for (int dy = 0; dy < m.getHauteur(); dy++) {
                                int vvx = vx + dx, vvy = vy + dy;
                                if (vvx < viewSizeX && vvy < viewSizeY) {
                                    tabIP[vvx][vvy].setPartie(dx, m.getLargeur(), dy, m.getHauteur());
                                    tabIP[vvx][vvy].setBackground(ico);
                                    tabIP[vvx][vvy].repaint(); // NOUVEAU : Force l'affichage !
                                }
                            }
                        }
                    } else {
                        tabIP[vx][vy].setBackground(ico);
                        tabIP[vx][vy].repaint(); // NOUVEAU : Force l'affichage !
                    } else {
                        if (m instanceof Tapis tapis) {
                            //re set des dir pour les tapis
                            tapis.setDirection(tapis.getDirection());
                        }
                        tabIP[x][y].setBackground(ico);
                    }
                    //draw des items (forme et couleur)
                    Item current = m.getCurrent();
                    if (current instanceof ItemShape) tabIP[vx][vy].setShape((ItemShape) current);
                    else                              tabIP[vx][vy].supprimeShape();
                }

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
                if (gisement instanceof ItemShape) tabIP[vx][vy].setShape((ItemShape) gisement);

                tabIP[vx][vy].repaint(); // NOUVEAU : Force l'affichage général de la case !
            }
        }
                if (gisement != null) {
                    if (gisement instanceof ItemShape) {
                        tabIP[x][y].setShape((ItemShape) gisement);
                    } else if (gisement instanceof ItemColor itemCouleur) {
                        tabIP[x][y].setFront(getIconeColor(itemCouleur.getColor())); // ← affiche la couleur du gisement
                    }

        Niveau n = jeu.getNiveauActuel();
        if (n != null) {
            int progression = n.getProgression() * 100 / n.getObjectif();
            barProgression.setValue(progression);
            barProgression.setString("Niveau " + (jeu.getNumeroNiveau() + 1)
                    + " — " + progression + "% | Objectif : " + n.getObjectif());
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
        SwingUtilities.invokeLater(() -> {
            mettreAJourAffichage();
            if (arg instanceof Niveau n) {
                afficherIntroNiveau(n, jeu.getNumeroNiveau());
            }
        });
    }
}