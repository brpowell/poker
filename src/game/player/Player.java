package game.player;

import game.Game;

public class Player {
	
	public Hand hand;
	private int chips;
	private int bet;
	private int startingWallet;
	
	
	public Player(int chips) {
		
		hand = new Hand();
		this.chips = chips;
		bet = 0;
		startingWallet = chips;
	}
	
	public int getChips() {
		return chips;
	}
	
	public int getBet() {
		return bet;
	}
	
	public int addBet() {
		if(bet < 25)
			bet++;
		return bet;
	}
	
	public Hand getHand() {
		return hand;
	}
	
	public void newHand() {
		hand.newHand(Game.deck);
	}
	
	public int addChips(int chips) {
		this.chips += chips;
		return this.chips;
	}
	
	public void bet() {
		chips -= bet;
	}
	
	public void newBet() {
		bet = 0;
	}
	
	public int startCash() {
		return startingWallet;
	}
}
