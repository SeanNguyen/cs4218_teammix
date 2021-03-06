package sg.edu.nus.comp.cs4218.impl.app;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import sg.edu.nus.comp.cs4218.Configurations;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.HeadException;
import sg.edu.nus.comp.cs4218.exception.LsException;

public class HeadCommandTest {
	private HeadCommand headCommand;
	private InputStream stdin;
	private OutputStream stdout;
	final private static String NOFILEMSG = "No such file or directory";
	final private static String INCORRECTARGMSG = "Incorrect argument(s)";
	final private static String CONTENT_1 = "1. CS4218 Shell is a command interpreter that provides a set of tools (applications):\n2. cd, pwd, ls, cat, echo, head, tail, grep, sed, find and wc.\n3. Apart from that, CS4218 Shell is a language for calling and combining these application.\n4. The language supports quoting of input data, semicolon operator for calling sequences of applications, command substitution and piping for connecting applications\' inputs and outputs, IO-redirection to load and save data processed by applications from/to files.\n5. More details can be found in \"Project Description.pdf\" in IVLE.\n6. Prerequisites\n7. CS4218 Shell requires the following versions of software:\n8. JDK 7\n9. Eclipse 4.3\n10. JUnit 4\n11. Compiler compliance level must be <= 1.7\n12. END-OF-FILE\n";
	final private static String CONTENT_2 = "1. CS4218 Shell is a command interpreter that provides a set of tools (applications):\n2. cd, pwd, ls, cat, echo, head, tail, grep, sed, find and wc.\n3. Apart from that, CS4218 Shell is a language for calling and combining these application.\n4. The language supports quoting of input data, semicolon operator for calling sequences of applications, command substitution and piping for connecting applications\' inputs and outputs, IO-redirection to load and save data processed by applications from/to files.\n5. More details can be found in \"Project Description.pdf\" in IVLE.\n";
	final private static String RESULT_CONTENT_2 = "%1$s1. CS4218 Shell is a command interpreter that provides a set of tools (applications):\n2. cd, pwd, ls, cat, echo, head, tail, grep, sed, find and wc.\n3. Apart from that, CS4218 Shell is a language for calling and combining these application.\n4. The language supports quoting of input data, semicolon operator for calling sequences of applications, command substitution and piping for connecting applications\' inputs and outputs, IO-redirection to load and save data processed by applications from/to files.\n5. More details can be found in \"Project Description.pdf\" in IVLE.\n";
	final private static String RESULT_CONTENT_1 = "%1$s1. CS4218 Shell is a command interpreter that provides a set of tools (applications):" + Configurations.NEWLINE + "2. cd, pwd, ls, cat, echo, head, tail, grep, sed, find and wc." + Configurations.NEWLINE + "3. Apart from that, CS4218 Shell is a language for calling and combining these application." + Configurations.NEWLINE + "4. The language supports quoting of input data, semicolon operator for calling sequences of applications, command substitution and piping for connecting applications\' inputs and outputs, IO-redirection to load and save data processed by applications from/to files." + Configurations.NEWLINE + "5. More details can be found in \"Project Description.pdf\" in IVLE." + Configurations.NEWLINE + "6. Prerequisites" + Configurations.NEWLINE + "7. CS4218 Shell requires the following versions of software:" + Configurations.NEWLINE + "8. JDK 7" + Configurations.NEWLINE + "9. Eclipse 4.3" + Configurations.NEWLINE + "10. JUnit 4" + Configurations.NEWLINE;
	final private static String RESULT_SHORT = "1. CS4218 Shell is a command interpreter that provides a set of tools (applications):" + Configurations.NEWLINE + "2. cd, pwd, ls, cat, echo, head, tail, grep, sed, find and wc." + Configurations.NEWLINE + "3. Apart from that, CS4218 Shell is a language for calling and combining these application." + Configurations.NEWLINE + "4. The language supports quoting of input data, semicolon operator for calling sequences of applications, command substitution and piping for connecting applications\' inputs and outputs, IO-redirection to load and save data processed by applications from/to files." + Configurations.NEWLINE + "5. More details can be found in \"Project Description.pdf\" in IVLE." + Configurations.NEWLINE + "";
	final private static String RESULT_10 = "1. CS4218 Shell is a command interpreter that provides a set of tools (applications):" + Configurations.NEWLINE + "2. cd, pwd, ls, cat, echo, head, tail, grep, sed, find and wc." + Configurations.NEWLINE + "3. Apart from that, CS4218 Shell is a language for calling and combining these application." + Configurations.NEWLINE + "4. The language supports quoting of input data, semicolon operator for calling sequences of applications, command substitution and piping for connecting applications\' inputs and outputs, IO-redirection to load and save data processed by applications from/to files." + Configurations.NEWLINE + "5. More details can be found in \"Project Description.pdf\" in IVLE." + Configurations.NEWLINE + "6. Prerequisites" + Configurations.NEWLINE + "7. CS4218 Shell requires the following versions of software:" + Configurations.NEWLINE + "8. JDK 7" + Configurations.NEWLINE + "9. Eclipse 4.3" + Configurations.NEWLINE + "10. JUnit 4" + Configurations.NEWLINE;
	final private static String RESULT_1 = "1. CS4218 Shell is a command interpreter that provides a set of tools (applications):" + Configurations.NEWLINE;
	final private static String RESULT_11 = "1. CS4218 Shell is a command interpreter that provides a set of tools (applications):" + Configurations.NEWLINE + "2. cd, pwd, ls, cat, echo, head, tail, grep, sed, find and wc." + Configurations.NEWLINE + "3. Apart from that, CS4218 Shell is a language for calling and combining these application." + Configurations.NEWLINE + "4. The language supports quoting of input data, semicolon operator for calling sequences of applications, command substitution and piping for connecting applications\' inputs and outputs, IO-redirection to load and save data processed by applications from/to files." + Configurations.NEWLINE + "5. More details can be found in \"Project Description.pdf\" in IVLE." + Configurations.NEWLINE + "6. Prerequisites" + Configurations.NEWLINE + "7. CS4218 Shell requires the following versions of software:" + Configurations.NEWLINE + "8. JDK 7" + Configurations.NEWLINE + "9. Eclipse 4.3" + Configurations.NEWLINE + "10. JUnit 4" + Configurations.NEWLINE + "11. Compiler compliance level must be <= 1.7" + Configurations.NEWLINE;
	final private static String RESULT_MAX = "1. CS4218 Shell is a command interpreter that provides a set of tools (applications):" + Configurations.NEWLINE + "2. cd, pwd, ls, cat, echo, head, tail, grep, sed, find and wc." + Configurations.NEWLINE + "3. Apart from that, CS4218 Shell is a language for calling and combining these application." + Configurations.NEWLINE + "4. The language supports quoting of input data, semicolon operator for calling sequences of applications, command substitution and piping for connecting applications\' inputs and outputs, IO-redirection to load and save data processed by applications from/to files." + Configurations.NEWLINE + "5. More details can be found in \"Project Description.pdf\" in IVLE." + Configurations.NEWLINE + "6. Prerequisites" + Configurations.NEWLINE + "7. CS4218 Shell requires the following versions of software:" + Configurations.NEWLINE + "8. JDK 7" + Configurations.NEWLINE + "9. Eclipse 4.3" + Configurations.NEWLINE + "10. JUnit 4" + Configurations.NEWLINE + "11. Compiler compliance level must be <= 1.7" + Configurations.NEWLINE + "12. END-OF-FILE" + Configurations.NEWLINE;
	final private static String FOLDERTEST = "folderTest";
	final private static String FOLDERTESTHIDE = ".FolderTestHide";
	final private static String FILE = "textFile1.txt"; 
	final private static String FILEEMPTY = "textFile2.txt"; 
	final private static String FILEHIDE = ".textFile3.txt"; 
	final private static String FILEHIDEEMPTY = ".textFile4.txt"; 
	final private static String FILESHORT = "textFile5.txt"; 
	final private static String FILESHORTHIDE = ".textFile6.txt"; 
	final private static Path PATH = Paths.get(FOLDERTEST);
	final private static Path PATHHIDE = Paths.get(FOLDERTESTHIDE);
	final private static Path PATHTOFILE = Paths.get(FILE);
	final private static Path PATHTOFILEEMPTY = Paths.get(FILEEMPTY);
	final private static Path PATHTOFILEHIDE = Paths.get(FILEHIDE);
	final private static Path PATHTOFILESHORT = Paths.get(FILESHORT);
	final private static Path PATHTOFILESHORTHIDE = Paths.get(FILESHORTHIDE);
	final private static Path PATHTOFILEHIDEEMPTY = Paths.get(FILEHIDEEMPTY);
	final private static Path PATHTOFOLDERFILE = Paths.get(FOLDERTEST + File.separator + FILE);
	final private static Path PATHTOFOLDERFILEEMPTY = Paths.get(FOLDERTEST + File.separator + FILEEMPTY);
	final private static Path PATHTOFOLDERFILEHIDE = Paths.get(FOLDERTEST + File.separator + FILEHIDE);
	final private static Path PATHTOFOLDERFILEHIDEEMPTY = Paths.get(FOLDERTEST + File.separator + FILEHIDEEMPTY);
	final private static Path PATHTOHIDDENFOLDERFILE = Paths.get(FOLDERTESTHIDE + File.separator + FILE);
	final private static Path PATHTOHIDDENFOLDERFILEEMPTY = Paths.get(FOLDERTESTHIDE + File.separator + FILEEMPTY);
	final private static Path PATHTOHIDDENFOLDERFILEHIDE = Paths.get(FOLDERTESTHIDE + File.separator + FILEHIDE);
	final private static Path PATHTOHIDDENFOLDERFILEHIDEEMPTY = Paths.get(FOLDERTESTHIDE + File.separator + FILEHIDEEMPTY);
	final private static Path[] FILESTOCREATE = {PATHTOFILEEMPTY, PATHTOFILESHORTHIDE, PATHTOFILESHORT, PATHTOFILE,
	PATHTOFILEHIDE, PATHTOFILEHIDEEMPTY, PATHTOFOLDERFILE, PATHTOFOLDERFILEEMPTY, PATHTOFOLDERFILEHIDE, PATHTOFOLDERFILEHIDEEMPTY,
    PATHTOHIDDENFOLDERFILE, PATHTOHIDDENFOLDERFILEEMPTY, PATHTOHIDDENFOLDERFILEHIDE, PATHTOHIDDENFOLDERFILEHIDEEMPTY};
	private final File workingDir = new File(System.getProperty("user.dir"));
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        String[] arrayOfFiles = { PATHTOFILE.toString(), PATHTOFILEHIDE.toString(),
        PATHTOFOLDERFILE.toString(), PATHTOFOLDERFILEHIDE.toString(),
        PATHTOHIDDENFOLDERFILE.toString(),
        PATHTOHIDDENFOLDERFILEHIDE.toString() };
        String[] arrayOfShortFiles = { PATHTOFILESHORT.toString(),
        PATHTOFILESHORTHIDE.toString() };
        Files.createDirectories(PATH);
        Files.createDirectories(PATHHIDE);
        for(int i = 0; i < FILESTOCREATE.length; i++) {
            try {
              Files.createFile(FILESTOCREATE[i]);
            } catch (FileAlreadyExistsException e) {
                System.err.println("File already exists: " + e.getMessage());
            }   
        }
        for (int i = 0; i < arrayOfFiles.length; i++) {
            try {
                File file = new File(arrayOfFiles[i]);
                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(CONTENT_1);
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < arrayOfShortFiles.length; i++) {
            try {
                File file = new File(arrayOfShortFiles[i]);
                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(CONTENT_2);
                bufferedWriter.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
        }
    }
	
	@Before
	public void setUp() throws Exception {
		headCommand = new HeadCommand();		
		stdout = new java.io.ByteArrayOutputStream();	
		Environment.currentDirectory = System.getProperty("user.dir");
	}
	
	@After
	public void tearDown() throws Exception {
		headCommand = null;
		stdout = null;
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
	  try {
		Files.delete(PATHTOFILE);
		Files.delete(PATHTOFILEEMPTY);
		Files.delete(PATHTOFILEHIDE);
		Files.delete(PATHTOFILEHIDEEMPTY);
		Files.delete(PATHTOFILESHORT);
		Files.delete(PATHTOFILESHORTHIDE);
		Files.delete(PATHTOFOLDERFILE);
		Files.delete(PATHTOFOLDERFILEEMPTY);
		Files.delete(PATHTOFOLDERFILEHIDE);
		Files.delete(PATHTOFOLDERFILEHIDEEMPTY);
		Files.delete(PATHTOHIDDENFOLDERFILE);
		Files.delete(PATHTOHIDDENFOLDERFILEEMPTY);
		Files.delete(PATHTOHIDDENFOLDERFILEHIDE);
		Files.delete(PATHTOHIDDENFOLDERFILEHIDEEMPTY);
		Files.delete(PATH);
		Files.delete(PATHHIDE);
	  } catch (IOException e) {
        //e.printStackTrace(); ingore becos windows can't delete
      }
	}

	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a file and display 10 lines
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadFile() throws HeadException {
		String[] args = {FILE};
		headCommand.run(args, stdin, stdout);
		assertEquals(RESULT_10, stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a empty file
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadFileEmpty() throws HeadException {
		String[] args = {FILEEMPTY};
		headCommand.run(args, stdin, stdout);
		assertEquals("", stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a hidden file and display 10 lines
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadFileHidden() throws HeadException {
		String[] args = {FILEHIDE};
		headCommand.run(args, stdin, stdout);
		assertEquals(RESULT_10, stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a hidden empty file
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadFileHiddenEmpty() throws HeadException {
		String[] args = {FILEHIDEEMPTY};
		headCommand.run(args, stdin, stdout);
		assertEquals("", stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a file in a folder and display 10 lines
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadFolderFile() throws HeadException {
		String[] args = {FOLDERTEST + File.separator + FILE};
		headCommand.run(args, stdin, stdout);
		assertEquals(RESULT_10, stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a empty file in a folder
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadFolderFileEmpty() throws HeadException {
		String[] args = {FOLDERTEST + File.separator + FILEEMPTY};
		headCommand.run(args, stdin, stdout);
		assertEquals("", stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a hidden file in a folder and display 10 lines
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadFolderFileHidden() throws HeadException {
		String[] args = {FOLDERTEST + File.separator + FILEHIDE};
		headCommand.run(args, stdin, stdout);
		assertEquals(RESULT_10, stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a hidden empty file in a folder
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadFolderFileHiddenEmpty() throws HeadException {
		String[] args = {FOLDERTEST + File.separator + FILEHIDEEMPTY};
		headCommand.run(args, stdin, stdout);
		assertEquals("", stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a file in a hidden folder and display 10 lines
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadHiddenFolderFile() throws HeadException {
		String[] args = {FOLDERTESTHIDE + File.separator + FILE};
		headCommand.run(args, stdin, stdout);
		assertEquals(RESULT_10, stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a empty file in a hidden folder
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadHiddenFolderFileEmpty() throws HeadException {
		String[] args = {FOLDERTESTHIDE + File.separator + FILEEMPTY};
		headCommand.run(args, stdin, stdout);
		assertEquals("", stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a hidden file in a hidden folder and display 10 lines
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadHiddenFolderFileHidden() throws HeadException {
		String[] args = {FOLDERTESTHIDE + File.separator + FILEHIDE};
		headCommand.run(args, stdin, stdout);
		assertEquals(RESULT_10, stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a short file and display 
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadShortFile() throws HeadException {
		String[] args = {FILESHORT};
		headCommand.run(args, stdin, stdout);
		assertEquals(RESULT_SHORT, stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a short hidden file and display
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadShortFileHide() throws HeadException {
		String[] args = {FILESHORTHIDE};
		headCommand.run(args, stdin, stdout);
		assertEquals(RESULT_SHORT, stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a hidden empty file in a hidden folder
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadHiddenFolderFileHiddenEmpty() throws HeadException {
		String[] args = {FOLDERTESTHIDE + File.separator + FILEHIDEEMPTY};
		headCommand.run(args, stdin, stdout);
		assertEquals("", stdout.toString());
	}
	
	@Test
	public void testHeadDisplayOneLine() throws HeadException {
		String[] args = {"-n", "1", FILE};
		headCommand.run(args, stdin, stdout);
		assertEquals(RESULT_1, stdout.toString());
	}
	
	@Test
	public void testHeadDisplayZeroLine() throws HeadException {
		String[] args = {"-n", "0", FILE};
		headCommand.run(args, stdin, stdout);
		assertEquals("", stdout.toString());
	}
	
	@Test
	public void testHeadDisplayNegativeLine() throws HeadException {
		expectedEx.expect(HeadException.class);
		expectedEx.expectMessage("illegal line count -- ");
		String[] args = {"-n", "-1", FILE};
		headCommand.run(args, stdin, stdout);
	}
	
	@Test
	public void testHeadDisplay11Lines() throws HeadException {
		String[] args = {"-n", "11", FILE};
		headCommand.run(args, stdin, stdout);
		assertEquals(RESULT_11, stdout.toString());
	}
	
	@Test
	public void testHeadDisplayOverMaxLines() throws HeadException {
		String[] args = {"-n", "100", FILE};
		headCommand.run(args, stdin, stdout);
		assertEquals(RESULT_MAX, stdout.toString());
	}
	
	@Test
	public void testHeadIllegalOption() throws HeadException {
		expectedEx.expect(HeadException.class);
		String[] args = {"-z", "10", FILE};
		headCommand.run(args, stdin, stdout);
	}
	
	@Test
	public void testHeadInvalidArguments() throws HeadException {
		expectedEx.expect(HeadException.class);
		String[] args = {"-z", FILEEMPTY, FILE};
		headCommand.run(args, stdin, stdout);
	}
	
	@Test
	public void testHeadInvalidFileWithArgs() throws HeadException {
		expectedEx.expect(HeadException.class);
		expectedEx.expectMessage(NOFILEMSG);
		String[] args = {"-n", "5", "NoSuchFile.txt"};
		headCommand.run(args, stdin, stdout);
	}
	
	@Test
	public void testHeadDisplayOverMaxEmptyFile() throws HeadException {
		String[] args = {"-n", "100", FILEEMPTY};
		headCommand.run(args, stdin, stdout);
		assertEquals("", stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a file that does not exist
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadNoSuchFile() throws HeadException {
		expectedEx.expect(HeadException.class);
		expectedEx.expectMessage(NOFILEMSG);
		String[] args = {"NoSuchFile.txt"};
		headCommand.run(args, stdin, stdout);
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a folder that does not exist
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadNoSuchDirectory() throws HeadException {
		expectedEx.expect(HeadException.class);
		expectedEx.expectMessage(NOFILEMSG);
		String[] args = {"NoSuchFile"};
		headCommand.run(args, stdin, stdout);
		assertEquals("", stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a directory
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadDirectory() throws HeadException {
		expectedEx.expect(HeadException.class);
		expectedEx.expectMessage("Is a directory");
		String[] args = {FOLDERTEST};
		headCommand.run(args, stdin, stdout);
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a hidden file that does not exist
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadNoSuchHiddenFile() throws HeadException {
		expectedEx.expect(HeadException.class);
		expectedEx.expectMessage(NOFILEMSG);
		String[] args = {".NoSuchFile.txt"};
		headCommand.run(args, stdin, stdout);
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a hidden directory
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadHiddenDirectory() throws HeadException {
		expectedEx.expect(HeadException.class);
		expectedEx.expectMessage("Is a directory");
		String[] args = {FOLDERTESTHIDE};
		headCommand.run(args, stdin, stdout);
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head a hidden directory that does not exist
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadNoSuchHiddenDirectory() throws HeadException {
		expectedEx.expect(HeadException.class);
		expectedEx.expectMessage(NOFILEMSG);
		String[] args = {".NoSuchFile"};
		headCommand.run(args, stdin, stdout);
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head 2 files
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadTwoFiles() throws HeadException {
		String[] args = {FILE, FILEHIDE};
		headCommand.run(args, stdin, stdout);	
		assertEquals(String.format(RESULT_CONTENT_1, "==>" + FILE + "<=="+Configurations.NEWLINE) + String.format(RESULT_CONTENT_1, "==>" +FILEHIDE +"<=="+Configurations.NEWLINE),stdout.toString());
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head 3 files
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadThreeFiles() throws HeadException {
		String[] args = {FILE, FILEEMPTY, FILEHIDE};
		headCommand.run(args, stdin, stdout);
		assertEquals(String.format(RESULT_CONTENT_1, "==>" + FILE + "<=="+Configurations.NEWLINE) + "==>" + FILEEMPTY + "<=="+Configurations.NEWLINE + String.format(RESULT_CONTENT_1, "==>" +FILEHIDE +"<=="+Configurations.NEWLINE),stdout.toString());
		
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head no args
	 * 
	 * @throw HeadException
	 */
	@Test
	public void testHeadNoFile() throws HeadException {
		expectedEx.expect(HeadException.class);
		String[] args = {};
		headCommand.run(args, stdin, stdout);
	}
	
	/**
	 * Test void run(String[] args, InputStream stdin, OutputStream stdout) Test
	 * Head empty string
	 * 
	 * @throw HeadException
	 */
	@Test(expected = HeadException.class)
	public void testHeadEmptyArg() throws HeadException {
		String[] args = {""};
		headCommand.run(args, stdin, stdout);
	}
	
	/**
	 * Test helper method getAbsolutePath
	 * input file
	 * 
	 * @throw CatException
	 */
	@Test
	public void testGetAbsolutePath() throws HeadException {		
		String result = headCommand.getAbsolutePath(FILE);
		assertEquals(workingDir.getAbsolutePath() + File.separator + FILE ,result);
	}
	
	/**
	 * Test helper method getAbsolutePath
	 * input file in folder
	 * 
	 * @throw CatException
	 */
	@Test
	public void testGetAbsolutePathInFolder() throws HeadException {		
		String result = headCommand.getAbsolutePath(FOLDERTEST + File.separator + FILE);
		assertEquals(workingDir.getAbsolutePath() + File.separator + FOLDERTEST + File.separator + FILE ,result);
	}
	
	/**
	 * Test helper method getAbsolutePath
	 * input hidden file
	 * 
	 * @throw CatException
	 */
	@Test
	public void testGetAbsolutePathInFolderHiddenFile() throws HeadException {		
		String result = headCommand.getAbsolutePath(FOLDERTEST + File.separator + FILEHIDE);
		assertEquals(workingDir.getAbsolutePath() + File.separator + FOLDERTEST + File.separator + FILEHIDE ,result);
	}
	
	/**
	 * Test helper method getAbsolutePath
	 * input file in hidden folder
	 * 
	 * @throw CatException
	 */
	@Test
	public void testGetAbsolutePathInHiddenFolder() throws HeadException {		
		String result = headCommand.getAbsolutePath(FOLDERTESTHIDE + File.separator + FILE);
		assertEquals(workingDir.getAbsolutePath() + File.separator + FOLDERTESTHIDE + File.separator + FILE ,result);
	}
	
	/**
	 * Test helper method getAbsolutePath
	 * input hidden file in hidden folder
	 * 
	 * @throw CatException
	 */
	@Test
	public void testGetAbsolutePathInHiddenFolderHiddenFile() throws HeadException {		
		String result = headCommand.getAbsolutePath(FOLDERTESTHIDE + File.separator + FILEHIDEEMPTY);
		assertEquals(workingDir.getAbsolutePath() + File.separator + FOLDERTESTHIDE + File.separator + FILEHIDEEMPTY ,result);
	}
	
	/**
	 * Test helper method doesFileExist
	 * input file that exist
	 * 
	 * @throw CatException
	 */
	@Test
	public void testDoesFileExist() throws HeadException {
		File file = new File(FILE);
		Boolean result = headCommand.doesFileExist(file);
		assertTrue(result);
	}
	
	/**
	 * Test helper method doesFileExist
	 * input hidden file that exist
	 * 
	 * @throw CatException
	 */
	@Test
	public void testDoesFileExistHidden() throws HeadException {
		File file = new File(FILEHIDEEMPTY);
		Boolean result = headCommand.doesFileExist(file);
		assertTrue(result);
	}
	
	/**
	 * Test helper method doesFileExist
	 * input file that does not exist
	 * 
	 * @throw CatException
	 */
	@Test
	public void testDoesFileExistNoSuchFile() throws HeadException {
		File file = new File("NoSuchFile.txt");
		Boolean result = headCommand.doesFileExist(file);
		assertFalse(result);
	}
	
	/**
	 * Test helper method doesFileExist
	 * input folder that exist
	 * 
	 * @throw CatException
	 */
	@Test
	public void testDoesFileExistFolder() throws HeadException {
		File file = new File(FOLDERTEST);
		Boolean result = headCommand.doesFileExist(file);
		assertFalse(result);
	}
	
	/**
	 * Test helper method isDirectory
	 * input folder
	 * 
	 * @throw CatException
	 */
	@Test
	public void testIsDirectory() throws HeadException {
		File file = new File(FOLDERTEST);
		Boolean result = headCommand.isDirectory(file);
		assertTrue(result);
	}
	
	/**
	 * Test helper method isDirectory
	 * input hidden folder
	 * 
	 * @throw CatException
	 */
	@Test
	public void testIsDirectoryHidden() throws HeadException {
		File file = new File(FOLDERTESTHIDE);
		Boolean result = headCommand.isDirectory(file);
		assertTrue(result);
	}
	
	/**
	 * Test helper method isDirectory
	 * folder that does not exist
	 * 
	 * @throw CatException
	 */
	@Test
	public void testIsDirectoryNoSuchDirectory() throws HeadException {
		File file = new File("NoSuchFolder");
		Boolean result = headCommand.isDirectory(file);
		assertFalse(result);
	}
	
    @Test
    public void stdinForHead() throws AbstractApplicationException {

        String pipeInputArg = "Oysters are a family of bivalves with rough, thick shells." + System.lineSeparator();
        pipeInputArg += "Many species are edible, and are usually served raw." + System.lineSeparator();
        pipeInputArg += "They are also good when cooked." + System.lineSeparator();
        pipeInputArg += "In history, they were an important food source, especially in France and Britain." + System.lineSeparator();
        pipeInputArg += "They used to grow in huge oyster beds, but were \"overfished\" in the 19th century." + System.lineSeparator();
        pipeInputArg += "Nowadays they are more expensive, so eaten less often.";
        stdin = new ByteArrayInputStream(pipeInputArg.getBytes());

        String[] args = new String[] { };
        String expected = "Oysters are a family of bivalves with rough, thick shells." + System.lineSeparator();
        expected += "Many species are edible, and are usually served raw." + System.lineSeparator();
        expected += "They are also good when cooked." + System.lineSeparator();
        expected += "In history, they were an important food source, especially in France and Britain." + System.lineSeparator();
        expected += "They used to grow in huge oyster beds, but were \"overfished\" in the 19th century." + System.lineSeparator();
        expected += "Nowadays they are more expensive, so eaten less often." + Configurations.NEWLINE;

        stdout = new ByteArrayOutputStream();
        headCommand.run(args, stdin, stdout);
        Assert.assertEquals(expected, stdout.toString());
    }
    
    @Test
    public void multipleFilesMoreThan3() throws AbstractApplicationException {
        
        String[] args = new String[] { "-n" , "15" , "a.txt", "b.txt" };
        stdout = new ByteArrayOutputStream();
        headCommand.run(args, null, stdout);
        Assert.assertEquals("==>a.txt<==" + Configurations.NEWLINE +
                              "this is a" + Configurations.NEWLINE +
                              "==>b.txt<==" + Configurations.NEWLINE +
                               "<a.txt" + Configurations.NEWLINE, stdout.toString());
    }
    
    @Test (expected = HeadException.class)
    public void nNegativeNumber() throws AbstractApplicationException {
        
        String[] args = new String[] { "-n" , "-15" , "a.txt", "b.txt" };
        stdout = new ByteArrayOutputStream();
        headCommand.run(args, null, stdout);
    }
    
    @Test (expected = HeadException.class)
    public void nNonNumber() throws AbstractApplicationException {
        
        String[] args = new String[] { "-n" , "error" , "alo.txt", "bas.txt" };
        stdout = new ByteArrayOutputStream();
        headCommand.run(args, null, stdout);
    }
    
    @Test (expected = HeadException.class)
    public void nNegativeNumber0file() throws AbstractApplicationException {
        
        String[] args = new String[] { "-n" , "-5"};
        stdout = new ByteArrayOutputStream();
        headCommand.run(args, null, stdout);
    }
    
    @Test
    public void doesNotExistFile() throws AbstractApplicationException {
        
        String[] args = new String[] { "-n" , "15" , "alo.txt", "bas.txt" };
        stdout = new ByteArrayOutputStream();
        headCommand.run(args, null, stdout);
        Assert.assertEquals( "head: alo.txt:: No such file or directory" + Configurations.NEWLINE + 
            "head: bas.txt:: No such file or directory" + Configurations.NEWLINE, stdout.toString());
    }
    
    @Test
    public void headADirectory() throws AbstractApplicationException {
        
        String[] args = new String[] { "-n" , "15" , "test-files-basic", "src" };
        stdout = new ByteArrayOutputStream();
        headCommand.run(args, null, stdout);
        Assert.assertEquals( "head: test-files-basic:: Is a directory" + Configurations.NEWLINE + 
            "head: src:: Is a directory" + Configurations.NEWLINE, stdout.toString());
    }
}
