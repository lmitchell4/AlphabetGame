
/******************************************
 *	Mitchell, Lara
 *	CS 170-02
 *	Final Project: An alphabet learning game
 *  for pre-school or first grade kids.
******************************************/

/**
 *   This class generates a session of the 
 *   alphabet learning game.
 */

import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.applet.*;

public class AlphabetGameGUI extends JFrame {
	private final int WINDOW_WIDTH = 750;
	private final int WINDOW_HEIGHT = 600;
	
	private boolean gameInSession;
	private int gameLevel;
	private int gameScore;
	private int difficulty;
	private boolean beingSaved;
	private String userName;
	
	private JPanel panel;
	private JPanel leftPanel;
	private JPanel rightPanel;
		
	private ScorePanel scorePanel;

	private JPanel menuPanel;
	private JButton newGameButton;
	private JButton saveGameButton;
	private JButton viewScoresButton;
	private JButton quitButton;
	
	private JPanel startPanel;
	private JPanel difficultyPanel;
	private JButton[] difficultyButtons;
	
	private GamePanel gamePanel;
	private GameData gameDat;

	
	/**
	 *   No-arg constructor generates and makes
	 *   visible the game window.
	 */
	
	public AlphabetGameGUI() {
		// Call JFrame constructor.
		super("Alphabet Learning Game");

		// Set window size.
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		// Specify what happens when close button is clicked.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Generate an instance of the class that accesses
		// and saves player data.
		try {
			gameDat = new GameData();
		} catch(Exception e) {
			// End program upon I/O error.
			JOptionPane.showMessageDialog(null,
					"Sorry, there was problem and the program needs to exit.","Game Error",-1);
			System.exit(0);
		}
		
		// Build score, menu, and start panels.
		scorePanel = new ScorePanel();
		buildMenuPanel();
		buildStartPanel();
		
		// Build main panel.
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		// Left panel contains score and menu panels.
		leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(3,1));
		leftPanel.add(scorePanel);
		leftPanel.add(menuPanel);
		
		// Right panel will contain the game board.
		rightPanel = new JPanel();
		
		panel.add(leftPanel, BorderLayout.WEST);
		panel.add(rightPanel, BorderLayout.CENTER);
		
		// Start a new game.
		setupNewGame();
		
		// Add panel to the frame's content pane.
		add(panel);
		
