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
    private final JButton BRotater;
    private final JButton BCutter;



    public Menu() {


        //importation des icônes pour le menu
        ImageIcon icoTapisDroite = new ImageIcon(new ImageIcon("./data/sprites/buildings/belt_top.png").getImage().getScaledInstance(82, 82, Image.SCALE_SMOOTH));
        ImageIcon icoMine = new ImageIcon(new ImageIcon("./data/sprites/buildings/miner.png").getImage().getScaledInstance(82, 82, Image.SCALE_SMOOTH));;
        ImageIcon icoPoubelle = new ImageIcon(new ImageIcon("./data/sprites/buildings/trash.png").getImage().getScaledInstance(82, 82, Image.SCALE_SMOOTH));;
        ImageIcon icoRotater = new ImageIcon(new ImageIcon("./data/sprites/buildings/rotater.png").getImage().getScaledInstance(82, 82, Image.SCALE_SMOOTH));;

        ImageIcon icoCutter = new ImageIcon(new ImageIcon("./data/sprites/buildings/cutter.png").getImage().getScaledInstance(82*2, 82, Image.SCALE_SMOOTH));;


        BTapis = new JButton("Tapis",icoTapisDroite);
        BMine = new JButton("Mine",icoMine);
        BPoubelle = new JButton("Poubelle",icoPoubelle);
        BRotater = new JButton("Rotater",icoRotater);

        BCutter = new JButton("Cutter",icoCutter);

        configBouton(BTapis);
        configBouton(BMine);
        configBouton(BPoubelle);
        configBouton(BRotater);

        configBouton(BCutter);

        add(BTapis);
        add(BMine);
        add(BPoubelle);
        add(BRotater);

        add(BCutter);

    }

    public JButton getBTapis() { return BTapis; }
    public JButton getBMine() { return BMine; }
    public JButton getBPoubelle() { return BPoubelle; }
    public JButton getBRotater() { return BRotater; }
    public JButton getBCutter() { return BCutter;}

    private void configBouton(JButton b) {
        b.setVerticalTextPosition(SwingConstants.TOP);
        b.setHorizontalTextPosition(SwingConstants.CENTER);

        Dimension tailleBouton = new Dimension(140, 140);
        b.setPreferredSize(tailleBouton);
    }
}

