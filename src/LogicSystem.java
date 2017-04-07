
/**
 * Logic system for tictactoe game
 * 
 * @author Zachary Zoltek 
 * @version 1.0
 * @since 1/28/2016
 */

import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

public class LogicSystem extends TicTacToe
{
	/** Primitive 2d array which holds nine arrays
	* of ints. All arrays are valid winning formats.
	* So if an X was placed at position 0,1, and 2
	* on the board, then it is a win for the player who
	* is using the X */
    final static int[][] WINNINGMOVES = {
            {0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {6,4,2}, {0,4,8}
            
    };
    /** Ties counter to keep track
	* of how many ties occur */
    private int ties = 0;
	/** The logic system needs access
	* to the protected properties of
	* whatever TicTacToe game is using it, so
	* we require a copy of this game */
    private TicTacToe mainGame;
	/** The playaers playing in the game */
    private Player player1;
    private Player player2;
    /** One AI if the user chooses to play against it
    * Initially set to null because we may not use it */
    private AI ai = null;
    /** A char array that will hold a previous board so we can
    * test it against a newer board so determine where moves were made */
    private char[] savedBoard;
    /** currTurn governs what function to call inside of whoseTurn */
    byte currTurn = 0;
    /** Method that decides whose turn function to call */
    private void whoseTurn(boolean AIEnabled)
    {
		/* If ai was passed as false . . .*/
        if(!AIEnabled)
        {
			/* Update the move list and game status, and then
			update our saved board since we already updated the move list */
            updateMoveList();
            updateGameStatus();
            savedBoard = mainGame.getBoardLayout();
			/* If the current turn is zero, then it is player 1's turn */
            if(currTurn == 0)
            {
				/* Check the layout to make sure there are no winning moves
				and that the board isnt full */
                chkLayout();
				/* Update the turn status console */
                updateTurnStatus();
				/* Modify current turn index */
                currTurn++;
                currTurn %= 2;
            
                getPlayer1Turn(false);
            }
			/* If the current turn is one, then it is player 2's turn */
            else if(currTurn == 1)
            {
				/* Check the layout to make sure there are no winning moves
				and that the board isnt full */
                chkLayout();
				/* Update the turn status console */
                updateTurnStatus();
				/* Modify the turn index */
                currTurn++;
                currTurn %= 2;
                /* Get player 2's turn */
                getPlayer2Turn();
            }
        } else { /* If ai was passed as true . . . */
			/* Update the move list and game status */
			updateMoveList();
			updateGameStatus();
			/* Overwrite our saved board */
			savedBoard = mainGame.getBoardLayout();
			/* If the current turn is zero, then it is
			the humans turn */
			if(currTurn == 0)
			{
				/* Check the board layout for any 
				wins or if it is full */
				chkLayout();
				/* Update turn status console */
				updateTurnStatus();
				/* Manipulate current turn */
				++currTurn;
				currTurn %= 2;
				/* Get the player's turn */
				getPlayer1Turn(true);
			} else if(currTurn == 1){ /* AIs turn */
				/* Set the button restriciton to the AIs type */
				mainGame.setButtonRestriction(ai.getLetterType());
				/* Check the layou to for and if the board is full */
				chkLayout();
				/* Update the turn console */
				updateTurnStatus();
				/* Manipulate the turn */
				++currTurn;
				currTurn %= 2;
				/* Get the current board of the game and
				pass it to the AI so we can get its move */
				char[] currBoard = mainGame.getBoardLayout();
				int aiMove = ai.getMove(currBoard);
				/* Click the button that the AI wants to use */
				mainGame.clickButton(aiMove);
				/* Get the next turn with AI enabled */
				whoseTurn(true);
			}
		}

    }
    /** Method that is called when a winner is
	* decided. The winners character should be provided,
	* and this method will check the players against the character
	*
	* It then outputs a dialog box telling the user of the winner,
	* and asks if they wish to play again */
    private void winner(char who)
    {
        if(player1.getLetterType() == who)
        {
            player1.incWins();
            player2.incLosses();
            
            JOptionPane.showConfirmDialog(mainGame, String.format("%s has won!", player1.getName()), "Winner",
                                          JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            
            int restart = JOptionPane.showOptionDialog(mainGame, "Would you like to play again?", "Play again?", JOptionPane.OK_CANCEL_OPTION,
                                        JOptionPane.QUESTION_MESSAGE, null, new String[]{ "Yes", "No" }, null);
            
            if(restart == 0)
            {
                restartLogic();
            } else {
                System.exit(0);
            }                   
        } else if(player2.getLetterType() == who){
            
            player2.incWins();
            player1.incLosses();
            
            JOptionPane.showConfirmDialog(mainGame, String.format("%s has won!", player2.getName()), "Winner",
                                          JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            
            int restart = JOptionPane.showOptionDialog(mainGame, "Would you like to play again?", "Play again?", JOptionPane.OK_CANCEL_OPTION,
                                        JOptionPane.QUESTION_MESSAGE, null, new String[]{ "Yes", "No" }, null);
            
            if(restart == 0)
            {
                restartLogic();
            } else {
                System.exit(0);
            }
        }
    }
    /** Restarts the entire game. First it restarts
	* tictactoe and then it sets currTurn to zero. It then 
	* calls whoseTurn and enables or disables AI accordingly */
    private void restartLogic()
    {
        mainGame.restart();
        currTurn = 0;
		if(ai == null)
			whoseTurn(false);
		else
			whoseTurn(true);
    }
    /** Checks the current layout of the board
	* to find any winners or to see if it is full.
	*
	* It first loops through to find any underscores. If it does
	* then the board isnt full, if it does then the board is full
	*
	* It then loop through all the winning move combinations,
	* checking the board for each one. If it finds one, a winner
	* is declared with the letter that was checked as the parameter
	*
	* If no winner is found, and the board is full, we tell the user
	* it is a tie, and ask if they want to play again 
	*
	* If no winner is found and the board is not full, we just get the next
	* turn */
    private void chkLayout()
    {
        boolean fullBoard = true;
        
        char[] layout = mainGame.getBoardLayout();
        
        for(int i = 0; i < layout.length; i++)
        {
            if(layout[i] == '_')
            {
                fullBoard = false;
                break;
            }
        }
        
        for(int i = 0; i < WINNINGMOVES.length; i++)
        {
            if(layout[WINNINGMOVES[i][0]] == 'X'
            && layout[WINNINGMOVES[i][1]] == 'X'
            && layout[WINNINGMOVES[i][2]] == 'X')
            {
                winner('X');
            }
            else if(layout[WINNINGMOVES[i][0]] == 'O'
            && layout[WINNINGMOVES[i][1]] == 'O'
            && layout[WINNINGMOVES[i][2]] == 'O') 
            {
                winner('O');
            }
        }
        
        if(fullBoard)
        {
            ties++;
            
            JOptionPane.showConfirmDialog(mainGame, String.format("It is a tie!", player2.getName()), "Tie!",
                                          JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                                          
            int restart = JOptionPane.showOptionDialog(mainGame, "Would you like to play again?", "Play again?", JOptionPane.OK_CANCEL_OPTION,
                                        JOptionPane.QUESTION_MESSAGE, null, new String[]{ "Yes", "No" }, null);
            
            if(restart == 0)
            {
                restartLogic();
            } else {
                System.exit(0);
            }
        }
        
    }
	/** Gets the turn for player 1. It sets the button 
	* restriction and then enters a while loop so that no
	* other logic is performed until a button is pressed,
	* and when that happens, the move queue should have a PLAYER2MOVE
	* as its last element, which means we can exit out of the loop
	* and get the next turn */
    private void getPlayer1Turn(boolean aiEn)
    {
        mainGame.setButtonRestriction(player1.getLetterType());
        
        if(mainGame.moveStack.size() == 0)
        {
            while(mainGame.moveStack.size() == 0)
            {}
            
			if(aiEn)
				whoseTurn(true);
			else
				whoseTurn(false);
        }
        
        while(mainGame.moveStack.lastElement() != TTTMoves.PLAYER2MOVE)
        {}
	
        if(aiEn)
			whoseTurn(true);
		else
			whoseTurn(false);
    }
    /** Gets the turn for player 2. It sets the button 
	* restriction and then enters a while loop so that no
	* other logic is performed until a button is pressed,
	* and when that happens, the move queue should have a PLAYER2MOVE
	* as its last element, which means we can exit out of the loop
	* and get the next turn */
    private void getPlayer2Turn()
    {
        mainGame.setButtonRestriction(player2.getLetterType());
        if(mainGame.moveStack.size() == 0)
        {
            while(mainGame.moveStack.size() == 0)
            {}
            whoseTurn(false);
        }
        
        while(mainGame.moveStack.lastElement() != TTTMoves.PLAYER1MOVE)
        {}
	
        whoseTurn(false);
    }
    /** Updates the game status console with
	* current wins, losses and ties */
    private void updateGameStatus()
    {
        String output = String.format("%s Wins: %d%n%s Losses: %d", player1.getName(), 
                                      player1.getWins(), player1.getName(), player1.getLosses());
        
        output += String.format("%n%n%s Wins: %d%n%s Losses: %d", player2.getName(),
                                      player2.getWins(), player2.getName(), player2.getLosses());
                                      
        output += String.format("%n%nTies: %d", ties);
        
        mainGame.gameStatus.setText(output);
        mainGame.gameStatus.setCaretPosition(0);
        
    }
    /** Updates the turn status determined by currTurn */
    private void updateTurnStatus()
    {
        if(currTurn == 0)
        {
            mainGame.turnWindow.setText(String.format("%s's turn", player1.getName()));
        }
        else if(currTurn == 1)
        {
            mainGame.turnWindow.setText(String.format("%s's turn", player2.getName()));
        }
    }
    /** Compares our saved board with the current board
	* to find any changes to list */
    private void updateMoveList()
    {
        String output = "";
        int yPos;
        
        char[] newBoard = mainGame.getBoardLayout();
        /* Loop over the saved board */
        for(int i = 0; i < savedBoard.length; i++)
        {
			/* If we found a pos in the saved board that does
			not match the new board we need to take a look */
            if(savedBoard[i] != newBoard[i])
            {
				/* If the new board has an x, then we 
				need to determine the column it is in */
                if(newBoard[i] == 'X')
                {
                    if(i == 0 || i == 3 || i == 6)
                    {
                        yPos = 1;
                    }
                    else if(i == 1 || i == 4 || i == 7)
                    {
                        yPos = 2;
                    }
                    else
                    {
                        yPos = 3;
                    }
                    /* Append the output string with the move */
                    output += String.format("X placed at position (%d,%d)%n%n", i+1, yPos);
                }
				/* Same with O as with X */
                else if(newBoard[i] == 'O')
                {
                    if(i == 0 || i == 3 || i == 6)
                    {
                        yPos = 1;
                    }
                    else if(i == 1 || i == 4 || i == 7)
                    {
                        yPos = 2;
                    }
                    else
                    {
                        yPos = 3;
                    }
                    
                    output += String.format("O placed at position (%d,%d)%n%n", i+1, yPos);
                }
            }
        }
        /* Append the output string to the movelist window 
		and set the caret position to the END */
        mainGame.moveList.append(output);
        mainGame.moveList.setCaretPosition(mainGame.moveList.getDocument().getLength());
    }
    /* Only constructor for the logicSystem
	Requires the tictactoe game that is running it */
    public LogicSystem(TicTacToe mainGame)
    {
		/* Set the game for this class to the one provided */
        this.mainGame = mainGame;
        /* Save the board immediately */
        savedBoard = mainGame.getBoardLayout();
        /* Ask if the user wants AI */
        int AI = JOptionPane.showOptionDialog(mainGame, "Would you like to play against the AI?", "Play against AI?", JOptionPane.OK_CANCEL_OPTION,
                                        JOptionPane.QUESTION_MESSAGE, null, new String[]{ "Yes", "No" }, null);
        
		/* User doesnt want AI */
        if(AI == 1)
        { 
			/* Initialize the players */
			player1 = new Player();
			player2 = new Player();
			/* Get first players name */
			player1.getPlayerName("Name for Player 1");
			/* Get player 2's name */
			while(true)
			{
				player2.getPlayerName("Name for Player 2");
				
				if(player2.getName().equals(player1.getName()))
				{
					JOptionPane.showConfirmDialog(null, 
					"Player 2 cannot have the same name as Player 1!", 
                    "Invalid Input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
					
					continue;
				}
				
				break;
			}
			/* If player 2's name is john cena, activate an easter egg */
            if(player2.getName().toLowerCase().equals("john cena"))
            {
				mainGame.audioBook.pauseFor(20_000, "background");
                mainGame.audioBook.start("jcena");
				mainGame.X = new ImageIcon(getClass().getResource("resources\\cena2.gif"));
				mainGame.O = new ImageIcon(getClass().getResource("resources\\shrinkingCena.gif"));
            }
            
            String temp;
            /* Get p1 char */
			player1.getPlayerChar("What letter will Player 1 be using? (X|O)");
            
			if(player1.getLetterType() == 'X')
			{
			    player2.setLetterType('O');
			 }
			 else
			 {
			     player2.setLetterType('X');
			 }
            /* Start the game up */
            updateGameStatus();
            updateMoveList();
            updateTurnStatus();

            whoseTurn(false);
			
        } else { /* AI Enabled */
            /* Setup player 1*/
			player1 = new Player();
			/* Get player 1's name */
			player1.getPlayerName("Your name?");
            
            String temp;
			
            char AIChar;
            /* Get player 1's char and then set the AI's
			char accordingly */
            player1.getPlayerChar("What letter will you be using? (X|O)");
                          
            if(player1.getLetterType() == 'O' || player1.getLetterType() == 'o')
            {
                AIChar = 'X';
            }
            else
            {
                AIChar = 'O';
            }
            /* Setup ai class and then setup player2 to be
			used for AI */
            ai = new AI(AIChar);
			player2 = new Player(ai.aiName, AIChar);
            /* Start game */
            updateGameStatus();
            updateMoveList();
            updateTurnStatus();

            whoseTurn(true);
        }
        
        
        
        
        
        
    }
}
