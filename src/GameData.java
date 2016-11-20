
/******************************************
 *	Mitchell, Lara
 *	CS 170-02
 *	Final Project: An alphabet learning game
 *  for pre-school or first grade kids.
******************************************/

/**
 *   This class accesses the file AlphabetGameData.txt 
 *   to process new and existing saved scores.
 */

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GameData {
	private File dataFile;
	private Map<String, PlayerRecord> transcript;
	
	
	/**
	 *   No-arg constructor. Calls method to read 
	 *   in player data from the file AlphabetGameData.txt.
	 */
	
	public GameData() throws IOException {
		// This path may not be right if this isn't run from Eclipse.
		dataFile = new File("./docs/AlphabetGameData.txt");
		
		// Create TreeMap to hold read-in data.
		transcript = new TreeMap<String, PlayerRecord>();
		
		readData();
	}
	
	
	/**
	 *   Method to read in saved data from the file AlphabetGameData.txt.
	 */
	
	private void readData() throws IOException {
		String userName, levelStr, scoreStr;
		int level, score;
		
		PlayerRecord tmpRecord;

		// If the file already exists, read in the contents.
		if(dataFile.exists() && dataFile.isFile()) {
			BufferedReader in = new BufferedReader(new FileReader(dataFile));
			String line = in.readLine();
			while(line != null) {
				StringTokenizer token = new StringTokenizer(line, "\t");
				userName = token.nextToken();
				levelStr = token.nextToken();
				scoreStr = token.nextToken();

				level = Integer.parseInt(levelStr);
				score = Integer.parseInt(scoreStr);

				tmpRecord = new PlayerRecord(userName, level, score);
				transcript.put(userName, tmpRecord);

				line = in.readLine();
			}
			in.close();
		} else {
			// Create a new file for the student.
			dataFile.createNewFile();
		}
	}
	
	
	/**
	 *   Method to check for duplicate user names.
	 */
	
	public boolean processDuplicate(String name) {
		boolean duplicate = false;

		if(transcript.containsKey(name)) {
			duplicate = true;
		}
		return(duplicate);
	}
	
	
	/**
	 *   Method to show a dialog box confirming that player 
	 *   data was successfully saved.
	 */
	
	public void showRecord(PlayerRecord record) {		
		JLabel msg = new JLabel("<html>The following record has been saved:<br>" + 
				"Player name: " + record.getUserName() + "<br>" + 
				"Level: " + record.getLevel() + "<br>" + 
				"Score: " + record.getScore() + "</html>");
		msg.setFont(new Font(msg.getFont().getFontName(), Font.PLAIN, 18));
		JOptionPane.showMessageDialog(null,msg,"Record Saved",-1);
	}
	
	
	/**
	 *   Method to update the score of a player already
	 *   whose data is alreayd being saved.
	 */
	
	public void updateRecord(String name, int newLevel, int newScore) throws IOException {
		// Theoretically, the user name "name" will already exist in transcript when
		// this method is called, but in case it doesn't, silently create a new
		// record for the player.
		
		if(transcript.containsKey(name)) {
			transcript.get(name).setLevel(newLevel);
			transcript.get(name).setScore(newScore);
			resaveTranscript();
			showRecord(transcript.get(name));
			
		} else {			
			addRecordCorrection(name, newLevel, newScore);
			showRecord(transcript.get(name));
		}
	}

	
	/**
	 *   Method to add a record for a player who should exist
	 *   in the data file already, but isn't in there for some 
	 *   reason.
	 */
	
	public void addRecordCorrection(String name, int level, int score) throws IOException {
		// This function is specifically for handling problems with finding players
		// that *should* already exist in the file.
		
		// Create the record.
		PlayerRecord newPlayer = new PlayerRecord(name, level, score);

		// Add the record to the transcript.
		transcript.put(name, newPlayer);

		// Update the transcript file.
		// Run resaveTranscript() in order to re-sort the file
		//  after a new record has been added.
		resaveTranscript();
	}
	
	
	/**
	 *   Method to add a new player to the data file.
	 */
	
	public String addRecord(int level, int score) throws IOException {
		String userInput = "";
		String msg = "Please enter a user name:";
		
		boolean isDuplicate;
		boolean ok = false;

		while(!ok) {
			userInput = getName(msg);
			
			if(userInput == "") {
				// The dialog box was cancelled; do nothing and exit.
				ok = true;
			} else {
				// If this record is a duplicate, show message and get new name.
				isDuplicate = processDuplicate(userInput);
	
				// Otherwise, add the record to transcript.
				if(!isDuplicate) {
					// Create the record.
					PlayerRecord newPlayer = new PlayerRecord(userInput, level, score);
	
					// Add the record to the transcript.
					transcript.put(userInput, newPlayer);
	
					// Update the transcript file.
					// Run resaveTranscript() in order to re-sort the file
					//  after a new record has been added.
					//addToTranscript(newCourse);
					resaveTranscript();
	
					showRecord(transcript.get(userInput));

					ok = true;
				} else {
					msg = "That user name has been taken. Please enter a new name:";
				}
			}
		}
		
		return(userInput);
	}
	
	
	/**
	 *   Method to re-save the data file after a change has
	 *   been made to the transcript TreeMap.
	 */
	
	public void resaveTranscript() throws IOException {

		// Double check that the data file exists.
		if(dataFile.exists() && dataFile.isFile()) {
			// Create a temporary text file.
			File tmpFile = new File("./docs/GameData_tmp.txt");
			tmpFile.createNewFile();

			// Open connection to the temporary file.
			PrintWriter out = new PrintWriter(new FileWriter(tmpFile));

			// Add in each course.
			for(PlayerRecord record: transcript.values()) {
				out.println(record.getUserName() + "\t" +
							record.getLevel() + "\t" +
							record.getScore());
			}

			// Close connection to the temporary file.
			out.close();

			// Delete the current data file.
		    dataFile.delete();

            // Rename the temporary file.
            tmpFile.renameTo(dataFile);
		} else {
			throw new IOException();
		}
	}


	/**
	 *   Method to show a dialog box showing all saved player
	 *   scores.
	 */
	
	public void viewScores() {		
		// Define transcriptCopySorted as a sorted TreeMap.
		Map<String, PlayerRecord> transcriptSorted = 
					new TreeMap<String, PlayerRecord>(new ValueComparator(transcript));
		for(PlayerRecord record: transcript.values()) {
			transcriptSorted.put(record.getUserName(), record);
		}
		
		Collection<PlayerRecord> records = transcript.values();
		// "\n %-20s\t%-10s\t%-10s
		String output = String.format("\n %-20s %-10s %-10s", "Player",
										"Level", "Score");
		for(PlayerRecord r : records) {
			output += String.format("\n %-20s %-10d %-10d", r.getUserName(),
					r.getLevel(), r.getScore());
		}
		
		JTextArea textArea = new JTextArea(output, 23, 60);
		textArea.setEditable(false);
        textArea.setOpaque(true);
        textArea.setTabSize(8);

        Font font = new Font("Courier", Font.PLAIN, 16);
        textArea.setFont(font);

        Color color = Color.WHITE;
        textArea.setBackground(color);

        // Include a scrollpane.
        JScrollPane scrollPane = new JScrollPane(textArea);
		JOptionPane.showMessageDialog(null,scrollPane,"Scores",-1);
	}
	
	
	/**
	 *   Method to sort player data by score.
	 */

	private class ValueComparator implements Comparator<Object> {
		private Map<String, PlayerRecord> map;

		public ValueComparator(Map<String, PlayerRecord> map) {
			this.map = map;
		}

		public int compare(Object keyA, Object keyB) {		
			double valueA = map.get(keyA).getScore();
			double valueB = map.get(keyB).getScore();

			int result = 0;
			
			if(valueA < valueB) {
				result = -1;
			} else if(valueA > valueB) {
				result = 1;
			} else {
				result = 0;		// the scores are the same
				
	        	// Default to sorting by user name. There can't be ties
	        	// in user names because the user name is also the key in the
	        	// TreeMap.
				String strvalueA = (String) keyA;	//map.get(keyA).getUserName();
				String strvalueB = (String) keyB;	//map.get(keyB).getUserName();
				result = strvalueA.compareTo(strvalueB);
			}

			return result;
		}
	}
	
	
	/**
	 *   Method to retrieve and validate player name.
	 */
	
	// Method to retrieve and validate a course title.
	private String getName(String message) {
		JTextField Jtitle;
		String title = "";
		int option;
		boolean isValid = false;

		while(!isValid) {
			try {
				Jtitle = new JTextField();

				Object[] prompt = {
					null,
					message, Jtitle
				};

				option = JOptionPane.showConfirmDialog(null, prompt,
									"User Name", JOptionPane.OK_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE);
				
				if(option == JOptionPane.OK_OPTION) {					
					title = Jtitle.getText();
										
					// Title should contain at least one letter, an underscore, and
					// at least one digit.
					if(title == null) {
						throw new Exception("Error: Please enter a user name.");
					}

					title = title.trim();
					if(title.equals("")) {
						throw new Exception("Error: Please enter a user name.");
					}
					
					isValid = true;
					
				} else {
					title = "";	// signal to cancel dialog box
					return title;
				}
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(),
						"User Name",JOptionPane.ERROR_MESSAGE);
			}
		}
		return title;
	}
	
}
