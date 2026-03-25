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
    private final JButton BCutter;


    public Menu() {

        //importation des icônes pour le menu
        ImageIcon icoTapisDroite = new ImageIcon("./data/sprites/buildings/belt_top.png");
        ImageIcon icoMine = new ImageIcon("./data/sprites/buildings/miner.png");
        ImageIcon icoPoubelle = new ImageIcon("./data/sprites/buildings/trash.png");
        ImageIcon icoCutter = new ImageIcon("./data/sprites/buildings/cutter.png");

        BTapis = new JButton("Tapis",icoTapisDroite);
        BMine = new JButton("Mine",icoMine);
        BPoubelle = new JButton("Poubelle",icoPoubelle);
        BCutter = new JButton("Cutter",icoCutter);

        add(BTapis);
        add(BMine);
        add(BPoubelle);
        add(BCutter);

    }

    public JButton getBTapis() { return BTapis; }
    public JButton getBMine() { return BMine; }
    public JButton getBPoubelle() { return BPoubelle; }
    public JButton getBCutter() { return BCutter;}
}

