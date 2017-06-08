package main.java.com.jterm;

import java.io.*;
import java.util.ArrayList;

public class Dir {
	
  /*
  * PrintDir() void
  * 
  * Prints the contents of a specified directory
  * to a file.
  *
  * ArrayList<String> options - command options
  * 
  * -f
  * 	Changes the output format to only file
  * 	and directory names
  * -h
  * 	Prints help information
  * directory
  * 	Prints this directory rather than the
  * 	current working directory.
  *
  * Examples
  *
  *   PrintDir(options);
  *     => [Contents of "dir/"]
  *     =>     F RW 	myFile.txt		2 KB
  */
  public static void PrintDir(ArrayList<String> options) throws NullPointerException {

	  String path = JTerm.currentDirectory;
	  boolean printFull = true;
	  
	  for (String option: options) {
		  if (option.equals("-f")) {
			  printFull = false;
			  
		  }
		  else if (option.equals("-h"))
		  {
			  System.out.println("Command syntax:\n\tdir [-f] [-h] [directory]\n\nPrints a detailed table of the current working directory's subfolders and files.");
			  return;
			  
		  }
		  else
		  {
			  path = option;
			  
		  }
		  
	  }
	  
	  File dir = new File(path);
	  File[] files = dir.listFiles();
	  
	  /*
	   * Format of output:
	   * [FD] [RWHE] [filename] [size in KB]
	   * 
	   * Prefix definitions:
	   * 	F -- File
	   * 	D -- Directory
	   * 	R -- Readable
	   * 	W -- Writable
	   * 	H -- Hidden
	   * 
	   * Example:
	   * 	F RW	myfile.txt	   5 KB
	   */
	  
	  System.out.println("[Contents of \"" + path + "\"]");
	  for (File file: files)
	  {
		  if (printFull) {
			  System.out.println("\t" + (file.isFile() ? "F " : "D ") + (file.canRead() ? "R" : "") + (file.canWrite() ? "W" : "") + (file.isHidden() ? "H" : "") + "\t" + file.getName() + (file.getName().length() < 8 ? "\t\t\t" : (file.getName().length() > 15 ? "\t" : "\t\t")) + (file.length() / 1024) + " KB");
			  
		  } else {
			  System.out.println("\t" + file.getName());
			  
		  }
		  
	  }
	  
  }
  
  
  /*
   * ChangeDir() void
   * 
   * Changes the working directory to the specified
   * input.
   * 
   * -h
   * 	Prints help information
   * directory
   * 	Path to change the working directory to.
   *
   * ArrayList<String> options - command options
   */
  public static void ChangeDir(ArrayList<String> options) {
	  
	  String newDirectory = "";
	  
	  for (String option: options) {
		  if (option.equals("-h"))
		  {
			  System.out.println("Command syntax:\n\tcd [-h] directory\n\nChanges the working directory to the path specified.");
			  return;
			  
		  }
		  else
		  {
			  newDirectory = option;
			  
		  }
		  
	  }
	  
	  if (newDirectory.equals("")) {
		  System.out.println("Path not specified. Type \"cd -h\" for more information.");
		  return;
		  
	  }
	  
	  // Test if the input exists and if it is a directory
	  File dir = new File(newDirectory);
	  File newDir = new File(JTerm.currentDirectory + newDirectory);
	  
	  if (newDir.exists() && newDir.isDirectory()) {
		  newDirectory = JTerm.currentDirectory + newDirectory;
		  
	  }
	  else if ((!dir.exists() || !dir.isDirectory()) && (!newDir.exists() || !newDir.isDirectory()))
	  {
		  System.out.println("ERROR: Directory \"" + newDirectory + "\" is either does not exist or is not a valid directory.");
		  return;
		  
	  }
	  
	  if (!newDirectory.endsWith("/"))
	  {
		  newDirectory += "/";
		  
	  }
	  
	  // It does exist, and it is a directory, so just change the global working directory variable to the input
	  JTerm.currentDirectory = newDirectory;
	  
  }
  
  /*
   * PrintWorkingDir() void
   * 
   * Prints the working directory to the console.
   * 
   * -h
   * 	Prints help information
   *
   * ArrayList<String> options - command options
   */
  public static void PrintWorkingDir(ArrayList<String> options) {
	  
	  for (String option: options) {
		  if (option.equals("-h"))
		  {
			  System.out.println("Command syntax:\n\tpwd\n\nPrints the current Working Directory.");
			  return;
			  
		  }
		  
	  }
	  
	  // Simply print the currentDirectory variable to the console
	  System.out.println(JTerm.currentDirectory);
	  
  }
  
  /*
  * NewDir() void
  * 
  * Creates a new directory.
  * 
  * -h
  * 	Prints help information
  *
  * ArrayList<String> options - command options
  */
  public static void NewDir(ArrayList<String> options) {
	  
	  String name = "";
	  
	  for (String option: options) {
		  if (option.equals("-h"))
		  {
			  System.out.println("Command syntax:\n\tmd [-h] name\n\nCreates a new directory.");
			  return;
			  
		  }
		  else
		  {
			  name = JTerm.currentDirectory + option;
		  }
	  }
	  
	  File dir = new File(name);
	  dir.mkdir();
  }
  
}
