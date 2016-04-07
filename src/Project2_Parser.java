import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This program implements a syntax parser for 
 * the MINI-LOL programming language. Project 2
 * in the CS 4323 (Compiler Construction) course 
 * at the University of Oklahoma.
 * 
 * @author Cameron
 * @version 2.0
 */
public class Project2_Parser {
	
	/**Used by DPDA simulation to translate integer symbols to text*/
	final static String[] DPDA_KEYSET = 
		{"Z","[id]","[const]","AN","BOTH OF","BOTH SAEM","DIFF OF","DIFFRINT",
				"EITHER OF","FAIL","FOUND YR","GIMMEH","GTFO","HAI","HOW IZ I",
				"I HAS A","I IZ","IF U SAY SO","IM IN YR","IM OUTTA YR","ITZ A",
				"KTHXBYE","MKAY","NO WAI","NOT","NUMBAR","NUMBR","O RLY?","OIC",
				"OMG","PRODUKT OF","QUOSHUNT OF","R","SUM OF","TROOF","VISIBLE",
				"WILE","WIN","WTF?","YA RLY","YR",";","<lol>","<body>","<stmt>",
				"<input>","<output>","<decl>","<type>","<asmt>","<loop>","<if>",
				"<case>","<omgs>","<omg>","<value>","<return>","<function>",
				"<args>","<arg>","<call>","<expr>","<arith>","<bool>", "<comp>"};
	
	/** Global variable representing the current dfa_state of the DFA. 
	 * 0 = initial dfa_state q_not.*/
	static int dfa_state = 0;
	
	/** Global variable representing the current line number 
	 * being parsed by the compiler.*/
	static int lineNumber = 1;
	
	/**The index of the source code string which 
	 * the parser is currently observing.*/
	static int i_prog = 0;
	
	/** Specified in project charter.*/
	final static int MAX_SYMBOL_COUNT = 100;
	
	/** Symbol table, managed by BOOKKEEPER()*/
	static String[] SYMTAB = new String[MAX_SYMBOL_COUNT];
	
	/** Global variable representing the token currently
	 *  being built. This is necessary in order for the ID
	 *  and const subroutines to track what characters have 
	 *  been read in.*/
	static String token = "";
	
	/** Constant used to output the tokens in a table-like 
	 * style.*/
	final static String OUT_FORMAT = "| %-7s| %-15s| %-15s| %s\n";
	
	/** Constant used to output SYMTAB in a table-like 
	 * style*/
	final static String SYMTAB_FORMAT = "|Line %-3d | %-15s | %-10s |%n";
	
	/**Used to hold output from the lexer.*/
	static String lex_output = "";
	
	/**
	 * Insertion point for program. Calls parser.
	 * @param args program arguments provided via command-line
	 */
	public static void main(String[] args) {
		try {
			String program = readFile("test.lol");
			/**Print out source code.*/
			System.out.println("*****SOURCE CODE*****\n"+program);
			/**Execute LL(1) Parser, which will print its output as it goes.*/
			PARSER(program);
			/**Print SYMTAB contents.*/
			printSYMTAB();
		} catch (IOException e) {
			System.err.println("Could not locate \"test.lol\" . Closing program.");
			System.exit(-1);
		}
	}
	
