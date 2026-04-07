package modele.item;

public class ItemShape extends Item {

    private String configurationItem;

    private SubShape[] tabSubShapes;//stockage des formes pour chaqu'un des 4 subshape
    private Couleur[] tabColors;//stockage des couleurs pour chaqu'un des 4 subcolor
    public enum Layer {one, two, three};


    public SubShape[] getSubShapes(Layer l) {
        switch(l) {
            case one : return new SubShape[] {tabSubShapes[0], tabSubShapes[1], tabSubShapes[2], tabSubShapes[3]};

            // TODO two & three
            default:
                throw new IllegalStateException("Unexpected value: " + l);
        }
    }

    public Couleur[] getColors(Layer l) {
        switch(l) {
            case one : return new Couleur[] {tabColors[0], tabColors[1], tabColors[2], tabColors[3]};
            // TODO two & three
            default:
                throw new IllegalStateException("Unexpected value: " + l);
        }
    }

    /**;
     * Initialisation des formes par chaîne de caractères
     * @param str : codage : (sous forme + couleur ) * (haut-droit, bas-droit, bas-gauche, haut-gauche) * 3 Layers
     *            str.length multiple de 4
     */
    public ItemShape(String str) {

        this.configurationItem = str;

        tabSubShapes = new SubShape[str.length()/2 ];
        tabColors = new Couleur[str.length()/2];

        for (int i = 0; i < 4; i++) { // fait uniquement pour la première couche
            switch (str.charAt(i*2)) {
                case 'C' : tabSubShapes[i] = SubShape.Carre;break;
                case 'R' : tabSubShapes[i] = SubShape.Circle;break;
                case 'F' :  tabSubShapes[i] = SubShape.Fan;break;
                case 'S' : tabSubShapes[i] = SubShape.Star;break;
                case '-' : tabSubShapes[i] = SubShape.None;break;
                default:
                    throw new IllegalStateException("Unexpected value: " + str.charAt(i));
            }

            switch (str.charAt((i*2 + 1))) {
                case 'r' : tabColors[i] = Couleur.Red; break;
                case 'g'  : tabColors[i] = Couleur.Green; break;
                case 'b' : tabColors[i] = Couleur.Blue; break;
                case 'y'  : tabColors[i] = Couleur.Yellow; break;
                case 'p'  : tabColors[i] = Couleur.Purple; break;
                case 'c' : tabColors[i] = Couleur.Cyan; break;
                case 'w' : tabColors[i] = Couleur.White; break;
                case '-' : tabColors[i] = null; break;
                default:
                    throw new IllegalStateException("Unexpected value: " + str.charAt((i + 1)*2));
            }


        }

    }




    // TODO : écrire l'ensemble des fonctions de transformation souhaitées, définir les paramètres éventuels (sens, axe, etc.)


    public ItemShape[] couper(){
        //fonction pour le cutter
        //prends les formes et couleurs de la shape de base, les sépares et crée deux nouvelles combler par du vide : "--"
        String moitierDroite = ""+ adapterShape(tabSubShapes[0]) + adapterCouleur(tabColors[0]) + adapterShape(tabSubShapes[2]) + adapterCouleur(tabColors[2]) + "--"  + "--";
        String moitierGauche = "--" + "--" + adapterShape(tabSubShapes[1]) + adapterCouleur(tabColors[1]) + adapterShape(tabSubShapes[3]) + adapterCouleur(tabColors[3]) + "";

        //debug
        //System.out.println(moitierGauche);
        //System.out.println(moitierDroite);

        //pour pouvoir ensuite ajouter au current de la machine
        ItemShape itemConteneur[] = new ItemShape[] {new ItemShape(moitierGauche),new ItemShape(moitierDroite)};
        return itemConteneur;
    }
//TODO : finir les diff cas
    //pour recrée une forme en fonction de l'input il faut la déconstruire
    /*
    Prends un subshape en paraètre.
    Retourne un string pour pouvoir reconstruire une forme derrière avec le string
     */
    public String adapterShape(SubShape s){
        if(s == SubShape.Carre)return "C";
        if(s == SubShape.Circle)return "R";
        if(s == SubShape.Fan)return "F";
        if(s == SubShape.Star)return "S";
        if(s == SubShape.None)return "-";
        return "-";
    }

    //pareil que pour adapterShape mais pour la couleur de la subshape
    public String adapterCouleur(Couleur c){
        if(c == Couleur.Red)return "r";
        if(c == Couleur.White)return "b";
        if(c == Couleur.Green)return "g";
        if(c == Couleur.Blue)return "b";
        if(c == Couleur.Yellow)return "y";
        if(c == Couleur.Purple)return "p";
        if(c == Couleur.Cyan)return "y";
        return "-";
    }


    public void rotate(boolean Horaire) {
        SubShape[] bufferSubShapes = new SubShape[4];
        Couleur[] bufferColors = new Couleur[4];
        if( Horaire)
        {
            bufferSubShapes[0] = tabSubShapes[3];
            bufferSubShapes[1] = tabSubShapes[0];
            bufferSubShapes[2] = tabSubShapes[1];
            bufferSubShapes[3] = tabSubShapes[2];

            bufferColors[0] = tabColors[3];
            bufferColors[1] = tabColors[0];
            bufferColors[2] = tabColors[1];
            bufferColors[3] = tabColors[2];
        }else
        {
            bufferSubShapes[0] = tabSubShapes[1];
            bufferSubShapes[1] = tabSubShapes[2];
            bufferSubShapes[2] = tabSubShapes[3];
            bufferSubShapes[3] = tabSubShapes[0];

            bufferColors[0] = tabColors[1];
            bufferColors[1] = tabColors[2];
            bufferColors[2] = tabColors[3];
            bufferColors[3] = tabColors[0];
        }
        tabSubShapes = bufferSubShapes;
        tabColors = bufferColors;


    }
    public void stack(ItemShape ShapeSup) { // ShapeSup est empilé sur this
        if ((tabSubShapes[1] == SubShape.None && tabSubShapes[3] == SubShape.None)
                && (ShapeSup.tabSubShapes[0] == SubShape.None && ShapeSup.tabSubShapes[2] == SubShape.None)) {
            //ajoute dans this la moitié droite de la forme dans l'autre input
            tabSubShapes[1] = ShapeSup.tabSubShapes[1];
            tabSubShapes[3] = ShapeSup.tabSubShapes[3];
            tabColors[1]    = ShapeSup.tabColors[1];
            tabColors[3]    = ShapeSup.tabColors[3];
        }
    }

    public ItemShape Cut() { // this et l'objet retourné correpondent au deux sorties
        return null;
    }

    public void Color(Couleur c) {

    }
    //peint la shubshape de la couleur en paramètre
    public void paint(Couleur c) {
        for (int i = 0; i < 4; i++) {
            if (tabSubShapes[i] != SubShape.None) {
                tabColors[i] = c;
            }
        }
    }
    //retourn eune chaine de caratère qui est composé des subshape et des subcolor
    public String getConfigItem() { return configurationItem; }
}
