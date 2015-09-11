package game;
import java.util.Comparator;


public class Card implements Comparator<Card>{
	
	public int value;
	public String suit;
	private boolean replace;
	
	public Card(int value, String suit) {
		this.value = value;
		this.suit = suit;
		replace = false;
	}

	@Override
	public int compare(Card c1, Card c2) {
		return c1.value - c2.value;
	}
	
	public boolean toggleReplace() {
		replace = !replace;
		return replace;
	}
	
	public boolean toReplace() {
		return replace;
	}

}
