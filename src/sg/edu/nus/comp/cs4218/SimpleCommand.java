package sg.edu.nus.comp.cs4218;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.ShellException;

public class SimpleCommand implements Command {

	//attributes
	private String appName;
	private Vector < String > arguments;
	
	//constructor
	public SimpleCommand (String appName, Vector < String > arguments) {
		this.appName = appName;
		this.arguments = arguments;
	}
	
	//public methods
	@Override
	public void evaluate(InputStream stdin, OutputStream stdout)
			throws AbstractApplicationException, ShellException {
		
	}

	@Override
	public void terminate() {
		
	}

}