import java.util.Scanner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Language{

	static FileOutputStream outputStream;
	static File outputFile;

	// scanner that reads source code
	static Scanner scanner;

	// current line of the source code
	static String currentLine;

	// all variables in the project
	static ArrayList<String> variables = new ArrayList<String>();

	// print in the console
	private static void print(String str){
		System.out.println(str);
	}

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

			print("exception");

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

			// write new line
			outputStream.write(System.getProperty("line.separator").getBytes());

			// flush the stream
			outputStream.flush();

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	// identify variable type
	private static String identifyVarType(String value){

		// check if string
		if(value.contains("\"")){
			return "String";
		}

		// check if boolean
		if(value.equals("true") || value.equals("false")){
			return "boolean";
		}

		try { 
       		Integer.parseInt(value); 
       		return "int";
    	} catch(Exception e){
    		
    	}

		// otherwise, it is a float
		return "float";
	}

	// handle variable declaration or initialization
	private static void handleVariable(){

		String ifStrVal = "";
		String value = "";
		String type = "";

		// if it is a string, extract the value first
		if(currentLine.contains("\"")){
			ifStrVal = currentLine.substring(currentLine.indexOf("\""), currentLine.lastIndexOf("\"") + 1);
		}

		// remove white spaces from line
		currentLine = currentLine.replaceAll("\\s+","");

		// get the index of the first letter of the variable name
		int firstLetterIndex = currentLine.indexOf('r') + 1;

		// get the index of the equal sign
		int equalIndex = currentLine.indexOf('=');

		// get the length of the line
		int lineLength = currentLine.length();

		// if line is not string, extract value
		if(ifStrVal.equals("")){
			value = currentLine.substring(equalIndex + 1, lineLength - 1);
		} else {
			value = ifStrVal;
		}

		// get the type of the variable
		type = identifyVarType(value);

		// extract the name of the variable
		String variableName = currentLine.substring(firstLetterIndex, equalIndex);

		// add the variable to the array list
		variables.add(variableName);

		// construct the translated java string
		String translated = type + " " + variableName + " = " + String.valueOf(value) + ";";

		// write to the output file
		writeToFile(translated);
	}

	// handles else statements
	private static void handleElse(){

		// remove white spaces
		currentLine = currentLine.replaceAll("\\s+","");

		// add space after else
		if(currentLine.contains("elseif")) currentLine = currentLine.substring(0, 4) + " " + currentLine.substring(4, currentLine.length());

		// replace : with {
		currentLine = currentLine.substring(0, currentLine.length() - 1) + "{";

		// write to the file
		writeToFile(currentLine);
	}

	// handle if statements
	private static void handleIfs(){

		// remove white spaces
		currentLine = currentLine.replaceAll("\\s+","");

		// replace : with { and add to translated code
		currentLine = currentLine.substring(0, currentLine.length() - 1) + "{";

		// write the first line to the output file
		writeToFile(currentLine);

		// read next line
		currentLine = scanner.nextLine();

		// handle if statement until you reach its end
		while(!currentLine.contains("endif;")){

			// remove white spaces before first char
			currentLine = currentLine.trim();

			print(currentLine);

			if(currentLine.contains("var")) handleVariable();
			else if(currentLine.startsWith("if")) handleIfs();
			else if(currentLine.startsWith("else")) handleElse();

			// read next line
			currentLine = scanner.nextLine();
		}

		// end the if statement
		writeToFile("}");
	}

	// parse the file
	private static void parseFile(Scanner scanner){

		// read the file line by line
		while(scanner.hasNextLine()){

			// get the current line in the source file
			currentLine = scanner.nextLine();

			// check if it is a variable declaration or initialization
			if(currentLine.startsWith("var")){

				// handle variables
				handleVariable();
			} 
			else if (currentLine.startsWith("if")){

				// handle if statements
				handleIfs();

			}
		}
	}

	public static void main(String[] args) {
		
		// specify the output file path
		outputFile = new File("C:\\Users\\Tudor\\Desktop\\OPEN\\output.txt");

		// ask user for input of source code
		print("Open file : ");

		// get the name of the file
		String sourceCode = getStringInput();

		// read the file
		readFile(sourceCode);
	}

}