	/**
	 * Simulates a DPDA for the LL(1) MINI-LOL CFG
	 * @param program The entire source code of the MINI-LOL program.
	 */
	private static void PARSER(String program){
		/**Stack for DPDA machine. All values init to -1.*/
		int[] pda_stack = new int[100];
		for(int i = 0; i<100; i++)pda_stack[i]=-1;
		/**Variables to represent indices in the stack and source code*/
		int i_stack = 0;
		/**The current lookahead symbol.*/
		int lookahead=-1;
		int stepNumber = 0;
		/**Initialize PDA. Push stack start symbol*/
		pda_stack[i_stack]=0;
		System.out.println("**********PARSER CONTENTS**********");
		System.out.format(OUT_FORMAT, "Step #", "STACK TOP", "LOOKAHEAD", "PARSER ACTION");
		for(int i = 0; i<80; i++){
			System.out.print("-");
		}
		System.out.println();
		
		/**Generate parse output for entire program.*/
		int top, t_lookahead=-1;
		String action = "";
		while(pda_stack[0]!=-1){
			if(lookahead==-1)while(i_prog < program.length() && (lookahead=SCANNER(program.charAt(i_prog++)))==-1);
			top = pda_stack[i_stack];
			switch(top){
			case 0:
				/** Push <lol>*/
				switch(lookahead){
				case -1:
					return;
				default:
					pda_stack[++i_stack]=42;
					action = "Push <lol>";
				}
				break;
			/**The following cases span all terminal symbols.*/
			case 1:case 2:case 3:case 4:case 5:case 6:case 7:case 8:
			case 9:case 10:case 11:case 12:case 13:case 14:case 15:
			case 16:case 17:case 18:case 19:case 20:case 21:case 22:
			case 23:case 24:case 25:case 26:case 27:case 28:case 29:
			case 30:case 31:case 32:case 33:case 34:case 35:case 36:
			case 37:case 38:case 39:case 40:case 41:
				if(lookahead==pda_stack[i_stack]){
					/**Consume Symbol on top of stack*/
					action ="Consume \""+DPDA_KEYSET[lookahead]+"\"";
					pda_stack[i_stack--]=-1;
					t_lookahead=lookahead;
					lookahead=-1;
				}
				else{
					SYN_ERROR(DPDA_KEYSET[pda_stack[i_stack]]+", found "+DPDA_KEYSET[lookahead]);
					return;
				}
				break;
			/**<lol>*/
			case 42:
				switch(lookahead){
				case 13:
					/**<lol> -> HAI <body> KTHXBYE*/
					pda_stack[i_stack]=21;
					pda_stack[++i_stack]=43;
					pda_stack[++i_stack]=13;
					action="<lol> -> HAI <body> KTHXBYE";
					break;
				default:
					SYN_ERROR(DPDA_KEYSET[42]);
					return;
				}
				break;
			/**<body>*/
			case 43:
				switch(lookahead){
				/**<body> -> <stmt> ; <body>*/
				case 11:case 35:case 15:case 1:case 18:case 27:case 38:case 10:case 12:case 14:case 16:
					pda_stack[i_stack]=43;
					pda_stack[++i_stack]=41;
					pda_stack[++i_stack]=44;
					action="<body> -> <stmt>";
					break;
				/**<body> -> \epsilon*/
				case 21:case 23:case 19:case 28:case 29:case 17:
					pda_stack[i_stack--]=-1;
					action="<body> -> \\epsilon";
				}
				break;
			/**<stmt>*/
			case 44:
				switch(lookahead){
				/**<stmt> -> <input>*/
				case 11:
					pda_stack[i_stack]=45;
					action = "<stmt> -> "+DPDA_KEYSET[45];
					break;
				/**<stmt> -> <output>*/
				case 35:
					pda_stack[i_stack]=46;
					action = "<stmt> -> "+DPDA_KEYSET[46];
					break;
				/**<stmt> -> <decl>*/
				case 15:
					pda_stack[i_stack]=47;
					action = "<stmt> -> "+DPDA_KEYSET[47];
					break;
				/**<stmt> -> <asmt>*/
				case 1:
					pda_stack[i_stack]=49;
					action = "<stmt> -> "+DPDA_KEYSET[49];
					break;
				/**<stmt> -> <loop>*/
				case 18:
					pda_stack[i_stack]=50;
					action = "<stmt> -> "+DPDA_KEYSET[50];
					break;
				/**<stmt> -> <if>*/
				case 27:
					pda_stack[i_stack]=51;
					action = "<stmt> -> "+DPDA_KEYSET[51];
					break;
				/**<stmt> -> <case>*/
				case 38:
					pda_stack[i_stack]=52;
					action = "<stmt> -> "+DPDA_KEYSET[52];
					break;
				/**<stmt> -> <return>*/
				case 10:case 12:
					pda_stack[i_stack]=56;
					action = "<stmt> -> "+DPDA_KEYSET[56];
					break;
				/**<stmt> -> <function>*/
				case 14:
					pda_stack[i_stack]=57;
					action = "<stmt> -> "+DPDA_KEYSET[57];
					break;
				/**<stmt> -> <call>*/
				case 16:
					pda_stack[i_stack]=60;
					action = "<stmt> -> "+DPDA_KEYSET[60];
					break;
				}
				break;
			case 45:
				pda_stack[i_stack] = 1;
				pda_stack[++i_stack] = 11;
				action = "<input> -> GIMMEH [id]";
				break;
			case 46:
				pda_stack[i_stack] = 61;
				pda_stack[++i_stack] = 35;
				action = "<output> -> VISIBLE <expr>";
				break;
			case 47:
				pda_stack[i_stack] = 48;
				pda_stack[++i_stack] = 20;
				pda_stack[++i_stack] = 1;
				pda_stack[++i_stack] = 15;
				action = "<decl> -> I HAS A [id] ITZ A <type>";
				break;
			case 48:
				switch(lookahead){
				case 25:case 26:case 34:
					pda_stack[i_stack] = lookahead;
					action = "<type> -> "+DPDA_KEYSET[lookahead];
					break;	
				default:
					SYN_ERROR("NUMBR, NUMBAR, or TROOF");
					return;
				}
				break;
			case 49:
				pda_stack[i_stack]=61;
				pda_stack[++i_stack]=32;
				pda_stack[++i_stack]=1;
				action="<asmt> -> [id] R <expr>";
				break;
			case 50:
				pda_stack[i_stack]=1;
				pda_stack[++i_stack]=19;
				pda_stack[++i_stack]=43;
				pda_stack[++i_stack]=63;
				pda_stack[++i_stack]=36;
				pda_stack[++i_stack]=1;
				pda_stack[++i_stack]=18;
				action = "<loop> -> IM IN YR [id] WILE <bool> "
						+ "<body> IM OUTTA YR [id]";
				break;
			case 51:
				pda_stack[i_stack]=28;
				pda_stack[++i_stack]=43;
				pda_stack[++i_stack]=23;
				pda_stack[++i_stack]=43;
				pda_stack[++i_stack]=39;
				pda_stack[++i_stack]=63;
				pda_stack[++i_stack]=27;
				action = "<if> -> O RLY? <bool> YA RLY <body> "
						+ "NO WAI <body> OIC";
				break;
			case 52:
				pda_stack[i_stack]=28;
				pda_stack[++i_stack]=53;
				pda_stack[++i_stack]=61;
				pda_stack[++i_stack]=38;
				action = "<case> -> WTF? <expr> <omgs> OIC";
				break;
			case 53:
				switch(lookahead){
				case 29:
					pda_stack[i_stack]=53;
					pda_stack[++i_stack]=54;
					action = "<omgs> -> <omg> <omgs>";
					break;
				case 28:
					pda_stack[i_stack--]=-1;
					action = "<omgs> -> \\epsilon";
					break;
				}
				break;
			case 54:
				pda_stack[i_stack]=43;
				pda_stack[++i_stack]=55;
				pda_stack[++i_stack]=29;
				action = "<omg> -> OMG <value> <body>";
				break;
			case 55:
				switch(lookahead){
				case 2:
					pda_stack[i_stack]=2;
					action = "<value> -> [const]";
					break;
				case 37:
					pda_stack[i_stack]=37;
					action = "<value> -> WIN";
					break;
				case 9:
					pda_stack[i_stack]=9;
					action = "<value> -> FAIL";
					break;
				default:
					SYN_ERROR("[const], WIN, or FAIL");
					return;
				}
				break;
			case 56:
				switch(lookahead){
				case 10:
					pda_stack[i_stack]=61;
					pda_stack[++i_stack]=10;
					action = "<return> -> FOUND YR <expr>";
					break;
				case 12:
					pda_stack[i_stack]=12;
					action = "<return> -> GTFO";
					break;
				}
				break;
			case 57:
				pda_stack[i_stack]=17;
				pda_stack[++i_stack]=43;
				pda_stack[++i_stack]=58;
				pda_stack[++i_stack]=1;
				pda_stack[++i_stack]=14;
				action = "<function> -> HOW IZ I [id] <args> "
						+ "<body> IF U SAY SO";
				break;
			case 58:
				switch(lookahead){
				case 40:
					pda_stack[i_stack]=58;
					pda_stack[++i_stack]=59;
					action = "<args> -> <arg> <args>";
					break;
				case 17:case 22:case 11:case 35:case 15:case 1:
				case 18:case 27:case 38:case 10:case 12:case 14:case 16:
					pda_stack[i_stack--]=-1;
					action = "<args> -> \\epsilon";
					break;
				}
				break;
			case 59:
				pda_stack[i_stack]=1;
				pda_stack[++i_stack]=40;
				action = "<arg> -> YR [id]";
				break;
			case 60:
				pda_stack[i_stack]=22;
				pda_stack[++i_stack]=58;
				pda_stack[++i_stack]=1;
				pda_stack[++i_stack]=16;
				action = "<call> -> I IZ [id] <args> MKAY";
				break;
			case 61:
				switch(lookahead){
				case 6:case 33:case 30:case 31:case 1:case 2:
					pda_stack[i_stack]=62;
					action = "<expr> -> <arith>";
					break;
				case 4:case 8:case 24:case 5:case 7:case 37:case 9:
					pda_stack[i_stack]=63;
					action = "<expr> -> <bool>";
					break;
				}
				break;
			case 62:
				switch(lookahead){
				case 33:
					pda_stack[i_stack]=62;
					pda_stack[++i_stack]=3;
					pda_stack[++i_stack]=62;
					pda_stack[++i_stack]=33;
					action = "<arith> -> SUM OF <arith> AN <arith>";
					break;
				case 6:
					pda_stack[i_stack]=62;
					pda_stack[++i_stack]=3;
					pda_stack[++i_stack]=62;
					pda_stack[++i_stack]=6;
					action = "<arith> -> DIFF OF <arith> AN <arith>";
					break;
				case 30:
					pda_stack[i_stack]=62;
					pda_stack[++i_stack]=3;
					pda_stack[++i_stack]=62;
					pda_stack[++i_stack]=30;
					action = "<arith> -> PRODUKT OF <arith> AN <arith>";
					break;
				case 31:
					pda_stack[i_stack]=62;
					pda_stack[++i_stack]=3;
					pda_stack[++i_stack]=62;
					pda_stack[++i_stack]=31;
					action = "<arith> -> QUOSHUNT OF <arith> AN <arith>";
					break;
				case 1:
					pda_stack[i_stack]=1;
					action = "<arith> -> [id]";
					break;
				case 2:
					pda_stack[i_stack]=2;
					action = "<arith> -> [const]";
					break;
				default:
					SYN_ERROR("arithmetic statement");
					return;
				}
				break;
			case 63:
				switch(lookahead){
				case 4:
					pda_stack[i_stack]=63;
					pda_stack[++i_stack]=3;
					pda_stack[++i_stack]=63;
					pda_stack[++i_stack]=4;
					action = "<bool> -> BOTH OF <bool> AN <bool>";
					break;
				case 8:
					pda_stack[i_stack]=63;
					pda_stack[++i_stack]=3;
					pda_stack[++i_stack]=63;
					pda_stack[++i_stack]=8;
					action = "<bool> -> EITHER OF <bool> AN <bool>";
					break;
				case 24:
					pda_stack[i_stack]=63;
					pda_stack[++i_stack]=24;
					action = "<bool> -> NOT <bool>";
					break;
				case 5:case 7:
					pda_stack[i_stack]=64;
					action = "<bool> -> <comp>";
					break;
				case 37:
					pda_stack[i_stack]=37;
					action = "<bool> -> WIN";
					break;
				case 9:
					pda_stack[i_stack]=9;
					action = "<bool> -> FAIL";
					break;
				default:
					SYN_ERROR("bool statement");
					return;
				}
				break;
			case 64:
				switch(lookahead){
				case 5:
					pda_stack[i_stack]=61;
					pda_stack[++i_stack]=3;
					pda_stack[++i_stack]=61;
					pda_stack[++i_stack]=5;
					action = "<comp> -> BOTH SAEM <expr> AN <expr>";
					break;
				case 7:
					pda_stack[i_stack]=61;
					pda_stack[++i_stack]=3;
					pda_stack[++i_stack]=61;
					pda_stack[++i_stack]=7;
					action = "<comp> -> DIFFRINT <expr> AN <expr>";
					break;
				}
				break;
			default:
				System.out.println("Unrecognized token "+pda_stack[i_stack]+". Terminating Program.");
				System.exit(-1);
			}
			stepNumber++;
			System.out.format(OUT_FORMAT, stepNumber, DPDA_KEYSET[top], 
					(lookahead!=-1 ? DPDA_KEYSET[lookahead] : (t_lookahead == -1 ? "-" : DPDA_KEYSET[t_lookahead])),
					action);
			t_lookahead=-1;
		}
	}
	
