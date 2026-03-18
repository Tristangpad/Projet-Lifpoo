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


    public Menu() {

        //importation des icônes pour le menu
        Image icoTapisDroite = new ImageIcon("./data/sprites/buildings/belt_top.png").getImage();
        Image icoMine = new ImageIcon("./data/sprites/buildings/miner.png").getImage();
        Image icoPoubelle = new ImageIcon("./data/sprites/buildings/trash.png").getImage();
        

        BTapis = new JButton("Tapis",icoTapisDroite);
        BMine = new JButton("Mine",icoMine);
        BPoubelle = new JButton("Poubelle",icoPoubelle);
        //coucou
        add(BTapis);
        add(BMine);
        add(BPoubelle);
    }

    public JButton getBTapis() { return BTapis; }
    public JButton getBMine() { return BMine; }
    public JButton getBPoubelle() { return BPoubelle; }
}

