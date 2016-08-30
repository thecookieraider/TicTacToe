
/**
 * TicTacToe main game
 *
 * @author Zachary Zoltek
 * @version 1.0
 * @since 1/28/2016
 */
 
 /* READ THE README */
 
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Vector;

public class TicTacToe extends JFrame
{
	/** Flag that governs whether or not sound
	* should be played */
    private boolean soundEnabled = true;
	/** String which holds the path to the
	* click sound. */
    private final String click_Audio = "click.wav";
	/** String which holds the path to the 
	* background music */
    private final String bg_Audio = "bg.wav";
	/** Integer that will keep track
	* of whose turn it is. This int will specifically
	* govern what TTTMoves value to push back in the
	* moveStack vector
	*
	* Not to be confused with currTurn which is inside of
	* LogicSystem which decides what function to call inside of
	* whoseTurn() */
    private int currTurnM = 0;
	/** Panel for our frame. Nothing fancy */
    private JPanel p = new JPanel();
	/** Array of XOButtons that are displayed on the board */
    private XOButton buttons[] = new XOButton[9];
	/** LogicSystem for our TicTacToe game */
    private LogicSystem sys;
    
	/** Scroll panes that will holds our text areas. Nothing
	* fancy or unique */
    private JScrollPane conScroll = new JScrollPane();
    private JScrollPane moveScroll = new JScrollPane();
    private JScrollPane statusScroll = new JScrollPane();
    
	/** Image icons for X and O buttons */
	protected ImageIcon X, O;
	/** Textareas for our consoles to display game data */
    protected JTextArea gameStatus = new JTextArea();
    protected JTextArea moveList = new JTextArea();
    protected JTextArea turnWindow = new JTextArea();
	/** A soundhandler to hold and manipulate our various sounds */
    protected SoundHandler audioBook = new SoundHandler();
	/** A vector that works as a mock-event-queue 
	* placing TTTMoves on top of each other so that
	* the program knows who turn it is and when to move on
	* to the next player's move */
    protected Vector<TTTMoves> moveStack = new Vector();
	
	/** Simple method that setups of all three
	* text areas. It does four things for each:
	* it sets the area to non-editable,
	* it enables line wraping,
	* it sets line wraping to occur on word boundaries
	* it finally takes the appropiate scroll pane, and adds
	* the text area to it */
    private void setupTextAreas()
    {
        gameStatus.setEditable(false);
        gameStatus.setLineWrap(true);
        gameStatus.setWrapStyleWord(true);
        
        moveList.setEditable(false);
        moveList.setLineWrap(true);
        moveList.setWrapStyleWord(true);
        
        turnWindow.setEditable(false);
        turnWindow.setLineWrap(true);
        turnWindow.setWrapStyleWord(true);
        
        conScroll.getViewport().add(turnWindow);
        statusScroll.getViewport().add(gameStatus);
        moveScroll.getViewport().add(moveList);
    }
    
