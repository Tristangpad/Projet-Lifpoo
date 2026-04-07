package vuecontroleur;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import java.awt.image.BufferedImage;

import modele.item.Couleur;
import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;
import modele.jeu.Jeu;
import modele.plateau.*;

public class VueControleur extends JFrame implements Observer {

    private Plateau plateau;
    private Jeu jeu;
    private final int plateauSizeX;
    private final int plateauSizeY;
    private Camera camera = new Camera();

    private int viewSizeX;
    private int viewSizeY;

    private JProgressBar barProgression;
    private Menu menu;

    // Icônes tapis droits
    private Image icoTapisHaut, icoTapisBas, icoTapisGauche, icoTapisDroite;
    // Icônes tapis courbés
    private Image icoTapisHautDroite, icoTapisBasDroite, icoTapisHautGauche, icoTapisBasGauche;
    private Image icoTapisDroiteHaut, icoTapisDroiteBas, icoTapisGaucheHaut, icoTapisGaucheBas;
    // Couleurs
    private Image icoRouge, icoBleu, icoCyan, icoVert, icoViolet, icoGris, icoBlanc, icoJaune;
    // Machines simples
    private Image icoPoubelle;
    private Image icoMineHaut, icoMineBas, icoMineGauche, icoMineDroite;
    private Image icoRotaterHaut, icoRotaterBas, icoRotaterGauche, icoRotaterDroite;
    private Image icoRotaterInvHaut, icoRotaterInvBas, icoRotaterInvGauche, icoRotaterInvDroite;
    // Machines avec extension
    private Image icoCutter, icoPainter, icoZoneDepot, icoStacker, icoMixer;

    private JComponent grilleIP;
    private JPanel conteneurCamera;
    private boolean mousePressed = false;
    private ImagePanel[][] tabIP;

    private JPanel menuOverlay;
    private GridBagConstraints contrainteMenu;
    private JPanel introNiveau;
    private JPanel niveauOverlay;
    private GridBagConstraints contrainteNiveauOverlay;
    private ImagePanel niveauOverlayForme;

    private int casePX = -1;
    private int casePY = -1;

    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        plateauSizeX = plateau.SIZE_X;
        plateauSizeY = plateau.SIZE_Y;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        afficherIntroNiveau(jeu.getNiveauActuel(), jeu.getNumeroNiveau());

