import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
public class gui {
	public static ArrayList<Process> processes;
	//using ID's for processes which will not change
	public static ArrayList<Integer> ready = new ArrayList<Integer>();
	public static ArrayList<Integer> io = new ArrayList<Integer>();
	public static ArrayList<Integer> NEW = new ArrayList<Integer>();
	public static Process running;
	public static ArrayList<Integer> terminated;
	public static ArrayList<String> status;
	public static String fileName;
	public static String algorithm;
	public static int fps;
	public static int quantum;
	public static boolean started = false;
	public static boolean paused = true;
	public static JTable processTable;
	
	
	public static void start() {
		JFrame frame = new JFrame("CS 405 Project 2 CPU Scheduler");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(750, 500);

		//create radio buttons to select auto/manual
		JRadioButton autoButton = new JRadioButton("Auto");
		autoButton.setSelected(true);
		//autoButton.setSize(70, 40);

	    JRadioButton manualButton = new JRadioButton("Manual");
	    //manualButton.setSize(70, 40);
	    
	    //add radio buttons to group
	    ButtonGroup autoSelectorGroup = new ButtonGroup();
	    autoSelectorGroup.add(autoButton);
	    autoSelectorGroup.add(manualButton);
	    
	    //create dropdown menu for scheduling algorithm
	    String[] algorithmStrings = {"First Come First Serve", "Priority", "Round Robin", "Shortest Job First"};
		JComboBox algorithmSelector = new JComboBox(algorithmStrings);
		
		//add quantum option
		JLabel quantumLabel = new JLabel("Quantum:");
		JTextField quantum = new JTextField();
		quantum.setColumns(4);
		quantum.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
	            String value = quantum.getText();
	            if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' || ke.getKeyCode() == 39 || ke.getKeyCode() == 37) {
	            	if(quantum.getText().length() == 1) {
	            		if(quantum.getText().charAt(0) < '0' || quantum.getText().charAt(0) > '9') {
	            			quantum.setText("");
	            		}
	            	}
	            }
	            else {
	            	quantum.setText("");
	            }
	         }
		});
		
		//add fps option
		Integer[] frames = {1, 2, 5, 15, 60};
		JComboBox framesPerSecond = new JComboBox(frames);
	    
	    //create browse button to select file
  		JButton browse = new JButton("Browse");
  		browse.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				//create file chooser
  				final JFileChooser fc = new JFileChooser();
  				FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
  				fc.setFileFilter(filter);
  				int returnVal = fc.showOpenDialog((Component)e.getSource());
  				if (returnVal == JFileChooser.APPROVE_OPTION) {
  			        File file = fc.getSelectedFile();
  			        try {
  			           fileName = file.toString();
  			           processes = Interpreter.parse();
  			        } catch (Exception ex) {
  			          System.out.println("problem accessing file"+file.getAbsolutePath());
  			        }
  			    } 
  			    else {
  			        System.out.println("File access cancelled by user.");
  			    }
  			}
  		});
  		
  		//create next button
  		JButton next = new JButton("Next");
  		//TODO
	    
	    //frame.getContentPane().add(autoSelectorGroup);
	    JPanel modePane = new JPanel();
	    modePane.setLayout(new BoxLayout(modePane, BoxLayout.LINE_AXIS));
	    modePane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	    modePane.add(browse);
	    modePane.add(Box.createHorizontalGlue());
	    modePane.add(algorithmSelector);
	    modePane.add(Box.createHorizontalGlue());
	    modePane.add(quantumLabel);
	    modePane.add(quantum);
	    modePane.add(Box.createHorizontalGlue());
	    modePane.add(framesPerSecond);
	    modePane.add(Box.createHorizontalGlue());
	    modePane.add(autoButton);
	    modePane.add(Box.createRigidArea(new Dimension(10, 0)));
	    modePane.add(manualButton);
		JButton startButton = new JButton("Start/Pause");
		
		
		
		
		//TODO
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(begin()) {
					frame.add(processTable, BorderLayout.PAGE_END);
					//SwingUtilities.updateComponentTreeUI(frame);
					frame.repaint();
				}
			}
		});
		
		
		
		
		
		//frame.getContentPane().add(start);
		modePane.add(Box.createRigidArea(new Dimension(10, 0)));
	    modePane.add(startButton);
	    modePane.add(next);
	    frame.add(modePane, BorderLayout.PAGE_START);
		
		
		frame.setVisible(true);
	}
	
	public static boolean begin() {
		if(!started) {
			String[] columnNames = {"Id", "Arrival", "Priority", "CPU Bursts", "IO Bursts", "Start Time", "Finish Time",
					"Wait Time", "Wait IO Time", "Status"};
			String[][] processData = new String[10][processes.size()];
			for(int i = 0; i < processes.size(); i++) {
				processData[0][i] = processes.get(i).name;
				processData[1][i] = Integer.toString(processes.get(i).arrivalTime);
				processData[2][i] = Integer.toString(processes.get(i).priority);
				processData[4][i] = processes.get(i).cpuBurstTime.toString();
				processData[5][i] = processes.get(i).ioBurstTime.toString();
				processData[6][i] = "";
				processData[7][i] = "";
				processData[8][i] = "";
				processData[9][i] = "";
				
			}
			processTable = new JTable(processData, columnNames);
			//JTable processTable = new JTable();
			started = true;
			return true;
		}
		return false;
	}
	
	public static void speed() {
		int timer = 1000 / fps;
		try {
			Thread.sleep(timer);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void add(Integer p) {
		ready.add(p);
	}
	
	public static void add(Integer p, int position) {
		ready.add(position, p);
	}
	
	public static void moveToIO(Integer p) {
		ready.remove(p);
		io.add(p);
	}
	
	public static void moveToReady(Integer p) {
		io.remove(p);
		ready.add(p);
	}
	
	public static void moveToReady(Integer p, int position) {
		io.remove(p);
		ready.add(position, p);
	}
	
	public static void finish(Integer p) {
		ready.remove(p);
	}
}
