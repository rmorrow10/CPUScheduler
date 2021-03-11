import java.util.*;
 
public class Algorithm {
	public static void fcfs(ArrayList<Process> processes)
	{
		int time = 0;
		ArrayList<Integer> ready = new ArrayList<Integer>();
		Integer cpu = null;
		ArrayList<Integer> io = new ArrayList<Integer>();
		Integer ioDevice = null;
		
 
		//sorting according to arrival times
		Collections.sort(processes, Comparator.comparing(Process:: getArrivalTime));
		
		//counter to increment through and add processes to ready queue
		int counter = 0;
		
		//while loop to execute within until processes complete
		while(time < processes.get(processes.size() - 1).arrivalTime || ready.size() > 0 || cpu != null || io.size() > 0 || ioDevice != null) {
			//add all processes with start time at current time to ready queue
			while(counter < processes.size() && processes.get(counter).arrivalTime == time) {
				ready.add(counter);
				processes.get(counter).state = State.valueOf("READY");
				counter++;
			}
			
			//check if process done in cpu and move to proper location if it is
			if(cpu != null && processes.get(cpu).getCurrentCpuBurst() == processes.get(cpu).burstTime) {
				//move to io queue
				if(processes.get(cpu).burstCount < processes.get(cpu).ioBurstTime.size()) {
					io.add(cpu);
					processes.get(cpu).burstTime = 0;
					processes.get(cpu).state = State.valueOf("WAITING");
					cpu = null;
				}
				//terminate process
				else {
					processes.get(cpu).completionTime = time;
					processes.get(cpu).state = State.valueOf("TERMINATED");
					processes.get(cpu).turnaroundTime = processes.get(cpu).completionTime - processes.get(cpu).arrivalTime;
					cpu = null;
				}
			}
			
			//move completed io bursts back to ready
			if(ioDevice != null && processes.get(ioDevice).burstTime == processes.get(ioDevice).getCurrentIoBurst()) {
				ready.add(ioDevice);
				processes.get(ioDevice).state = State.valueOf("READY");
				processes.get(ioDevice).burstCount++;
				ioDevice = null;
			}
			
			//if ioDevice is empty, move process from io queue to ioDevice
			if(ioDevice == null && io.size() > 0) {
				ioDevice = io.get(0);
				io.remove(0);
			}
			
			//if cpu is empty, move process from ready to cpu
			if(cpu == null && ready.size() > 0) {
				cpu = ready.get(0);
				processes.get(cpu).state = State.valueOf("RUNNING");
				ready.remove(0);
			}
			
			//TODO add stats and GUI here
			
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
			for(int i = 0; i < ready.size(); i++) {
				processes.get(i).waitingTime++;
			}
			time++;
		}
	}
	
	
	public static void roundRobin(ArrayList<Process> processes, int quantum) {
		int time = 0;
		int timer = 0;
		ArrayList<Integer> ready = new ArrayList<Integer>();
		Integer cpu = null;
		ArrayList<Integer> io = new ArrayList<Integer>();
		Integer ioDevice = null;
		
 
		//sorting according to arrival times
		Collections.sort(processes, Comparator.comparing(Process:: getArrivalTime));
		
		//counter to increment through and add processes to ready queue
		int counter = 0;
		
		//while loop to execute within until processes complete
		while(time < processes.get(processes.size() - 1).arrivalTime || ready.size() > 0 || cpu != null || io.size() > 0 || ioDevice != null) {
			//add all processes with start time at current time to ready queue
			while(counter < processes.size() && processes.get(counter).arrivalTime == time) {
				ready.add(counter);
				processes.get(counter).state = State.valueOf("READY");
				counter++;
			}
			
			//check if process done in cpu and move to proper location if it is
			if(cpu != null && processes.get(cpu).getCurrentCpuBurst() == processes.get(cpu).burstTime) {
				//move to io queue
				if(processes.get(cpu).burstCount < processes.get(cpu).ioBurstTime.size()) {
					io.add(cpu);
					processes.get(cpu).burstTime = 0;
					processes.get(cpu).state = State.valueOf("WAITING");
					cpu = null;
				}
				//terminate process
				else {
					processes.get(cpu).completionTime = time;
					processes.get(cpu).state = State.valueOf("TERMINATED");
					processes.get(cpu).turnaroundTime = processes.get(cpu).completionTime - processes.get(cpu).arrivalTime;
					cpu = null;
				}
				timer = 0;
			}
			
			//check if process had quantum cycles and if so run a different process if one is available, otherwise continue and restart timer
			if(timer == quantum) {
				//change process being run
				if(ready.size() > 0) {
					ready.add(cpu);
					processes.get(cpu).state = State.valueOf("READY");
					cpu = ready.get(0);
					processes.get(cpu).state = State.valueOf("RUNNING");
					ready.remove(0);
				}
				//reset timer
				timer = 0;
			}
			
			//move completed io bursts back to ready
			if(ioDevice != null && processes.get(ioDevice).burstTime == processes.get(ioDevice).getCurrentIoBurst()) {
				ready.add(ioDevice);
				processes.get(ioDevice).state = State.valueOf("READY");
				processes.get(ioDevice).burstCount++;
				ioDevice = null;
			}
			
			//if ioDevice is empty, move process from io queue to ioDevice
			if(ioDevice == null && io.size() > 0) {
				ioDevice = io.get(0);
				io.remove(0);
			}
			
			//if cpu is empty, move process from ready to cpu
			if(cpu == null && ready.size() > 0) {
				cpu = ready.get(0);
				ready.remove(0);
				processes.get(cpu).state = State.valueOf("RUNNING");
				timer = 0;
			}

			//TODO add stats and GUI here
			
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
			for(int i = 0; i < ready.size(); i++) {
				processes.get(i).waitingTime++;
			}
			time++;
		}
	}
	
	
	public static void shortestJobFirst(ArrayList<Process> processes) {
		int time = 0;
		ArrayList<Integer> ready = new ArrayList<Integer>();
		Integer cpu = null;
		ArrayList<Integer> io = new ArrayList<Integer>();
		Integer ioDevice = null;
		
 
		//sorting according to arrival times
		Collections.sort(processes, Comparator.comparing(Process:: getArrivalTime));
		
		//counter to increment through and add processes to ready queue
		int counter = 0;
		
		//while loop to execute within until processes complete
		while(time < processes.get(processes.size() - 1).arrivalTime || ready.size() > 0 || cpu != null || io.size() > 0 || ioDevice != null) {
			//add all processes with start time at current time to ready queue
			while(counter < processes.size() && processes.get(counter).arrivalTime == time) {
				ready.add(counter);
				processes.get(counter).state = State.valueOf("READY");
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
					cpu = null;
				}
				//terminate process
				else {
					processes.get(cpu).completionTime = time;
					processes.get(cpu).state = State.valueOf("TERMINATED");
					processes.get(cpu).turnaroundTime = processes.get(cpu).completionTime - processes.get(cpu).arrivalTime;
					cpu = null;
				}
			}

			//move completed io bursts back to ready
			if(ioDevice != null && processes.get(ioDevice).burstTime == processes.get(ioDevice).getCurrentIoBurst()) {
				ready.add(ioDevice);
				processes.get(ioDevice).state = State.valueOf("READY");
				processes.get(ioDevice).burstCount++;
				ioDevice = null;
			}
			
			//if ioDevice is empty, move process from io queue to ioDevice
			if(ioDevice == null && io.size() > 0) {
				ioDevice = io.get(0);
				io.remove(0);
			}
			
			//if cpu is empty, move process from ready to cpu
			if(cpu == null && ready.size() > 0) {
				ready = sortByJobLength(processes, ready);
				cpu = ready.get(0);
				processes.get(cpu).state = State.valueOf("RUNNING");
				ready.remove(0);
			}

			//TODO add stats and GUI here
			
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
			for(int i = 0; i < ready.size(); i++) {
				processes.get(i).waitingTime++;
			}
			time++;
		}
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
	
