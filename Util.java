package langpack;
import java.util.Scanner;

public class Util{

	// helper function to print something
	public static void print(Object obj){
		System.out.println(String.valueOf(obj));
	}

	// helper function to get a string
	public static String getStringInput(){
		print("Please insert a string : ");
		Scanner scanner = new Scanner(System.in);
		return scanner.nextLine();
	}

	// helper function to get an int
	public static int getIntInput(){
		print("Please insert an integer : ");
		Scanner scanner = new Scanner(System.in);
		
		try {
			return Integer.parseInt(scanner.nextLine());
		} catch (Exception e){
			print("Invalig value inserted");
			return -1;
		}
	}

	// helper function to get a float 
	public static float getFloatInput(){
		print("Please insert a float : ");

		Scanner scanner = new Scanner(System.in);
		
		try {
			return Long.parseLong(scanner.nextLine());
		} catch (Exception e){
			print("Invalig value inserted");
			return -1;
		}
	}

	// helper function to get a boolean
	public static boolean getBooleanInput(){
		print("Please insert a boolean : ");

		Scanner scanner = new Scanner(System.in);
		
		try {
			return Boolean.parseBoolean(scanner.nextLine());
		} catch (Exception e){
			print("Invalig value inserted");
			return false;
		}
	}

	public static boolean prim(int a){
		int n = 2;
		while(n<a/2){
			if(a%2==0) return false;
			else n++;
		}
		return true;
	}

	public static int cmmdc(int a, int b){
		while (a != b) {
			if(a > b) a = a - b;
			else b = b - a;
		}
		return a;
	}

	public static int factorial(int a){
		int p = 1;
		for(int i = 1; i <= a; i++) p = p * i;
		return p;
	}

	public static double putere(int a, double b){
		for(int i = 0; i < a; i++) b = b*b;
		return b;
	}

	public static void main(String[] args) {
	}
}