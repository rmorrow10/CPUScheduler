import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) {
		System.out.print("Please enter simulation file: ");
		Scanner user = new Scanner(System.in);
		String fileName = user.next();
		File inputFile = new File(fileName);
		Scanner in;
		ArrayList<Process> processes = new ArrayList<Process>();
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
				processes.add(new Process(results.get(0), Integer.parseInt(results.get(1)), Integer.parseInt(results.get(2))));
				for(int i = 3; i < results.size() - 1; i += 2) {
					processes.get(counter).addBursts(Integer.parseInt(results.get(i)), Integer.parseInt(results.get(i + 1)));
				}
				processes.get(counter).addCpuBurst(Integer.parseInt(results.get(results.size() - 1)));
				processes.get(counter).pid = counter;
				counter++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Process p: processes) {
			System.out.println(p.toString());
		}
		System.out.print("Please enter number for desired sorting algorithm as follows: 1 First Come First Serve 2 Round Robin 3 Shortest Job First 4 Priority");
		int choice = user.nextInt();
		switch(choice) {
			case 1:
				Algorithm.fcfs(processes);
				break;
			case 2:
				System.out.print("Please enter desired quantum: ");
				int quantum = user.nextInt();
				Algorithm.roundRobin(processes, quantum);
				break;
			case 3:
				Algorithm.shortestJobFirst(processes);
				break;
			case 4:
				Algorithm.priority(processes);
				break;
			default:
				System.out.println("Invalid input. Run program again. Program terminated.");
		}
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
