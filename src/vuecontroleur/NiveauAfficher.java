package vuecontroleur;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

import modele.item.ItemShape;
import modele.plateau.*;

public class NiveauAfficher extends JPanel {

    private JLabel labelNiveau;
    private JLabel labelObjectif;

    private JButton btnCommencer;

    private ImagePanel formeObjectif; // pour afficher la forme demandée

    public NiveauAfficher(Niveau n, int numeroNiveau) {
        setLayout(new GridBagLayout());
        setBackground(new Color(0, 0, 0, 150)); // fond semi-transparent
        setOpaque(true);

        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setBackground(new Color(255, 255, 255, 220));
        contenu.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // titre
        labelNiveau = new JLabel("Niveau " + (numeroNiveau + 1));
        labelNiveau.setFont(new Font("Arial", Font.BOLD, 28));
        labelNiveau.setAlignmentX(Component.CENTER_ALIGNMENT);

        // objectif texte
        labelObjectif = new JLabel("Produire " + n.getObjectif() + " x " + n.getFormeDemander());
        labelObjectif.setFont(new Font("Arial", Font.PLAIN, 16));
        labelObjectif.setAlignmentX(Component.CENTER_ALIGNMENT);

        // aperçu de la forme
        formeObjectif = new ImagePanel();
        formeObjectif.setShape(new ItemShape(n.getFormeDemander()));
        formeObjectif.setPreferredSize(new Dimension(80, 80));
        formeObjectif.setMaximumSize(new Dimension(80, 80));
        formeObjectif.setAlignmentX(Component.CENTER_ALIGNMENT);

        // bouton commencer
        btnCommencer = new JButton("Commencer !");
        btnCommencer.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCommencer.setFont(new Font("Arial", Font.BOLD, 14));
        btnCommencer.addActionListener(e -> {
            Container parent = getParent();
            parent.remove(this);
            parent.revalidate();
            parent.repaint();
        });


        contenu.add(Box.createVerticalStrut(10));
        contenu.add(labelNiveau);
        contenu.add(Box.createVerticalStrut(10));
        contenu.add(formeObjectif);
        contenu.add(Box.createVerticalStrut(10));
        contenu.add(labelObjectif);
        contenu.add(Box.createVerticalStrut(20));
        contenu.add(btnCommencer);
        contenu.add(Box.createVerticalStrut(10));

        add(contenu);
    }
}
