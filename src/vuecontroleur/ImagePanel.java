package vuecontroleur;

import modele.item.ItemShape;
import modele.item.SubShape;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image imgBackground;
    private Image imgFront;
    private ItemShape shape;

    //dimentions par defauts d'une machine
    private int dimentionPartX = 0;
    private int dimentionPartY = 0;
    private int dimentionTotalX = 1;
    private int dimentionTotalY = 1;

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
            modele.item.Color[] tabC = shape.getColors(ItemShape.Layer.one);

            for (int i = 0; i < 4; i++) {

                    SubShape ss = tabS[i];

                    if (ss != SubShape.None) {

                        switch (tabC[i]) {
                            case modele.item.Color.Red:
                                g.setColor(Color.RED);
                                break;
                            case modele.item.Color.Green:
                                g.setColor(Color.GREEN);
                                break;
                            case modele.item.Color.White:
                                g.setColor(Color.WHITE);
                                break;
                            case modele.item.Color.Cyan:
                                g.setColor(Color.CYAN);
                                break;
                            case modele.item.Color.Blue:
                                g.setColor(Color.BLUE);
                                break;
                            case modele.item.Color.Yellow:
                                g.setColor(Color.YELLOW);
                                break;

                            // TODO autres couleurs
                        }

                        int gap = 2;
                        int startAngle = (4 - i) % 4 * 90;

                        int qx = xFront + (widthFront / 2) * ((i >> 1) ^ 1) + gap;
                        int qy = yFront + (heigthFront / 2) * ((i & 1) ^ ((i >> 1) & 1)) + gap;
                        int qw = widthFront / 2 - gap * 2;
                        int qh = heigthFront / 2 - gap * 2;

                        switch (ss) {
                            case SubShape.Circle: {
                                Color savedColor = g.getColor();
                                Graphics2D g2 = (Graphics2D) g;

                                Shape oldClip = g2.getClip();
                                g2.setClip(qx, qy, qw, qh);

                                // Cercle dimensionné avec gap → rayon = exactement la taille du quadrant
                                g2.fillArc(xFront + gap, yFront + gap, widthFront - gap * 2, heigthFront - gap * 2, startAngle, 90);

                                g2.setClip(oldClip);
                                g2.setColor(savedColor);
                                break;
                            }
                            case SubShape.Carre: {
                                Color savedColor = g.getColor();
                                g.fillRect(qx, qy, qw, qh);
                                g.setColor(savedColor);
                                break;
                            }
                        }
                    }
                }
            }

        }



    }





