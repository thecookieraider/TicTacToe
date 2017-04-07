
/**
 * Player class for tictactoe
 * 
 * @author Zachary Zoltek
 * @version 1.0
 * @since 1/28/2016
 */
import javax.swing.JOptionPane;

public class Player
{
	/** Every player has four attributes:
	* a name, win count, loss count, and what
	* letter (either X or O) they are using
	* for the current tictactoe game */
    private String name;
	private int wins;
	private int losses;
	
	private char letter;
	
	/** Constructor #1: takes a name, wins, losses
	* and letter as parameters, and sets all four to 
	* the corresponding user attributes */
	public Player(String name, int wins, int losses,
	char letter)
	{
		this.name = name;
		this.wins = wins;
		this.losses = losses;
		this.letter = letter;
	}
	/** Constructor #2: takes just a name and the
	* char that the player is using for the tictactoe
	* game, and sets the corresponding class variables
	* In this constructor, wins and losses are set to 0 */
	public Player(String name, char letter)
	{
		this.name = name;
		this.letter = letter;
		wins = 0;
		losses = 0;
	}
	/** Constructor #3: Takes zero parameters, but sets
	* wins and losses to zero */
	public Player()
	{
		wins = 0;
		losses = 0;
	}
	/** Getter function that returns the name
	* of the player */
	public String getName()
	{
		return name;
	}
	/** Getter function that returns the wins
	of the player */
	public int getWins()
	{
		return wins;
	}
	/** Getter function that returns the losses
	* of the player */
	public int getLosses()
	{
		return losses;
	}
	/** Getter function that returns the player'set
	* character type (either X or O) */
	public char getLetterType()
	{
		return letter;
	}
	/** Manipulating function that increases wins by 1 */
	public void incWins()
	{
		wins++;
	}
	/** Manipulating function that increases losses by 1 */
	public void incLosses()
	{
		losses++;
	}
	/** Manipulating function that resets wins and losses to zero */
	public void reset()
	{
		wins = 0;
		losses = 0;
	}
	/** Function that displays a joptionpane continually
	* until the user gives a non-empty answer
	*
	* If the user exits the box by either pressing "cancel" or 
	* by hitting the window's 'X' then System.exit(0) is called */
	public void getPlayerName(String toDisplay)
	{
		 while(true){
                
                this.name = JOptionPane.showInputDialog(null, toDisplay);
            
                if(name == "" || name == null)
                {
                    System.exit(0);
                }
                
                if(name.replace(" ", "").length() == 0)
                {
                    JOptionPane.showConfirmDialog(null, "Must have at least one alphanumeric character in your name!", 
                    "Invalid Input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                    
                    continue;
                }
                
                break;
        }   	
	}
	/** Function that displays a joptionpane continually 
	* until the user gives a non-empty answer that is
	* either X or O 
	* 
	* If the user exits the box by either pressing "cancel" or 
	* by hitting the window's 'X' then System.exit(0) is called */
	public void getPlayerChar(String toDisplay)
	{
		String temp;
		while(true){
                
                temp = JOptionPane.showInputDialog(null, toDisplay);
                
                if(temp == "" || temp == null)
                {
                    System.exit(0);
                }
                
                if(!temp.equals("O") && !(temp.equals("o" ))
                && (!temp.equals("X")) && (!temp.equals("x")))
                {
                    JOptionPane.showConfirmDialog(null, "Invalid character type", 
                    "Invalid Input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                    
                    continue;
                }
                else
                {
					this.letter = Character.toUpperCase(temp.charAt(0));
                    break;
                }
        }
	}
	
	public void setLetterType(char letter)
	{
	    this.letter = letter;
	   }
}
