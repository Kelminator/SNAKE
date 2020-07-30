package application;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square {
	Rectangle head;
	Color color = Color.BLACK;
	ArrayList<Rectangle> body;
	public Square (Rectangle head, ArrayList<Rectangle> body){
		this.head = head;
		this.head.setFill(color);
		this.body = body;
		for(Rectangle r: body) {
			r.setFill(color);
		}
	}
	public Square (Rectangle head){
		this.head = head;
		this.head.setFill(color);
		this.body = null;
	}
}