	/**
	 * Prints out syntax errors encountered while parsing.
	 * @param kw expected value, which is assumed to not be the value
	 *  that was encountered.
	 */
	private static void SYN_ERROR(String kw) {
		System.out.format("ERROR: Line %d: Expected %s\n",lineNumber,kw);
	}
	
	/**
	 * Used to access the correct integer-coding for the keyword.
	 * @param tok the desired keyword
	 * @return the index at which the keyword is located in the 
	 * DPDA_KEYSET array. Otherwise, returns -1.
	 */

	private static int TOINT(String tok){
		if(tok.equals(""))return -1;
		String kw;
		if(tok.contains("$")){
			kw = tok.split("\\$")[1];
		}
		else {
			kw=tok;
		}
		for(int i = 0; i<DPDA_KEYSET.length; i++){
			if(kw.equals(DPDA_KEYSET[i])){
				return i;
			}
		}
		return -1;
	}
	/**
	 * A simple file reader. Returns all read text to the calling method.
	 * @param fName the name of the file to be read.
	 * @return Plain-text form of the file contents
	 * @throws IOException Covers any miscellaneous I/O exceptions 
	 * thrown while reading/opening the file.
	 */
	private static String readFile(String fName) throws IOException{
		String program = "";
		BufferedReader br = new BufferedReader(new FileReader(fName));
		String buffer;
		while((buffer = br.readLine())!=null)program += buffer + "\n";
		
		br.close();
		return program;
	}
	
