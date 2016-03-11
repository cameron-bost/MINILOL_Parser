/**
 * This class will ultimately be implemented as a Lexical 
 * parser for the MINI-LOL language, but for now it is being 
 * implemented for Homework 1.
 * 
 * @author Cameron Bost
 * @version 1.0
 */
public class HW1_Scanner {
	final static char Q_NOT = 'A';
	/**
	 * Automates the process depicted by problem 1 
	 * in Homework 1 for CS 4323 (Compiler Construction).
	 * @param input String given by user.
	 * @return boolean value showing whether or not the 
	 * scanner accepted the string given.
	 */
	public static boolean Scanner_HW1(String input) {
		/** Used as placeholder in input. 
		 * Program terminates when index reaches 
		 * end of input string.*/
		int inputIndex = 0;
		
		/** Whether or not the Scanner accepts the string*/
		boolean accepting = false;
		
		/** Set starting state to be the 
		 * declared initial state Q_NOT. This value
		 * will change throughout the DFA.*/
		char currentState = Q_NOT;
		
		/** Start DFA*/
		while(inputIndex != input.length() - 1){
			/** Since we do not know the destination state, 
			 * we must assume that we aren't accepting.*/
			accepting=false;
			
			/** Simultaneously read next character and 
			 * increment index.*/
			char c_input = input.charAt(inputIndex++);
			
			/** Switch statement based on which state we're in.*/
			switch(currentState){
			case 'A':
				/** Switch statement for input character. 
				 * A similar switch statement is in all 
				 * state options.*/
				switch(c_input){
				case 'a':
					/** Expresses one transition in 
					 * the transition function by 
					 * changing the state of the 
					 * DFA. A similar format is used 
					 * for all state-input combinations.*/
					currentState = 'C';
					break;
				case 'b':
					currentState = 'B';
					break;
				default:
					/** If any state fails to recognize 
					 * a character, the DFA does not 
					 * accept the string given. 
					 * A similar default case is used 
					 * for all states.*/
					return false;
				}
				break;
			case 'B':
				switch(c_input){
				case 'a':
					currentState = 'A';
					break;
				case 'b':
					currentState = 'B';
					break;
				default:
					return false;
				}
				break;
			case 'C':
				switch(c_input){
				case 'a':
					currentState='D';
					/** Since D is an accepting state, we 
					 * set accepting to be true.*/
					accepting = true;
					break;
				case 'b':
					currentState = 'A';
					break;
				default:
					return false;
				}
				break;
			case 'D':
				switch(c_input){
				case 'a':
					currentState = 'C';
					break;
				case 'b':
					currentState = 'B';
					break;
				default:
					return false;
				}
				break;
			}	
		}
		return accepting;
		
	}

}
