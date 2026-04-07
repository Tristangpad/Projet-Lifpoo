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

import modele.item.Item;
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

    private Image icoTapisHaut, icoTapisBas, icoTapisGauche, icoTapisDroite;
    private Image icoTapisHautDroite, icoTapisBasDroite, icoTapisHautGauche, icoTapisBasGauche;
    private Image icoTapisDroiteHaut, icoTapisDroiteBas, icoTapisGaucheHaut, icoTapisGaucheBas;
    private Image icoPoubelle;
    private Image icoMineHaut, icoMineBas, icoMineGauche, icoMineDroite;
    private Image icoRotaterHaut, icoRotaterBas, icoRotaterGauche, icoRotaterDroite;
    private Image icoCutter, icoZoneDepot;

    private JComponent grilleIP;
    private JPanel conteneurCamera;
    private boolean mousePressed = false;
    private ImagePanel[][] tabIP;

    private JPanel menuOverlay;
    private GridBagConstraints contrainteMenu;
    private JPanel introNiveau;

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

        // NOUVEAU : J'ai supprimé appliquerZoom() d'ici, il est maintenant
        // géré par le ComponentListener plus bas pour s'activer à la bonne taille d'écran !
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
        icoTapisHaut   = new ImageIcon("./data/sprites/buildings/belt_top.png").getImage();
        icoTapisBas    = rotateIcon(icoTapisHaut, 180);
        icoTapisGauche = rotateIcon(icoTapisHaut, 270);
        icoTapisDroite = rotateIcon(icoTapisHaut, 90);

        icoTapisHautDroite  = new ImageIcon("./data/sprites/buildings/belt_right.png").getImage();
        icoTapisHautGauche  = new ImageIcon("./data/sprites/buildings/belt_left.png").getImage();
        icoTapisBasDroite   = rotateIcon(icoTapisHautGauche, 180);
        icoTapisBasGauche   = rotateIcon(icoTapisHautDroite, 180);
        icoTapisDroiteHaut  = rotateIcon(icoTapisHautGauche, 90);
        icoTapisDroiteBas   = rotateIcon(icoTapisHautDroite, 90);
        icoTapisGaucheHaut  = rotateIcon(icoTapisHautDroite, 270);
        icoTapisGaucheBas   = rotateIcon(icoTapisHautGauche, 270);

        icoPoubelle  = new ImageIcon("./data/sprites/buildings/trash.png").getImage();

        icoMineHaut   = new ImageIcon("./data/sprites/buildings/miner.png").getImage();
        icoMineBas    = rotateIcon(icoMineHaut, 180);
        icoMineGauche = rotateIcon(icoMineHaut, 270);
        icoMineDroite = rotateIcon(icoMineHaut, 90);

        icoRotaterHaut   = new ImageIcon("./data/sprites/buildings/rotater.png").getImage();
        icoRotaterBas    = rotateIcon(icoRotaterHaut, 180);
        icoRotaterGauche = rotateIcon(icoRotaterHaut, 270);
        icoRotaterDroite = rotateIcon(icoRotaterHaut, 90);

        icoCutter    = new ImageIcon("./data/sprites/buildings/cutter.png").getImage();
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
        if (m instanceof Mine mine) {
            return switch (mine.getDirection()) {
                case North -> icoMineHaut;
                case South -> icoMineBas;
                case West  -> icoMineGauche;
                case East  -> icoMineDroite;
            };
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
        if (m instanceof Cutter)    return icoCutter;
        if (m instanceof ZoneDepot) return icoZoneDepot;
        return null;
    }

    public void afficherIntroNiveau(Niveau n, int numNiveau) {
        if (introNiveau != null) {
            menuOverlay.remove(introNiveau);
        }
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
        contrainteMenu.anchor = GridBagConstraints.SOUTH;
        contrainteMenu.weighty = 1.0;
        contrainteMenu.fill = GridBagConstraints.HORIZONTAL;
        contrainteMenu.ipadx = 10;
        contrainteMenu.ipady = 10;
        contrainteMenu.insets = new Insets(0, 0, 50, 0);

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

        menu.getBCutter().addActionListener(e -> jeu.setMachineChoisie(new Cutter()));
        menu.getBCutter().setFocusable(false);

        setFocusable(true);
        requestFocusInWindow();

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

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "haut");
        am.put("haut", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                camera.deplacer(0, -1, plateauSizeX, plateauSizeY, viewSizeX, viewSizeY);
                mettreAJourAffichage();
            }
        });

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

        for (int vx = 0; vx < viewSizeX; vx++) {
            for (int vy = 0; vy < viewSizeY; vy++) {
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
                                    tabIP[vvx][vvy].repaint(); // NOUVEAU : Force l'affichage !
                                }
                            }
                        }
                    } else {
                        tabIP[vx][vy].setBackground(ico);
                        tabIP[vx][vy].repaint(); // NOUVEAU : Force l'affichage !
                    }

                    Item current = m.getCurrent();
                    if (current instanceof ItemShape) tabIP[vx][vy].setShape((ItemShape) current);
                    else                              tabIP[vx][vy].supprimeShape();
                }

                Item gisement = c.getGisement();
                if (gisement instanceof ItemShape) tabIP[vx][vy].setShape((ItemShape) gisement);

                tabIP[vx][vy].repaint(); // NOUVEAU : Force l'affichage général de la case !
            }
        }

        Niveau n = jeu.getNiveauActuel();
        if (n != null) {
            int progression = n.getProgression() * 100 / n.getObjectif();
            barProgression.setValue(progression);
            barProgression.setString("Niveau " + (jeu.getNumeroNiveau() + 1)
                    + " — " + progression + "% | Objectif : " + n.getObjectif());
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