	public static void priority(ArrayList<Process> processes) {
		int time = 0;
		ArrayList<Integer> ready = new ArrayList<Integer>();
		Integer cpu = null;
		ArrayList<Integer> io = new ArrayList<Integer>();
		Integer ioDevice = null;
		
 
		//sorting according to arrival times
		Collections.sort(processes, Comparator.comparing(Process:: getArrivalTime));
		
		//counter to increment through and add processes to ready queue
		int counter = 0;
		
		//while loop to execute within until processes complete
		while(time < processes.get(processes.size() - 1).arrivalTime || ready.size() > 0 || cpu != null || io.size() > 0 || ioDevice != null) {
			//add all processes with start time at current time to ready queue
			while(counter < processes.size() && processes.get(counter).arrivalTime == time) {
				//check if new process has higher priority than what is in cpu preemptively
				if(cpu != null && processes.get(counter).priority < processes.get(cpu).priority) {
					ready.add(cpu);
					processes.get(counter).state = State.valueOf("RUNNING");
					cpu = counter;
				}
				else {
					ready.add(counter);
					processes.get(counter).state = State.valueOf("READY");
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
					cpu = null;
				}
				//terminate process
				else {
					processes.get(cpu).completionTime = time;
					processes.get(cpu).state = State.valueOf("TERMINATED");
					processes.get(cpu).turnaroundTime = processes.get(cpu).completionTime - processes.get(cpu).arrivalTime;
					cpu = null;
				}
			}
			
			//move completed io bursts back to ready
			if(ioDevice != null && processes.get(ioDevice).burstTime == processes.get(ioDevice).getCurrentIoBurst()) {
				ready.add(ioDevice);
				processes.get(ioDevice).state = State.valueOf("READY");
				processes.get(ioDevice).burstCount++;
				ioDevice = null;
			}
			
			//if ioDevice is empty, move process from io queue to ioDevice
			if(ioDevice == null && io.size() > 0) {
				ioDevice = io.get(0);
				io.remove(0);
			}
			
			//if cpu is empty, move process from ready to cpu
			if(cpu == null && ready.size() > 0) {
				ready = sortByJobLength(processes, ready);
				cpu = ready.get(0);
				processes.get(cpu).state = State.valueOf("RUNNING");
				ready.remove(0);
			}

			//TODO add stats and GUI here
			
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
			for(int i = 0; i < ready.size(); i++) {
				processes.get(i).waitingTime++;
			}
			time++;
		}
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
}