        plateau.addObserver(this);
        mettreAJourAffichage();
    }

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
        // Tapis droits
        icoTapisHaut   = new ImageIcon("./data/sprites/buildings/belt_top.png").getImage();
        icoTapisBas    = rotateIcon(icoTapisHaut, 180);
        icoTapisGauche = rotateIcon(icoTapisHaut, 270);
        icoTapisDroite = rotateIcon(icoTapisHaut, 90);
        // Tapis courbés
        icoTapisHautDroite = new ImageIcon("./data/sprites/buildings/belt_right.png").getImage();
        icoTapisHautGauche = new ImageIcon("./data/sprites/buildings/belt_left.png").getImage();
        icoTapisBasDroite  = rotateIcon(icoTapisHautGauche, 180);
        icoTapisBasGauche  = rotateIcon(icoTapisHautDroite, 180);
        icoTapisDroiteHaut = rotateIcon(icoTapisHautGauche, 90);
        icoTapisDroiteBas  = rotateIcon(icoTapisHautDroite, 90);
        icoTapisGaucheHaut = rotateIcon(icoTapisHautDroite, 270);
        icoTapisGaucheBas  = rotateIcon(icoTapisHautGauche, 270);
        // Couleurs
        icoRouge  = new ImageIcon("./data/sprites/colors/red.png").getImage();
        icoBleu   = new ImageIcon("./data/sprites/colors/blue.png").getImage();
        icoCyan   = new ImageIcon("./data/sprites/colors/cyan.png").getImage();
        icoVert   = new ImageIcon("./data/sprites/colors/green.png").getImage();
        icoViolet = new ImageIcon("./data/sprites/colors/purple.png").getImage();
        icoGris   = new ImageIcon("./data/sprites/colors/uncolored.png").getImage();
        icoBlanc  = new ImageIcon("./data/sprites/colors/white.png").getImage();
        icoJaune  = new ImageIcon("./data/sprites/colors/yellow.png").getImage();
        // Machines
        icoPoubelle      = new ImageIcon("./data/sprites/buildings/trash.png").getImage();
        icoMineHaut      = new ImageIcon("./data/sprites/buildings/miner.png").getImage();
        icoMineBas       = rotateIcon(icoMineHaut, 180);
        icoMineGauche    = rotateIcon(icoMineHaut, 270);
        icoMineDroite    = rotateIcon(icoMineHaut, 90);
        icoRotaterHaut   = new ImageIcon("./data/sprites/buildings/rotater.png").getImage();
        icoRotaterBas    = rotateIcon(icoRotaterHaut, 180);
        icoRotaterGauche = rotateIcon(icoRotaterHaut, 270);
        icoRotaterDroite = rotateIcon(icoRotaterHaut, 90);
        icoRotaterInvHaut   = new ImageIcon("./data/sprites/buildings/rotater-ccw.png").getImage();
        icoRotaterInvBas    = rotateIcon(icoRotaterInvHaut, 180);
        icoRotaterInvGauche = rotateIcon(icoRotaterInvHaut, 270);
        icoRotaterInvDroite = rotateIcon(icoRotaterInvHaut, 90);
        // Machines avec extension
        icoCutter    = new ImageIcon("./data/sprites/buildings/cutter.png").getImage();
        icoPainter   = new ImageIcon("./data/sprites/buildings/painter.png").getImage();
        icoStacker   = new ImageIcon("./data/sprites/buildings/stacker.png").getImage();
        icoMixer     = new ImageIcon("./data/sprites/buildings/mixer.png").getImage();
        icoZoneDepot = new ImageIcon("./data/sprites/buildings/hub.png").getImage();
    }

    private void appliquerZoom() {
        int px = camera.getPxCase();

        int winWidth  = conteneurCamera.getWidth()  > 0 ? conteneurCamera.getWidth()  : 1000;
        int winHeight = conteneurCamera.getHeight() > 0 ? conteneurCamera.getHeight() : 800;

        viewSizeX = Math.min(winWidth  / px, plateauSizeX);
        viewSizeY = Math.min(winHeight / px, plateauSizeY);

        construireGrille();
        mettreAJourAffichage();
    }

    private void construireGrille() {
        grilleIP.removeAll();
        ((JPanel) grilleIP).setLayout(new GridLayout(viewSizeY, viewSizeX));

        int px = camera.getPxCase();
        grilleIP.setPreferredSize(new Dimension(viewSizeX * px, viewSizeY * px));

        tabIP = new ImagePanel[viewSizeX][viewSizeY];

        for (int y = 0; y < viewSizeY; y++) {
            for (int x = 0; x < viewSizeX; x++) {
                ImagePanel iP = new ImagePanel();
                iP.setBackground(new Color(220, 220, 220));
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
                            if (SwingUtilities.isLeftMouseButton(e)) {
                                if (casePX != -1 && casePY != -1) {
                                    Direction dir = jeu.calculLiaisonTapis(casePX, casePY, ppx, ppy);
                                    jeu.setDirectionMachine(dir);
                                }
                                casePX = ppx;
                                casePY = ppy;
                                jeu.slide(ppx, ppy);
                            } else if (SwingUtilities.isRightMouseButton(e)) {
                                jeu.suppMachineJeu(ppx, ppy);
                            }
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
        return switch (c) {
            case Red    -> icoRouge;
            case White  -> icoBlanc;
            case Blue   -> icoBleu;
            case Cyan   -> icoCyan;
            case Green  -> icoVert;
            case Purple -> icoViolet;
            case Yellow -> icoJaune;
            default     -> icoGris;
        };
    }

    private Image getIconeMachine(Machine m) {
        if (m instanceof Tapis t) {
            Direction sortie = t.getDirection();
            Direction entree = t.getDirInput();
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
        if (m instanceof Mine mine) return switch (mine.getDirection()) {
            case North -> icoMineHaut;
            case South -> icoMineBas;
            case West  -> icoMineGauche;
            case East  -> icoMineDroite;
        };
        if (m instanceof Poubelle) return icoPoubelle;
        if (m instanceof RotaterInverser r) return switch (r.getDirection()) {
            case North -> icoRotaterInvHaut;
            case South -> icoRotaterInvBas;
            case West  -> icoRotaterInvGauche;
            case East  -> icoRotaterInvDroite;
        };
        if (m instanceof Rotater r) return switch (r.getDirection()) {
            case North -> icoRotaterHaut;
            case South -> icoRotaterBas;
            case West  -> icoRotaterGauche;
            case East  -> icoRotaterDroite;
        };
        if (m instanceof Cutter)    return icoCutter;

        // --- CORRECTION AMBIGUÏTÉ : On précise que Painter vient de ton modèle ! ---
        if (m instanceof modele.plateau.Painter)   return icoPainter;

        if (m instanceof Stacker)   return icoStacker;
        if (m instanceof Mixer)     return icoMixer;
        if (m instanceof ZoneDepot) return icoZoneDepot;
        return null;
    }

    public void afficherIntroNiveau(Niveau n, int numNiveau) {
        if (introNiveau != null) {
            menuOverlay.remove(introNiveau);
            introNiveau.setVisible(false);
        }
        menu.setVisible(false);

        introNiveau = new NiveauAfficher(n, numNiveau);

        GridBagConstraints contrainteNiveau = new GridBagConstraints();
        contrainteNiveau.gridx = 0;
        contrainteNiveau.gridy = 0;
        contrainteNiveau.weightx = 1;
        contrainteNiveau.weighty = 1;
        contrainteNiveau.fill = GridBagConstraints.BOTH;

        menuOverlay.add(introNiveau, contrainteNiveau);
        menuOverlay.revalidate();
        menuOverlay.repaint();
    }

    public void mettreAJourNiveauOverlay(Niveau n) {
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
        barProgression.setBackground(new Color(30, 30, 30));
        barProgression.setForeground(new Color(100, 180, 100));
        add(barProgression, BorderLayout.NORTH);

        grilleIP = new JPanel();
        grilleIP.setBackground(new Color(200, 200, 200));

        conteneurCamera = new JPanel(new GridBagLayout());
        conteneurCamera.setBackground(Color.DARK_GRAY);
        conteneurCamera.add(grilleIP);
        add(conteneurCamera, BorderLayout.CENTER);

        conteneurCamera.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                appliquerZoom();
            }
        });

        contrainteMenu = new GridBagConstraints();
        contrainteMenu.anchor = GridBagConstraints.SOUTH;
        contrainteMenu.weighty = 1.0;
        contrainteMenu.fill = GridBagConstraints.HORIZONTAL;
        contrainteMenu.ipadx = 10;
        contrainteMenu.ipady = 10;
        contrainteMenu.insets = new Insets(0, 20, 20, 20);
        menuOverlay.add(menu, contrainteMenu);
        menuOverlay.setVisible(true);

        menu.getBTapis().addActionListener(e -> jeu.setMachineChoisie(new Tapis()));
        menu.getBTapis().setFocusable(false);
        menu.getBMine().addActionListener(e -> jeu.setMachineChoisie(new Mine()));
        menu.getBMine().setFocusable(false);
        menu.getBPoubelle().addActionListener(e -> jeu.setMachineChoisie(new Poubelle()));
        menu.getBPoubelle().setFocusable(false);

        // --- CORRECTION SUPPRESSION : L'ancien bouton "getBRotater()" qui n'existe plus a été enlevé ici ---

        menu.getBRotaterD().addActionListener(e -> jeu.setMachineChoisie(new Rotater()));
        menu.getBRotaterD().setFocusable(false);
        menu.getBRotaterG().addActionListener(e -> jeu.setMachineChoisie(new RotaterInverser()));
        menu.getBRotaterG().setFocusable(false);
        menu.getBCutter().addActionListener(e -> jeu.setMachineChoisie(new Cutter()));
        menu.getBCutter().setFocusable(false);

        // --- CORRECTION AMBIGUÏTÉ : On précise que Painter vient de ton modèle ! ---
        menu.getBPainter().addActionListener(e -> jeu.setMachineChoisie(new modele.plateau.Painter()));
        menu.getBPainter().setFocusable(false);

        menu.getBStacker().addActionListener(e -> jeu.setMachineChoisie(new Stacker()));
        menu.getBStacker().setFocusable(false);
        menu.getBMixer().addActionListener(e -> jeu.setMachineChoisie(new Mixer()));
        menu.getBMixer().setFocusable(false);

        niveauOverlay = new JPanel();
        niveauOverlay.setLayout(new BoxLayout(niveauOverlay, BoxLayout.Y_AXIS));
        niveauOverlay.setBackground(new Color(0, 0, 0, 150));
        niveauOverlay.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel labelObjectif = new JLabel("Objectif :");
        labelObjectif.setForeground(Color.WHITE);
        labelObjectif.setAlignmentX(Component.CENTER_ALIGNMENT);

        niveauOverlayForme = new ImagePanel();
        niveauOverlayForme.setPreferredSize(new Dimension(80, 80));
        niveauOverlayForme.setMaximumSize(new Dimension(80, 80));
        niveauOverlayForme.setAlignmentX(Component.CENTER_ALIGNMENT);

        niveauOverlay.add(labelObjectif);
        niveauOverlay.add(niveauOverlayForme);

        contrainteNiveauOverlay = new GridBagConstraints();
        contrainteNiveauOverlay.anchor = GridBagConstraints.NORTHEAST;
        contrainteNiveauOverlay.weightx = 1.0;
        contrainteNiveauOverlay.weighty = 1.0;
        contrainteNiveauOverlay.insets = new Insets(10, 0, 0, 10);
        menuOverlay.add(niveauOverlay, contrainteNiveauOverlay);

        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,     0), "gauche");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,    0), "droite");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,       0), "haut");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,     0), "bas");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD,      0), "zoomIn");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS,   0), "zoomIn");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, 0), "zoomOut");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,    0), "zoomOut");

        am.put("gauche",  new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) {
            camera.deplacer(-1, 0, plateauSizeX, plateauSizeY, viewSizeX, viewSizeY); mettreAJourAffichage(); }});
        am.put("droite",  new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) {
            camera.deplacer( 1, 0, plateauSizeX, plateauSizeY, viewSizeX, viewSizeY); mettreAJourAffichage(); }});
        am.put("haut",    new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) {
            camera.deplacer(0, -1, plateauSizeX, plateauSizeY, viewSizeX, viewSizeY); mettreAJourAffichage(); }});
        am.put("bas",     new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) {
            camera.deplacer(0,  1, plateauSizeX, plateauSizeY, viewSizeX, viewSizeY); mettreAJourAffichage(); }});

        AbstractAction zoomIn = new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) {
            camera.zoomer(); appliquerZoom(); }};
        AbstractAction zoomOut = new AbstractAction() { public void actionPerformed(java.awt.event.ActionEvent e) {
            camera.dezoomer(); appliquerZoom(); }};
        am.put("zoomIn",  zoomIn);
        am.put("zoomOut", zoomOut);
    }

    private void mettreAJourAffichage() {
        if (tabIP == null) return;

        // Reset
        for (int x = 0; x < viewSizeX; x++) {
            for (int y = 0; y < viewSizeY; y++) {
                tabIP[x][y].setBackground((Image) null);
                tabIP[x][y].setFront(null);
                tabIP[x][y].setShape(null);
                tabIP[x][y].resetPartie();
                tabIP[x][y].repaint();
            }
        }

        for (int vx = 0; vx < viewSizeX; vx++) {
            for (int vy = 0; viewSizeY > vy; vy++) {
                int px = vx + camera.offsetX;
                int py = vy + camera.offsetY;

                if (px >= plateauSizeX || py >= plateauSizeY) continue;

                Case c = plateau.getCases()[px][py];
                Machine m = c.getMachine();

                if (c.isExtention()) continue;

                if (m != null) {
                    Image ico = getIconeMachine(m);

                    if (m.getLargeur() > 1 || m.getHauteur() > 1) {
                        for (int dx = 0; dx < m.getLargeur(); dx++) {
                            for (int dy = 0; dy < m.getHauteur(); dy++) {
                                int vvx = vx + dx, vvy = vy + dy;
                                if (vvx < viewSizeX && vvy < viewSizeY) {
                                    tabIP[vvx][vvy].setPartie(dx, m.getLargeur(), dy, m.getHauteur());
                                    tabIP[vvx][vvy].setBackground(ico);
                                    tabIP[vvx][vvy].repaint();
                                }
                            }
                        }
                    } else {
                        tabIP[vx][vy].setBackground(ico);
                        tabIP[vx][vy].repaint();
                    }

                    Item current = m.getCurrent();
                    if (current instanceof ItemShape s)       tabIP[vx][vy].setShape(s);
                    else if (current instanceof ItemColor cl) tabIP[vx][vy].setFront(getIconeColor(cl.getColor()));
                    else                                      tabIP[vx][vy].supprimeShape();
                }

                Item gisement = c.getGisement();
                if (gisement instanceof ItemShape s)       tabIP[vx][vy].setShape(s);
                else if (gisement instanceof ItemColor cl) tabIP[vx][vy].setFront(getIconeColor(cl.getColor()));

                tabIP[vx][vy].repaint();
            }
        }

        Niveau n = jeu.getNiveauActuel();
        if (n != null) {
            int progression = n.getProgression() * 100 / n.getObjectif();
            barProgression.setValue(progression);
            barProgression.setString("Niveau " + (jeu.getNumeroNiveau() + 1)
                    + " — " + progression + "% | Objectif : " + n.getObjectif());
            mettreAJourNiveauOverlay(n);
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