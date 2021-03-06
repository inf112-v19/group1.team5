package inf112.skeleton.app;

public class ConveyorBelt extends TilePiece {

    private Vector2Di dir;
    private boolean is_express;

    public ConveyorBelt(Vector2Di dir, boolean is_express) {
        this.dir = dir;
        this.is_express = is_express;
    }

    public Vector2Di getDir() {
        return dir;
    }

    public boolean is_express(){ return is_express;}
}