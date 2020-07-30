package application;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Main extends Application {
	public static final int MOVE = 25;
	public static final int SIZE = 25;
	public static final int XMAX = 24*SIZE; //you can adjust the size of the mesh but make sure its a square.
	public static final int YMAX = 24*SIZE;
	public static final int[][] MESH = new int[XMAX / SIZE][YMAX / SIZE];
	private static Square snake = Controller.makeSnake();
	private static Square food = Controller.makeSQ();
	private static int debriCurr = 0;
	private static int debriMax = 45;
	private static Pane group = new Pane();
	private static boolean game = true;
	private static Scene scene = new Scene(group, XMAX, YMAX +25 , Color.WHITE);
	private static String direction = "";
	private static Text scoretext;
	private static Text pause;
	private static Text startgame;
	private static int score = 0;
	private static int existtimer = 0;
	private static int timer = 200;


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		for (int[] a : MESH) {
			Arrays.fill(a, 0);
		}

		// To make the grid lines
		for(int i = 0; i < XMAX/SIZE; i++) {
			Line rowline = new Line(SIZE*i+SIZE, 0, SIZE*i +SIZE, YMAX);
			Line colline = new Line(0, SIZE*i+SIZE, XMAX, SIZE*i+SIZE);
			rowline.setStroke(Color.GRAY);
			colline.setStroke(Color.GRAY);
			group.getChildren().addAll(rowline, colline);
		}
		
		//add startgame
		startgame = new Text("PRESS A DIRECTION TO START");
		startgame.setFill(Color.RED);
		startgame.setStyle("-fx-font: 30 arial;");
		startgame.setY(YMAX/2);
		startgame.setX(XMAX/8);
		group.getChildren().add(startgame);
		
		
		//add score text
		scoretext = new Text("Score: " + Integer.toString(score));
		scoretext.setStyle("-fx-font: 20 arial;");
		scoretext.setY(YMAX +20);
		scoretext.setX(5);
		scoretext.setFill(Color.BLACK);
		group.getChildren().add(scoretext);

		//setup the location of the food
		MESH[((int) food.head.getX()/SIZE)][((int) food.head.getY()/SIZE)] = 1;
		food.head.setFill(Color.RED);
		food.head.setArcHeight(25);
		food.head.setArcWidth(25);
		
		snake.head.setFill(Color.BROWN);

		group.getChildren().addAll(food.head, snake.head);
		group.getChildren().addAll(snake.body);
		moveOnKeyPress(snake);


		stage.setScene(scene);
		stage.setTitle("S N A K E");
		stage.show();


		//the heart of the game
		Timer rungame = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						//if this is false then that means the snake ate itself
						if (!game) {
							Text over = new Text("GAME OVER");
							over.setFill(Color.RED);
							over.setStyle("-fx-font: 70 arial;");
							over.setY(YMAX/2);
							over.setX(XMAX/8);
							group.getChildren().add(over);
							existtimer++;
						}
						if(existtimer == 10)
							System.exit(0);
						//the game is still in play so we need to push snake around when the player doesn't do anything
						else{
							MoveDirection(direction, snake);
						}
					}

				});
			}
		};
		rungame.schedule(task, 0, timer);

		Timer debri = new Timer();
		TimerTask addDebri = new TimerTask() {
			public void run() {
				Platform.runLater(new Runnable() {
					public void run() {
						if(direction!= "")
						if(debriMax>debriCurr) {
							debriCurr++;
							Square debri = Controller.makeSQ();
							debri.head.setFill(Color.GRAY);
							MESH[(int) (debri.head.getX()/SIZE)][(int) (debri.head.getY()/SIZE)]= 2;
							group.getChildren().add(debri.head);
						}
					}
				});
			}
		};
		debri.schedule(addDebri, 0, 5000);


	}

	private void MoveDirection(String dir, Square s) {
		switch(dir) {
		case "up":
			if(startgame != null) {
				group.getChildren().remove(startgame);
			}
			if(pause!=null) {
				group.getChildren().remove(pause);
			}
			MoveUp(s);
			break;

		case "down":
			if(startgame != null) {
				group.getChildren().remove(startgame);
			}
			if(pause!=null) {
				group.getChildren().remove(pause);
			}
			MoveDown(s);
			break;

		case "left":
			if(startgame != null) {
				group.getChildren().remove(startgame);
			}
			if(pause!=null) {
				group.getChildren().remove(pause);
			}
			MoveLeft(s);
			break;

		case "right":
			if(startgame != null) {
				group.getChildren().remove(startgame);
			}
			if(pause!=null) {
				group.getChildren().remove(pause);
			}
			MoveRight(s);
			break;
		case "":
			break;
		}


	}

	private void moveOnKeyPress(Square sq) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				case RIGHT:
					direction = "right";
					break;

				case DOWN:
					direction = "down";
					break;

				case LEFT:
					direction = "left";
					break;

				case UP:
					direction = "up";
					break;
				case P:
					direction = "";
					pause = new Text("PAUSE");
					pause.setFill(Color.RED);
					pause.setStyle("-fx-font: 70 arial;");
					pause.setY(YMAX/2);
					pause.setX(XMAX/4);
					group.getChildren().add(pause);
					break;

				}
			}
		});
	}

	private void MoveDown(Square s) {
		if (s.head.getY() + MOVE < YMAX) {
			//check if the snake is on itself
			for(Rectangle r : s.body) {
				if(s.head.getY() + MOVE == r.getY() && s.head.getX() == r.getX()) {
					game = false;
				}
			}
			//hit debri
			if(MESH[((int) s.head.getX()/SIZE)][((int) s.head.getY()/SIZE)+1] == 2) {
				game = false;
			}
			//check if we are about to touch the food
			if(MESH[((int) s.head.getX()/SIZE)][((int) s.head.getY()/SIZE)+1] == 1) {
				//we need to make the food a part of the snake
				AddFoodToSnake(s);
				
				//update the score
				score++;
				scoretext.setText("Score: " + Integer.toString(score));

			}
			else {
				//the snake is moving into an empty space
				double currX = s.head.getX();
				double currY = s.head.getY();
				double tempX= 0;
				double tempY =0;
				s.head.setY(s.head.getY() + MOVE);
				for(Rectangle r : s.body) {
					tempX = r.getX();
					tempY = r.getY();
					r.setX(currX);
					r.setY(currY);
					currX = tempX;
					currY = tempY;
				}
			}	
		}
		else {
			// we are going to hit out of bounds so we end the game
			game = false;

		}
	}

	private void MoveRight(Square s) {
		if (s.head.getX() + MOVE <= XMAX - SIZE) {
			//check if the snake is on itself
			for(Rectangle r : s.body) {
				if(s.head.getY() == r.getY() && s.head.getX() + MOVE == r.getX()) {
					game = false;
				}
			}
			//hit debri
			if(MESH[(int) s.head.getX()/SIZE + 1][(int) s.head.getY()/SIZE] == 2) {
				game = false;
			}
			//check if we are about to touch the food
			if(MESH[(int) s.head.getX()/SIZE + 1][(int) s.head.getY()/SIZE] == 1) {
				//we need to make the food a part of the snake
				AddFoodToSnake(s);
				
				//update the score
				score++;
				scoretext.setText("Score: " + Integer.toString(score));

			}
			else {
				//the snake is moving into an empty space
				double currX = s.head.getX();
				double currY = s.head.getY();
				double tempX= 0;
				double tempY =0;
				s.head.setX(s.head.getX() + MOVE);
				for(Rectangle r : s.body) {
					tempX = r.getX();
					tempY = r.getY();
					r.setX(currX);
					r.setY(currY);
					currX = tempX;
					currY = tempY;
				}
			}		
		}
		else {
			// we are going to hit out of bounds so we end the game
			game = false;			
		}
	}

	private void MoveLeft(Square s) {
		if (s.head.getX() - MOVE >= 0) {
			//check if the snake is on itself
			for(Rectangle r : s.body) {
				if(s.head.getY() == r.getY() && s.head.getX() - MOVE == r.getX()) {
					game = false;
				}
			}
			//hit debri
			if(MESH[(int) s.head.getX()/SIZE-1][(int) s.head.getY()/SIZE] == 2) {
				game = false;
			}
			//check if we are about to touch the food
			if(MESH[(int) s.head.getX()/SIZE-1][(int) s.head.getY()/SIZE] == 1) {
				//we need to make the food a part of the snake
				AddFoodToSnake(s);

				//update score
				score++;
				scoretext.setText("Score: " + Integer.toString(score));

			}
			else {
				//the snake is moving into an empty space
				double currX = s.head.getX();
				double currY = s.head.getY();
				double tempX= 0;
				double tempY =0;
				s.head.setX(s.head.getX() - MOVE);
				for(Rectangle r : s.body) {
					tempX = r.getX();
					tempY = r.getY();
					r.setX(currX);
					r.setY(currY);
					currX = tempX;
					currY = tempY;
				}
			}	
		}
		else {
			// we are going to hit out of bounds so we end the game
			game = false;

		}
	}

	private void MoveUp(Square s) {
		if (s.head.getY() - MOVE >= 0) {
			//check if the snake is on itself
			for(Rectangle r : s.body) {
				if(s.head.getY() - MOVE == r.getY() && s.head.getX() == r.getX()) {
					game = false;
				}
			}
			//hit debri
			if(MESH[((int) s.head.getX()/SIZE)][((int) s.head.getY()/SIZE)-1] == 2) {
				game = false;
			}
			//check if we are about to touch the food
			if(MESH[((int) s.head.getX()/SIZE)][((int) s.head.getY()/SIZE)-1] == 1) {
				//we need to make the food a part of the snake
				AddFoodToSnake(s);

				//update score
				score++;
				scoretext.setText("Score: " + Integer.toString(score));

			}
			else {
				//the snake is moving into an empty space
				double currX = s.head.getX();
				double currY = s.head.getY();
				double tempX= 0;
				double tempY =0;
				s.head.setY(s.head.getY() - MOVE);
				for(Rectangle r : s.body) {
					tempX = r.getX();
					tempY = r.getY();
					r.setX(currX);
					r.setY(currY);
					currX = tempX;
					currY = tempY;
				}
			}		
		}
		else {
			// we are going to hit out of bounds so we end the game
			game = false;	
		}
	}

	private void AddFoodToSnake(Square s) {
		//add the current head to the body
		s.head.setFill(Color.BLACK);
		s.body.add(0,s.head);
		
		//make the food the new head
		Rectangle newHead = Controller.Copy(food);
		newHead.setFill(Color.BROWN);
		MESH[(int) (food.head.getX()/SIZE)][(int) (food.head.getY()/SIZE)] = 0;
		group.getChildren().add(newHead);
		group.getChildren().remove(food.head);
		s.head = newHead;
		moveOnKeyPress(s);
		
		//add a new food for the snake to eat
		food = Controller.makeSQ();
		food.head.setFill(Color.RED);
		food.head.setArcHeight(25);
		food.head.setArcWidth(25);
		group.getChildren().add(food.head);
		MESH[(int) food.head.getX()/SIZE][(int) food.head.getY()/SIZE] = 1;
		
	} 

}
