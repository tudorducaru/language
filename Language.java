package langpack;

import java.util.Scanner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import langpack.Util;

public class Language{

	static FileOutputStream outputStream;
	static File outputFile;

	// parts of the file
	static String header = "";
	static String classes = "";
	static String functions = "";
	static String mainBody = "";

	// scanner that reads source code
	static Scanner scanner;

	// current line of the source code
	static String currentLine;

	// get input from user
	private static String getStringInput(){
		Scanner scanner = new Scanner(System.in);
		return scanner.nextLine();
	}

	// read a file 
	private static void readFile(String filename){

		// create file instance
		File file = new File(filename);

		try {
			// get a scanner of that file
			scanner = new Scanner(file);

			// go through the file
			parseFile(scanner);

		} catch (Exception e){

			Util.print("exception");

			e.printStackTrace();
		}	
	}

	// write new line
	private static void writeNewLine(){

		try {
			// create the output stream
			outputStream = new FileOutputStream(outputFile, true);

			outputStream.write(System.getProperty("line.separator").getBytes());
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	// write to external file
	private static void writeToFile(String str){

		try{

			// create the output stream
			outputStream = new FileOutputStream(outputFile, true);

			// create the output file if it does not exist
			if(!outputFile.exists()){
				outputFile.createNewFile();
			}

			// convert string to byte array
			byte[] bytes = str.getBytes();

			// write to the file
			outputStream.write(bytes);

			writeNewLine();

			// flush the stream
			outputStream.flush();

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	// handles else statements
	private static void handleElse(int mode){

		// remove white spaces
		currentLine = currentLine.replaceAll("\\s+","");

		// add space after else
		if(currentLine.contains("elseif")) currentLine = currentLine.substring(0, 4) + " " + currentLine.substring(4, currentLine.length());

		// replace : with {
		currentLine = currentLine.substring(0, currentLine.length() - 1) + "{";

		// add } at the beginning
		currentLine = "} " + currentLine;


		if(mode == 0){
			mainBody += currentLine;
		} else {
			functions += currentLine;
		}
	}

	// handle if, while and for statements
	private static void handleWIF(int mode){

		// remove white spaces
		currentLine = currentLine.replaceAll("\\s+","");

		// replace : with { and add to translated code
		currentLine = currentLine.substring(0, currentLine.length() - 1) + "{";

		if(mode == 0){
			mainBody += currentLine;
		} else {
			functions += currentLine;
		}

		// read next line
		currentLine = scanner.nextLine();

		// handle if statement until you reach its end
		while(!currentLine.contains("end;")){

			// remove white spaces before first char
			currentLine = currentLine.trim();

			analyzeLine(mode);

			// read next line
			currentLine = scanner.nextLine();
		}


		if(mode == 0){
			mainBody += "}";
		} else {
			functions += "}";
		}
	}

	// helper function to generate getter for object
	private static void generateGetter(String name, String type){

		// capitalize name
		String capName = name.substring(0, 1).toUpperCase() + name.substring(1);

		classes += "	public " + type + " get" + capName + "(){";

		classes += "		return this." + name + ";";

		classes += "	}";
	}

	// helper function to generate setter for object
	private static void generateSetter(String name, String type){

		// capitalize name
		String capName = name.substring(0, 1).toUpperCase() + name.substring(1);

		classes += "	public void" + " set" + capName + "(" + type + " " + name + "){";

		classes += "		this." + name + " = " + name + ";";

		classes += "	}";
	}

	// instantiate a class
	private static void handleObjectInstantiation(){

		// split the string into words
		String[] strParts = currentLine.split(" ");

		// get the name of the object (class)
		String objectName = strParts[2].replaceAll("\\s+","");

		// write the header
		classes += " " +  "private static class " + objectName + " {";

		// skip through a line
		currentLine = scanner.nextLine();
		currentLine = scanner.nextLine();

		// generate an empty constructor
		classes += "	public " + objectName + "(){}";

		// add fields until you meet the functions
		while(!currentLine.contains("end")){

			// split fields in key value pairs
			String[] memberParts = currentLine.split(":");

			// get the name of the member and its type
			String memberName = memberParts[0].replaceAll("\\s+","");
			String memberType = memberParts[1].replaceAll("\\s+","");

			// add the member
			classes += "	private " + memberType + " " + memberName + ";";

			// generate getter and setter for the member
			generateGetter(memberName, memberType);
			generateSetter(memberName, memberType);

			// read another line
			currentLine = scanner.nextLine();
		}	

		// finish the class definition
		classes += "}";
	}

	// handle header 
	private static void handleHeader(){

		// read until you finish import export statements
		while(currentLine.startsWith("package") || currentLine.startsWith("import")){

			header += currentLine;

			currentLine = scanner.nextLine();
		}
	}

	// handle function
	private static void handleFunction(){

		// split into parts
		String[] parts = currentLine.split(" ");

		String namePart = "";

		for(int i = 1; i < parts.length; i++){
			namePart += " " + parts[i];
		}

		// remove last char
		namePart = namePart.substring(0, namePart.length() - 1) + "{";

		functions += " " + "public static " + namePart;

		// read next line
		currentLine = scanner.nextLine();

		// read and analyze the lines until you get to the return statement
		while(!currentLine.contains("return")){

			analyzeLine(1);

			currentLine = scanner.nextLine();
		}

		functions += currentLine + "}";
	}

	// function to analyze each line
	private static void analyzeLine(int mode){

		// check if it is a variable declaration or initialization
		if(currentLine.startsWith("else")) handleElse(mode);
		else if(currentLine.startsWith("while") || currentLine.startsWith("for") || currentLine.startsWith("if")) handleWIF(mode);
		else if(currentLine.startsWith("new")) handleObjectInstantiation();
		else if(currentLine.startsWith("function")) handleFunction();
		else if(currentLine.startsWith("package") || currentLine.startsWith("import")) handleHeader();
		else {
			if(mode == 0){
				mainBody += currentLine;
			} else {
				functions += currentLine;
			}
		}
	}

	// parse the file
	private static void parseFile(Scanner scanner){

		// read the file line by line
		while(scanner.hasNextLine()){

			// get the current line in the source file
			currentLine = scanner.nextLine();

			analyzeLine(0);
		}

		// asemble the output file
		assembleFile();
	}

	// function to assemble the pieces of the program
	private static void assembleFile(){

		writeToFile(header);

		writeNewLine();

		writeToFile("public class Output{");

		writeToFile(classes);

		writeNewLine();

		writeToFile(functions);

		writeNewLine();

		writeToFile("public static void main(String[] args) {");

		writeNewLine();

		writeToFile(mainBody);

		writeNewLine();

		writeToFile("	}");
		writeToFile("}");

	}

	public static void main(String[] args) {
		
		// specify the output file path
		outputFile = new File("C:\\Users\\Tudor\\Desktop\\OPEN\\Output.txt");

		// ask user for input of source code
		Util.print("Open file : ");

		// get the name of the file
		String sourceCode = getStringInput();

		// read the file
		readFile(sourceCode);
	}

}