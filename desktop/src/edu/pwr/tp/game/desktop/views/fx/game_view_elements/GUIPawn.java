package edu.pwr.tp.game.desktop.views.fx.game_view_elements;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

class GUIPawn extends Circle {

    //private GUIBoard board;

    GUIPawn(GUIBoard board, Paint fill){

        super(8,fill);

        setOnMouseEntered(mouseEvent -> {
            if(true){
                //on mouse enter effects:
                setRadius(getRadius()+1);
                setEffect(new DropShadow());

                //removing changes for the object
                setOnMouseExited(mouseEvent1 -> {
                    setRadius(getRadius()-1);
                    setEffect(null);
                });

                //activating GUIFields to listen after drag
                setOnMouseDragEntered(mouseDragEvent -> {
                    //TODO
                });

            }
        });
    }
}
