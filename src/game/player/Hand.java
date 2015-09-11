package game.player;
import game.Card;
import game.Deck;
import game.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Hand {
	
	private static ArrayList<Card> hand;
	
	private class CardComparator implements Comparator<Card> {
		public int compare(Card c1, Card c2) {
			return c1.value - c2.value;
		}	
	}
	
	public Hand() {
		hand = new ArrayList<>();
	}
	
	public void newHand(Deck deck) {
		deck.newDeck();
		hand.clear();
		for(int i = 0; i < 5; i++) 
			hand.add(deck.draw());
	}
	
	public void replace(int index) {
		hand.set(index, Game.deck.draw());
	}
	
	public int evaluate(int bet) {
		
		int payout = 0;
		String handType = "No Hand";
		int pairs;
		
		// Royal Flush
		if(royalFlush()) {
			handType = "Royal Flush";
			payout = 250*bet;
		}
		// Straight Flush
		else if(straight() && flush()){
			handType = "Straight Flush";
			payout = 50*bet;
		}
		// Four of a kind
		else if(nOfAKind(4)) {
			handType = "Four of a Kind";
			payout = 25*bet;
		}
		// Full house
		else if(fullHouse()) {
			handType = "Full House";
			payout = 9*bet;
		}
		// Flush
		else if(flush()) {
			handType = "Flush";
			payout = 6*bet;
		}
		// Straight
		else if(straight()) {
			handType = "Straight";
			payout = 4*bet;
		}
		// Three of a kind
		else if(nOfAKind(3)) {
			handType = "Three of a Kind";
			payout = 3*bet;
		}
		// Pairs
		else if((pairs = pair()) > 0) {
			if(pairs == 1) {
				handType = "One Pair";
				payout = bet;
			}
			else {
				handType = "Two Pair";
				payout = 2*bet;
			}
		}
		
		if(!handType.equals("No Hand"))
			System.out.println(handType + " - You Win!");
		else {
			System.out.println("No Hand - You Lose!");
		}
		System.out.println(payout);
		
		Game.window.setHand(handType);
		return payout;
 	}
	
	private int pair() {
		
		Card a, b;
		Card c = null;
		int count = 0;
		int runs = 2;
		boolean quit = false;
		ArrayList<Card> handCopy = new ArrayList<>();
		for(int i = 0; i < hand.size(); i++){
			c = hand.get(i);
			handCopy.add(new Card(c.value, c.suit));
		}
		
		while(runs > 0){
			for(int i = 0; i < handCopy.size(); i++) {
				a = handCopy.get(i);
				for(int j = i+1; j < handCopy.size(); j++) {
					b = handCopy.get(j);
					if(a.value == b.value) {
						count++;
						c = handCopy.remove(j);
						handCopy.remove(i);
						quit = true;
						break;
					}
				}
				if(quit){
					quit = false;
					break;
				}	
			}
			if(count == 0)
				break;
			runs--;
		}
		if(count == 1 && c.value < 11){
			count--;
				
		}
		return count;
	}
	
	private boolean nOfAKind(int n) {
		Card a, b;
		int count = 0;
		for(int i = 0; i < hand.size(); i++) {
			a = hand.get(i);
			for(int j = i + 1; j < hand.size(); j++) {
				b = hand.get(j);
				if(a.value == b.value) {
					count++;
					if(count == n-1)
						return true;
				}
			}
			count = 0;
		}
		return false;
	}
	
	private boolean flush() {
		
		Card a = hand.get(0);
		Card b = hand.get(1);
		Card c = hand.get(2);
		Card d = hand.get(3);
		Card e = hand.get(4);
		
		if(a.suit == b.suit && b.suit == c.suit && c.suit == d.suit && d.suit == e.suit)
			return true;
		return false;
		
	}
	
	private boolean royalFlush() {
		
		ArrayList<Card> handCopy = new ArrayList<>();
		for(int i = 0; i < hand.size(); i++){
			Card c = hand.get(i);
			handCopy.add(new Card(c.value, c.suit));
		}
		
		if(straight() && flush()) {
			for(int i = 0; i < hand.size(); i++) {
				if(hand.get(i).value < 10)
					return false;
			}
			return true;
		}
		return false;
		
	}
	
	private boolean fullHouse() {
		Card a, b;
		int k = 0;
		ArrayList<Card> handCopy = new ArrayList<>();
		for(int i = 0; i < hand.size(); i++){
			Card c = hand.get(i);
			handCopy.add(new Card(c.value, c.suit));
		}
		int count = 0;
		for(int i = 0; i < hand.size(); i++) {
			a = handCopy.get(i);
			for(int j = i+1; j < hand.size(); j++) {
				b = hand.get(j);
				if(a.value == b.value) {
					count++;
					if(count == 1)
						k = j;
					if(count == 2) {
						handCopy.remove(j);
						handCopy.remove(k);
						handCopy.remove(i);
						Card c = handCopy.get(0);
						Card d = handCopy.get(1);
						if(c.value == d.value)
							return true;
						return false;
					}
				}
			}
			count = 0;
		}
		return false;
	}
	
	private boolean straight() {

		ArrayList<Card> handCopy = new ArrayList<>();
		for(int i = 0; i < hand.size(); i++){
			Card c = hand.get(i);
			handCopy.add(new Card(c.value, c.suit));
		}
		
		Collections.sort(handCopy, new CardComparator());
		
		Card c1 = handCopy.get(0);
		Card c2 = handCopy.get(1);
		Card c3 = handCopy.get(2);
		Card c4 = handCopy.get(3);
		Card c5 = handCopy.get(4);
		
		if(c1.value+1 == c2.value && c2.value+1 == c3.value){
			if(c3.value+1 == c4.value && c4.value+1 == c5.value)
				return true;
		}
		return false;
		
	}

	public ArrayList<Card> getHand() {
		return hand;
	}
	
}
