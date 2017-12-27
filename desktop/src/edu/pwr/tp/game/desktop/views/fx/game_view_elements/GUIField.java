package edu.pwr.tp.game.desktop.views.fx.game_view_elements;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GUIField extends Circle {

    private int x, y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    GUIField(int x, int y) {
        super(10, Color.rgb(0xff,0xff,0xff)); //TODO: change Paint of the Field to more pretty one
        this.x=x;
        this.y=y;
        this.setStroke(Color.rgb(0,0,0)); //TODO: change color of Stroke, delete this line or delete todo
        this.setCenterY(y*20+10); //+10 to not start from 0
        this.setCenterX(x*20+10*y+10-6*20); //this will transform generated quadratic star to good star and move it to proper place
    }
}
