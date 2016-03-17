import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This program implements a lexical parser for 
 * the MINI-LOL programming language. Project 1 
 * in the CS 4323 (Compiler Construction) course 
 * at the University of Oklahoma.
 * 
 * @author Cameron
 * @version 1.0
 */
public class Project1_Scanner {
	
	/** Global variable representing the current state of the DFA. 
	 * 0 = initial state q_not.*/
	static int state = 0;
	
	/** Global variable representing the current line number 
	 * being parsed by the compiler.*/
	static int lineNumber = 0;
	
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
	final static String OUT_FORMAT = "|Line %-3d | %-12s | %-30s |%n";
	
	/** Constant used to output SYMTAB in a table-like 
	 * style*/
	final static String SYMTAB_FORMAT = "|Line %-3d | %-15s | %-10s |%n";
	
	/**
	 * Insertion point for program. Calls parser.
	 * @param args program arguments provided via command-line
	 */
	public static void main(String[] args) {
		
		/** Init all of SYMTAB to null.*/
		setupSYMTAB();
		/** Console Formatting*/
		System.out.println("*****ENCOUNTERED TOKENS*****");
		setupFormatting();
		
		/**Init reader vars*/
		String fileName = "src\\MiniLOLTest";
		BufferedReader reader = null;
		
		try {
			/** Initialize reader object.*/
			reader = new BufferedReader(new FileReader(fileName));
			/** Start read loop.*/
			String line;
			while((line = reader.readLine()) != null){
				/** Unfortunately using readLine causes the '\n' to be 
				 * truncated, so we have to add it back in.*/
				line += '\n';
				/** Increase lineNumber*/
				lineNumber++;
				
				/** Parse line one character at a time.*/
				for(char c: line.toCharArray()){
					
					/** Call SCANNER with current character.*/
					String t_token = SCANNER(c);
					
					/** Scanner will return "" until a token is encountered, 
					 * and so we skip any instances in which "" is returned.*/
					if(!(t_token.equals(""))){
						String[] t_token_sp = t_token.split("\\$");
						System.out.format(OUT_FORMAT,lineNumber,t_token_sp[1],t_token_sp[0]);
						token = "";
					}
				}
			}
			
		/** Catch miscellaneous IO Exceptions.*/
		} catch (FileNotFoundException e) {
			System.err.println("File "+fileName+" could not be found. Terminating program.");
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("Encountered IOException while reading from file. Terminating program.");
			System.exit(-1);
		} finally {
			/** Force any remaining open streams to close.*/
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println("Encountered IOException while closing "
							+ "stream. Terminating program.");
					System.exit(-1);
				}
			}
		}
		/** Close formatting.*/
		for(int i = 1; i<=OUT_FORMAT.length()+29;i++)System.out.print("-");
		System.out.println();
		/** Print SYMTAB */
		printSYMTAB();
	}
	
	/**
	 * Simulates a DFA. Returns a single token in Mini-LOL syntax.
	 * @param c the current character being observed by the DFA
	 * @return A token in the format "[type]$[token]". This will be returned as "" until a token has been fully generated.
	 * @throws InterruptedException 
	 */
	public static String SCANNER(char c){

		/** Used to return built strings.*/
		String ret;
		/** State conventions are followed as digits RRQQ, where RR is the route being 
		 * followed and QQ is the current sub-state within RR.*/
		switch(state){
		
		/**Initial State*/
		case 0:
			switch(c){
			/**Special symbol case.*/
			case ';':
				return "SS$;";
			case ' ':return "";
			case '\n':return "";
			case 'A':state = 100;break;
			case 'B':state = 200;break;
			case 'D':state = 300;break;
			case 'E':state = 400;break;
			case 'F':state = 500;break;
			case 'G':state = 600;break;
			case 'H':state = 700;break;
			case 'I':state = 800;break;
			case 'K':state = 900;break;
			case 'M':state = 1000;break;
			case 'N':state = 1100;break;
			case 'O':state = 1200;break;
			case 'P':state = 1300;break;
			case 'Q':state = 1400;break;
			case 'R':state = 1500;break;
			case 'S':state = 1600;break;
			case 'T':state = 1700;break;
			case 'V':state = 1800;break;
			case 'W':state = 1900;break;
			case 'Y':state = 2000;break;
			/**GOTO ID subroutine*/
			case 'a':case 'b':case 'c':case 'd':case 'e':case 'f':case 'g':
			case 'h':case 'i':case 'j':case 'k':case 'l':case 'm':case 'n':
			case 'o':case 'p':case 'q':case 'r':case 's':case 't':case 'u':
			case 'v':case 'w':case 'x':case 'y':case 'z':state = 5000;break;
			/**GOTO CONST subroutine*/
			case '0':case '1':case '2':case '3':case '4':case '5':
			case '6':case '7':case '8':case '9':state = 6000;break;
			case '.':state = 7000;break;
			/**DEFAULT: unrecognized character*/
			default:state=9999;return SCANNER(c);
			}
			break;
		case 100:
			switch(c){
			case 'N':state = 101;break;
			default:state=9999;return SCANNER(c);
			}
			break;
		case 101:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$AN";
			case ';':
				state = 0;token = "";
				return "KEYWORD$AN;";
			default:state=9999;return SCANNER(c);
			}
		case 200:
			switch(c){
			case 'O':state = 201;break;
			case 'T':state = 202;break;
			default:state=9999;return SCANNER(c);
			}
			break;
		case 201:
			switch(c){
			case 'T':state = 203;break;
			default:state=9999;return SCANNER(c);
			}
			break;
		case 202:
			switch(c){
			case 'W':state = 9998;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 203:
			switch(c){
			case 'H':state = 205;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		/** Note: case 204 is missing because I'm dumb. */
		case 205:
			switch(c){
			case ' ':state = 206;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 206:
			switch(c){
			case 'O':state = 207;break;
			case 'S':state = 208;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 207:
			switch(c){
			case 'F':state = 209;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 208:
			switch(c){
			case 'A':state = 210;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 209:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$BOTH OF";
			case ';':
				state = 0;token = "";
				return "KEYWORD$BOTH OF$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 210:
			switch(c){
			case 'E':state = 211;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 211:
			switch(c){
			case 'M':state = 212;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 212:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$BOTH SAEM";
			case ';':
				state = 0;token = "";
				return "KEYWORD$BOTH SAEM$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 300:
			switch(c){
			case 'I':state = 301;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 301:
			switch(c){
			case 'F':state = 302;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 302:
			switch(c){
			case 'F':state = 303;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 303:
			switch(c){
			case ' ':state = 304;break;
			case 'R':state = 305;break;
			default:state=9999;return SCANNER(c);
			}
			break;
		case 304:
			switch(c){
			case 'O':state = 306;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 305:
			switch(c){
			case 'I':state = 307;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 306:
			switch(c){
			case 'F':state = 308;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 307:
			switch(c){
			case 'N':state = 309;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 308:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$DIFF OF";
			case ';':
				state = 0;token = "";
				return "KEYWORD$DIFF OF$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 309:
			switch(c){
			case 'T':state = 310;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 310:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$DIFFRINT";
			case ';':
				state = 0;token = "";
				return "KEYWORD$DIFFRINT$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 400:
			switch(c){
			case 'I':state = 401;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 401:
			switch(c){
			case 'T':state = 402;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 402:
			switch(c){
			case 'H':state = 403;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 403:
			switch(c){
			case 'E':state = 404;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 404:
			switch(c){
			case 'R':state = 405;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 405:
			switch(c){
			case ' ':state = 406;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 406:
			switch(c){
			case 'O':state = 407;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
			
		case 407:
			switch(c){
			case 'F':state = 408;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 408:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$EITHER OF";
			case ';':
				state = 0;token = "";
				return "KEYWORD$EITHER OF$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 500:
			switch(c){
			case 'A':state = 501;break;
			case 'O':state = 502;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 501:
			switch(c){
			case 'I':state = 503;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 502:
			switch(c){
			case 'U':state = 504;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 503:
			switch(c){
			case 'L':state = 505;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 504:
			switch(c){
			case 'N':state = 506;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 505:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$FAIL";
			case ';':
				state = 0;token = "";
				return "KEYWORD$FAIL$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 506:
			switch(c){
			case 'D':state = 507;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 507:
			switch(c){
			case ' ':state = 508;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 508:
			switch(c){
			case 'Y':state = 509;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 509:
			switch(c){
			case 'R':state = 510;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 510:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$FOUND YR";
			case ';':
				state = 0;token = "";
				return "KEYWORD$FOUND YR$;";
			default:state=9999;return SCANNER(c);
			}
		case 600:
			switch(c){
			case 'I':state = 601;break;
			case 'T':state = 602;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 601:
			switch(c){
			case 'M':state = 603;break;
			default:state=9999;return SCANNER(c);
			}
			break;
		case 602:
			switch(c){
			case 'F':state = 604;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 603:
			switch(c){
			case 'M':state = 605;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 604:
			switch(c){
			case 'O':state = 606;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 605:
			switch(c){
			case 'E':state = 607;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 606:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$GTFO";
			case ';':
				state = 0;token = "";
				return "KEYWORD$GTFO$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 607:
			switch(c){
			case 'H':state = 608;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 608:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$GIMMEH";
			case ';':
				state = 0;token = "";
				return "KEYWORD$GIMMEH$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 700:
			switch(c){
			case 'A':state = 701;break;
			case 'O':state = 702;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 701:
			switch(c){
			case 'I':state = 703;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 702:
			switch(c){
			case 'W':state = 704;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 703:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$HAI";
			case ';':
				state = 0;token = "";
				return "KEYWORD$HAI$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 704:
			switch(c){
			case ' ':state = 705;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 705:
			switch(c){
			case 'I':state = 706;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 706:
			switch(c){
			case 'Z':state = 707;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 707:
			switch(c){
			case ' ':state = 708;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 708:
			switch(c){
			case 'I':state = 709;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 709:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$HOW IZ I";
			case ';':
				state = 0;token = "";
				return "KEYWORD$HOW IZ I$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 800:
			switch(c){
			case ' ':state = 801;break;
			case 'F':state = 809;break;
			case 'M':state = 819;break;
			case 'T':state = 834;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 801:
			switch(c){
			case 'H':state = 802;break;
			case 'I':state = 807;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 802:
			switch(c){
			case 'A':state = 803;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 803:
			switch(c){
			case 'S':state = 804;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 804:
			switch(c){
			case ' ':state = 805;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 805:
			switch(c){
			case 'A':state = 806;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 806:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$I HAS A";
			case ';':
				state = 0;token = "";
				return "KEYWORD$I HAS A$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 807:
			switch(c){
			case 'Z':state = 808;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 808:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$I IZ";
			case ';':
				state = 0;token = "";
				return "KEYWORD$I IZ$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 809:
			switch(c){
			case ' ':state = 810;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 810:
			switch(c){
			case 'U':state = 811;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 811:
			switch(c){
			case ' ':state = 812;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 812:
			switch(c){
			case 'S':state = 813;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 813:
			switch(c){
			case 'A':state = 814;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 814:
			switch(c){
			case 'Y':state = 815;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 815:
			switch(c){
			case ' ':state = 816;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 816:
			switch(c){
			case 'S':state = 817;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 817:
			switch(c){
			case 'O':state = 818;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 818:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$IF U SAY SO";
			case ';':
				state = 0;token = "";
				return "KEYWORD$IF U SAY SO$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 819:
			switch(c){
			case ' ':state = 820;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 820:
			switch(c){
			case 'I':state = 821;break;
			case 'O':state = 826;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 821:
			switch(c){
			case 'N':state = 822;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 822:
			switch(c){
			case ' ':state = 823;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 823:
			switch(c){
			case 'Y':state = 824;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 824:
			switch(c){
			case 'R':state = 825;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 825:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$IM IN YR";
			case ';':
				state = 0;token = "";
				return "KEYWORD$IM IN YR$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 826:
			switch(c){
			case 'U':state = 827;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 827:
			switch(c){
			case 'T':state = 828;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 828:
			switch(c){
			case 'T':state = 829;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 829:
			switch(c){
			case 'A':state = 830;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 830:
			switch(c){
			case ' ':state = 831;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 831:
			switch(c){
			case 'Y':state = 832;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 832:
			switch(c){
			case 'R':state = 833;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 833:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$IM OUTTA YR";
			case ';':
				state = 0;token = "";
				return "KEYWORD$IM OUTTA YR$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 834:
			switch(c){
			case 'Z':state = 835;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 835:
			switch(c){
			case ' ':state = 836;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 836:
			switch(c){
			case 'A':state = 837;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 837:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$ITZ A";
			case ';':
				state = 0;token = "";
				return "KEYWORD$ITZ A$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 900:
			switch(c){
			case 'T':state = 901;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 901:
			switch(c){
			case 'H':state = 902;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 902:
			switch(c){
			case 'X':state = 903;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 903:
			switch(c){
			case 'B':state = 904;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 904:
			switch(c){
			case 'Y':state = 905;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 905:
			switch(c){
			case 'E':state = 906;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 906:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$KTHXBYE";
			case ';':
				state = 0;token = "";
				return "KEYWORD$KTHXBYE;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1000:
			switch(c){
			case 'K':state = 1001;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1001:
			switch(c){
			case 'A':state = 1002;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1002:
			switch(c){
			case 'Y':state = 1003;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1003:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$MKAY";
			case ';':
				state = 0;token = "";
				return "KEYWORD$MKAY$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1100:
			switch(c){
			case 'O':state = 1101;break;
			case 'U':state = 1107;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1101:
			switch(c){
			case ' ':state = 1102;break;
			case 'T':state = 1103;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1102:
			switch(c){
			case 'W':state = 1104;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1103:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$NOT";
			case ';':
				state = 0;token = "";
				return "KEYWORD$NOT;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1104:
			switch(c){
			case 'A':state = 1105;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1105:
			switch(c){
			case 'I':state = 1106;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1106:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$NO WAI";
			case ';':
				state = 0;token = "";
				return "KEYWORD$NO WAI;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1107:
			switch(c){
			case 'M':state = 1108;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1108:
			switch(c){
			case 'B':state = 1109;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1109:
			switch(c){
			case 'A':state = 1110;break;
			case 'R':state = 1111;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1110:
			switch(c){
			case 'R':state = 1112;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1111:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$NUMBR";
			case ';':
				state = 0;token = "";
				return "KEYWORD$NUMBR$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1112:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$NUMBAR";
			case ';':
				state = 0;token = "";
				return "KEYWORD$NUMBAR$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1200:
			switch(c){
			case ' ':state = 1201;break;
			case 'I':state = 1206;break;
			case 'M':state = 1208;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1201:
			switch(c){
			case 'R':state = 1202;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1202:
			switch(c){
			case 'L':state = 1203;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1203:
			switch(c){
			case 'Y':state = 1204;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1204:
			switch(c){
			case '?':state = 1205;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1205:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$O RLY?";
			case ';':
				state = 0;token = "";
				return "KEYWORD$O RLY?$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1206:
			switch(c){
			case 'C':state = 1207;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1207:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$OIC";
			case ';':
				state = 0;token = "";
				return "KEYWORD$OIC$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1208:
			switch(c){
			case 'G':state=1209;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1209:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$OMG";
			case ';':
				state = 0;token = "";
				return "KEYWORD$OMG$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1300:
			switch(c){
			case 'R':state=1301;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1301:
			switch(c){
			case 'O':state=1302;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1302:
			switch(c){
			case 'D':state=1303;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1303:
			switch(c){
			case 'U':state=1304;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1304:
			switch(c){
			case 'K':state=1305;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1305:
			switch(c){
			case 'T':state=1306;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1306:
			switch(c){
			case ' ':state=1307;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1307:
			switch(c){
			case 'O':state=1308;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1308:
			switch(c){
			case 'F':state=1309;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1309:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$PRODUKT OF";
			case ';':
				state = 0;token = "";
				return "KEYWORD$PRODUKT OF$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1400:
			switch(c){
			case 'U':state=1401;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1401:
			switch(c){
			case 'O':state=1402;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1402:
			switch(c){
			case 'S':state=1403;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1403:
			switch(c){
			case 'H':state=1404;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1404:
			switch(c){
			case 'U':state=1405;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1405:
			switch(c){
			case 'N':state=1406;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1406:
			switch(c){
			case 'T':state=1407;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1407:
			switch(c){
			case ' ':state=1408;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1408:
			switch(c){
			case 'O':state=1409;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1409:
			switch(c){
			case 'F':state=1410;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1410:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$QUOSHUNT OF";
			case ';':
				state = 0;token = "";
				return "KEYWORD$QUOSHUNT OF$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1500:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$R";
			case ';':
				state = 0;token = "";
				return "KEYWORD$R$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1600:
			switch(c){
			case 'U':state=1601;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1601:
			switch(c){
			case 'M':state=1602;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1602:
			switch(c){
			case ' ':state=1603;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1603:
			switch(c){
			case 'O':state=1604;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1604:
			switch(c){
			case 'F':state=1605;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1605:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$SUM OF";
			case ';':
				state = 0;token = "";
				return "KEYWORD$SUM OF$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1700:
			switch(c){
			case 'R':state=1701;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1701:
			switch(c){
			case 'O':state=1702;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1702:
			switch(c){
			case 'O':state=1703;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1703:
			switch(c){
			case 'F':state=1704;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1704:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$TROOF";
			case ';':
				state = 0;token = "";
				return "KEYWORD$TROOF$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1800:
			switch(c){
			case 'I':state=1801;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1801:
			switch(c){
			case 'S':state=1803;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1803:
			switch(c){
			case 'I':state=1804;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1804:
			switch(c){
			case 'B':state=1805;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1805:
			switch(c){
			case 'L':state=1806;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1806:
			switch(c){
			case 'E':state=1807;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1807:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$VISIBLE";
			case ';':
				state = 0;token = "";
				return "KEYWORD$VISIBLE$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1900:
			switch(c){
			case 'I':state = 1901;break;
			case 'T':state = 1905;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1901:
			switch(c){
			case 'L':state = 1902;break;
			case 'N':state = 1904;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1902:
			switch(c){
			case 'E':state = 1903;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1903:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$WILE";
			case ';':
				state = 0;token = "";
				return "KEYWORD$WILE$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1904:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$WIN";
			case ';':
				state = 0;token = "";
				return "KEYWORD$WIN$;";
			default:state=9999;return SCANNER(c);
			}
			
		case 1905:
			switch(c){
			case 'F':state = 1906;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1906:
			switch(c){
			case '?':state = 1907;break;
			default:state=9999;return SCANNER(c);
			}
			break;
			
		case 1907:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$WTF?";
			case ';':
				state = 0;token = "";
				return "KEYWORD$WTF?$;";
			default:state=9999;return SCANNER(c);
			}
			

		case 2000:
			switch(c){
			case 'A':state = 2002;break;
			case 'R':state = 2003;break;
			default:state=9999;return SCANNER(c);
			}
			break;
		case 2002:
			switch(c){
			case ' ':state = 2004;break;
			default:state=9999;return SCANNER(c);
			}
			break;
		case 2003:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$YR";
			case ';':
				state = 0;token = "";
				return "KEYWORD$YR$;";
			default:state=9999;return SCANNER(c);
			}

		case 2004:
			switch(c){
			case 'R':state = 2005;break;
			default:state=9999;return SCANNER(c);
			}
			break;
		case 2005:
			switch(c){
			case 'L':state = 2006;break;
			default:state=9999;return SCANNER(c);
			}
			break;
		case 2006:
			switch(c){
			case 'Y':state = 2007;break;
			default:state=9999;return SCANNER(c);
			}
			break;
		case 2007:
			switch(c){
			case ' ':case '\n':
				state = 0;token = "";
				return "KEYWORD$YA RLY";
			case ';':
				state = 0;token = "";
				return "KEYWORD$YA RLY$;";
			default:state=9999;return SCANNER(c);
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
				state = 0;token = "";
				return BOOKKEEPER("ID$"+ret);
			case ';':
				ret = token;
				state = 0;token = "";
				return BOOKKEEPER("ID$"+ret+"$;");
			default:state=9999;break;
			}
			break;
			
		case 6000:
			switch(c){
			case '0':case '1':case '2':case '3':case '4':case '5':
			case '6':case '7':case '8':case '9':state = 6000;break;
			
			case '.':state = 7001;break;
			
			case ' ':case '\n':
				ret = token;
				state = 0;token = "";
				return BOOKKEEPER("CONST$"+ret);
			case ';':
				ret = token;
				state = 0;token = "";
				return BOOKKEEPER("CONST$"+ret+"$;");
			default:state=9999;break;
			}
			break;
			
		case 7000:
			switch(c){
			case '0':case '1':case '2':case '3':case '4':case '5':
			case '6':case '7':case '8':case '9':state = 7001;break;
			default:state=9999;break;
			}
			
		case 7001:
			switch(c){
			case '0':case '1':case '2':case '3':case '4':case '5':
			case '6':case '7':case '8':case '9':state = 7001;break;
			case ' ':case '\n':
				ret = token;
				state = 0;token = "";
				return BOOKKEEPER("CONST$"+ret);
			case ';':
				ret = token;
				state = 0;token = "";
				return BOOKKEEPER("CONST$"+ret+"$;");
			default:state=9999;break;
			}
			break;
			
			
		/** Comment case. Skip all characters until '\n'.*/
		case 9998:
			switch(c){
			case '\n':
				state = 0;token = "";
				return "";
			}
			break;
		
		/** Error case. Generate next token as an error.*/
		case 9999:
			switch(c){
			case ' ':case '\n':
				ret = token;
				state = 0;token = "";
				return ERROR(ret);
			case ';':
				ret = token;
				state = 0;token = "";
				return ERROR(ret);
			}
			break;
		default:state=9999;return SCANNER(c);
		}
		token += c;
		/** Default token return value. Implies that a token has not been found yet.*/
		return "";
	}//End SCANNER
	
	
	/**
	 * Error handler. Writes error messages to the command line.
	 * @param not_token The error text given to the parser.
	 * @return Any residual information appended to the token.
	 * @throws InterruptedException 
	 */
	private static String ERROR(String not_token){
		System.out.format(OUT_FORMAT,lineNumber,not_token,"ERROR: Unrecognized Token.");
		return (not_token.endsWith("$;") ? "SS$;" : "");
	}

	/**
	 * Maintains the SYMTAB object, adding identifiers and 
	 * their associated types as necessary.
	 * @param token Obtained from DFA implementation. Token will be added to the table.
	 */
	private static String BOOKKEEPER(String token){
		token = lineNumber+"$"+token;
		String[] token_sp = token.split("\\$");
		String id = token_sp[2];
		System.out.format(OUT_FORMAT, Integer.parseInt(token_sp[0]),token_sp[2],token_sp[1]);
		int i = 0;
		String ret = (token.endsWith("$;") ? "SS$;" : "");
		
		
		/** Duplicate entry check.*/
		for(; i<MAX_SYMBOL_COUNT && SYMTAB[i] != null; i++){
			if(id.equals(SYMTAB[i].split("\\$")[2]))return ret;
		}
		/** Add entry to SYMTAB*/
		SYMTAB[i]=token;
		return ret;
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
	 * Initialize all values in SYMTAB to be null.
	 */
	private static void setupSYMTAB() {
		for(int i = 0; i<MAX_SYMBOL_COUNT; i++)SYMTAB[i]=null;
	}

	/**
	 * Used to print '-' and table header for token table output.
	 */
	private static void setupFormatting() {
		for(int i = 1; i<=OUT_FORMAT.length()+29;i++)System.out.print("-");
		System.out.println();
		System.out.format("|Line %3s | %-12s | %-30s |%n","#","Token","Type");
		for(int i = 1; i<=OUT_FORMAT.length()+29;i++)System.out.print("-");
		System.out.println();
	}

}
