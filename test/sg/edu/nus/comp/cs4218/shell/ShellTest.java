package sg.edu.nus.comp.cs4218.shell;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Configurations;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.app.CatCommand;
import sg.edu.nus.comp.cs4218.impl.app.CdCommand;
import sg.edu.nus.comp.cs4218.impl.app.EchoCommand;
import sg.edu.nus.comp.cs4218.impl.app.FindCommand;
import sg.edu.nus.comp.cs4218.impl.app.HeadCommand;
import sg.edu.nus.comp.cs4218.impl.app.LsCommand;
import sg.edu.nus.comp.cs4218.impl.app.PwdCommand;
import sg.edu.nus.comp.cs4218.impl.app.TailCommand;
import sg.edu.nus.comp.cs4218.impl.app.WcCommand;

public class ShellTest {

	private Shell shell;

	@BeforeClass
	public static void intiEnvironment() {
		// initialize apps in environment
		Environment.nameAppMaps.put(Configurations.APPNAME_CD, new CdCommand());
		Environment.nameAppMaps.put(Configurations.APPNAME_LS, new LsCommand());
		Environment.nameAppMaps.put(Configurations.APPNAME_ECHO,
				new EchoCommand());
		Environment.nameAppMaps.put(Configurations.APPNAME_CAT,
				new CatCommand());
		Environment.nameAppMaps.put(Configurations.APPNAME_HEAD,
				new HeadCommand());
		Environment.nameAppMaps.put(Configurations.APPNAME_TAIL,
				new TailCommand());
		Environment.nameAppMaps.put(Configurations.APPNAME_PWD,
				new PwdCommand());
		Environment.nameAppMaps.put(Configurations.APPNAME_FIND,
				new FindCommand());
		Environment.nameAppMaps.put(Configurations.APPNAME_WC, new WcCommand());
	}

	@Before
	public void setUp() throws Exception {
		this.shell = new SimpleShell();
	}

	@Test
	public void simpleCmdLine() throws AbstractApplicationException,
			ShellException {
		String cmdLine = "echo abc";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		String result = outputStream.toString();
		String expected = "abc";
		assertEquals(expected, result);
	}

	@Test
	public void appNameInQuote() throws AbstractApplicationException,
			ShellException {
		String cmdLine = "\"echo\" abc";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		String result = outputStream.toString();
		String expected = "abc";
		assertEquals(expected, result);
	}

	@Test
	public void appNameFromCmdSubstitute() throws AbstractApplicationException,
			ShellException {
		String cmdLine = "`echo echo` abc";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		String result = outputStream.toString();
		String expected = "abc";
		assertEquals(expected, result);
	}
	
	@Test
	public void appNameFromCmdSubstitute2() throws AbstractApplicationException,
			ShellException {
		String cmdLine = "`cat c.txt`";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		String result = outputStream.toString();
		String expected = "abc";
		assertEquals(expected, result);
	}

	@Test
	public void appNameFromCmdSubstituteInQuote()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "\"`echo echo`\" abc";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		String result = outputStream.toString();
		String expected = "abc";
		assertEquals(expected, result);
	}
	
	@Test
	public void seqCommands()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "echo abc;echo xyz";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		String result = outputStream.toString();
		String expected = "abcxyz";
		assertEquals(expected, result);
	}
	
	@Test
	public void pipeCommands()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "echo abc|echo xyz";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		String result = outputStream.toString();
		String expected = "xyz";
		assertEquals(expected, result);
	}
	
	@Test
	public void substituteCommands()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "echo \"front `echo middle` back\"";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		String result = outputStream.toString();
		String expected = "front middle back";
		assertEquals(expected, result);
	}
	
	@Test (expected = ShellException.class)
	public void incomleteCommands()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "echo \"front `echo middle back\"";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
	}
	
	@Test (expected = ShellException.class)
	public void missingApp()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "someapp arg1";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
	}
	
	@Test (expected = ShellException.class)
	public void missingDir()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "cat <somedir.txt";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
	}
	
	@Test (expected = AbstractApplicationException.class)
	public void appError()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "cat";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
	}
	
	
	
	@Test
	public void ioBeforeAppName()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "<a.txt cat";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
		String result = outputStream.toString();
		String expected = "this is a" + Configurations.NEWLINE;
		assertEquals(expected, result);
	}
	
	@Test (expected = ShellException.class)
	public void emptyCommand()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
	}
	
//	@Test (expected = ShellException.class)
//	public void emptyPipeCommand()
//			throws AbstractApplicationException, ShellException {
//		String cmdLine = "echo a;";
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		shell.parseAndEvaluate(cmdLine, outputStream);
//	}
//	
//	@Test (expected = ShellException.class)
//	public void emptyCallCommand()
//			throws AbstractApplicationException, ShellException {
//		String cmdLine = "echo a|";
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//		shell.parseAndEvaluate(cmdLine, outputStream);
//	}
	
	@Test (expected = ShellException.class)
	public void commandWithOnlyIo()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "<a.txt > b.txt";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
	}
	
	@Test (expected = ShellException.class)
	public void multipleInputIo()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "<a.txt < b.txt";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
	}
	
	@Test (expected = ShellException.class)
	public void multipleOutputIo()
			throws AbstractApplicationException, ShellException {
		String cmdLine = ">a.txt > b.txt";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
	}
	
	@Test
	public void validOutputStream()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "echo abc >ramdomFile.txt";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
	}
	
	@Test (expected = ShellException.class)
	public void invalidInputFile()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "echo abc <notExist.txt";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
	}
	
	@Test (expected = ShellException.class)
	public void invalidOutputFile()
			throws AbstractApplicationException, ShellException {
		String cmdLine = "echo abc <no#/tExist.txt";
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		shell.parseAndEvaluate(cmdLine, outputStream);
	}

	@Test
	public void mainTest() {
		try {
			Environment.running = false;
			SimpleShell.main(null);
		} catch (AbstractApplicationException e) {
			//pass
		} catch (ShellException e) {
			//pass
		} catch (IOException e) {
			//pass
		}
	}
}
