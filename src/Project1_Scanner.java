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
 * @version 0.0
 */
public class Project1_Scanner {
	
	/** Global variable representing the current state of the DFA. 
	 * 0 = initial state q_not.*/
	static int state = 0;
	
	/** Global variable representing the line number at which some 
	 * token or error was encountered.*/
	static int lineNumber = 0;
	
	/** Allows for SCANNER to build tokens.*/
	static String token = "";
	
	/**
	 * Insertion point for program. Calls parser.
	 * @param args program arguments provided via command-line
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		String fileName = "src\\MiniLOLTest";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			/** Start read loop.*/
			char c = Character.MIN_VALUE;
			while((c = (char)reader.read()) != (char) -1){
				String t_token = SCANNER(c);
				if(!(t_token.equals(""))){
					System.out.println("Line "+lineNumber + ". "+t_token);
				}
			}
			if(!(token.equals(""))){
				System.out.println("Line " + ++lineNumber + ". "+token);
			}
		} catch (FileNotFoundException e) {
			System.err.println("File "+fileName+" could not be found. Terminating program.");
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("Encountered IOException while reading from file. Terminating program.");
			System.exit(-1);
		} finally {
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
	}
	
	/**
	 * Simulates a DFA. Returns a single token in Mini-LOL syntax.
	 * @param c the current character being observed by the DFA
	 * @return A token, with its identifier appended to it with '$[type]'. This will be returned as "" until a token has been generated.
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
			/**Special symbol cases.*/
			case ' ':
				return "";
			case ';':
				return ";";
			case (char) 10:
				return "";
			case (char) 13:
				lineNumber++;
				return "";
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
			case 'a':case 'b':case 'c':case 'd':case 'e':case 'f':case 'g':
			case 'h':case 'i':case 'j':case 'k':case 'l':case 'm':case 'n':
			case 'o':case 'p':case 'q':case 'r':case 's':case 't':case 'u':
			case 'v':case 'w':case 'x':case 'y':case 'z':state = 5000;break;
			case '0':case '1':case '2':case '3':case '4':case '5':
			case '6':case '7':case '8':case '9':state = 6000;break;
			case '.':state = 7000;break;
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
			case ' ':
				state = 0;token = "";
				return "AN";
			case ';':
				state = 0;token = "";
				return "AN;";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "AN";
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
			case ' ':
				state = 0;token = "";
				return "BOTH OF";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "BOTH OF";
			case ';':
				state = 0;token = "";
				return "BOTH OF$;";
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
			case ' ':
				state = 0;token = "";
				return "BOTH SAEM";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "BOTH SAEM";
			case ';':
				state = 0;token = "";
				return "BOTH SAEM$;";
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
			case ' ':
				state = 0;token = "";
				return "DIFF OF";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "DIFF OF";
			case ';':
				state = 0;token = "";
				return "DIFF OF$;";
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
			case ' ':
				state = 0;token = "";
				return "DIFFRINT";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "DIFFRINT";
			case ';':
				state = 0;token = "";
				return "DIFFRINT$;";
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
			case ' ':
				state = 0;token = "";
				return "EITHER OF";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "EITHER OF";
			case ';':
				state = 0;token = "";
				return "EITHER OF$;";
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
			case ' ':
				state = 0;token = "";
				return "FAIL";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "FAIL";
			case ';':
				state = 0;token = "";
				return "FAIL$;";
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
			case ' ':
				state = 0;token = "";
				return "FOUND YR";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "FOUND YR";
			case ';':
				state = 0;token = "";
				return "FOUND YR$;";
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
			case ' ':
				state = 0;token = "";
				return "GTFO";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "GTFO";
			case ';':
				state = 0;token = "";
				return "GTFO$;";
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
			case ' ':
				state = 0;token = "";
				return "GIMMEH";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "GIMMEH";
			case ';':
				state = 0;token = "";
				return "GIMMEH$;";
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
			case ' ':
				state = 0;token = "";
				return "HAI";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "HAI";
			case ';':
				state = 0;token = "";
				return "HAI$;";
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
			case ' ':
				state = 0;token = "";
				return "HOW IZ I";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "HOW IZ I";
			case ';':
				state = 0;token = "";
				return "HOW IZ I$;";
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
			case ' ':
				state = 0;token = "";
				return "I HAS A";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "I HAS A";
			case ';':
				state = 0;token = "";
				return "I HAS A$;";
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
			case ' ':
				state = 0;token = "";
				return "I IZ";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "I IZ";
			case ';':
				state = 0;token = "";
				return "I IZ$;";
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
			case ' ':
				state = 0;token = "";
				return "IF U SAY SO";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "IF U SAY SO";
			case ';':
				state = 0;token = "";
				return "IF U SAY SO$;";
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
			case ' ':
				state = 0;token = "";
				return "IM IN YR";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "IM IN YR";
			case ';':
				state = 0;token = "";
				return "IM IN YR$;";
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
			case ' ':
				state = 0;token = "";
				return "IM OUTTA YR";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "IM OUTTA YR";
			case ';':
				state = 0;token = "";
				return "IM OUTTA YR$;";
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
			case ' ':
				state = 0;token = "";
				return "ITZ A";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "ITZ A";
			case ';':
				state = 0;token = "";
				return "ITZ A$;";
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
			case ' ':
				state = 0;token = "";
				return "YR";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "YR";
			case ';':
				state = 0;token = "";
				return "YR$;";
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
			case ' ':
				state = 0;token = "";
				return "YA RLY";
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "YA RLY";
			case ';':
				state = 0;token = "";
				return "YA RLY$;";
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
			case (char) 13:
				ret = token;
				state = 0;token = "";
				return "$ID$"+ret;
			case ' ':
				ret = token;
				state = 0;token = "";
				return "$ID$"+ret;
			case '\n':
				lineNumber++;
				ret = token;
				state = 0;token = "";
				return "$ID$"+ret;
			case ';':
				ret = token;
				state = 0;token = "";
				return "$ID$"+ret+c;
			default:state=9999;break;
			}
			break;
			
		/** Comment case. Skip all characters until '\n'.*/
		case 9998:
			switch(c){
			case (char) 13:
			case '\n':
				lineNumber++;
				state = 0;token = "";
				return "";
			}
			break;
		
		/** Error case. Generate next token as an error.*/
		case 9999:
			switch(c){
			case ' ':
				ret = token;
				state = 0;token = "";
				return "$ERR$"+ret;
			case '\n':
				lineNumber++;
				ret = token;
				state = 0;token = "";
				return "$ERR$"+ret;
			case ';':
				ret = token;
				state = 0;token = "";
				return "$ERR$"+ret+c;
			}
			break;
		default:state=9999;return SCANNER(c);
		}
		token += c;
		/** Default token return value. Implies that a token has not been found yet.*/
		return "";
	}
}
