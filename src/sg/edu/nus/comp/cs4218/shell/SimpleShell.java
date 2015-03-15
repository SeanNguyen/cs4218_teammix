package sg.edu.nus.comp.cs4218.shell;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import sg.edu.nus.comp.cs4218.Configurations;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;
import sg.edu.nus.comp.cs4218.impl.app.*;

public class SimpleShell implements Shell {
	// public methods
	@Override
	public void parseAndEvaluate(String cmdline, OutputStream stdout)
			throws AbstractApplicationException, ShellException {
		try {
			Parser parser = new Parser();
			Command command = parser.parseCommandLine(cmdline);
			command.evaluate(null, stdout);
		} catch (Exception e) {
			try {
				stdout.write(e.getMessage().getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws AbstractApplicationException,
			ShellException, IOException {
		System.out.println(Configurations.MESSAGE_WELCOME);
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
		Environment.nameAppMaps.put(Configurations.APPNAME_SED, new SedCommand());

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
