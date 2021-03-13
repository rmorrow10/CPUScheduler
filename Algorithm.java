import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
 
public class Algorithm {
	public static void fcfs(ArrayList<Process> processes, int fps) throws IOException
	{
		int time = 0;
		ArrayList<Integer> ready = new ArrayList<Integer>();
		Integer cpu = null;
		ArrayList<Integer> io = new ArrayList<Integer>();
		Integer ioDevice = null;
		FileWriter result = new FileWriter("results.txt");
		int cpuIdle = 0;
		double cpuUtilization = 0;
		int completed = 0;
		double throughput = 0;
		int waitingTime = 0;
		
 
		//sorting according to arrival times
		Collections.sort(processes, Comparator.comparing(Process:: getArrivalTime));
		
		//counter to increment through and add processes to ready queue
		int counter = 0;
		
		//while loop to execute within until processes complete
		while(time < processes.get(processes.size() - 1).arrivalTime || ready.size() > 0 || cpu != null || io.size() > 0 || ioDevice != null) {
			//add all processes with start time at current time to ready queue
			while(counter < processes.size() && processes.get(counter).arrivalTime == time) {
				result.write("\nProcess " + processes.get(counter).name + " is created at " + time);
				ready.add(counter);
				processes.get(counter).state = State.valueOf("READY");
				result.write("\nProcess " + processes.get(counter).name + " moved from new to ready at " + time);
				counter++;
			}
			
			//check if process done in cpu and move to proper location if it is
			if(cpu != null && processes.get(cpu).getCurrentCpuBurst() == processes.get(cpu).burstTime) {
				//move to io queue
				if(processes.get(cpu).burstCount < processes.get(cpu).ioBurstTime.size()) {
					io.add(cpu);
					processes.get(cpu).burstTime = 0;
					processes.get(cpu).state = State.valueOf("WAITING");
					result.write("\nProcess " + processes.get(cpu).name + " moved from CPU to IO queue at " + time);
					cpu = null;
				}
				//terminate process
				else {
					processes.get(cpu).completionTime = time;
					processes.get(cpu).state = State.valueOf("TERMINATED");
					processes.get(cpu).turnaroundTime = processes.get(cpu).completionTime - processes.get(cpu).arrivalTime;
					result.write("\nProcess " + processes.get(cpu).name + " terminated at " + time);
					cpu = null;
					completed++;
				}
			}
			
			//move completed io bursts back to ready
			if(ioDevice != null && processes.get(ioDevice).burstTime == processes.get(ioDevice).getCurrentIoBurst()) {
				ready.add(ioDevice);
				processes.get(ioDevice).state = State.valueOf("READY");
				result.write("\nProcess " + processes.get(ioDevice).name + " moved from IO to ready at " + time);
				processes.get(ioDevice).burstCount++;
				ioDevice = null;
			}
			
			//if ioDevice is empty, move process from io queue to ioDevice
			if(ioDevice == null && io.size() > 0) {
				ioDevice = io.get(0);
				io.remove(0);
				result.write("\nProcess " + processes.get(ioDevice).name + " moved from IO queue to IO at " + time);
			}
			
			//if cpu is empty, move process from ready to cpu
			if(cpu == null && ready.size() > 0) {
				cpu = ready.get(0);
				processes.get(cpu).state = State.valueOf("RUNNING");
				ready.remove(0);
				result.write("\nProcess " + processes.get(cpu).name + " moved from ready to CPU at " + time);
			}
			
			//TODO add stats and GUI here
			if(time != 0) {
				cpuUtilization = (1 - (double) cpuIdle / (double) time) * 100;
				throughput = (double) completed / (double) time;
			}
			UI.draw(processes, ready, io, cpu, ioDevice, time);
			System.out.println("CPU utilization: " + cpuUtilization +  '%');
			System.out.println("Throughput: " + throughput);
			
			//increment everything
			if(ioDevice != null) {
				processes.get(ioDevice).burstTime++;
			}
			for(int i = 0; i < io.size(); i++) {
				processes.get(io.get(i)).ioWaitingTime++;
			}
			if(cpu != null) {
				processes.get(cpu).burstTime++;
			}
			else {
				cpuIdle++;
			}
			for(int i = 0; i < ready.size(); i++) {
				processes.get(i).waitingTime++;
			}
			waitingTime += ready.size();
			wait(fps);
			time++;
		}
		cpuUtilization = (1 - (double) cpuIdle / (double) time) * 100;
		result.close();
	}
	
	
	public static void roundRobin(ArrayList<Process> processes, int quantum, int fps) throws IOException {
		int time = 0;
		int timer = 0;
		ArrayList<Integer> ready = new ArrayList<Integer>();
		Integer cpu = null;
		ArrayList<Integer> io = new ArrayList<Integer>();
		Integer ioDevice = null;
		FileWriter result = new FileWriter("results.txt");
		int cpuIdle = 0;
		double cpuUtilization = 0;
		int completed = 0;
		double throughput = 0;
		int waitingTime = 0;
		int responseTime = 0;
		
 
		//sorting according to arrival times
		Collections.sort(processes, Comparator.comparing(Process:: getArrivalTime));
		
		//counter to increment through and add processes to ready queue
		int counter = 0;
		
		//while loop to execute within until processes complete
		while(time < processes.get(processes.size() - 1).arrivalTime || ready.size() > 0 || cpu != null || io.size() > 0 || ioDevice != null) {
			//add all processes with start time at current time to ready queue
			while(counter < processes.size() && processes.get(counter).arrivalTime == time) {
				result.write("\nProcess " + processes.get(counter).name + " is created at " + time);
				ready.add(counter);
				processes.get(counter).state = State.valueOf("READY");
				result.write("\nProcess " + processes.get(counter).name + " moved from new to ready at " + time);
				counter++;
			}
			
			//check if process done in cpu and move to proper location if it is
			if(cpu != null && processes.get(cpu).getCurrentCpuBurst() == processes.get(cpu).burstTime) {
				//move to io queue
				if(processes.get(cpu).burstCount < processes.get(cpu).ioBurstTime.size()) {
					io.add(cpu);
					processes.get(cpu).burstTime = 0;
					processes.get(cpu).state = State.valueOf("WAITING");
					result.write("\nProcess " + processes.get(cpu).name + " moved from CPU to IO queue at " + time);
					cpu = null;
				}
				//terminate process
				else {
					processes.get(cpu).completionTime = time;
					processes.get(cpu).state = State.valueOf("TERMINATED");
					processes.get(cpu).turnaroundTime = processes.get(cpu).completionTime - processes.get(cpu).arrivalTime;
					result.write("\nProcess " + processes.get(cpu).name + " terminated at " + time);
					cpu = null;
					completed++;
				}
				timer = 0;
			}
			
			//check if process had quantum cycles and if so run a different process if one is available, otherwise continue and restart timer
			if(timer == quantum) {
				//change process being run
				if(ready.size() > 0) {
					ready.add(cpu);
					processes.get(cpu).state = State.valueOf("READY");
					result.write("\nProcess " + processes.get(cpu).name + " interrupted from CPU and moved to back of ready at " + time);
					cpu = ready.get(0);
					processes.get(cpu).state = State.valueOf("RUNNING");
					result.write("\nProcess " + processes.get(cpu).name + " moved from ready to CPU at " + time);
					ready.remove(0);
				}
				//reset timer
				timer = 0;
			}
			
			//move completed io bursts back to ready
			if(ioDevice != null && processes.get(ioDevice).burstTime == processes.get(ioDevice).getCurrentIoBurst()) {
				ready.add(ioDevice);
				processes.get(ioDevice).state = State.valueOf("READY");
				result.write("\nProcess " + processes.get(ioDevice).name + " moved from IO to ready at " + time);
				processes.get(ioDevice).burstCount++;
				ioDevice = null;
			}
			
			//if ioDevice is empty, move process from io queue to ioDevice
			if(ioDevice == null && io.size() > 0) {
				ioDevice = io.get(0);
				io.remove(0);
				result.write("\nProcess " + processes.get(ioDevice).name + " moved from IO queue to IO at " + time);
			}
			
			//if cpu is empty, move process from ready to cpu
			if(cpu == null && ready.size() > 0) {
				cpu = ready.get(0);
				ready.remove(0);
				processes.get(cpu).state = State.valueOf("RUNNING");
				result.write("\nProcess " + processes.get(cpu).name + " moved from ready to CPU at " + time);
				timer = 0;
			}

			//TODO add stats and GUI here
			if(time != 0) {
				cpuUtilization = (1 - (double) cpuIdle / (double) time) * 100;
				throughput = (double) completed / (double) time;
			}
			System.out.println("CPU utilization: " + cpuUtilization +  '%');
			System.out.println("Throughput: " + throughput);
			
			UI.draw(processes, ready, io, cpu, ioDevice, time);
			
			//increment everything
			if(ioDevice != null) {
				processes.get(ioDevice).burstTime++;
			}
			for(int i = 0; i < io.size(); i++) {
				processes.get(io.get(i)).ioWaitingTime++;
			}
			if(cpu != null) {
				processes.get(cpu).burstTime++;
				timer++;
			}
			else {
				cpuIdle++;
			}
			for(int i = 0; i < ready.size(); i++) {
				processes.get(i).waitingTime++;
			}
			wait(fps);
			time++;
		}
		cpuUtilization = (1 - (double) cpuIdle / (double) time) * 100;
		System.out.println(cpuUtilization +  '%');
		result.close();
	}
	
	
	public static void shortestJobFirst(ArrayList<Process> processes, int fps) throws IOException {
		int time = 0;
		ArrayList<Integer> ready = new ArrayList<Integer>();
		Integer cpu = null;
		ArrayList<Integer> io = new ArrayList<Integer>();
		Integer ioDevice = null;
		FileWriter result = new FileWriter("results.txt");
		int cpuIdle = 0;
		double cpuUtilization = 0;
		int completed = 0;
		double throughput = 0;
		int waitingTime = 0;
		int responseTime = 0;
		
 
		//sorting according to arrival times
		Collections.sort(processes, Comparator.comparing(Process:: getArrivalTime));
		
		//counter to increment through and add processes to ready queue
		int counter = 0;
		
		//while loop to execute within until processes complete
		while(time < processes.get(processes.size() - 1).arrivalTime || ready.size() > 0 || cpu != null || io.size() > 0 || ioDevice != null) {
			//add all processes with start time at current time to ready queue
			while(counter < processes.size() && processes.get(counter).arrivalTime == time) {
				result.write("\nProcess " + processes.get(counter).name + " is created at " + time);
				ready.add(counter);
				processes.get(counter).state = State.valueOf("READY");
				result.write("\nProcess " + processes.get(counter).name + " moved from new to ready at " + time);
				counter++;
				ready = sortByJobLength(processes, ready);
			}
			
			//check if process done in cpu and move to proper location if it is
			if(cpu != null && processes.get(cpu).getCurrentCpuBurst() == processes.get(cpu).burstTime) {
				//move to io queue
				if(processes.get(cpu).burstCount < processes.get(cpu).ioBurstTime.size()) {
					io.add(cpu);
					processes.get(cpu).burstTime = 0;
					processes.get(cpu).state = State.valueOf("WAITING");
					result.write("\nProcess " + processes.get(cpu).name + " moved from CPU to IO queue at " + time);
					cpu = null;
				}
				//terminate process
				else {
					processes.get(cpu).completionTime = time;
					processes.get(cpu).state = State.valueOf("TERMINATED");
					processes.get(cpu).turnaroundTime = processes.get(cpu).completionTime - processes.get(cpu).arrivalTime;
					result.write("\nProcess " + processes.get(cpu).name + " terminated at " + time);
					cpu = null;
					completed++;
				}
			}

			//move completed io bursts back to ready
			if(ioDevice != null && processes.get(ioDevice).burstTime == processes.get(ioDevice).getCurrentIoBurst()) {
				ready.add(ioDevice);
				processes.get(ioDevice).state = State.valueOf("READY");
				result.write("\nProcess " + processes.get(ioDevice).name + " moved from IO to ready at " + time);
				processes.get(ioDevice).burstCount++;
				ioDevice = null;
			}
			
			//if ioDevice is empty, move process from io queue to ioDevice
			if(ioDevice == null && io.size() > 0) {
				ioDevice = io.get(0);
				io.remove(0);
				result.write("\nProcess " + processes.get(ioDevice).name + " moved from IO queue to IO at " + time);
			}
			
			//if cpu is empty, move process from ready to cpu
			if(cpu == null && ready.size() > 0) {
				ready = sortByJobLength(processes, ready);
				cpu = ready.get(0);
				processes.get(cpu).state = State.valueOf("RUNNING");
				result.write("\nProcess " + processes.get(cpu).name + " moved from ready to CPU at " + time);
				ready.remove(0);
			}

			//TODO add stats and GUI here
			if(time != 0) {
				cpuUtilization = (1 - (double) cpuIdle / (double) time) * 100;
				throughput = (double) completed / (double) time;
			}
			System.out.println("CPU utilization: " + cpuUtilization +  '%');
			System.out.println("Throughput: " + throughput);
			
			UI.draw(processes, ready, io, cpu, ioDevice, time);
			
			//increment everything
			if(ioDevice != null) {
				processes.get(ioDevice).burstTime++;
			}
			for(int i = 0; i < io.size(); i++) {
				processes.get(io.get(i)).ioWaitingTime++;
			}
			if(cpu != null) {
				processes.get(cpu).burstTime++;
			}
			else {
				cpuIdle++;
			}
			for(int i = 0; i < ready.size(); i++) {
				processes.get(i).waitingTime++;
			}
			wait(fps);
			time++;
		}
		cpuUtilization = (1 - (double) cpuIdle / (double) time) * 100;
		System.out.println(cpuUtilization +  '%');
		result.close();
	}
	
