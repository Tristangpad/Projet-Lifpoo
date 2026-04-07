package modele.plateau;

public enum Direction {
    North(0, -1), South(0, 1), East(1, 0), West(-1, 0);

    int dx;
    int dy;
    private Direction(int _dx, int _dy) {
        dx = _dx;
        dy = _dy;
    }
    //si appeller la dir de la machine tourne dans le sens des aiguilles d'une montre
    public Direction suivante() {
        return switch(this) {
            case North -> East;
            case West-> North;
            case South -> West;
            case East -> South;
        };
    }
    //renvoie l'opposer de la dir actuelle
    public Direction oppose() {
        return switch (this) {
            case North -> South;
            case South -> North;
            case East -> West;
            case West -> East;
        };
    }


}
