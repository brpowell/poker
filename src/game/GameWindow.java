package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import game.player.Player;


@SuppressWarnings("serial")
public class GameWindow extends JFrame implements ActionListener, MouseListener{
	
	private ArrayList<JLabel> cardImgs;
	
	private boolean sounds = true;
	private boolean replacing = false;
	private int currentCard = 0;
	
	private Timer timer;
	
	private JButton btnDeal, btnBet, btnMaxBet, btnSounds, btnCashOut;
	private JLabel lblBalance, lblBet;
	private JLabel lblStatus;
	final private Icon cardBack = new ImageIcon("cards/backred.png");
	
	private Player player;
	
	public GameWindow() {
		super("Poker: Jacks or Better");
		
		this.init();
		this.setSize(600, 200);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	void init() {
		player = Game.player;
		
		// Timer for displaying cards
		timer = new Timer(300, this);
		timer.setRepeats(true);
		
		// Background container for holding other panels
		ImageIcon bgIcon = new ImageIcon("tableTexture.jpg");
		Image bgImg = bgIcon.getImage();
		BackgroundPanel globalPanel = new BackgroundPanel(bgImg);
		globalPanel.setLayout(new BorderLayout());
		
		// Create panels
		JPanel cardPanel = new JPanel();
		JPanel btnPanel = new JPanel();
		JPanel lblPanel = new JPanel();
		JPanel statusPanel = new JPanel();		
		btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.PAGE_AXIS));

		// Create labels for displaying cards
		cardImgs = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			cardImgs.add(new JLabel(cardBack));
			cardImgs.get(i).addMouseListener(this);
			cardPanel.add(cardImgs.get(i));
		}
		
		// Create buttons and add actionListener to them
		btnDeal = new JButton("Deal");
		btnBet = new JButton("Bet");
		btnMaxBet = new JButton("Max Bet");
		btnCashOut = new JButton("Cash Out");
		btnDeal.addActionListener(this);
		btnBet.addActionListener(this);
		btnMaxBet.addActionListener(this);
		btnCashOut.addActionListener(this);
		btnDeal.setEnabled(false);
		btnCashOut.setEnabled(false);
		
		// Create status label and set font
		lblStatus = new JLabel("Welcome to Poker");
		Font font = lblStatus.getFont();
		Font boldFont = new Font(font.getFontName(), Font.BOLD, 20);
		lblStatus.setFont(boldFont);
		
		// Create player info labels and set font
		Font infoFont = new Font(boldFont.getFontName(), Font.BOLD, 14);
		lblBalance = new JLabel("Balance: " + player.getChips());
		lblBet = new JLabel("Bet: " + player.getBet());
		lblBalance.setFont(infoFont);
		lblBet.setFont(infoFont);
		lblBalance.setForeground(Color.white);
		lblBet.setForeground(Color.white);
		
		// Add components to panels
		btnPanel.add(btnDeal);
		btnPanel.add(btnBet);
		btnPanel.add(btnMaxBet);
		btnPanel.add(btnCashOut);
		lblPanel.add(lblBalance);
		lblPanel.add(lblBet);
		statusPanel.add(lblStatus);
		globalPanel.add(cardPanel, BorderLayout.CENTER);
		globalPanel.add(btnPanel, BorderLayout.EAST);
		globalPanel.add(lblPanel, BorderLayout.NORTH);
		globalPanel.add(statusPanel, BorderLayout.SOUTH);
		this.add(globalPanel);
	}
	
	// Set status text to hand
	public void setHand(String hand) {
		if(hand.equals("No Hand"))
			lblStatus.setForeground(Color.red);
		else
			lblStatus.setForeground(new Color(51, 204, 51));
		lblStatus.setText(hand);
	}
	
	// Generate new hand and set cardImgs to back
	public void newHand() {
		for(int i = 0; i < 5; i++) {
			cardImgs.get(i).setIcon(cardBack);
		}
		player.newHand();
	}
	
	// Replace the currentCard
	private boolean replace() {
		ArrayList<Card> cards = player.hand.getHand();
		Card c;
		
		for(int i = 0; i < cards.size(); i++) {
			c = cards.get(i);
			if(c.toReplace()) {
				player.hand.replace(i);
				currentCard = i;
				c.toggleReplace();
				return true;
			}
		}
		return false;
	}

	// Display the currentCard
	private void showCard() {
		String suit;
		int value;
		ArrayList<Card> cards = player.hand.getHand();
			
		suit = cards.get(currentCard).suit;
		value = cards.get(currentCard).value;
		Icon cardImage = new ImageIcon("cards/" + suit + value + ".png");
		cardImgs.get(currentCard).setIcon(cardImage);
		playSound("sfx/draw.wav");
	}
	
	private void playSound(String path) {
		
		if(sounds) {
			try {
				File soundFile = new File(path);
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
				Clip clip = AudioSystem.getClip();
				clip.open(audioIn);
				clip.start();
			} catch (UnsupportedAudioFileException e) {
		         e.printStackTrace();
		      } catch (IOException e) {
		         e.printStackTrace();
		      } catch (LineUnavailableException e) {
		         e.printStackTrace();
		      }
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == timer) {
			if(!replacing && currentCard < 5) {
				showCard();
				currentCard++;
			}
			else if(replacing && replace()) {
				showCard();
			}
			else {
				Player p = player;
				currentCard = 0;
				timer.stop();
				if(replacing) {
					p.addChips(p.hand.evaluate(p.getBet()));
					lblBalance.setText("Balance: " + p.getChips());
					p.newBet();
					lblBet.setText("Bet: 0");
					btnBet.setEnabled(true);
					btnMaxBet.setEnabled(true);
					
					if(player.getChips() > player.startCash()) {
						btnCashOut.setEnabled(true);
					}
					else {
						btnCashOut.setEnabled(false);
					}
				}
				else
					btnDeal.setEnabled(true);
				
				replacing = !replacing;
			}
		}
		
		if(e.getSource() == btnBet) {
			btnDeal.setEnabled(true);
			lblBet.setText("Bet: " + player.addBet());
			playSound("sfx/chip.wav");
		}
		else if(e.getSource() == btnDeal) {
			btnDeal.setEnabled(false);
			btnMaxBet.setEnabled(false);
			if(!replacing && player.getBet() > 0) {
				lblStatus.setForeground(Color.black);
				lblStatus.setText("Dealing...");
				player.bet();
				lblBalance.setText("Balance: " + player.getChips());
				btnBet.setEnabled(false);
				newHand();
			}
			else {
				lblStatus.setText("Replacing...");
			}
			timer.start();
		}
		else if(e.getSource() == btnMaxBet) {
			btnDeal.setEnabled(true);
			playSound("sfx/maxbet.wav");
			for(int i = 0; i < player.getChips(); i++)
				player.addBet();
			lblBet.setText("Bet: " + player.getBet());
		}
		else if(e.getSource() == btnSounds) {
			if(sounds)
				btnSounds.setText("Sounds On");
			else
				btnSounds.setText("Sounds Off");
			sounds = !sounds;
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JLabel lblCard;
		if(replacing) {
			for(int i = 0; i < cardImgs.size(); i++) {
				lblCard = cardImgs.get(i);
				if(e.getSource() == cardImgs.get(i)) {
					Card c = player.hand.getHand().get(i);
					if(c.toggleReplace())
						cardImgs.get(i).setIcon(cardBack);
					else {
						String suit = c.suit;
						int value = c.value;
						Icon cardImage = new ImageIcon("cards/" + suit + value + ".png");
						lblCard.setIcon(cardImage);
					}
				}
			}
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
