
/******************************************
 *	Mitchell, Lara
 *	CS 170-02
 *	Final Project: An alphabet learning game
 *  for pre-school or first grade kids.
******************************************/

/**
 *   This class generates a JPanel that displays
 *   a player's user name, level, and score. 
 */

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ScorePanel extends JPanel {
	private JPanel subPanel;
	private TitledBorder myBorder;
	
	private String panelTitle;

	private String levelString;	
	private String scoreString;

	private JLabel levelLabel;
	private JLabel scoreLabel;

	private JLabel levelTitle;
	private JLabel scoreTitle;
	
	
	/**
	 *   Constructor to generate score panel.
	 *   Initialize user name, level, and score.
	 */
	
	public ScorePanel() {
		panelTitle = "Player 1";
		levelString = "";
		scoreString ="";
		
		buildPanel();
	}
	
	
	/**
	 *   Method to build the score panel.
	 */
	
	private void buildPanel() {
		setLayout(new BorderLayout());

		add(new JLabel(" "), BorderLayout.SOUTH);
		
		subPanel = new JPanel();		
		subPanel.setLayout(new GridLayout(4,1));
		myBorder = BorderFactory.createTitledBorder(null, panelTitle, 
					TitledBorder.CENTER, TitledBorder.TOP, 
					new Font("Dialog",Font.PLAIN,18), Color.BLACK);
		subPanel.setBorder(myBorder);
		subPanel.setBackground(Color.WHITE);

		levelTitle = new JLabel("Level", SwingConstants.CENTER);
		levelTitle.setFont(new Font(levelTitle.getFont().getFontName(), Font.PLAIN, 18));
		subPanel.add(levelTitle);
		
		levelLabel = new JLabel(levelString, SwingConstants.CENTER);
		levelLabel.setFont(new Font(levelLabel.getFont().getFontName(), Font.PLAIN, 18));
		subPanel.add(levelLabel);
		
		scoreTitle = new JLabel("Score", SwingConstants.CENTER);
		scoreTitle.setFont(new Font(scoreTitle.getFont().getFontName(), Font.PLAIN, 18));
		subPanel.add(scoreTitle);
		
		scoreLabel = new JLabel(scoreString, SwingConstants.CENTER);
		scoreLabel.setFont(new Font(scoreLabel.getFont().getFontName(), Font.PLAIN, 18));
		subPanel.add(scoreLabel);
				
		add(subPanel, BorderLayout.CENTER);
	}
	
	
	/**
	 *   Method to reset level and score when
	 *   a new game is started.
	 */
	
	public void reset() {	
		levelString = "";
		levelLabel.setText(levelString);
		
		scoreString = "";
		scoreLabel.setText(scoreString);
		
		panelTitle = "Player 1";
		myBorder.setTitle(panelTitle);
		myBorder.setTitleColor(Color.BLACK);
		
		repaint();
	}
	
	
	/**
	 *   Method to set level.
	 */
	
	public void setLevel(int i) {
		levelString = String.valueOf(i);
		levelLabel.setText(levelString);
		repaint();
	}
	
	
	/**
	 *   Method to set score.
	 */
	
	public void setScore(int i) {
		scoreString = String.valueOf(i);
		scoreLabel.setText(scoreString);
		repaint();
	}
	
	
	/**
	 *   Method to update player name.
	 */
	
	public void updateTitle(String name) {
		panelTitle = name;
		myBorder.setTitle(panelTitle);
		myBorder.setTitleColor(Color.BLUE);
		repaint();
	}
	
	
	/**
	 *   Method to get level.
	 */
	
	public String getLevel() {
		return levelString;
	}
	
	
	/**
	 *   Method to update panel.
	 */
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