		// Display window.
		setVisible(true);			
	}
	
	
	/**
	 *   Method to build the menu panel, which holds
	 *   menu buttons.
	 */

	private void buildMenuPanel() {
		// Create menu panel.
		menuPanel = new JPanel();
		menuPanel.setLayout(new BorderLayout());
		
		// Create a sub menu panel to hold the menu buttons.
		JPanel menuSubPanel = new JPanel();		
		menuSubPanel.setLayout(new GridLayout(4,1));
		menuSubPanel.setBorder(BorderFactory.createTitledBorder(null, "Menu", 
				TitledBorder.CENTER, TitledBorder.TOP, 
				new Font("Dialog",Font.PLAIN,18), Color.BLACK));
		
		// Create button to start a new game.
		newGameButton = new JButton("New Game");
//		newGameButton.setFont(new Font(newGameButton.getFont().getFontName(), Font.PLAIN, 14));
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// If this button is pressed multiple times in a row, don't need to ask
				// about saving.
				if(gameInSession) {
					// Check whether player wants to save their current score.
					boolean check = askAboutSaving("Would you like to save your current score?");
					if(check) {
						// If a game already exists, remove the old panel.
						if(rightPanel.getComponentCount() > 0) {
							rightPanel.removeAll();
						}

						setupNewGame();
					} else {
						// Dialog box was cancelled; don't start a new game.
					}
				} else {
					// Keep the current startPanel up instead of generating a new one.
				}

			}
		});
		
		// Create button to save player's current score.
		saveGameButton = new JButton("Save Score");
		saveGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(gameInSession) {
					saveScore();
				}
			}
			
		});
		
		// Create button to view all saved scores.
		viewScoresButton = new JButton("View Scores");
		viewScoresButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameDat.viewScores();
			}
		});
		
		// Create button to quit the application.
		quitButton = new JButton("Quit");
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				if(gameInSession) {
					// Check if player wants to save their score before quitting.
					boolean check = askAboutSaving("Would you like to save your current score before quitting?");
					if(check) {
						System.exit(0);
					};
				} else {
					System.exit(0);
				}
			}
		});
		
		// Add buttons to sub menu panel.
		menuSubPanel.add(newGameButton);
		menuSubPanel.add(saveGameButton);
		menuSubPanel.add(viewScoresButton);
		menuSubPanel.add(quitButton);
	
		// Add sub menu panel to menu panel.
		menuPanel.add(menuSubPanel, BorderLayout.CENTER);
	}
	
	
	/**
	 *   Method to build the start panel, which asks 
	 *   player to select a difficulty level.
	 */

	private void buildStartPanel() {
		// Create message label.
		JLabel msg = new JLabel("Pick a difficulty level to start playing!", SwingConstants.CENTER);
		msg.setFont(new Font(msg.getFont().getFontName(), Font.PLAIN, 26));

		// Create buttons indicating difficulty.
		difficultyPanel = new JPanel();
		difficultyPanel.setLayout(new GridLayout(1,5,10,10));

		// Add empty label to achieve desired spacing.
		difficultyPanel.add(new JLabel(""));
		
		// Create and add three difficulty buttons that set the
		// value of the variable "difficulty" and generate a new
		// game.
		difficultyButtons = new JButton[3];
		difficultyButtons[0] = new JButton("Easy");
		difficultyButtons[1] = new JButton("Medium");
		difficultyButtons[2] = new JButton("Hard");
		
		for(int i = 0; i < difficultyButtons.length; i++) {
			difficultyButtons[i].addActionListener(new ActionListener() {							
				public void actionPerformed(ActionEvent e) {
					gameInSession = true;
					
					// Update the AlphabetGameGUI variable to match this button's difficulty.
					Object source = e.getSource();
					if(source == difficultyButtons[0]) {
						difficulty = 1;
					} else if(source == difficultyButtons[1]) {
						difficulty = 2;
					} else if(source == difficultyButtons[2]) {
						difficulty = 3;
					}
					
					// Start the game.
					startNewLevel();
					scorePanel.setScore(gameScore);
					scorePanel.setLevel(gameLevel);
				}
			});
			difficultyButtons[i].setFont(new Font(difficultyButtons[i].getFont().getFontName(), Font.PLAIN, 16));
			difficultyButtons[i].setPreferredSize(new Dimension(100, 100));
			difficultyPanel.add(difficultyButtons[i]);
		}
		
		// Add empty label to achieve desired spacing.
		difficultyPanel.add(new JLabel(""));
		
		// Create start panel.
		startPanel = new JPanel();
		startPanel.setLayout(new GridLayout(3,1));
		startPanel.add(new JLabel(""));
		startPanel.add(msg);
		startPanel.add(difficultyPanel);
	}

	
	/**
	 *   Method to set up a new game. Resets game 
	 *   properties and generates a new start panel.
	 */

	private void setupNewGame() {
		// Reset score, level, and difficulty.
		gameInSession = false;
		gameLevel = 1;
		gameScore = 0;
		difficulty = 1;
		beingSaved = false;
		userName = "";
		scorePanel.reset();
		
		// Show start panel to get difficulty level.
		rightPanel.add(startPanel);
		repaint();
		setVisible(true);
	}
	

	/**
	 *   Method to start a new level in an existing
	 *   game.
	 */

	private void startNewLevel() {
		// Remove any existing components in the right panel (namely, startPanel).
		if(rightPanel.getComponentCount() > 0) {
			rightPanel.removeAll();
		}

		// Generate the new game panel.
		gamePanel = new GamePanel(difficulty);
		rightPanel.add(gamePanel);
		repaint();
		setVisible(true);
	}
	
	
	/**
	 *   Method to save player's score.
	 */

	private void saveScore() {
		// If user has already saved once, continue saving under their
		// user name, otherwise ask them for a user name.
		try {
			if(!beingSaved) {	// User has not saved before.
				// Create new player record.
				// addRecord returns "" if dialog box was cancelled.
				userName = gameDat.addRecord(gameLevel, gameScore);
				if(!userName.equals("")) {
					beingSaved = true;
					
					// Show user name on the score panel.
					scorePanel.updateTitle(userName);
					repaint();
				}
			} else {			// User has saved before.
				// Update existing player record.
				// userName *should* already exist in the AlphabetGameData.txt file,
				// but if it doesn't, a new record will be created with that user name.
				gameDat.updateRecord(userName, gameLevel, gameScore);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Sorry, your score was not able to be saved.","Game Error",-1);
		}
	}
	
	
	/**
	 *   Method to check whether or not player wants to 
	 *   save their score.
	 */

	private boolean askAboutSaving(String msg) {
		// This method returns a save status. 
		// If true, then user selected yes or no, and program should 
		// proceed with the next step.
		// If false, then user cancelled the dialog box; don't proceed with the next step.
		int option = JOptionPane.showConfirmDialog(null, msg,"Save Score?",
									JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		if(option == JOptionPane.YES_OPTION) {
			// Call method to save score.
			saveScore();
			
			// Return true as the method status.
			return true;
		} else if(option == JOptionPane.NO_OPTION) {
			// Do nothing. 
			// Return true as the method status.
			return true;
		} else {
			// Do nothing.
			// Return false as the method status.
			return false;
		}
	}
	
	
	/**
	 *   Method to update player's score and show new 
	 *   score on the score panel. Called each time a
	 *   tile is placed correctly.
	 */
	
	public void scoreChange() {
		gameScore++;
		scorePanel.setScore(gameScore);		
	}

	
	/**
	 *   Method to process that a level was completed.
	 *   Updates player's level and shows new level on 
	 *   the score panel. Calls method to start a new level.
	 */

	private void levelCompleted() {
		// Update level.
		gameLevel++;
		scorePanel.setLevel(gameLevel);
		startNewLevel();
	}
	
	
	/**
	 *   Private inner class to AlphabetGameGUI that generates 
	 *   a new game panel. Contains private inner classes that 
	 *   create the letter tiles.
	 */
	
	private class GamePanel extends JPanel {		
		private final int WINDOW_WIDTH = 600;
		private final int WINDOW_HEIGHT = 550;
		
		private JLayeredPane layeredPane;	
		private JPanel bottomPanel;
		private JPanel topPanel;
		
		private String[] letters = {"a","b","c","d","e","f","g","h","i",
									"j","k","l","m","n","o","p","q","r",
									"s","t","u","v","w","x","y","z"};
		private String imageDirectory;
		private int difficulty;

		private int imgSize;
		private int whiteBorderWidth;
		private int endGameWaitTime;
		
		// Number of tiles in the solution.
		private int nSolution;
		
		// Number of blanks in the solution.
		private int nBlank;
		
		private int[][] saveInd;
		private int[] solutionIndex;
		private int[] insideIndex;
		private int[] outsideIndex;

		private StaticTile[] insideTiles;
		private DraggableTile[] outsideTiles;
		HashMap<String,StaticTile> insideTilesMap;	
		
		private int localScore;
		private AudioClip tileSound;
		
		
		/**
		 *  Constructor generates a new game panel with the
		 *  indicated difficulty level.
		 */
		
		public GamePanel(int diff) {
			difficulty = diff;
			
			// localScore is used to keep track of when
			// the current game is completed.
			localScore = 0;
			
			if(difficulty == 1) {
				nSolution = 4;
				nBlank = 2;
				imgSize = 80;
				whiteBorderWidth = 15;
				endGameWaitTime = 1500;
			} else if(difficulty == 2) {
				nSolution = 6;
				nBlank = 4;
				imgSize = 65;
				whiteBorderWidth = 10;
				endGameWaitTime = 1500;
			} else if(difficulty == 3) {
				nSolution = 8;
				nBlank = 6;
				imgSize = 50;
				whiteBorderWidth = 10;
				endGameWaitTime = 2000;
			}
		
			imageDirectory = "images/tiles_" + imgSize + "/";
			
			// Get audio clip to play when a tile is put into place.
			File audiofile = new File("audio/mouse-doubleclick-02.wav");
			URI uri = audiofile.toURI();
			try {
				URL url = uri.toURL();
				tileSound = Applet.newAudioClip(url);
				System.out.println(tileSound);
			} catch (MalformedURLException e) {
				// Send this to the console, but keep running the application.
				System.out.println("Unable to play audio due to MalformedURLException.");
			}
			
			// Build and show the game panel.
			buildPanel();
		}
		
		
		/**
		 *  Method to build and show the game panel.
		 */
		
		public void buildPanel() {			
			// Set up the main panel.
			setPreferredSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
	        setBorder(BorderFactory.createTitledBorder(null, "", 
									TitledBorder.CENTER, TitledBorder.TOP, 
									new Font("Dialog",Font.PLAIN,18), Color.BLACK));
	        setLayout(new GridLayout(1,1));
	        
	        
	        // Create and set up a layered pane.
	        layeredPane = new JLayeredPane();
	        layeredPane.setBorder(BorderFactory.createTitledBorder(null, "", 
									TitledBorder.CENTER, TitledBorder.TOP, 
									new Font("Dialog",Font.PLAIN,18), Color.BLACK));
	        layeredPane.setBorder(BorderFactory.createTitledBorder(null, "", 
									TitledBorder.CENTER, TitledBorder.TOP, 
									new Font("Dialog",Font.PLAIN,18), Color.BLACK));        

			// Get draw of letters to define solution.
			saveInd = RandomNum.generateSpecialSubset(nBlank, nSolution, 2*nSolution, 26);		
			solutionIndex = saveInd[0];		// sequence of numbers
			insideIndex = saveInd[1];		// indices of letters shown on inside
			outsideIndex = saveInd[2];		// indices of letters shown on outside
			
			// These get used throughout the constructor.
			String ltr;
			String imgName;
			ImageIcon tmpIC;
			
			// Create static tiles for the inside letters.
			insideTiles = new StaticTile[insideIndex.length];
			insideTilesMap = new HashMap<String,StaticTile>();
			for(int i = 0; i < insideTiles.length; i++) {
				ltr = letters[solutionIndex[i]];
				if(insideIndex[i] == -1) {
					imgName = imageDirectory + "/outline-" + imgSize + ".png";
					insideTiles[i] = new StaticTile(new ImageIcon(imgName), whiteBorderWidth);
					
					insideTilesMap.put(ltr, insideTiles[i]);
				} else {
					imgName = imageDirectory + ltr + "-" + imgSize + ".png";
					tmpIC = new ImageIcon(imgName);
					insideTiles[i] = new StaticTile(tmpIC, whiteBorderWidth);
				}
			}
			
			// Create draggable tiles for the outside letters.
			outsideTiles = new DraggableTile[outsideIndex.length];
			for(int i = 0; i < outsideIndex.length; i++) {
				ltr = letters[outsideIndex[i]];
				imgName = imageDirectory + ltr + "-" + imgSize + ".png";
				tmpIC = new ImageIcon(imgName);
				
				if(insideTilesMap.containsKey(ltr)) {
					// If this tile is part of the solution, pass it the corresponding blank tile.
					outsideTiles[i] = new DraggableTile(tmpIC, insideTilesMap.get(ltr));
				} else {
					outsideTiles[i] = new DraggableTile(tmpIC);
				}
			}
	        
			// Place inside tiles on the bottom panel.
	        bottomPanel = new JPanel();
	        bottomPanel.setLayout(new GridBagLayout());
	        GridBagConstraints mmc[] = new GridBagConstraints[nSolution];
	        for(int i = 0; i < mmc.length; i++) {
	        	mmc[i] = new GridBagConstraints();
		        mmc[i].gridx = i;
		        mmc[i].gridy = 1;
		        mmc[i].insets = new Insets(0,0,0,0);
		        bottomPanel.add(insideTiles[i],mmc[i]);
	        }
	        
	        // Place outside tiles on the top panel. Use a null layout
	        // so the tiles can be dragged and dropped.
	        topPanel = new JPanel();
	        topPanel.setLayout(null);
	        topPanel.setOpaque(false);

			// Add some "noise" to the placement of the outside tiles - 
	        // like puzzle pieces spread out on a table.
	        int gapWidth = (int) (WINDOW_WIDTH - nSolution*imgSize)/(nSolution + 1);	       
	        int normalTopY = (int) (WINDOW_HEIGHT/2 - ((whiteBorderWidth + imgSize)/2))/2 - imgSize/2;
	        int normalBottomY = (int) WINDOW_HEIGHT - normalTopY - imgSize;
	        int initX, initY;
	        for(int i = 0; i < nSolution; i++) {
	        	topPanel.add(outsideTiles[i]);
	        	initX = gapWidth*(i + 1) + imgSize*i + RandomNum.generatePosNegInt((int) gapWidth/3);
	        	initY = normalTopY + RandomNum.generatePosNegInt((int) normalTopY/3);
	        	outsideTiles[i].setBounds(initX, initY, imgSize, imgSize);
	        }
	        
	        for(int i = 0; i < nSolution; i++) {
	        	topPanel.add(outsideTiles[i + nSolution]);
	        	initX = gapWidth*(i + 1) + imgSize*i + RandomNum.generatePosNegInt((int) gapWidth/3);
	        	initY = normalBottomY + RandomNum.generatePosNegInt((int) normalTopY/3);
	        	outsideTiles[i + nSolution].setBounds(initX, initY, imgSize, imgSize);
	        }

	        // Put the top panel on top of the bottom panel.
	        bottomPanel.setSize(WINDOW_WIDTH, WINDOW_HEIGHT); // Size needed here because of null layout
	        topPanel.setSize(WINDOW_WIDTH, WINDOW_HEIGHT); // Size needed here because of null layout
	        layeredPane.add(bottomPanel, JLayeredPane.DEFAULT_LAYER);
	        layeredPane.add(topPanel, JLayeredPane.PALETTE_LAYER);
	        
	        // Add the layeredPane to the JPanel defined by this class.
			add(layeredPane);		
		}
		
		
		/**
		 *  Method to "drop" the remaining tiles off the screen 
		 *  when the game is completed.
		 */
		
		private void dropTiles() {
			// Get the tiles remaining on the game panel and "drop" them off the screen
			// in a random order.
			
			// Determine the random order in which the tiles will drop.
			Component[] comps = topPanel.getComponents();
			int[] randomizedIndices = RandomNum.generateSubset(comps.length, comps.length);
			
			// Create an action listener to pass to the timer. This is in charge of
			// stopping the source tile's tiler once the counter reaches a set value.
			class MyActionListener implements ActionListener {
				private Component c;
				private int counter;
				
				MyActionListener(Component c) {
					this.c = c;
					counter = 0;
				}
				
				public void actionPerformed(ActionEvent e) {
			        counter++;
					c.setLocation(c.getX(), c.getY() + 5);
					repaint();
			        if(counter == 500) {  // overkill
			            ((Timer)e.getSource()).stop();
			        }
				}
			}
			
			// Create a separate time for each tile.
			javax.swing.Timer[] timer = new javax.swing.Timer[randomizedIndices.length];
			for(int i = 0; i < randomizedIndices.length; i++) {
				timer[i] = new javax.swing.Timer(5, new MyActionListener(comps[i]));
				
				// Delay each tile's dropping motion by a random amount.
				int pause = (int) (Math.random() * 1000);
				timer[i].setInitialDelay(pause);
				
				timer[i].setRepeats(true);
				timer[i].start();
				// The action listener is in charge of stopping each tile's timer.
			}
			
		}
		

		/**
		 *  Method to remove a draggable time when it is placed 
		 *  correctly. The tile's mouse listener methods take care
		 *  of passing the tile's letter icon to the correct static
		 *  tile.
		 */
		
		private void fixTile(DraggableTile dt) {
			// Play clicking sound once.
			tileSound.play();
			
			// Remove the draggable tile that has been placed correctly.
			topPanel.remove(dt);
			
			// Update the current game's score.
			localScore++;
			
			// If all tiles have been placed correctly, the game has been completed.
			if(localScore == nBlank) {
				// Animate the remaining tiles to "drop" off the screen.
				dropTiles();
				
				// Give the dropping tiles some time to drop, then call levelCompleted() 
				// method. 
				// The method call may occur before all of the tiles have finished
				// dropping off of the screen. That's ok. It will be a little different each time.
				javax.swing.Timer timer = new javax.swing.Timer(endGameWaitTime, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// Communicate with outer class AlphabetGameGUI to let it
						// know that the current game has been completed.
						AlphabetGameGUI.this.levelCompleted();					
					}	
				});
				timer.setRepeats(false);
				timer.start();
			}
			
			// Communicate with outer class AlphabetGameGUI to update the 
			// overall score and show new score in the score panel.
			AlphabetGameGUI.this.scoreChange();		
		}
		
		
		/**
		 *  Private inner class to GamePanel that creates draggable
		 *  letter tiles that listen for mouse events.
		 */
		
		private class DraggableTile extends JComponent {
			// Letter icon.
			private Icon icon;
			
			// Reference to the corresponding static
			// tile where this draggable tile should be 
			// placed.
			private StaticTile solutionTile;
			
			// Instance of MouseDragger class, which
			// provides methods for tile to listen for
			// mouse events.
			private MouseDragger dragger;

			
			/**
			 *  Constructor that sets the tile's icon and adds
			 *  mouse listeners. This is for tiles that are NOT 
			 *  part of the solution and hence don't have a 
			 *  corresponding static solution tile.
			 */
			
			public DraggableTile(Icon icon) {
				this.icon = icon;
				this.solutionTile = null;
				
				dragger = new MouseDragger();
		        this.addMouseListener(dragger);
		        this.addMouseMotionListener(dragger);
		        
				setOpaque(false);	// wipes background clear
			}
			
			
			/**
			 *  Constructor that sets the tile's icon and adds
			 *  mouse listeners. This is for tiles that ARE 
			 *  part of the solution, so it also passes a 
			 *  reference to this draggable tile's corresponding 
			 *  static solution tile. It calls the one-arg
			 *  Constructor first, then overwrites the solutionTile variable.
			 */
			
			public DraggableTile(Icon icon, StaticTile solutionTile) {
				this(icon);
				this.solutionTile = solutionTile;
			}
			
			
			/**
			 *  Method to update the tile.
			 */
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				// Show the letter icon.
				icon.paintIcon(this, g, 0, 0);
			}

			
			/**
			 *  Method to get the preferred size of the tile.
			 */
			
		    public Dimension getPreferredSize() {
		    	return new Dimension(icon.getIconWidth(), icon.getIconHeight());
		    }
		    
		    
			/**
			 *  Private inner class to allow the tile to respond
			 *  to the mouse.
			 */    		    

			private class MouseDragger extends MouseAdapter {
			    private Point startPoint;
			    
			    // Reference to the current tile.
			    private DraggableTile thisTile;
			    
			    // Variable to keep track of whether or not
			    // the tile has been dragged into its correct
			    // position.
			    private boolean requirement;
			    
			    
				/**
				 *  No-arg constructor; sets the requirement 
				 *  variable to false.
				 */ 
			    
			    public MouseDragger() {
			    	requirement = false;
			    }
			    
			    
				/**
				 *  Method responding to mouse being pressed.
				 *  Define a reference to the current tile and
				 *  save its current location.
				 */
			    
			    public void mousePressed(MouseEvent e) {
			    	thisTile = (DraggableTile) e.getSource();
			        startPoint = SwingUtilities.convertPoint(thisTile, e.getPoint(), thisTile.getParent());
			    }

			 
				/**
				 *  Method responding to mouse being dragged.
				 *  Allow current tile to be dragged and keep track
				 *  of whether it was dragged into its correct position.
				 */
			    
			    public void mouseDragged(MouseEvent e) {
			    	// Make sure tile cannot be dragged outside of the window.
			        Point location = SwingUtilities.convertPoint(thisTile, e.getPoint(), thisTile.getParent());
			        if(thisTile.getParent().getBounds().contains(location)) {		        
			            Point newLocation = thisTile.getLocation();
			            newLocation.translate(location.x - startPoint.x, location.y - startPoint.y);
			            newLocation.x = Math.max(newLocation.x, 0);
			            newLocation.x = Math.min(newLocation.x, thisTile.getParent().getWidth() - thisTile.getWidth());
			            newLocation.y = Math.max(newLocation.y, 0);
			            newLocation.y = Math.min(newLocation.y, thisTile.getParent().getHeight() - thisTile.getHeight());

			            thisTile.setLocation(newLocation);
			            startPoint = location;
			        }

			        // If this tile is part of the solution, check whether
			        // it was dragged into the correct position. The correct
			        // position is defined as: within a specified number of 
			        // pixels of the corresponding static tile.
					if(solutionTile != null) {						
						int upperLeftX = thisTile.getX();
						int upperLeftY = thisTile.getY();
			
						int correctCenterX = solutionTile.getMyX();
						int correctCenterY = solutionTile.getMyY();
						
						int dx = upperLeftX - correctCenterX;
						int dy = upperLeftY - correctCenterY;
						
						if(-3 <= dx && dx <= 5 && -3 <= dy && dy <= 5) {
							requirement = true;
						} else {
							requirement = false;
						}
					}
			    }

			    
				/**
				 *  Method responding to mouse being released. Mouse 
				 *  must be released will "requirement" is true for  
				 *  the tile to be processed as solved.
				 */
			    
			    public void mouseReleased(MouseEvent e) {
					if(requirement) {
						// Give the corresponding solution tile the correct 
						// letter image.
						solutionTile.updateImage(thisTile.icon);
						
						// Communicate with outer class GamePanel to remove this tile.
						GamePanel.this.fixTile(thisTile);
					}
			    }
			}
		}

		
		/**
		 *  Private inner class to GamePanel that creates static
		 *  letter tiles.
		 */
		
		private class StaticTile extends JComponent {
			private Icon icon;
			
			// Give static tiles a white border.
			private int borderWidth;
			private int totalWidth;
			
			// Upper left corner of the tile icon,
			// not the tile itself.
			private int upperLeftX;
			private int upperLeftY;
			
			
			/**
			 *  Constructor setting the letter icon and
			 *  white border width.
			 */
			
			public StaticTile(Icon icon, int borderwidth) {				
				this.icon = icon;
				this.borderWidth = borderwidth;
				this.totalWidth = icon.getIconWidth() + 2 * borderWidth;
				
				setOpaque(false);	// wipes background clear
			}
		
			
			/**
			 *  Method to update the tile.
			 */
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				// Show the white border.
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, totalWidth, totalWidth);
				
				// Show the letter icon.
				icon.paintIcon(this, g, borderWidth, borderWidth);
				
				// Update upper left corner coordinates.
				upperLeftX = getX() + borderWidth;
				upperLeftY = getY() + borderWidth;
			}
			
			
			/**
			 *  Method to get the preferred size of the tile.
			 */
			
		    public Dimension getPreferredSize() {
		    	return new Dimension(totalWidth, totalWidth);
		    }
		    
		    
			/**
			 *  Method to get the x-coordinate of the upper
			 *  left corner of the tile icon.
			 */
		    
		    protected int getMyX() {
		    	return upperLeftX;
		    }
		    
		    
			/**
			 *  Method to get the y-coordinate of the upper
			 *  left corner of the tile icon.
			 */
		    
		    protected int getMyY() {
		    	return upperLeftY;
		    }
		    
		    
			/**
			 *  Method to update the letter icon when a
			 *  draggable tile is solved.
			 */
		    
		    protected void updateImage(Icon newIcon) {
		    	icon = newIcon;
		    	repaint();
		    }
		} // end StaticTile class
	
	} // end GamePanel class
	
} // end AlphabetGameGUI class











