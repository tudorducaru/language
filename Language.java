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
	private static void handleVariable(String line){

		String ifStrVal = "";
		String value = "";
		String type = "";

		// if it is a string, extract the value first
		if(line.contains("\"")){
			ifStrVal = line.substring(line.indexOf("\""), line.lastIndexOf("\"") + 1);
		}

		// remove white spaces from line
		line = line.replaceAll("\\s+","");

		// get the index of the first letter of the variable name
		int firstLetterIndex = line.indexOf('r') + 1;

		// get the index of the equal sign
		int equalIndex = line.indexOf('=');

		// get the length of the line
		int lineLength = line.length();

		// if line is not string, extract value
		if(ifStrVal.equals("")){
			value = line.substring(equalIndex + 1, lineLength - 1);
		} else {
			value = ifStrVal;
		}

		// get the type of the variable
		type = identifyVarType(value);

		// extract the name of the variable
		String variableName = line.substring(firstLetterIndex, equalIndex);

		// add the variable to the array list
		variables.add(variableName);

		// construct the translated java string
		String translated = type + " " + variableName + " = " + String.valueOf(value) + ";";

		// write to the output file
		writeToFile(translated);
	}

	// handle if statements
	private static void handleIfs(String line){

		// remove white spaces
		line = line.replaceAll("\\s+","");

		// replace : with { and add to translated code
		line = line.substring(0, line.length() - 1) + "{";

		// write the first line to the output file
		writeToFile(line);

		// read next line
		line = scanner.nextLine();

		// handle if statement until you reach its end
		while(!line.contains("endif;")){

			if(line.contains("var")) handleVariable(line);
			else if(line.contains("if")) handleIfs(line);

			// read next line
			line = scanner.nextLine();

			print(line);
		}

		// end the if statement
		writeToFile("}");
	}

	// parse the file
	private static void parseFile(Scanner scanner){

		// read the file line by line
		while(scanner.hasNextLine()){

			// get the current line in the source file
			String currentLine = scanner.nextLine();

			// check if it is a variable declaration or initialization
			if(currentLine.startsWith("var")){

				// handle variables
				handleVariable(currentLine);
			} else if (currentLine.startsWith("if")){

				// handle if statements
				handleIfs(currentLine);
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