package vuecontroleur;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

public class Menu extends JPanel {

    private final JButton BMine;
    private final JButton BPoubelle;
    private final JButton BTapis;
    private final JButton BRotaterG;
    private final JButton BRotaterD;

    private final JButton BCutter;
    private final JButton BPainter;
    private final JButton BStacker;
    private final JButton BMixer;



    public Menu() {

        //mise en forme du menue
        setBackground(new Color(100, 100, 100, 200));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 8));

        //importation des icônes pour le menu
        ImageIcon icoTapisDroite = new ImageIcon(new ImageIcon("./data/sprites/buildings/belt_top.png").getImage().getScaledInstance(82, 82, Image.SCALE_SMOOTH));
        ImageIcon icoMine = new ImageIcon(new ImageIcon("./data/sprites/buildings/miner.png").getImage().getScaledInstance(82, 82, Image.SCALE_SMOOTH));
        ImageIcon icoPoubelle = new ImageIcon(new ImageIcon("./data/sprites/buildings/trash.png").getImage().getScaledInstance(82, 82, Image.SCALE_SMOOTH));
        ImageIcon icoRotaterD = new ImageIcon(new ImageIcon("./data/sprites/buildings/rotater.png").getImage().getScaledInstance(82, 82, Image.SCALE_SMOOTH));
        ImageIcon icoRotaterG = new ImageIcon(new ImageIcon("./data/sprites/buildings/rotater-ccw.png").getImage().getScaledInstance(82, 82, Image.SCALE_SMOOTH));

        ImageIcon icoCutter = new ImageIcon(new ImageIcon("./data/sprites/buildings/cutter.png").getImage().getScaledInstance(82*2, 82, Image.SCALE_SMOOTH));
        ImageIcon icoPainter = new ImageIcon(new ImageIcon("./data/sprites/buildings/painter.png").getImage().getScaledInstance(82*2, 82, Image.SCALE_SMOOTH));
        ImageIcon icoStacker = new ImageIcon(new ImageIcon("./data/sprites/buildings/stacker.png").getImage().getScaledInstance(82*2, 82, Image.SCALE_SMOOTH));
        ImageIcon icoMixer = new ImageIcon(new ImageIcon("./data/sprites/buildings/mixer.png").getImage().getScaledInstance(82*2, 82, Image.SCALE_SMOOTH));

        //création des boutons
        BTapis = new JButton("Tapis",icoTapisDroite);
        BMine = new JButton("Mine",icoMine);
        BPoubelle = new JButton("Poubelle",icoPoubelle);
        BRotaterG = new JButton("Rotater g",icoRotaterG);
        BRotaterD = new JButton("Rotater d",icoRotaterD);

        BCutter = new JButton("Cutter",icoCutter);
        BPainter = new JButton("Painter",icoPainter);
        BStacker = new JButton("Stacker",icoStacker);
        BMixer = new JButton("Mixer",icoMixer);

        //config des boutons
        configBouton(BTapis);
        configBouton(BMine);
        configBouton(BPoubelle);
        configBouton(BRotaterG);
        configBouton(BRotaterD);

        configBouton(BCutter);
        configBouton(BPainter);
        configBouton(BStacker);
        configBouton(BMixer);

        //ajout des boutons au menue
        add(BTapis);
        add(BMine);
        add(BPoubelle);
        add(BRotaterG);
        add(BRotaterD);

        add(BCutter);
        add(BPainter);
        add(BStacker);
        add(BMixer);
    }

    //getters
    public JButton getBTapis() { return BTapis; }
    public JButton getBMine() { return BMine; }
    public JButton getBPoubelle() { return BPoubelle; }
    public JButton getBRotaterG() { return BRotaterG; }
    public JButton getBRotaterD() { return BRotaterD; }
    public JButton getBCutter() { return BCutter;}
    public JButton getBPainter() { return BPainter;}
    public JButton getBStacker() { return BStacker;}
    public JButton getBMixer() { return BMixer;}

    //definits l'apparance de chaque boutton dans le menue
    private void configBouton(JButton b) {
        b.setVerticalTextPosition(SwingConstants.TOP);
        b.setHorizontalTextPosition(SwingConstants.CENTER);

        //style sombre
        b.setBackground(new Color(90, 90, 90));
        b.setForeground(new Color(220, 220, 220));
        b.setFont(new Font("Arial", Font.BOLD, 11));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        b.setFocusPainted(false);
        b.setContentAreaFilled(true);
        b.setOpaque(true);

        //effet d'hover
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(new Color(70, 70, 70)); //plus clair si survolé
                b.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)
                ));
            }
            public void mouseExited(MouseEvent e) {
                b.setBackground(new Color(45, 45, 45));
                b.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)
                ));
            }
        });

        Dimension tailleBouton = new Dimension(100, 100);
        b.setPreferredSize(tailleBouton);
    }
}

