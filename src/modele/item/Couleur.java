package modele.item;

public enum Couleur  {
    Red, Green, Blue, Yellow, Purple, Cyan, White,None;

    public String adapterEnString() {
        return switch (this) {
            case Red    -> "r";
            case White  -> "w";
            case Green  -> "g";
            case Blue   -> "b";
            case Yellow -> "y";
            case Purple -> "p";
            case Cyan   -> "c";
            case None -> "-";
        };
    }
    }
