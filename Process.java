import java.util.ArrayList;

public class Process {
	String name;
	int pid;
	Integer arrivalTime;
	Integer priority;
	ArrayList<Integer> cpuBurstTime;
	ArrayList<Integer> ioBurstTime;
	Integer turnaroundTime;
	Integer waitingTime = 0;
	Integer ioWaitingTime = 0;
	Integer completionTime;
	Integer burstTime = 0;
	Integer burstCount = 0;
	State state;
	
	public Process() {
		
	}
	
	public Process(String name, int arrivalTime, int priority) {
		this.name = name;
		this.arrivalTime = arrivalTime;
		this.priority = priority;
		cpuBurstTime = new ArrayList<Integer>();
		ioBurstTime = new ArrayList<Integer>();
		waitingTime = 0;
		state = State.valueOf("NEW");
	}
	
	public void addBursts(int cpuBurstTime, int ioBurstTime) {
		this.cpuBurstTime.add(cpuBurstTime);
		this.ioBurstTime.add(ioBurstTime);
	}
	
	public void addCpuBurst(int cpuBurstTime) {
		this.cpuBurstTime.add(cpuBurstTime);
	}
	
	public String toString() {
		String result = name + ' ' + arrivalTime + ' ' + priority;
		for(int i = 0; i < ioBurstTime.size(); i++) {
			result = result + ' ' + cpuBurstTime.get(i) + ' ' + ioBurstTime.get(i);
		}
		result = result + ' ' + cpuBurstTime.get(cpuBurstTime.size() - 1);
		return result;
	}
	
	public int getArrivalTime() {
		return arrivalTime;
	}
	
	public int getPriority() {
		return priority;
	}
	
	//returns the total cpu burst time remaining
	public int getJobLength() {
		//return cpuBurstTime.get(burstCount);
		//if it is supposed to be next burst time, comment out the below lines and uncomment the above line
		int result = 0;
		for(int i = burstCount; i < cpuBurstTime.size(); i++) {
			result += cpuBurstTime.get(i);
		}
		return result;
	}
	
	//returns how long the current cpu burst lasts
	public int getCurrentCpuBurst() {
		return cpuBurstTime.get(burstCount);
	}
	
	//returns how long the current io burst lasts
	public int getCurrentIoBurst() {
		return ioBurstTime.get(burstCount);
	}
}
