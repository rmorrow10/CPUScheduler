import java.util.ArrayList;

public class UI {
	public UI() {
		
	}
	
	public static void draw(ArrayList<Process> processes, ArrayList<Integer> ready, ArrayList<Integer> io, Integer cpu, Integer ioDevice, int time) {
		System.out.println("\nTime: " + time);
		System.out.println("_________________");
		System.out.print("| CPU 0: ");
		if(cpu == null) {
			System.out.print("idle    |  ");
		}
		else {
			System.out.print(processes.get(cpu).name);
			for(int i = 0; i < 8 - processes.get(cpu).name.length(); i++) {
				System.out.print(" ");
			}
			System.out.print("|  ");
		}
		System.out.print("Ready:      ");
		for(int i = 0; i < ready.size(); i++) {
			System.out.print(" " + processes.get(ready.get(i)).name);
		}
		System.out.println("\n_________________");
		System.out.print("| I/O 0: ");
		if(ioDevice == null) {
			System.out.print("idle    |  ");
		}
		else {
			System.out.print(processes.get(ioDevice).name);
			for(int i = 0; i < 8 - processes.get(ioDevice).name.length(); i++) {
				System.out.print(" ");
			}
			System.out.print("|  ");
		}
		System.out.print("I/O queue: ");
		for(int i = 0; i < io.size(); i++) {
			System.out.print(" " + processes.get(io.get(i)).name);
		}
		System.out.println("\n_________________");
	}
}
