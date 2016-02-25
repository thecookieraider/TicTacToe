
/**
 * AI for TTT. An extension of player with one new function
 *
 * @author Zachary Zoltek
 * @version 1.0
 * @since 1/31/2016
 */
 
 import java.util.Random;
 import javax.swing.JButton;
 
public class AI extends Player
{

  final private static int[][] WINNINGMOVES = {
            {0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {6,4,2}, {0,4,8}
            
  };

  final public static String aiName = "AI";

  /** Our only constructor needs just one parameter:
   * the character that the AI is going to use
   * This constructor simply calls its parent's constructor
   * using "AI" as the name and the parameter provided as its letter type */
   public AI(char charType)
   {
       super(aiName, charType);
   }
   /** The only new method introduced within this class.
    * Requires the board layout from the current game and
    * simply randomly chooses a blank tile to place its move
    * on. If it finds that the tile isn't empty, it calls
    * itself. Once it gets a valid tile, it returns that
    * tile's index */
   public int getMove(char[] board)
   {
		Random rand = new Random();
		
		int toPlay = rand.nextInt(9);
		
		if(board[toPlay] == '_')
		{
			return toPlay;
		}

		return getMove(board);
   }
}
