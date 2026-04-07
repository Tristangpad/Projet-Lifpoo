package vuecontroleur;

import modele.item.ItemShape;
import modele.item.SubShape;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class ImagePanel extends JPanel {
    private Image imgBackground;
    private Image imgFront;
    private ItemShape shape;

    //dimentions par defauts d'une machine
    private int dimentionPartX = 0;
    private int dimentionPartY = 0;
    private int dimentionTotalX = 1;
    private int dimentionTotalY = 1;

    //si jamais une machine a une dim supp a 1:1 elle est découpr en partie
    public void setPartie(int partX, int totalX,int partY, int totalY) {
        this.dimentionPartX = partX;
        this.dimentionTotalX = totalX;
        this.dimentionPartY = partY;
        this.dimentionTotalY = totalY;
    }

    public void resetPartie() {
        this.dimentionPartX = 0;
        this.dimentionTotalX = 1;
        this.dimentionPartY = 0;
        this.dimentionTotalY = 1;
    }


    public void setShape(ItemShape _shape) {
        shape = _shape;
    }

    public void supprimeShape() {
        this.shape = null;
    }

    public void setBackground(Image _imgBackground) {
        imgBackground = _imgBackground;
    }

    public void setFront(Image _imgFront) {
        imgFront = _imgFront;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final int bordure= 1;
        final int xBack = bordure;
        final int yBack = bordure;
        final int widthBack = getWidth() - bordure*2;
        final int heigthBack = getHeight() - bordure*2;

        final int subPartWidth = widthBack / 4;
        final int subPartHeigth = heigthBack / 4;

        final int xFront = bordure + subPartWidth;
        final int yFront = bordure + subPartHeigth;
        final int widthFront = subPartWidth*2;
        final int heigthFront = subPartHeigth*2;


        // cadre
        g.drawRoundRect(bordure, bordure, widthBack, heigthBack, bordure, bordure);


        if (imgBackground != null) {
            if(dimentionTotalX == 1 && dimentionTotalY == 1) {
                g.drawImage(imgBackground, xBack, yBack, widthBack, heigthBack, this);
            }else {
                int largeurP = imgBackground.getWidth(null)/dimentionTotalX;
                int hauteurP = imgBackground.getHeight(null)/dimentionTotalY; //image deja charger donc ont met null a lieu de this (sensé avertir le panel)
                //chaque case a sa propre partie, si parfois la mahcine est de 1 par 1 alors elle sra juste dessiner en une fois sur une case sinon...
                g.drawImage(imgBackground,
                        xBack, yBack, xBack + widthBack, yBack + heigthBack,
                        dimentionPartX*largeurP,dimentionPartY*hauteurP,
                        (dimentionPartX + 1) * largeurP,(dimentionPartY + 1) * hauteurP,
                        this);
            }
        }

        if (imgFront != null) {
            g.drawImage(imgFront, xFront, yFront, widthFront, heigthFront, this);
        }

        if (shape != null) {

            // TODO autres layers
            SubShape[] tabS = shape.getSubShapes(ItemShape.Layer.one);
            modele.item.Couleur[] tabC = shape.getColors(ItemShape.Layer.one);


            for (int i = 0; i < 4; i++) {

                    SubShape ss = tabS[i];

                    if (ss != SubShape.None) {

                        switch (tabC[i]) {
                            case modele.item.Couleur.Red:
                                g.setColor(Color.RED);
                                break;
                            case modele.item.Couleur.Green:
                                g.setColor(Color.GREEN);
                                break;
                            case modele.item.Couleur.White:
                                g.setColor(Color.WHITE);
                                break;
                            case modele.item.Couleur.Cyan:
                                g.setColor(Color.CYAN);
                                break;
                            case modele.item.Couleur.Blue:
                                g.setColor(Color.BLUE);
                                break;
                            case modele.item.Couleur.Yellow:
                                g.setColor(Color.YELLOW);
                                break;

                            // TODO autres couleurs
                        }

                        int gap = 1;
                        int startAngle = (4 - i) % 4 * 90;

                        int qx = xFront + (widthFront / 2) * ((i >> 1) ^ 1) + gap;
                        int qy = yFront + (heigthFront / 2) * ((i & 1) ^ ((i >> 1) & 1)) + gap;
                        int qw = widthFront / 2 - gap * 2;
                        int qh = heigthFront / 2 - gap * 2;

                        //centre et rayon global
                        int cx = xFront + widthFront/2;
                        int cy = yFront + heigthFront/2;
                        int r = Math.min(widthFront, heigthFront)/2;

                        //...
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        switch (ss) {
                            case SubShape.Circle: {
                                java.awt.Color savedColor = g.getColor();
                                Shape oldClip = g2.getClip();
                                g2.setClip(qx, qy, qw, qh);
                                g2.fillArc(cx - r + gap, cy - r + gap, r * 2 - gap * 2, r * 2 - gap * 2,
                                        startAngle, 90);
                                g2.setClip(oldClip);
                                g2.setColor(savedColor);
                                break;
                            }
                            case SubShape.Carre: {
                                java.awt.Color savedColor = g.getColor();
                                g.fillRect(qx, qy, qw, qh);
                                g.setColor(savedColor);
                                break;
                            }
                            //de claude
                            case SubShape.Star: {

                                java.awt.Color savedColor = g.getColor();
                                Shape oldClip = g2.getClip();
                                g2.setClip(qx, qy, qw, qh);

                                float tipSize = Math.min(qw, qh) * 0.75f;

                                // Coin extérieur selon le vrai mapping
                                float ex, ey;
                                switch (i) {
                                    case 0 -> { ex = qx + qw; ey = qy; }       // haut-droite
                                    case 1 -> { ex = qx + qw; ey = qy + qh; }  // bas-droite
                                    case 2 -> { ex = qx;      ey = qy + qh; }  // bas-gauche
                                    default-> { ex = qx;      ey = qy; }        // haut-gauche
                                }

                                float dx = ex - cx;
                                float dy = ey - cy;
                                float len = (float) Math.sqrt(dx * dx + dy * dy);
                                float px = -dy / len * tipSize;
                                float py =  dx / len * tipSize;

                                Path2D branch = new Path2D.Float();
                                branch.moveTo(cx + px, cy + py);
                                branch.lineTo(ex, ey);
                                branch.lineTo(cx - px, cy - py);
                                branch.closePath();

                                g2.fill(branch);
                                g2.setClip(oldClip);
                                g.setColor(savedColor);
                                break;
                            }
                            //de claude aussi
                            case SubShape.Fan: {
                                java.awt.Color savedColor = g.getColor();
                                Shape oldClip = g2.getClip();
                                g2.setClip(qx, qy, qw, qh);

                                float cutRatio = 0.5f;
                                Path2D fan = new Path2D.Float();

                                float tlx = qx,      tly = qy;
                                float trx = qx + qw, try_ = qy;
                                float blx = qx,      bly = qy + qh;
                                float brx = qx + qw, bry = qy + qh;

                                switch (i) {
                                    case 1 -> { // haut-droite : coin extérieur = haut-droite → on coupe ce coin
                                        fan.moveTo(tlx, tly);                          // coin intérieur haut-gauche
                                        fan.lineTo(trx - qw * cutRatio, try_);         // bord haut, avant le coin
                                        fan.lineTo(trx, try_ + qh * cutRatio);         // bord droit, après le coin
                                        fan.lineTo(brx, bry);                          // coin intérieur bas-droite
                                        fan.lineTo(blx, bly);                          // coin intérieur bas-gauche
                                    }
                                    case 2 -> { // bas-droite : coin extérieur = bas-droite
                                        fan.moveTo(tlx, tly);
                                        fan.lineTo(trx, try_);
                                        fan.lineTo(brx, bry - qh * cutRatio);          // bord droit, avant le coin
                                        fan.lineTo(brx - qw * cutRatio, bry);          // bord bas, après le coin
                                        fan.lineTo(blx, bly);
                                    }
                                    case 3 -> { // bas-gauche : coin extérieur = bas-gauche
                                        fan.moveTo(tlx, tly);
                                        fan.lineTo(trx, try_);
                                        fan.lineTo(brx, bry);
                                        fan.lineTo(blx + qw * cutRatio, bly);          // bord bas, avant le coin
                                        fan.lineTo(tlx, bly - qh * cutRatio);          // bord gauche, après le coin
                                    }
                                    case 0 -> { // haut-gauche : coin extérieur = haut-gauche
                                        fan.moveTo(tlx + qw * cutRatio, tly);          // bord haut, après le coin
                                        fan.lineTo(trx, try_);
                                        fan.lineTo(brx, bry);
                                        fan.lineTo(blx, bly);
                                        fan.lineTo(tlx, bly - qh * cutRatio);          // bord gauche, avant le coin
                                    }
                                }
                                fan.closePath();
                                g2.fill(fan);
                                g2.setClip(oldClip);
                                g.setColor(savedColor);
                                break;
                            }
                        }
                    }
                }
            }

        }



    }