	/**
	 * Error handler. Writes error messages to the command line.
	 * @param not_token The error text given to the parser.
	 * @return Any residual information appended to the token.
	 */
	private static int ERROR(String not_token){
		System.out.format(OUT_FORMAT,lineNumber,not_token,"ERROR: Unrecognized Token.");
		return TOINT((not_token.endsWith("$;") ? "SS$;" : ""));
	}

	/**
	 * Maintains the SYMTAB object, adding identifiers and 
	 * their associated types as necessary.
	 * @param token Obtained from DFA implementation. Token will be added to the table.
	 * @return the integer-coded form of the type of token being inserted.
	 */
	private static int BOOKKEEPER(String token){
		token = lineNumber+"$"+token;
		String[] token_sp = token.split("\\$");
		String id = token_sp[2];
		int i = 0;
		String ret = token_sp[1];		
		/** Duplicate entry check.*/
		for(; i<MAX_SYMBOL_COUNT && SYMTAB[i] != null; i++){
			if(id.equals(SYMTAB[i].split("\\$")[2]))return TOINT(ret);
		}
		/** Add entry to SYMTAB*/
		SYMTAB[i]=token;
		return TOINT(ret);
	}
	
	/**
	 * Print all values in SYMTAB.
	 */
	private static void printSYMTAB() {
		/** Init local variables*/
		int i = 0;
		String out = "";
		System.out.println("*****SYMTAB CONTENTS*****");
		for(int j = 0; j<42; j++)System.out.print("-");
		System.out.format("%n|Line %3s | %-15s | %-10s |%n", "#","VAR_NAME","VAR_TYPE");
		for(int j = 0; j<42; j++)System.out.print("-");
		System.out.println();
		while((out = SYMTAB[i]) != null && i < MAX_SYMBOL_COUNT){
			String[] sym_sp = out.split("\\$");
			System.out.format(SYMTAB_FORMAT, Integer.parseInt(sym_sp[0]),sym_sp[2],sym_sp[1]);
			i++;
		}
		for(int j = 0; j<42; j++)System.out.print("-");
		System.out.println();
	}


