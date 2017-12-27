package edu.pwr.tp.game.desktop.views.fx.game_view_elements;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GUIBoard extends Pane {

    private Paint playerColor;

    public Paint getPlayerColor() {
        return playerColor;
    }

    public GUIBoard(int players, Paint playerColor){ //TODO: alternatively we can move that to server (server does the same so we can just get the fields)
        super();
        setPrefSize(260,340);
        for(int x=0; x<=17; x++)
            for(int y=0; y<=17; y++){
                if(x<=12&&y<=12&&x+y>=12) getChildren().add(new GUIField(x,y));
                else if(x>=4&&y>=4&&x+y<=20) getChildren().add(new GUIField(x,y));
            }
            GUIPawn pawn = new GUIPawn(this, Color.BLACK);
            pawn.setCenterY(80+10);
            pawn.setCenterX(120+10);
            getChildren().add(pawn);
    }

}
