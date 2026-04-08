package modele.item;

public enum SubShape {
    Circle, Carre, Fan, Star, None;

    public String adapterEnString() {
        return switch (this) {
            case Carre  -> "C";
            case Circle -> "R";
            case Fan    -> "F";
            case Star   -> "S";
            case None   -> "-";
        };
    }
}
