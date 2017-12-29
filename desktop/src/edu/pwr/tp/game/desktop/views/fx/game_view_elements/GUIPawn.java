package edu.pwr.tp.game.desktop.views.fx.game_view_elements;

import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

class GUIPawn extends Circle {

    private GUIBoard board;
    private int colorID;
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setXY(int x, int y){
        this.x=x;
        this.y=y;
    }

    private final EventHandler<MouseEvent> pawnMover = mouseEvent -> {
        double previousCenterX = getCenterX();
        double previousCenterY = getCenterY();
        double startingMouseX = mouseEvent.getSceneX();
        double startingMouseY = mouseEvent.getSceneY();
        EventHandler<MouseEvent> mover = mouseEvent1 -> {
            double deltaX = mouseEvent1.getSceneX()-startingMouseX;
            double deltaY = mouseEvent1.getSceneY()-startingMouseY;
            setCenterX(previousCenterX+deltaX);
            setCenterY(previousCenterY+deltaY);
        };
        setOnMouseDragged(mover);
        setOnMouseReleased(mouseDragEvent -> {
            removeEventHandler(MouseEvent.MOUSE_DRAGGED, mover);
            boolean found = false;
            for (int i=0; i<board.getChildren().size(); i++) {
                if (!(board.getChildren().get(i) instanceof GUIField)) continue;
                GUIField field = (GUIField) board.getChildren().get(i);
                double distance = Math.sqrt(
                        (field.getCenterX()-getCenterX())*(field.getCenterX()-getCenterX()) +
                                (field.getCenterY()-getCenterY())*(field.getCenterY()-getCenterY())  );
                if(distance<field.getRadius()){
                    found = true;
                    board.sendMoveToServer(getX(),getY(),field.getX(),field.getY());
                    setXY(field.getX(),field.getY());
                    setCenterY(field.getCenterY());
                    setCenterX(field.getCenterX());
                    break;
                }
            }
            if(!found){
                setCenterX(previousCenterX);
                setCenterY(previousCenterY);
            }
        });
    };

    public void setColorID(int colorID) {
        this.colorID = colorID;
    }

    public int getColorID() {
        return colorID;
    }

    public void enableMove() {
        setOnMousePressed(pawnMover);
    }

    public void disableMove() {
        removeEventHandler(MouseEvent.MOUSE_PRESSED, pawnMover);
    }

    GUIPawn(GUIBoard board, Paint fill) {

        super(8, fill);
        this.board = board;
        setOnMouseEntered((MouseEvent mouseEvent) -> {
            if (this.colorID==board.getPlayerIndex()) {
                //on mouse enter effects:
                setRadius(getRadius() + 1);
                setEffect(new DropShadow());
            }
        });
        //removing changes for the object
        setOnMouseExited(mouseEvent1 -> {
            if (this.colorID==board.getPlayerIndex()) {
                setRadius(getRadius() - 1);
                setEffect(null);
            }
        });

        board.getChildren().add(this);
    }
}
