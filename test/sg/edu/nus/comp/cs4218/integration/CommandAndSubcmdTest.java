package sg.edu.nus.comp.cs4218.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.Configurations;
import sg.edu.nus.comp.cs4218.Environment;
import sg.edu.nus.comp.cs4218.Shell;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.LsException;
import sg.edu.nus.comp.cs4218.exception.SedException;
import sg.edu.nus.comp.cs4218.exception.ShellException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.Application;
import sg.edu.nus.comp.cs4218.exception.AbstractApplicationException;
import sg.edu.nus.comp.cs4218.exception.SedException;
import sg.edu.nus.comp.cs4218.impl.app.CatCommand;
import sg.edu.nus.comp.cs4218.impl.app.CdCommand;
import sg.edu.nus.comp.cs4218.impl.app.EchoCommand;
import sg.edu.nus.comp.cs4218.impl.app.FindCommand;
import sg.edu.nus.comp.cs4218.impl.app.GrepCommand;
import sg.edu.nus.comp.cs4218.impl.app.HeadCommand;
import sg.edu.nus.comp.cs4218.impl.app.LsCommand;
import sg.edu.nus.comp.cs4218.impl.app.PwdCommand;
import sg.edu.nus.comp.cs4218.impl.app.SedCommand;
import sg.edu.nus.comp.cs4218.impl.app.TailCommand;
import sg.edu.nus.comp.cs4218.impl.app.WcCommand;
import sg.edu.nus.comp.cs4218.shell.SimpleShell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class CommandAndSubcmdTest {
  private static Shell shell;
  private static ByteArrayOutputStream stdout;
  private static ByteArrayInputStream stdin;
  String[] args;

  @Before
  public void setUp() throws Exception {
	  Environment.currentDirectory = System.getProperty("user.dir");
      shell = new SimpleShell();
      stdout = new ByteArrayOutputStream();
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
      Environment.nameAppMaps.put(Configurations.APPNAME_GREP, new GrepCommand());
  }
  
  
  @Test
  public void testSedAndLsNegativeCase() throws AbstractApplicationException,
      ShellException {
    String input = "sed s/txt/javax/g `ls test-files-basic`";
    shell.parseAndEvaluate(input, stdout);
    Assert.assertEquals("NormalFolder: Does not exist" + Configurations.NEWLINE + Configurations.NEWLINE +"One.txt: Does not exist"+Configurations.NEWLINE , stdout.toString());
  }
  
  @Test
  public void testSedAndFind()
          throws AbstractApplicationException, ShellException {
      String input = "sed s/a/javax/g `find -name a.txt`";
      shell.parseAndEvaluate(input, stdout);
      Assert.assertEquals("this is javax" + Configurations.NEWLINE , stdout.toString());
  }
  
  @Test
  public void testSedAndFind2()
          throws AbstractApplicationException, ShellException {
      String input = "sed s/Day/Tmr/g `find -name One.txt`";
      shell.parseAndEvaluate(input, stdout);
      String expected = "This is one.txt"  + Configurations.NEWLINE +
          "Not two.txt or three.txt"  + Configurations.NEWLINE +
          "Tmr 1 was a long day."  + Configurations.NEWLINE + 
          "Tmr 2 was a short day." + Configurations.NEWLINE + 
          "Tmr 56 was great.";
      Assert.assertEquals(expected, stdout.toString());
  }
  
  @Test
  public void testLsAndEcho()
          throws AbstractApplicationException, ShellException {
      String input = "ls `echo \"test-files-basic\"`";
      shell.parseAndEvaluate(input, stdout);
      String expected = "NormalFolder" + File.separator + "\tOne.txt\t" + Configurations.NEWLINE;
      Assert.assertEquals(expected, stdout.toString());
  }
  
  @Test
  public void testLsAndFind()
          throws AbstractApplicationException, ShellException {
      String input = "ls `find -name NormalFolder`";
      shell.parseAndEvaluate(input, stdout);
      String expected = "Normal.txt\t" + Configurations.NEWLINE;
      Assert.assertEquals(expected, stdout.toString());
  }
  
  @Test
  public void testLsAndFind2()
          throws AbstractApplicationException, ShellException {
      String input = "ls `find test-files-basic -name .HideFolder`";
      shell.parseAndEvaluate(input, stdout);
      String expected = "textFile1.txt\ttextFile2.txt\t" + Configurations.NEWLINE;
      Assert.assertEquals(expected, stdout.toString());
  }
  
  @Test(expected = AbstractApplicationException.class)
  public void testLsAndFindNegativeCaseMissingName()
          throws AbstractApplicationException, ShellException {
      String input = "ls `find test-files-basic -n .FolderTestHide`";
      shell.parseAndEvaluate(input, stdout);
      String expected = "find: Missing -name";
      Assert.assertEquals(expected, stdout.toString());
  }
  
  @Test(expected = LsException.class)
  public void testLsAndLsNegativeNotDirectory()
          throws AbstractApplicationException, LsException, ShellException {
      String input = "ls `ls test-files-basic" + File.separator +"One.txt`";
      shell.parseAndEvaluate(input, stdout);
      String expected = "ls: One.txt: Not a directory";
      Assert.assertEquals(expected, stdout.toString());
  }
  
  @Test (expected = LsException.class)
  public void testLsAndFindaNonDirectory()
          throws AbstractApplicationException, ShellException{
      String input = "ls `find test-files-basic -name *.txt`";
      shell.parseAndEvaluate(input, stdout);
  }
  
  @Test
  public void testLsAndEcho2()
          throws AbstractApplicationException, ShellException {
      String input = "ls `echo test-files-basic`";
      shell.parseAndEvaluate(input, stdout);
      String expected = "NormalFolder"+File.separator + "\tOne.txt\t" + Configurations.NEWLINE;
      Assert.assertEquals(expected, stdout.toString());
  }
  
  @Test
  public void testLsAndEchoAndEcho()
          throws AbstractApplicationException, ShellException {
      String input = "ls `echo test-files-basic` `echo " + File.separator + "NormalFolder`";
      shell.parseAndEvaluate(input, stdout);
      String expected = "test-files-basic:" + Configurations.NEWLINE + "NormalFolder"+File.separator + "\tOne.txt\t" + Configurations.NEWLINE
    		  + Configurations.NEWLINE + "NormalFolder: Does not exist";
      Assert.assertEquals(expected, stdout.toString());
  }
  
  @Test
  public void testCatAndEchoAndEcho()
          throws AbstractApplicationException, ShellException {
      String input = "tail -n 1 `echo test-files-basic` `echo " + File.separator + "NormalFolder`";
      shell.parseAndEvaluate(input, stdout);
      String expected = "tail: test-files-basic:: Is a directory" + Configurations.NEWLINE + 
    		  "tail: NormalFolder:: No such file or directory"  + Configurations.NEWLINE;
      Assert.assertEquals(expected, stdout.toString());
  }
  
  @Test
  public void testGrepAndEchoAndEcho()
          throws AbstractApplicationException, ShellException {
      String input = "grep basic `echo test-files-basic` `echo " + File.separator + "NormalFolder`";
      shell.parseAndEvaluate(input, stdout);
      String expected = "grep: test-files-basic: Is a directory" + Configurations.NEWLINE + 
    		  "grep: "+File.separator+"NormalFolder: No such file or directory"  + Configurations.NEWLINE;
      Assert.assertEquals(expected, stdout.toString());
  }
}
