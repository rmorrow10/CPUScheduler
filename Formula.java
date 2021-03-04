/**
 * @author Riley Morrow
 * Date: March 4, 2021
 * Description: A class file to define the variables and functions for the CPU Scheduler
 */

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class Formula {
	
	private OperatingSystemMXBean mbean = (com.sun.management.OperatingSystemMXBean)
	ManagementFactory.getOperatingSystemMXBean();
	
	private double CPUUtilization;
	private double throughput;
	private double turnaroundTime;
	private double waitingTime;
	private double responseTime;
	
	public Formula() {
	}
	
	public void setCPUUtilization() {
		this.CPUUtilization = CPUUtilization;
	}
	
	public double getCPUUtilization(OperatingSystemMXBean mbean) {
		CPUUtilization = mbean.getSystemCpuLoad() * 100.0;
		return CPUUtilization;
	}
	
	public void setThroughput() {
		this.throughput = throughput;
	}
	
	public double getThroughput() {
		//long startTime = System.nanoTime();
		//int threads;
		long endTime = System.nanoTime();
		//double timeDiffSeconds = (endTime - startTime) / 1000000000.0;
		//throughput = (threads / timeDiffSeconds);
		return throughput;
	}
	
	public void setTurnaroundTime() {
		this.turnaroundTime = turnaroundTime;
	}
	
	public double getTurnaroundTime() {
		return turnaroundTime;
	}
	
	public void setWaitingTime() {
		this.waitingTime = waitingTime;
	}
	
	public double getWaitingTime() {
		return waitingTime;
	}
	
	public void setResponseTime() {
		this.responseTime = responseTime;
	}
	
	public double getResponseTime() {
		return responseTime;
	}

}