	/** Sets up the menu bar for the
	* tic tac toe game. The menubar has one menu: 'File'
	* but this one menu has about ten menu items */
    private void setupMenu()
    {
		/* New menu bar and its only menu */
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        /* Set a mnemonic for the file menu so
		that the user can access it quicky by pressing the 'f' key */
        file.setMnemonic(KeyEvent.VK_F);
        
		/* Menu items */
        JMenuItem changeOs = new JMenuItem("Change 'O' icon . . .");
        JMenuItem changeXs = new JMenuItem("Change 'X' icon . . .");
		JMenuItem changeBG = new JMenuItem("Change background music . . .");
        JMenuItem changeXO = new JMenuItem("Reset X/O to default icons");
		JMenuItem resetBG = new JMenuItem("Reset background music to default");
        JMenuItem exit = new JMenuItem("Exit");
        
        JMenuItem clearConsole1 = new JMenuItem("Clear console 1");
        JMenuItem clearConsole2 = new JMenuItem("Clear console 2");
        JMenuItem clearConsole3 = new JMenuItem("Clear console 3");
        final JMenuItem sound = new JMenuItem("Mute Sound");
		/* Menu items */
		
		/* Set accelerators for important menu items so the user 
		doesnt even have to open up the menu to use them */
		clearConsole1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
		ActionEvent.CTRL_MASK));
		
		clearConsole2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
		ActionEvent.CTRL_MASK));
		
		clearConsole3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,
		ActionEvent.CTRL_MASK));
		
		resetBG.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
		ActionEvent.CTRL_MASK));
		
		changeXO.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
		ActionEvent.CTRL_MASK));
		
		sound.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		ActionEvent.CTRL_MASK));
		
		/* Map actions to each menu item */
		resetBG.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                resetBGMusic();
            }
        });
		
		changeBG.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                changeBGMusic();
            }
        });
		
        exit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        changeOs.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                changeIcon('O');
            }
        });
        
        changeXs.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                changeIcon('X');
            }
        });
        
        changeXO.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                resetIcons();
            }
        });
        
        clearConsole1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                clearConsole(1);
            }
        });
        
        clearConsole2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                clearConsole(2);
            }
        });
        
        
        clearConsole3.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                clearConsole(3);
            }
        });
        
        sound.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                if(soundEnabled)
                {
                    audioBook.stopAll();
					sound.setText("Un-Mute Sound");
                    soundEnabled = false;
                }
                else
                {
					audioBook.start("background");
					sound.setText("Mute Sound");
                    soundEnabled = true;
                }
            }
        });
        
        
        /* Add the menu items to the file
		menu, adding in separators where needed */
        file.add(changeOs);
        file.add(changeXs);
        file.add(changeBG);
		
        file.addSeparator();
        
        file.add(clearConsole1);
        file.add(clearConsole2);
        file.add(clearConsole3);
        
        file.addSeparator();
        
        file.add(sound);
        file.add(changeXO);
		file.add(resetBG);
        file.add(exit);
		/* Add the menu itself to the menubar
		and set the menu bar */
        menuBar.add(file);
        
        setJMenuBar(menuBar);
    }
    /** Method that resets the background
	* music for tictactoe. It simply adds in 
	* the bg music and starts it. Our SoundHandler
	* will automatically stop the current background music
	* and overrite it */
	private void resetBGMusic()
	{
		audioBook.add("bg.wav", "background");
		audioBook.loop("background", true);
		audioBook.start("background");
	}
	/** Method used for changing background music.
	* Opens up a jfilechooser with a filter that only accepts
	* .wave, .aif, .aifc, .au, and .snd */
	private void changeBGMusic()
	{
		JFileChooser fc = new JFileChooser();
        
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new MusicFilter());
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int returnVal = fc.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fc.getSelectedFile();
            audioBook.add(selectedFile.getPath(), "background");
			audioBook.loop("background", true);
			audioBook.start("background");
        }
        else
        {
            return;
        }
	}
	/** Method that sets the restriction for all buttons
	* on the board */
    protected void setButtonRestriction(char arg)
    {
        for(int i = 0; i < buttons.length; i++)
        {
            buttons[i].setRestriction(arg);
        }
    }
	/** Method that returns a 1d char array that is
	* composed of Xs,Os, and underscores. An X
	* at position 0 would mean the first button
	* on the board is an X. Likewise for O.
	* Underscore means the space is free */
	protected char[] getBoardLayout()
	{
		char[] layout = new char[buttons.length];
		
		for(int i = 0; i < buttons.length; i++)
		{
			layout[i] = buttons[i].getIconChar();
		}
		
		return layout;
	}
	/** XOButton that is used by tictactoe. It has three
	* of its own properties: one to hold the current icon that 
	* it is using, and two to determine what letter it is restricted
	* to showing */
    private class XOButton extends JButton implements ActionListener
    {
	  
	   char currIcon;
	   
       private boolean restrictedToO = false;
       private boolean restrictedToX = false;
       /** Only constructor in which X and O icons are set
	   * to their default values, and an action listener is added (itself) */
       public XOButton() {
           X = new ImageIcon(this.getClass().getResource("X.png"));
           O = new ImageIcon(this.getClass().getResource("O.png"));
           addActionListener(this);
           
       }
       /** Method that governs what will happen when the 
	   * button is pressed. If the current turn inside of
	   * TicTacToe is zero, then it is player 1's move, and
	   * if this method is being called, then player 1 just used
	   * their turn. So we put PLAYER2MOVE into our event
	   * queue because it is now player 2's turn. Same logic for player 1 
	   *
	   *
	   * Next the method checks if there are any restrictions on
	   * the button, and if there are (there will be), then it determines
	   * which restriction it is and sets the icon of the button accordingly.
	   *
	   * The currTurnM is then manipulated for the next button and the action listener for this
	   * button is removed since a move has been made on it */
       public void actionPerformed(ActionEvent e)
       {
		   if(currTurnM == 0) {
			   moveStack.add(TTTMoves.PLAYER2MOVE);
		   }
		   else if(currTurnM == 1){
			   moveStack.add(TTTMoves.PLAYER1MOVE);
		   }

           if(restrictedToX || restrictedToO)
           {
                if(restrictedToO) {
                    setIcon(O);
					currIcon = 'O';
				}
                else if(restrictedToX) {
                    setIcon(X);
					currIcon = 'X';
				}
				
				if(soundEnabled)
				{
					audioBook.start("buttonclick");
				}
				
				currTurnM++;
				currTurnM %= 2;
				
				removeActionListener(this);
                return;
           }

       }
       /** Method that redraws the button */
       public void redraw()
       {
          if(currIcon == 'O')
			  setIcon(O);
		  else if(currIcon == 'X')
			  setIcon(X);
		  else
			  setIcon(null);
       }
	   /** Method that returns what character is
	   * currently placed on the button */
       protected char getIconChar()
       {
           if((ImageIcon)getIcon() == X)
		   {
			   return 'X';
		   }
		   else if((ImageIcon)getIcon() == O)
		   {
		       return 'O';
		   }
		   
		   return '_';
       }
	  /** Method that sets the restriction for
	  * the button. Two arguments will produce results:
	  * 'X' and 'O' */
       protected void setRestriction(char arg)
       {
            if(arg == 'O') {
                restrictedToO = true;
                restrictedToX = false;
            }
            else if(arg == 'X') {
                restrictedToX = true;
                restrictedToO = false;
            }
            else {
                return;
            }
       }

    }
    /** A simple function that programatically presses
	* a button within the XOButton array at the specified index */
	protected void clickButton(int pos)
	{
		buttons[pos].doClick();
	}
	/** Restart function for resetting the tictactoe game
	* The move queue is cleared, the current turn is given back to player 1
	* All buttons have their icons set to nothing, their variable currIcon set
	* to a space (meaning null), and we remove the action listeners from each button
	* and re-add them to prevent ourselves from adding two action listeners on a button
	* that may not have been used and thus still has its action listener */
	protected void restart()
	{
		moveStack.clear();
		currTurnM = 0;
		
		for(int i = 0; i < buttons.length; i++)
		{
			buttons[i].setIcon(null);
			buttons[i].currIcon = ' ';
			buttons[i].removeActionListener(buttons[i]);
			buttons[i].addActionListener(buttons[i]);
		}
	}
	/** Method that clears a console determined
	* by the number given */
    private void clearConsole(int console)
    {
        switch(console){
            case(1) :
                turnWindow.setText("");
                break;
            case(2) :
                gameStatus.setText("");
                break;
            case(3) :
                moveList.setText("");
                break;
        }
    }
	/** Method that sets icons X and O to their
	* defaults when the program started. It then
	* redraws all buttons to reflect the changes */
    private void resetIcons()
    {
        X = new ImageIcon(getClass().getResource("X.png"));
        O = new ImageIcon(getClass().getResource("O.png"));
        
        for(int i = 0; i < buttons.length; i++)
        {
           buttons[i].redraw();
        }
    }
	/** Method that uses a jfilechooser to allow the user
	* to choose a new image file for either an X or an O
	* depending on what menu item selected.
	*
	* It then redraws all buttons 
	*
	* Valid file formats are: .png, .jpeg, .jpg, .gif */
    private void changeIcon(char type)
    {
        JFileChooser fc = new JFileChooser();
        
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new ImageFilter());
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int returnVal = fc.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            File selectedFile = fc.getSelectedFile();
            
            if(type == ('O'))
            {
                O = IconGetter.getImageIcon(selectedFile.getPath());
                for(int i = 0; i < buttons.length; i++)
                {
                    buttons[i].redraw();
                }
            }
            else if(type == ('X'))
            {
                X = IconGetter.getImageIcon(selectedFile.getPath());
                for(int i = 0; i < buttons.length; i++)
                {
                    buttons[i].redraw();
                }
            }
            else{
                return;
            }
        }
        else
        {
            return;
        }
    }
    /** Only constructor for the class. Simply
	* creates the JFrame with the title "TicTacToe" */
    public TicTacToe()
    {
        super("TicTacToe");
    }
    /** The method that actually starts the game. Sets a special
	* keybinding for the middle console that triggers an easter egg (CTRL-B)
	* It then gets the screen's height and width and adds in the audio files being used
	*
	* The JFrame's size is then set to be half the width of the screen, but the height
	* would be half the screen, plus 1/3
	*
	* The JFrame is then set to be un-resizable, and set in the middle of the screen
	* It default close operation is to exit on close
	*
	* Our panel for the JFrame is given a grid layout, and all nine buttons
	* in our XOButton array are added to the panel
	*
	* Text area and jmenu constructor functions are called,
	* and finally the textpanes are added to the panel
	* and the panel is added to the frame
	*
	* The frame is brought to the front, set to visible,
	* and then we start up our logic system to get the ball rollin */
	
    public void start(){
		gameStatus.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK), "winning");
		gameStatus.getActionMap().put("winning", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				X = new ImageIcon(getClass().getResource("win.gif"));
				O = new ImageIcon(getClass().getResource("chickenman.gif"));
				for(int i = 0; i < buttons.length; i++)
				{
					buttons[i].redraw();
				}
			} });
        Dimension gd = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = gd.getWidth();
        double screenHeight = gd.getHeight();
        
        audioBook.add(click_Audio, "buttonclick");
        audioBook.add("cena.wav", "jcena");
		audioBook.add(bg_Audio, "background");
		
		audioBook.loop("background", true);
		audioBook.start("background");
		
        setSize((int)screenWidth / 2, (int)screenHeight / 2 + ((int)screenHeight / 3));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        p.setLayout(new GridLayout(4,4));

        for(int i = 0; i<9; i++){
            buttons[i] = new XOButton();
            p.add(buttons[i]);
        }
        
        setupTextAreas();
        setupMenu();
        
        p.add(conScroll);
        p.add(statusScroll);
        p.add(moveScroll);
        
        add(p);
        
        toFront();
        setVisible(true);
        
        sys = new LogicSystem(this);
    }
}
