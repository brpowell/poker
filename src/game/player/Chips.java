package game.player;

public class Chips {
	
	private int chips;
	private int bet;
	
	public Chips() {
		chips = 1000;
		bet = 0;
	}
	
	public Chips(int chips) {
		this.chips = chips;
		bet = 0;
	}
	
	public int getChips() {
		return chips;
	}
	
	public int getBet() {
		return bet;
	}
	
	public int submitBet() {
		chips -= bet;
		bet = 0;
		return chips;
	}

}
