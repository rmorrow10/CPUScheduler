import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Interpreter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		gui.start();
	}
	
	public static ArrayList<Process> parse() {
		File inputFile = new File(gui.fileName);
		Scanner in;
		ArrayList<Process> process = new ArrayList<Process>();
		try {
			in = new Scanner(inputFile);
			int counter = 0;
			while(in.hasNextLine()) {
				String line = in.nextLine();
				String[] result = line.split(" ");
				//eliminate blanks from results
				ArrayList<String> results = new ArrayList<String>();
				for(int i = 0; i < result.length; i++) {
					if(!result[i].equals("")) {
						results.add(result[i]);
					}
				}
				process.add(new Process(results.get(0), Integer.parseInt(results.get(1)), Integer.parseInt(results.get(2))));
				for(int i = 3; i < results.size() - 1; i += 2) {
					process.get(counter).addBursts(Integer.parseInt(results.get(i)), Integer.parseInt(results.get(i + 1)));
				}
				process.get(counter).addCpuBurst(Integer.parseInt(results.get(results.size() - 1)));
				counter++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Process p: process) {
			System.out.println(p.toString());
		}
		return process;
	}

	public boolean isInt(String s) {
		if(s == null) {
			return false;
		}
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
