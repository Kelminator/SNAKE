package application;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Controller {
	public static final int MOVE = Main.MOVE;
	public static final int SIZE = Main.SIZE;
	public static int XMAX = Main.XMAX;
	public static int YMAX = Main.YMAX;
	public static int[][] MESH = Main.MESH;
	
	public static Rectangle Copy(Square s) {
		Rectangle r = new Rectangle(SIZE-1, SIZE -1);
		r.setX(s.head.getX());
		r.setY(s.head.getY());
		r.setFill(s.color);
		return r;
	}
		
	public static Square makeSQ() {
		Random rnd = new Random();
		int xCoord = rnd.nextInt(XMAX/SIZE-1);
		int yCoord = rnd.nextInt(YMAX/SIZE-1);
		Rectangle h =new Rectangle(SIZE-1, SIZE-1);
		while(MESH[xCoord][yCoord]==2 || MESH[xCoord][yCoord]== 1) {
			xCoord = rnd.nextInt(XMAX/SIZE);
			yCoord = rnd.nextInt(YMAX/SIZE);
		}
		h.setX(xCoord*SIZE);
		h.setY(yCoord*SIZE);
		
		return new Square(h);
	}
	
	public static Square makeSnake() {
		int xCoord = XMAX/2;
		int yCoord = YMAX/2;
		ArrayList<Rectangle> body = new ArrayList<Rectangle>();
		Rectangle h =new Rectangle(SIZE-1, SIZE-1);
		h.setX(xCoord);
		h.setY(yCoord);
		h.setFill(Color.BLACK);
		
		Rectangle b1 =new Rectangle(SIZE-1, SIZE-1);
		Rectangle b2 =new Rectangle(SIZE-1, SIZE-1);
		Rectangle b3 =new Rectangle(SIZE-1, SIZE-1);
		Rectangle b4 =new Rectangle(SIZE-1, SIZE-1);
		
		b1.setX(xCoord +SIZE);
		b1.setY(yCoord);
		b1.setFill(Color.BLACK);
		body.add(b1);
		
		b2.setX(xCoord +SIZE*2);
		b2.setY(yCoord);
		b2.setFill(Color.BLACK);
		body.add(b2);
		
		b3.setX(xCoord +SIZE*3);
		b3.setY(yCoord);
		b3.setFill(Color.BLACK);
		body.add(b3);
		
		b4.setX(xCoord +SIZE*4);
		b4.setY(yCoord);
		b4.setFill(Color.BLACK);
		body.add(b4);
		
		return new Square(h, body);
	}
}
