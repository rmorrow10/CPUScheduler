import java.util.ArrayList;

public class Process {
	String name;
	Integer arrivalTime;
	Integer priority;
	ArrayList<Integer> cpuBurstTime;
	ArrayList<Integer> ioBurstTime;
	
	public Process() {
		
	}
	
	public Process(String name, int arrivalTime, int priority) {
		this.name = name;
		this.arrivalTime = arrivalTime;
		this.priority = priority;
		cpuBurstTime = new ArrayList<Integer>();
		ioBurstTime = new ArrayList<Integer>();
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
}