	public static ArrayList<Integer> sortByJobLength(ArrayList<Process> processes, ArrayList<Integer> ready) {
		for(int i = 0; i < ready.size(); i++) {
			for(int j = 0; j < ready.size() - 1; j++) {
				if(processes.get(ready.get(j)).getJobLength() > processes.get(ready.get(j + 1)).getJobLength()) {
					Integer temp = ready.get(j);
					ready.remove(j);
					ready.add(j + 1, temp);
				}
			}
		}
		return ready;
	}
	
	public static void priority(ArrayList<Process> processes, int fps) throws IOException {
		int time = 0;
		ArrayList<Integer> ready = new ArrayList<Integer>();
		Integer cpu = null;
		ArrayList<Integer> io = new ArrayList<Integer>();
		Integer ioDevice = null;
		FileWriter result = new FileWriter("results.txt");
		int cpuIdle = 0;
		double cpuUtilization = 0;
		int completed = 0;
		double throughput = 0;
		int waitingTime = 0;
		int responseTime = 0;
		
 
		//sorting according to arrival times
		Collections.sort(processes, Comparator.comparing(Process:: getArrivalTime));
		
		//counter to increment through and add processes to ready queue
		int counter = 0;
		
		//while loop to execute within until processes complete
		while(time < processes.get(processes.size() - 1).arrivalTime || ready.size() > 0 || cpu != null || io.size() > 0 || ioDevice != null) {
			//add all processes with start time at current time to ready queue
			while(counter < processes.size() && processes.get(counter).arrivalTime == time) {
				result.write("Process " + processes.get(counter).name + " is created at " + time);
				//check if new process has higher priority than what is in cpu preemptively
				if(cpu != null && processes.get(counter).priority < processes.get(cpu).priority) {
					if(processes.get(cpu).burstCount < processes.get(cpu).ioBurstTime.size()) {
						processes.get(cpu).completionTime = time;
						processes.get(cpu).state = State.valueOf("TERMINATED");
						processes.get(cpu).turnaroundTime = processes.get(cpu).completionTime - processes.get(cpu).arrivalTime;
						result.write("\nProcess " + processes.get(cpu).name + " terminated at " + time);
						completed++;
					}
					else {
						ready.add(cpu);
						processes.get(cpu).state = State.valueOf("WAITING");
					}
					result.write("\nProcess " + processes.get(counter).name + " moved from new to CPU at " + time);
					processes.get(counter).state = State.valueOf("RUNNING");
					cpu = counter;
				}
				else {
					ready.add(counter);
					processes.get(counter).state = State.valueOf("READY");
					result.write("\nProcess " + processes.get(counter).name + " moved from new to ready at " + time);
				}
				counter++;
				ready = sortByPriority(processes, ready);
			}
			
			//check if process done in cpu and move to proper location if it is
			if(cpu != null && processes.get(cpu).getCurrentCpuBurst() == processes.get(cpu).burstTime) {
				//move to io queue
				if(processes.get(cpu).burstCount < processes.get(cpu).ioBurstTime.size()) {
					io.add(cpu);
					processes.get(cpu).burstTime = 0;
					processes.get(cpu).state = State.valueOf("WAITING");
					result.write("\nProcess " + processes.get(cpu).name + " moved from CPU to IO queue at " + time);
					cpu = null;
				}
				//terminate process
				else {
					processes.get(cpu).completionTime = time;
					processes.get(cpu).state = State.valueOf("TERMINATED");
					processes.get(cpu).turnaroundTime = processes.get(cpu).completionTime - processes.get(cpu).arrivalTime;
					result.write("\nProcess " + processes.get(cpu).name + " terminated at " + time);
					cpu = null;
					completed++;
				}
			}
			
			//move completed io bursts back to ready
			if(ioDevice != null && processes.get(ioDevice).burstTime == processes.get(ioDevice).getCurrentIoBurst()) {
				ready.add(ioDevice);
				processes.get(ioDevice).state = State.valueOf("READY");
				result.write("\nProcess " + processes.get(ioDevice).name + " moved from IO to ready at " + time);
				processes.get(ioDevice).burstCount++;
				ioDevice = null;
			}
			
			//if ioDevice is empty, move process from io queue to ioDevice
			if(ioDevice == null && io.size() > 0) {
				ioDevice = io.get(0);
				io.remove(0);
				result.write("\nProcess " + processes.get(ioDevice).name + " moved from IO queue to IO at " + time);
			}
			
			//if cpu is empty, move process from ready to cpu
			if(cpu == null && ready.size() > 0) {
				ready = sortByJobLength(processes, ready);
				cpu = ready.get(0);
				processes.get(cpu).state = State.valueOf("RUNNING");
				result.write("\nProcess " + processes.get(cpu).name + " moved from ready to CPU at " + time);
				ready.remove(0);
			}

			//TODO add stats and GUI here
			if(time != 0) {
				cpuUtilization = (1 - (double) cpuIdle / (double) time) * 100;
				throughput = (double) completed / (double) time;
			}
			System.out.println("CPU utilization: " + cpuUtilization +  '%');
			System.out.println("Throughput: " + throughput);
			
			UI.draw(processes, ready, io, cpu, ioDevice, time);
			
			//increment everything
			if(ioDevice != null) {
				processes.get(ioDevice).burstTime++;
			}
			for(int i = 0; i < io.size(); i++) {
				processes.get(io.get(i)).ioWaitingTime++;
			}
			if(cpu != null) {
				processes.get(cpu).burstTime++;
			}
			else {
				cpuIdle++;
			}
			for(int i = 0; i < ready.size(); i++) {
				processes.get(i).waitingTime++;
			}
			wait(fps);
			time++;
		}
		cpuUtilization = (1 - (double) cpuIdle / (double) time) * 100;
		System.out.println(cpuUtilization +  '%');
		result.close();
	}
	
	//sorts ready queue of processes (ready queue stores process location) by priority and returns the sorted array
	public static ArrayList<Integer> sortByPriority(ArrayList<Process> processes, ArrayList<Integer> ready) {
		for(int i = 0; i < ready.size(); i++) {
			for(int j = 0; j < ready.size() - 1; j++) {
				if(processes.get(ready.get(j)).priority > processes.get(ready.get(j + 1)).priority) {
					Integer temp = ready.get(j);
					ready.remove(j);
					ready.add(j + 1, temp);
				}
			}
		}
		return ready;
	}
	
	public static void wait(int fps) {
		try {
			if(fps > 0) {
				Thread.sleep(1000/fps);
			}
			else if(fps == -1) {
				System.in.read();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			//do nothing
		}
	}
}
