
/******************************************
 *	Mitchell, Lara
 *	CS 170-02
 *	Final Project: An alphabet learning game
 *  for pre-school or first grade kids.
******************************************/

/**
 *   This class generates a score record for 
 *   one player. It is used to save and retrieve
 *   the player's name, level, and score. It can
 *   also update the level and score values. 
 */

public class PlayerRecord {
	private String userName;
	private int level;
	private int score;
	
	
	/**
	 *   Constructor to set user name, level, and score.
	 */
	
	public PlayerRecord(String un, int l, int s) {
		userName = un;
		level = l;
		score = s;
	}
	
	
	/**
	 *   Method to get user name.
	 */
	
	public String getUserName() {
		return userName;
	}
	
	
	/**
	 *   Method to get user level.
	 */
	
	public int getLevel() {
		return level;
	}
	
	
	/**
	 *   Method to get user score.
	 */
	
	public int getScore() {
		return score;
	}
	
	
	/**
	 *   Method to set user level.
	 */
	
	protected void setLevel(int newLevel) {
		level = newLevel;
	}
	
	
	/**
	 *   Method to set user score.
	 */
	
	protected void setScore(int newScore) {
		score = newScore;
	}	
}
