package modele.item;

public class ItemShape extends Item {

    private String configurationItem;

    private SubShape[] tabSubShapes;
    private Color[] tabColors;
    public enum Layer {one, two, three};


    public SubShape[] getSubShapes(Layer l) {
        switch(l) {
            case one : return new SubShape[] {tabSubShapes[0], tabSubShapes[1], tabSubShapes[2], tabSubShapes[3]};

            // TODO two & three
            default:
                throw new IllegalStateException("Unexpected value: " + l);
        }
    }

    public Color[] getColors(Layer l) {
        switch(l) {
            case one : return new Color[] {tabColors[0], tabColors[1], tabColors[2], tabColors[3]};
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
        tabColors = new Color[str.length()/2];

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
                case 'r' : tabColors[i] = Color.Red; break;
                case 'g'  : tabColors[i] = Color.Green; break;
                case 'b' : tabColors[i] = Color.Blue; break;
                case 'y'  : tabColors[i] = Color.Yellow; break;
                case 'p'  : tabColors[i] = Color.Purple; break;
                case 'c' : tabColors[i] = Color.Cyan; break;
                case 'w' : tabColors[i] = Color.White; break;
                case '-' : tabColors[i] = null; break;
                default:
                    throw new IllegalStateException("Unexpected value: " + str.charAt((i + 1)*2));
            }


        }

    }




    // TODO : écrire l'ensemble des fonctions de transformation souhaitées, définir les paramètres éventuels (sens, axe, etc.)


    public ItemShape[] couper(){

        String moitierDroite = ""+ adapterShape(tabSubShapes[0]) + adapterCouleur(tabColors[0]) + adapterShape(tabSubShapes[2]) + adapterCouleur(tabColors[2]) + "--"  + "--";
        String moitierGauche = "--" + "--" + adapterShape(tabSubShapes[1]) + adapterCouleur(tabColors[1]) + adapterShape(tabSubShapes[3]) + adapterCouleur(tabColors[3]) + "";

        //debug
        System.out.println(moitierGauche);
        System.out.println(moitierDroite);

        ItemShape itemConteneur[] = new ItemShape[] {new ItemShape(moitierGauche),new ItemShape(moitierDroite)};
        return itemConteneur;
    }

    public String adapterShape(SubShape s){
        if(s == SubShape.Carre)return "C";
        if(s == SubShape.None)return "-";
        return "-";
    }

    public String adapterCouleur(Color c){
        if(c == Color.Red)return "r";
        if(c == Color.White)return "b";
        return "-";
    }


    public void rotate() {

        SubShape[] bufferSubShapes = new SubShape[4];
        bufferSubShapes[0] = tabSubShapes[3];
        bufferSubShapes [1] = tabSubShapes[0];
        bufferSubShapes [2] = tabSubShapes[1];
        bufferSubShapes [3] = tabSubShapes[2];

        Color[] bufferColors = new Color[4];
        bufferColors[0] = tabColors[3];
        bufferColors [1] = tabColors[0];
        bufferColors [2] = tabColors[1];
        bufferColors [3] = tabColors[2];

        tabSubShapes = bufferSubShapes;
        tabColors = bufferColors;


    }
    public void stack(ItemShape ShapeSup) { // ShapeSup est empilé sur this

    }

    public ItemShape Cut() { // this et l'objet retourné correpondent au deux sorties
        return null;
    }

    public void Color(Color c) {

    }

    public String getConfigItem() { return configurationItem; }
}
