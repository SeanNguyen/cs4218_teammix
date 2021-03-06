package sg.edu.nus.comp.cs4218.shell;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import sg.edu.nus.comp.cs4218.Command;
import sg.edu.nus.comp.cs4218.Configurations;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.app.*;

public class SimpleShell implements Shell {

	public SimpleShell() {
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
		Environment.nameAppMaps.put(Configurations.APPNAME_SED,
				new SedCommand());
		Environment.nameAppMaps.put(Configurations.APPNAME_GREP,
				new GrepCommand());
	}

	// public methods
	@Override
	public void parseAndEvaluate(String cmdline, OutputStream stdout)
			throws AbstractApplicationException, ShellException {
		Parser parser = new Parser();
		Command command = parser.parseCommandLine(cmdline);
		command.evaluate(null, stdout);
	}

	public static void main(String[] args) throws AbstractApplicationException,
			ShellException, IOException {
		System.out.println(Configurations.MESSAGE_WELCOME);
		// setup shell
		Scanner input = new Scanner(System.in);
		Shell shell = new SimpleShell();
		while (Environment.running) {
			System.out.printf(Environment.currentDirectory
					+ Configurations.MESSAGE_PROMPT);
			String line = input.nextLine();
			shell.parseAndEvaluate(line, System.out);
			System.out.println();
		}
		input.close();
	}
}
