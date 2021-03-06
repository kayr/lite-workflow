package com.kayr.github.liteworkflow;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Hello world!
 *
 */
public class App {

	private static Scanner scanner;
	private static PrintStream out;
	private static PrintStream err;

	static {
		scanner = new Scanner(System.in);
		out = System.out;
		err = System.out;
	}

//	public static void main(String[] args) {
//		Net net = Resources.getNetWithAnd1();
//
////		executeTask(net);
//
//		net.setValue("Name", "tonny");
//		net.setValue("village", "Kiwatule");
//		net.setValue("householdID", "HID205");
//		net.setValue("Age", "25");
//
////		net.setValue("VillageName", "Bweyos");
////		net.setValue("VillageLocation", "Kiriniay");
////		net.setValue("VillageCode", "BWY090");
////		net.setValue("MotherID_", "MOT78");
////		net.setValue("RoundNumber", "89382");
////		net.setValue("FACode", "90");
////		net.setValue("WomanName_", "Jane");
////		net.setValue("enumerator_u", "oxd78");
//		//net.setValue("AnyPreviousBirth", "No");
//
//		net.start();
//		String str = "";
//		int numTask = 0;
//
//		out.println(net);
//		out.println("[ERROR] Net Initiliased");
//
//		while (net.isStarted()) {
//			Vector<Task> currentEnabledTasks = net.getCurrentEnabledTasks();
//			for (int i = 0; i < currentEnabledTasks.size(); i++) {
//				Task task = currentEnabledTasks.elementAt(i);
//				executeTask(task);
//				net.complete(task);
//				str += "," + task.getId();
//				numTask++;
//				err.println("[ERROR] NetVariables: " + net.getVariablesTable());
//			}
//		}
//
//		out.println("[ERROR] Workflow End");
//		out.println(numTask + " Tasks (" + str + ")");
//	}

	public static void main(String[] args) {
		Net net = ServiceCycle.instance();

		net.setNetID("cows");
		net.setValue("cowname","Nshirug");
		net.setValue("cowid","ID002");

//		executeTask(net);

//		net.setValue("Name", "tonny");
//		net.setValue("village", "Kiwatule");
//		net.setValue("householdID", "HID205");
//		net.setValue("Age", "25");

//		net.setValue("VillageName", "Bweyos");
//		net.setValue("VillageLocation", "Kiriniay");
//		net.setValue("VillageCode", "BWY090");
//		net.setValue("MotherID_", "MOT78");
//		net.setValue("RoundNumber", "89382");
//		net.setValue("FACode", "90");
//		net.setValue("WomanName_", "Jane");
//		net.setValue("enumerator_u", "oxd78");
		//net.setValue("AnyPreviousBirth", "No");

		net.start();
		String str = "";
		int numTask = 0;

		out.println(net);
		out.println("[ERROR] Net Initialised");

		while (net.isStarted()) {
			List<Task> currentEnabledTasks = net.getCurrentEnabledTasks();
			for (int i = 0; i < currentEnabledTasks.size(); i++) {
				Task task = currentEnabledTasks.get(i);
				executeTask(task);
				net.complete(task);
				str += "," + task.getId();
				numTask++;
				err.println("    NetVariables: " + net.getVariablesTable().values());
			}
		}

		out.println(" Workflow End");
		out.println(numTask + " Tasks (" + str + ")");
	}

	private static AtomicLong count = new AtomicLong();

	private static void executeTask(Element task) {
		Map<String, Variable> variablesTable = task.getVariablesTable();

		System.out.println("\n " + count.incrementAndGet() + ": Task: " + task.getId() + " (" + task.toString() + ")");
		out.println("-----------------------------");

		for (Variable variable : variablesTable.values()) {
			if (variable.isInput()) {
				System.out.println("    ++ " + variable.getName() + "=" + variable.getValue());
			}
		}

		for (Variable variable : variablesTable.values()) {
			if ((!variable.isOutput() && !(task instanceof Net)) || (task instanceof Net && variable.isOutput())) {
				continue;
			}
			out.print("    " + variable.getId() + ": ");
			variable.setValue(scanner.nextLine());
		}

	}

	private static void completeTask(Task enabledTask, Net net) {
		try {
			net = StreamUtil.copyNet(net);
			net.complete(enabledTask);
			net = StreamUtil.copyNet(net);
		} catch (Exception ex) {
			throw new RuntimeException("Unable to copy net", ex);
		}
	}
}
