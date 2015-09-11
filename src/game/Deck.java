package game;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
	
	private ArrayList<Card> deck;
	private static Random generator = new Random();
	
	public Deck() {
		deck = new ArrayList<>();
		this.newDeck();
	}
	
	public void newDeck() {
		String suit;
		deck.clear();
		
		// Fill 13 cards for each suit
		for(int s = 0; s < 4; s++) {
			switch(s) {
			case 0:
				suit = "h";
				break;
			case 1:
				suit = "s";
				break;
			case 2:
				suit = "c";
				break;
			default:
				suit = "d";
				break;
			}
			
			for(int v = 2; v <= 14; v++) {
				deck.add(new Card(v, suit));
			}
		}
	}
	
	public Card draw() {
		int cardIndex = generator.nextInt(deck.size());
		return deck.remove(cardIndex);
	}
}