	/**
	 * Simulates a DFA. Returns a single token in Mini-LOL syntax.
	 * @param c the current character being observed by the DFA
	 * @return A token in the format "[type]$[token]". This will be returned as "" until a token has been fully generated.
	 */
	public static int SCANNER(char c){
		if(c=='\n')lineNumber++;
		
		/** Used to return built strings.*/
		String ret;
		/** State conventions are followed as digits RRQQ, where RR is the route being 
		 * followed and QQ is the current sub-dfa_state within RR.*/
		switch(dfa_state){
		
		/**Initial State*/
		case 0:
			switch(c){
			/**Special symbol case.*/
			case ';':
				return TOINT(";");
			case ' ':return TOINT("");
			case '\n':return TOINT("");
			case 'A':dfa_state = 100;break;
			case 'B':dfa_state = 200;break;
			case 'D':dfa_state = 300;break;
			case 'E':dfa_state = 400;break;
			case 'F':dfa_state = 500;break;
			case 'G':dfa_state = 600;break;
			case 'H':dfa_state = 700;break;
			case 'I':dfa_state = 800;break;
			case 'K':dfa_state = 900;break;
			case 'M':dfa_state = 1000;break;
			case 'N':dfa_state = 1100;break;
			case 'O':dfa_state = 1200;break;
			case 'P':dfa_state = 1300;break;
			case 'Q':dfa_state = 1400;break;
			case 'R':dfa_state = 1500;break;
			case 'S':dfa_state = 1600;break;
			case 'T':dfa_state = 1700;break;
			case 'V':dfa_state = 1800;break;
			case 'W':dfa_state = 1900;break;
			case 'Y':dfa_state = 2000;break;
			/**GOTO ID subroutine*/
			case 'a':case 'b':case 'c':case 'd':case 'e':case 'f':case 'g':
			case 'h':case 'i':case 'j':case 'k':case 'l':case 'm':case 'n':
			case 'o':case 'p':case 'q':case 'r':case 's':case 't':case 'u':
			case 'v':case 'w':case 'x':case 'y':case 'z':dfa_state = 5000;break;
			/**GOTO CONST subroutine*/
			case '0':case '1':case '2':case '3':case '4':case '5':
			case '6':case '7':case '8':case '9':dfa_state = 6000;break;
			case '.':dfa_state = 7000;break;
			/**DEFAULT: unrecognized character*/
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
		case 100:
			switch(c){
			case 'N':dfa_state = 101;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
		case 200:
			switch(c){
			case 'O':dfa_state = 201;break;
			case 'T':dfa_state = 202;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
		case 201:
			switch(c){
			case 'T':dfa_state = 203;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
		case 202:
			switch(c){
			case 'W':dfa_state = 9998;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 203:
			switch(c){
			case 'H':dfa_state = 205;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		/** Note: case 204 is missing because I'm dumb. */
		case 205:
			switch(c){
			case ' ':dfa_state = 206;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 206:
			switch(c){
			case 'O':dfa_state = 207;break;
			case 'S':dfa_state = 208;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 207:
			switch(c){
			case 'F':dfa_state = 209;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 208:
			switch(c){
			case 'A':dfa_state = 210;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 210:
			switch(c){
			case 'E':dfa_state = 211;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 211:
			switch(c){
			case 'M':dfa_state = 212;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 300:
			switch(c){
			case 'I':dfa_state = 301;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 301:
			switch(c){
			case 'F':dfa_state = 302;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 302:
			switch(c){
			case 'F':dfa_state = 303;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 303:
			switch(c){
			case ' ':dfa_state = 304;break;
			case 'R':dfa_state = 305;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
		case 304:
			switch(c){
			case 'O':dfa_state = 306;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 305:
			switch(c){
			case 'I':dfa_state = 307;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 306:
			switch(c){
			case 'F':dfa_state = 308;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 307:
			switch(c){
			case 'N':dfa_state = 309;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 309:
			switch(c){
			case 'T':dfa_state = 310;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 400:
			switch(c){
			case 'I':dfa_state = 401;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 401:
			switch(c){
			case 'T':dfa_state = 402;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 402:
			switch(c){
			case 'H':dfa_state = 403;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 403:
			switch(c){
			case 'E':dfa_state = 404;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 404:
			switch(c){
			case 'R':dfa_state = 405;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 405:
			switch(c){
			case ' ':dfa_state = 406;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 406:
			switch(c){
			case 'O':dfa_state = 407;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
			
		case 407:
			switch(c){
			case 'F':dfa_state = 408;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 500:
			switch(c){
			case 'A':dfa_state = 501;break;
			case 'O':dfa_state = 502;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 501:
			switch(c){
			case 'I':dfa_state = 503;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 502:
			switch(c){
			case 'U':dfa_state = 504;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 503:
			switch(c){
			case 'L':dfa_state = 505;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 504:
			switch(c){
			case 'N':dfa_state = 506;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 506:
			switch(c){
			case 'D':dfa_state = 507;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 507:
			switch(c){
			case ' ':dfa_state = 508;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 508:
			switch(c){
			case 'Y':dfa_state = 509;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 509:
			switch(c){
			case 'R':dfa_state = 510;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 600:
			switch(c){
			case 'I':dfa_state = 601;break;
			case 'T':dfa_state = 602;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 601:
			switch(c){
			case 'M':dfa_state = 603;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
		case 602:
			switch(c){
			case 'F':dfa_state = 604;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 603:
			switch(c){
			case 'M':dfa_state = 605;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 604:
			switch(c){
			case 'O':dfa_state = 606;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 605:
			switch(c){
			case 'E':dfa_state = 607;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 607:
			switch(c){
			case 'H':dfa_state = 608;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 700:
			switch(c){
			case 'A':dfa_state = 701;break;
			case 'O':dfa_state = 702;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 701:
			switch(c){
			case 'I':dfa_state = 703;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 702:
			switch(c){
			case 'W':dfa_state = 704;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 704:
			switch(c){
			case ' ':dfa_state = 705;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 705:
			switch(c){
			case 'I':dfa_state = 706;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 706:
			switch(c){
			case 'Z':dfa_state = 707;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 707:
			switch(c){
			case ' ':dfa_state = 708;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 708:
			switch(c){
			case 'I':dfa_state = 709;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 800:
			switch(c){
			case ' ':dfa_state = 801;break;
			case 'F':dfa_state = 809;break;
			case 'M':dfa_state = 819;break;
			case 'T':dfa_state = 834;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 801:
			switch(c){
			case 'H':dfa_state = 802;break;
			case 'I':dfa_state = 807;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 802:
			switch(c){
			case 'A':dfa_state = 803;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 803:
			switch(c){
			case 'S':dfa_state = 804;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 804:
			switch(c){
			case ' ':dfa_state = 805;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 805:
			switch(c){
			case 'A':dfa_state = 806;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 807:
			switch(c){
			case 'Z':dfa_state = 808;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 809:
			switch(c){
			case ' ':dfa_state = 810;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 810:
			switch(c){
			case 'U':dfa_state = 811;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 811:
			switch(c){
			case ' ':dfa_state = 812;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 812:
			switch(c){
			case 'S':dfa_state = 813;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 813:
			switch(c){
			case 'A':dfa_state = 814;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 814:
			switch(c){
			case 'Y':dfa_state = 815;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 815:
			switch(c){
			case ' ':dfa_state = 816;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 816:
			switch(c){
			case 'S':dfa_state = 817;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 817:
			switch(c){
			case 'O':dfa_state = 818;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 819:
			switch(c){
			case ' ':dfa_state = 820;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 820:
			switch(c){
			case 'I':dfa_state = 821;break;
			case 'O':dfa_state = 826;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 821:
			switch(c){
			case 'N':dfa_state = 822;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 822:
			switch(c){
			case ' ':dfa_state = 823;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 823:
			switch(c){
			case 'Y':dfa_state = 824;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 824:
			switch(c){
			case 'R':dfa_state = 825;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 826:
			switch(c){
			case 'U':dfa_state = 827;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 827:
			switch(c){
			case 'T':dfa_state = 828;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 828:
			switch(c){
			case 'T':dfa_state = 829;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 829:
			switch(c){
			case 'A':dfa_state = 830;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 830:
			switch(c){
			case ' ':dfa_state = 831;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 831:
			switch(c){
			case 'Y':dfa_state = 832;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 832:
			switch(c){
			case 'R':dfa_state = 833;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 834:
			switch(c){
			case 'Z':dfa_state = 835;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 835:
			switch(c){
			case ' ':dfa_state = 836;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 836:
			switch(c){
			case 'A':dfa_state = 837;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 900:
			switch(c){
			case 'T':dfa_state = 901;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 901:
			switch(c){
			case 'H':dfa_state = 902;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 902:
			switch(c){
			case 'X':dfa_state = 903;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 903:
			switch(c){
			case 'B':dfa_state = 904;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 904:
			switch(c){
			case 'Y':dfa_state = 905;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 905:
			switch(c){
			case 'E':dfa_state = 906;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1000:
			switch(c){
			case 'K':dfa_state = 1001;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1001:
			switch(c){
			case 'A':dfa_state = 1002;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1002:
			switch(c){
			case 'Y':dfa_state = 1003;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1100:
			switch(c){
			case 'O':dfa_state = 1101;break;
			case 'U':dfa_state = 1107;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1101:
			switch(c){
			case ' ':dfa_state = 1102;break;
			case 'T':dfa_state = 1103;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1102:
			switch(c){
			case 'W':dfa_state = 1104;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1104:
			switch(c){
			case 'A':dfa_state = 1105;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1105:
			switch(c){
			case 'I':dfa_state = 1106;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1107:
			switch(c){
			case 'M':dfa_state = 1108;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1108:
			switch(c){
			case 'B':dfa_state = 1109;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1109:
			switch(c){
			case 'A':dfa_state = 1110;break;
			case 'R':dfa_state = 1111;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1110:
			switch(c){
			case 'R':dfa_state = 1112;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1200:
			switch(c){
			case ' ':dfa_state = 1201;break;
			case 'I':dfa_state = 1206;break;
			case 'M':dfa_state = 1208;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1201:
			switch(c){
			case 'R':dfa_state = 1202;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1202:
			switch(c){
			case 'L':dfa_state = 1203;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1203:
			switch(c){
			case 'Y':dfa_state = 1204;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1204:
			switch(c){
			case '?':dfa_state = 1205;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1206:
			switch(c){
			case 'C':dfa_state = 1207;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1208:
			switch(c){
			case 'G':dfa_state=1209;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1300:
			switch(c){
			case 'R':dfa_state=1301;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1301:
			switch(c){
			case 'O':dfa_state=1302;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1302:
			switch(c){
			case 'D':dfa_state=1303;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1303:
			switch(c){
			case 'U':dfa_state=1304;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1304:
			switch(c){
			case 'K':dfa_state=1305;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1305:
			switch(c){
			case 'T':dfa_state=1306;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1306:
			switch(c){
			case ' ':dfa_state=1307;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1307:
			switch(c){
			case 'O':dfa_state=1308;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1308:
			switch(c){
			case 'F':dfa_state=1309;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1400:
			switch(c){
			case 'U':dfa_state=1401;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1401:
			switch(c){
			case 'O':dfa_state=1402;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1402:
			switch(c){
			case 'S':dfa_state=1403;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1403:
			switch(c){
			case 'H':dfa_state=1404;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1404:
			switch(c){
			case 'U':dfa_state=1405;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1405:
			switch(c){
			case 'N':dfa_state=1406;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1406:
			switch(c){
			case 'T':dfa_state=1407;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1407:
			switch(c){
			case ' ':dfa_state=1408;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1408:
			switch(c){
			case 'O':dfa_state=1409;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1409:
			switch(c){
			case 'F':dfa_state=1410;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1600:
			switch(c){
			case 'U':dfa_state=1601;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1601:
			switch(c){
			case 'M':dfa_state=1602;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1602:
			switch(c){
			case ' ':dfa_state=1603;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1603:
			switch(c){
			case 'O':dfa_state=1604;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1604:
			switch(c){
			case 'F':dfa_state=1605;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1700:
			switch(c){
			case 'R':dfa_state=1701;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1701:
			switch(c){
			case 'O':dfa_state=1702;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1702:
			switch(c){
			case 'O':dfa_state=1703;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1703:
			switch(c){
			case 'F':dfa_state=1704;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1800:
			switch(c){
			case 'I':dfa_state=1801;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1801:
			switch(c){
			case 'S':dfa_state=1803;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1803:
			switch(c){
			case 'I':dfa_state=1804;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1804:
			switch(c){
			case 'B':dfa_state=1805;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1805:
			switch(c){
			case 'L':dfa_state=1806;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1806:
			switch(c){
			case 'E':dfa_state=1807;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1900:
			switch(c){
			case 'I':dfa_state = 1901;break;
			case 'T':dfa_state = 1905;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1901:
			switch(c){
			case 'L':dfa_state = 1902;break;
			case 'N':dfa_state = 1904;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1902:
			switch(c){
			case 'E':dfa_state = 1903;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1905:
			switch(c){
			case 'F':dfa_state = 1906;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		case 1906:
			switch(c){
			case '?':dfa_state = 1907;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;

		case 2000:
			switch(c){
			case 'A':dfa_state = 2002;break;
			case 'R':dfa_state = 2003;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
		case 2002:
			switch(c){
			case ' ':dfa_state = 2004;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;

		case 2004:
			switch(c){
			case 'R':dfa_state = 2005;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
		case 2005:
			switch(c){
			case 'L':dfa_state = 2006;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
		case 2006:
			switch(c){
			case 'Y':dfa_state = 2007;break;
			default:dfa_state=9999;return SCANNER(c);
			}
			break;
			
		/** Accepting states*/
		case 101:case 209:case 212:case 308:case 310:
		case 408:case 505:case 510:case 606:case 608:
		case 703:case 709:case 806:case 808:case 818:
		case 825:case 833:case 837:case 906:case 1003:
		case 1103:case 1106:case 1111:case 1112:case 1205:
		case 1207:case 1209:case 1309:case 1410:case 1500:
		case 1605:case 1704:case 1807:case 1903:case 1904:
		case 1907:case 2003:case 2007:
			switch(c){
			case ' ':case '\n':
				ret = token;dfa_state = 0;token = "";
				return TOINT("KEYWORD$"+ret);
			case ';':
				ret = token;dfa_state = 0;token = "";i_prog--;
				return TOINT("KEYWORD$"+ret);
			default:dfa_state=9999;return SCANNER(c);
			}
		
		/** ID subroutine.*/	
		case 5000:
			switch(c){
			case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h':
			case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': case 'o': case 'p':
			case 'q': case 'r': case 's': case 't': case 'u': case 'v': case 'w': case 'x':
			case 'y': case 'z': case '0': case '1': case '2': case '3': case '4': case '5':
			case '6': case '7': case '8': case '9':case '_':break;
			case ' ':case '\n':
				ret = token;
				dfa_state = 0;token = "";
				return BOOKKEEPER("[id]$"+ret);
			case ';':
				ret = token;
				dfa_state = 0;token = "";i_prog--;
				return BOOKKEEPER("[id]$"+ret);
			default:dfa_state=9999;break;
			}
			break;
			
		case 6000:
			switch(c){
			case '0':case '1':case '2':case '3':case '4':case '5':
			case '6':case '7':case '8':case '9':dfa_state = 6000;break;
			
			case '.':dfa_state = 7001;break;
			
			case ' ':case '\n':
				ret = token;
				dfa_state = 0;token = "";
				return BOOKKEEPER("[const]$"+ret);
			case ';':
				ret = token;
				dfa_state = 0;token = "";i_prog--;
				return BOOKKEEPER("[const]$"+ret);
			default:dfa_state=9999;break;
			}
			break;
			
		case 7000:
			switch(c){
			case '0':case '1':case '2':case '3':case '4':case '5':
			case '6':case '7':case '8':case '9':dfa_state = 7001;break;
			default:dfa_state=9999;break;
			}
			
		case 7001:
			switch(c){
			case '0':case '1':case '2':case '3':case '4':case '5':
			case '6':case '7':case '8':case '9':dfa_state = 7001;break;
			case ' ':case '\n':
				ret = token;
				dfa_state = 0;token = "";
				return BOOKKEEPER("[const]$"+ret);
			case ';':
				ret = token;
				dfa_state = 0;token = "";i_prog--;
				return BOOKKEEPER("[const]$"+ret);
			default:dfa_state=9999;break;
			}
			break;
			
			
		/** Comment case. Skip all characters until '\n'.*/
		case 9998:
			switch(c){
			case '\n':
				dfa_state = 0;token = "";
				return TOINT("");
			}
			break;
		
		/** Error case. Generate next token as an error.*/
		case 9999:
			switch(c){
			case ' ':case '\n':
				ret = token;
				dfa_state = 0;token = "";
				return ERROR(ret);
			case ';':
				ret = token;
				dfa_state = 0;token = "";i_prog--;
				return ERROR(ret);
			}
			break;
		default:dfa_state=9999;return SCANNER(c);
		}
		token += c;
		/** Default token return value. Implies that a token has not been found yet.*/
		return TOINT("");
	}//End SCANNER
	
}
