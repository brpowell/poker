package game;

import game.player.*;

public class Game {

	public static Player player = new Player(300);
	public static Deck deck = new Deck();
	public static GameWindow window = new GameWindow();
	
	public static void main(String[] args) {
		
		System.out.println("Welcome to Five Card Poker!");
		
	}
	
}
