package cisi.citysight.auth.enums;

public enum ContentAllowed {
    MODERATE(1),
    FIX(2),
    ALLOW(3),
    REMOVE(4);

    private int allowed;

    private ContentAllowed(int allowed) {
        this.allowed = allowed;
    }

    public int value(){
        return allowed;
    }
}
