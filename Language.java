import java.util.Scanner;
import java.io.File;

public class Language{

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
			Scanner fileScanner = new Scanner(file);

			// go through the file
			parseFile(fileScanner);

		} catch (Exception e){

			print("exception");

			e.printStackTrace();
		}	
	}

	// parse the file
	private static void parseFile(Scanner scanner){

		// read the file line by line
		while(scanner.hasNextLine()){
			
			print(scanner.nextLine());
		}

	}

	public static void main(String[] args) {
		
		// ask user for input of source code
		print("Open file : ");

		// get the name of the file
		String sourceCode = getStringInput();

		// read the file
		readFile(sourceCode);
	}

}