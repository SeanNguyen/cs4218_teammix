package sg.edu.nus.comp.cs4218;

public final class Configurations {
	public static final String MESSAGE_WELCOME = "WELCOME TO CS4128 SHELL";
	public static final String MESSAGE_PROMPT = "shell > ";
	public static final String MESSAGE_ERROR_PARSING = "Your Command Line is Invalid";
	public static final String MESSAGE_ERROR_APP = "Application Error:";
	public static final String MESSAGE_ERROR_APPMISSING = "Command Not Found";
	public static final String MESSGE_ERROR_OUTSTREAMMISSING = "The output stream is missing";
	
	public static final char QUOTE_SINGLE = '\'';
	public static final char QUOTE_DOUBLE = '\"';
	public static final char QUOTE_BACK = '`';
	public static final String SEMICOLON = ";";
	public static final char SEMICOLONCHAR = ';';
	public static final String INPUTREDIRECTION_TOKEN = ">";
	public static final String OUTPUTREDIRECTION_TOKEN = "<";
	public static final String NEWLINE = String.format("%n");
	public static final String SEPERATORREGEX = String.format("[ \t]");
	
	
	public static final String APPNAME_CD = "cd";
	public static final String APPNAME_LS = "ls";
	public static final String APPNAME_ECHO = "echo";
	public static final String APPNAME_PWD = "pwd";
	public static final String APPNAME_CAT = "cat";
	public static final String APPNAME_HEAD = "head";
	public static final String APPNAME_TAIL = "tail";
}
