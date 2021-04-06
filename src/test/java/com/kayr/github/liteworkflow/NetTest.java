package com.kayr.github.liteworkflow;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author kay
 */
public class NetTest extends TestCase {

	private Net instance;

	public void testNetInstaceCorrectVariable() {
		Net net = Resources.getNet();
		checkVariableExistWithName(net, "name", "sex", "is_father", "is_pregnant");

		Task task = net.getTask("enterdetails");
		checkVariableExistWithName(task, "name", "sex");

		task = net.getTask("confirm");
		checkVariableExistWithName(task, "name", "sex", "detail");
	}

	private void checkVariableExistWithName(Element net, String... taskVarNames) {
		for (int i = 0; i < taskVarNames.length; i++) {
			String taskVarId = taskVarNames[i];
			Variable variable = net.getVariable(taskVarId);
			assertEquals(taskVarId, variable.getName());
		}

	}

	protected Map<String, Variable> checkCorrectVariableTableSize(Task enabledTask, int size) {
		Map<String, Variable> variablesTable = enabledTask.getVariablesTable();
		assertEquals(size + " varibles are expect in task " + enabledTask.toString(), size, variablesTable.size());
		return variablesTable;
	}

	protected Variable checkVariable(Element element, String varName, String value) {
		Map<String, Variable> variablesTable = element.getVariablesTable();
		Variable nameVariable = variablesTable.get(varName);

		assertNotNull("Variable not expected to be null", nameVariable);
		assertEquals("Expected " + element.getId() + ".Variable: " + varName + " = " + value, value, nameVariable.getValue());
		return nameVariable;
	}

	@Override
	protected void setUp() throws Exception {
		instance = Resources.getNet();
//		System.out.println("Finished Initialising Workflow:\n" + instance.toString());

		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetOutFlows() {
		System.out.println("getOutFlows");
		List result = instance.getOutFlows();
		assertEquals(1, result.size());
	}

	public void testNumberOfTasksIsFour() {
		Map<String, Task> netTasks = instance.getNetTasks();
		assertEquals(6, netTasks.size()); //six including start and end
	}

	public void testWorkFlowFlowsSmoothly() {
		//Start the workflow
		instance.start();

		System.out.println(instance);


		//** ====================== Task 1  **//
		List<Task> currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(currentEnabledTasks.size(), 1);

		Task enabledTask = currentEnabledTasks.get(0);
		assertEquals("enterdetails", enabledTask.getId());
		checkCorrectVariableTableSize(enabledTask, 2);

		Variable nameVariable = checkVariable(enabledTask, "name", null);
		Variable sexVariable = checkVariable(enabledTask, "sex", null);
		nameVariable.setValue("Ronald");
		sexVariable.setValue("m");

		completeTask(enabledTask);
		assertTrue(instance.isStarted());
		assertFalse(instance.isComplete());
		assertEquals("Net Variables Not Having correct Values", "Ronald", instance.getValue("name"));
		assertEquals("net varibale are expected to have correct values", "m", instance.getValue("sex"));
		//** Compled Task 1 **//



		//** ======================== Task2 **//
		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(1, currentEnabledTasks.size());

		Task isFatherTask = currentEnabledTasks.get(0);
		assertEquals(isFatherTask.getId(), "isfather");
		checkCorrectVariableTableSize(isFatherTask, 2);

		//Check correct variable names and values
		checkVariable(isFatherTask, "name", "Ronald");
		checkVariable(isFatherTask, "is_father", null);

		//fill in the values
		isFatherTask.setValue("is_father", "father");

		//complete task
		completeTask(isFatherTask);

		assertTrue(instance.isStarted());
		assertFalse(instance.isComplete());
		checkVariable(instance, "is_father", "father");
		checkVariable(instance, "name", "Ronald");
		checkVariable(instance, "sex", "m");
		//**  Completed Task 2**//



		//**========================= Task 3 **//
		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(1, currentEnabledTasks.size());

		Task comfirmTask = currentEnabledTasks.get(0);
		assertEquals("confirm", comfirmTask.getId());
		checkCorrectVariableTableSize(comfirmTask, 3);

		checkVariable(comfirmTask, "detail", "father");
		checkVariable(comfirmTask, "name", "Ronald");
		checkVariable(comfirmTask, "sex", "m");

		comfirmTask.setValue("detail", "father-hood");
		comfirmTask.setValue("name", "Ronald.K");

		completeTask(comfirmTask);

		checkVariable(instance, "is_father", "father-hood");
		checkVariable(instance, "name", "Ronald.K");
		checkVariable(instance, "sex", "m");
		//** Complete Task 3 **//

		//************ Check instance is complete ****/
		currentEnabledTasks = instance.getCurrentEnabledTasks();

		assertTrue("No Tasks Are Expected to be enabled", currentEnabledTasks.isEmpty());
		assertTrue("Net is expected to be in completed state", instance.isComplete());
		assertFalse(instance.isStarted());

		//***********Complete//

	}

	public void testWorkFlowFlowsSmoothly2() {
		//Start the workflow
		instance.start();


		//** ====================== Task 1  **//
		List<Task> currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(currentEnabledTasks.size(), 1);

		Task enabledTask = currentEnabledTasks.get(0);
		assertEquals("enterdetails", enabledTask.getId());
		checkCorrectVariableTableSize(enabledTask, 2);

		Variable nameVariable = checkVariable(enabledTask, "name", null);
		Variable sexVariable = checkVariable(enabledTask, "sex", null);
		nameVariable.setValue("Ronald");
		sexVariable.setValue("blah");

		completeTask(enabledTask);
		assertFalse(instance.isStarted());
		assertTrue(instance.isComplete());
		assertEquals("Net Variables Not Having correct Values", "Ronald", instance.getValue("name"));
		assertEquals("net varibale are expected to have correct values", "blah", instance.getValue("sex"));
		//** Compled Task 1 **/
	}

	public void testWorkFlowFlowsSmoothly3() {
		//Start the workflow
		instance.start();

		System.out.println(instance);


		//** ====================== Task 1  **//
		List<Task> currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(currentEnabledTasks.size(), 1);

		Task enabledTask = currentEnabledTasks.get(0);
		assertEquals("enterdetails", enabledTask.getId());
		checkCorrectVariableTableSize(enabledTask, 2);

		Variable nameVariable = checkVariable(enabledTask, "name", null);
		Variable sexVariable = checkVariable(enabledTask, "sex", null);
		nameVariable.setValue("Ronald");
		sexVariable.setValue("f");

		completeTask(enabledTask);
		assertTrue(instance.isStarted());
		assertFalse(instance.isComplete());
		assertEquals("Net Variables Not Having correct Values", "Ronald", instance.getValue("name"));
		assertEquals("net varibale are expected to have correct values", "f", instance.getValue("sex"));
		//** Compled Task 1 **//



		//** ======================== Task2 **//
		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(1, currentEnabledTasks.size());

		Task isPregnantTask = currentEnabledTasks.get(0);
		assertEquals(isPregnantTask.getId(), "ispregnant");
		checkCorrectVariableTableSize(isPregnantTask, 2);

		//Check correct variable names and values
		checkVariable(isPregnantTask, "name", "Ronald");
		checkVariable(isPregnantTask, "is_pregnant", null);

		//fill in the values
		isPregnantTask.setValue("is_pregnant", "pregnant");

		//complete task
		completeTask(isPregnantTask);

		assertTrue(instance.isStarted());
		assertFalse(instance.isComplete());
		checkVariable(instance, "is_pregnant", "pregnant");
		checkVariable(instance, "name", "Ronald");
		checkVariable(instance, "sex", "f");
		//**  Completed Task 2**//



		//**========================= Task 3 **//
		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(1, currentEnabledTasks.size());

		Task comfirmTask = currentEnabledTasks.get(0);
		assertEquals("confirm", comfirmTask.getId());
		checkCorrectVariableTableSize(comfirmTask, 3);

		checkVariable(comfirmTask, "detail", null);
		checkVariable(comfirmTask, "name", "Ronald");
		checkVariable(comfirmTask, "sex", "f");

		comfirmTask.setValue("detail", "father-hood");
		comfirmTask.setValue("name", "Ronald.K");

		completeTask(comfirmTask);

		checkVariable(instance, "is_father", "father-hood");
		checkVariable(instance, "name", "Ronald.K");
		checkVariable(instance, "sex", "f");
		//** Complete Task 3 **//

		//************ Check instance is complete ****/
		currentEnabledTasks = instance.getCurrentEnabledTasks();

		System.out.println(currentEnabledTasks);

		assertTrue("No Tasks Are Expected to be enabled", currentEnabledTasks.isEmpty());
		assertTrue("Net is expected to be in completed state", instance.isComplete());
		assertFalse(instance.isStarted());

		//***********Complete//

	}

	public void testPeterNetFlowsSmoothlyPregnancyTrue() throws IOException, IllegalAccessException, InstantiationException {
		instance = Resources.getNetWithAnd1();

		instance.setValue("Name", "tonny");
		instance.setValue("village", "Kiwatule");
		instance.setValue("householdID", "HID205");
		instance.setValue("Age", "25");
		//Start the workflow
		instance.start();

		System.out.println(instance);


		//** ====================== Task 1  **//
		List<Task> currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(currentEnabledTasks.size(), 1);

		Task enabledTask = currentEnabledTasks.get(0);
		assertEquals("Reproductive_info_4", enabledTask.getId());
		checkCorrectVariableTableSize(enabledTask, 5);

		enabledTask.setValue("familyplanningmethod", "injectaplan");

		completeTask(enabledTask);
		assertTrue(instance.isStarted());
		assertFalse(instance.isComplete());
		checkVariable(instance, "familyplanningmethod", "injectaplan");

		//** Compled Task 1 **//



		//** ======================== Task2 **//
		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(2, currentEnabledTasks.size());

		for (Task task : currentEnabledTasks) {
			if (task.getId().equals("Maternal_Info_6")) {
				task.setValue("Pregnancy", "true");
				completeTask(task);
			} else if (task.getId().equals("Update_Register_9")) {
				task.setValue("educationlevel", "s6");
				completeTask(task);
			}
		}

		assertTrue("Net Ended Prematurel", instance.isStarted());
		assertFalse(instance.isComplete());
		checkVariable(instance, "Pregnancy", "true");
		checkVariable(instance, "educationlevel", "s6");
		//**  Completed Task 2**//



		//**========================= Task 3 **//
		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(1, currentEnabledTasks.size());
		Task Child_Info_7Task = instance.getTask("Child_Info_7");
		assertTrue("Child_Info_7 task was not found", currentEnabledTasks.contains(Child_Info_7Task));
		Child_Info_7Task.setValue("dateofbirth", "1986-Nov");
		completeTask(Child_Info_7Task);
		checkVariable(instance, "dateofbirth", "1986-Nov");
		//** Complete Task 3 **//

		//**========================= Task 4 **//
		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(1, currentEnabledTasks.size());
		Task finishTask = instance.getTask("finish");
		assertTrue("finish task was not found", currentEnabledTasks.contains(finishTask));
		completeTask(finishTask);
		//** Complete Task 4 **//

		//************ Check instance is complete ****/
		currentEnabledTasks = instance.getCurrentEnabledTasks();

		assertTrue("No Tasks Are Expected to be enabled", currentEnabledTasks.isEmpty());
		assertTrue("Net is expected to be in completed state", instance.isComplete());
		assertFalse(instance.isStarted());

		//***********Complete//

	}

	public void testPeterNetFlowsSmoothlyPregnancyTrueAndRegisterUpdatedLater() throws IOException, IllegalAccessException, InstantiationException {
		instance = Resources.getNetWithAnd1();

		instance.setValue("Name", "tonny");
		instance.setValue("village", "Kiwatule");
		instance.setValue("householdID", "HID205");
		instance.setValue("Age", "25");
		//Start the workflow
		instance.start();

		System.out.println(Cypher.generate(instance));

		System.out.println(instance);


		//** ====================== Task 1  **//
		List<Task> currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(currentEnabledTasks.size(), 1);

		Task enabledTask = currentEnabledTasks.get(0);
		assertEquals("Reproductive_info_4", enabledTask.getId());
		checkCorrectVariableTableSize(enabledTask, 5);

		enabledTask.setValue("familyplanningmethod", "injectaplan");

		completeTask(enabledTask);
		assertTrue(instance.isStarted());
		assertFalse(instance.isComplete());
		checkVariable(instance, "familyplanningmethod", "injectaplan");

		//** Compled Task 1 **//


		//** ======================== Task2 **//

		Task updateRegisterTask = null;

		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(2, currentEnabledTasks.size());

		for (Task task : currentEnabledTasks) {
			if (task.getId().equals("Maternal_Info_6")) {
				task.setValue("Pregnancy", "true");
				completeTask(task);
			} else if (task.getId().equals("Update_Register_9")) {
				task.setValue("educationlevel", "s6");
				updateRegisterTask = task;
			}
		}

		assertNotNull(updateRegisterTask);

		assertTrue("Net Ended Prematurel", instance.isStarted());
		assertFalse(instance.isComplete());
		checkVariable(instance, "Pregnancy", "true");
//		checkVariable(instance, "educationlevel", "s6");
		//**  Completed Task 2**//


		//**========================= Task 3 **//
		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(2, currentEnabledTasks.size());
		Task Child_Info_7Task = instance.getTask("Child_Info_7");
		assertTrue("Child_Info_7 task was not found", currentEnabledTasks.contains(Child_Info_7Task));
		Child_Info_7Task.setValue("dateofbirth", "1986-Nov");
		completeTask(Child_Info_7Task);
		checkVariable(instance, "dateofbirth", "1986-Nov");
		//** Complete Task 3 **//


		//**========================= Task 4 **//
		//check that finish is not activated since

		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(1, currentEnabledTasks.size());
		updateRegisterTask = instance.getTask(updateRegisterTask.getId());
		assertTrue("register info should be the only one enabled", currentEnabledTasks.contains(updateRegisterTask));
		completeTask(updateRegisterTask);


		//**========================= Task 4 **//
		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(1, currentEnabledTasks.size());
		Task finishTask = instance.getTask("finish");
		assertTrue("finish task was not found", currentEnabledTasks.contains(finishTask));
		completeTask(finishTask);
		//** Complete Task 4 **//

		//************ Check instance is complete ****/
		currentEnabledTasks = instance.getCurrentEnabledTasks();

		assertTrue("No Tasks Are Expected to be enabled", currentEnabledTasks.isEmpty());
		assertTrue("Net is expected to be in completed state", instance.isComplete());
		assertFalse(instance.isStarted());

		//***********Complete//

	}

	private void completeTask(Task enabledTask) {
		instance = completeTask(enabledTask, instance);
	}

	private Net completeTask(Task enabledTask, Net instance) {
		try {
			instance = StreamUtil.copyNet(instance);
			System.out.print("Completing task: " + enabledTask.getId());
			instance.complete(enabledTask);
			instance = StreamUtil.copyNet(instance);
			System.out.print("\n  -> Next tasks: " + instance.getCurrentEnabledTasks().stream().map(Element::getId).collect(Collectors.joining(",", "[", "]")));
		} catch (Exception ex) {
			throw new RuntimeException("Unable to copy net: " + ex, ex);
		}finally {
			System.out.println();
		}
		return instance;
	}

	public void testNetDoesNotEndPrematurely() {
		instance = Resources.getNetWithAnd1();

		System.out.println(Cypher.generate(instance));

		instance.getTask("finish").withJoinType(Junction.TYPE.OR);
		instance.setValue("Name", "tonny");
		instance.setValue("village", "Kiwatule");
		instance.setValue("householdID", "HID205");
		instance.setValue("Age", "25");
		//Start the workflow
		instance.start();
		System.out.println(instance);


		//** ====================== Task 1  **//
		List<Task> currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(currentEnabledTasks.size(), 1);

		Task enabledTask = currentEnabledTasks.get(0);
		assertEquals("Reproductive_info_4", enabledTask.getId());
		checkCorrectVariableTableSize(enabledTask, 5);

		enabledTask.setValue("familyplanningmethod", "injectaplan");

		completeTask(enabledTask);
		assertTrue(instance.isStarted());
		assertFalse(instance.isComplete());
		checkVariable(instance, "familyplanningmethod", "injectaplan");

		//** Compled Task 1 **//



		//** ======================== Task2 **//
		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(2, currentEnabledTasks.size());

		for (int i = 0; i < currentEnabledTasks.size(); i++) {
			Task task = currentEnabledTasks.get(i);
			if (task.getId().equals("Maternal_Info_6")) {
				task.setValue("Pregnancy", "true");
				completeTask(task);
			} else if (task.getId().equals("Update_Register_9")) {
				task.setValue("educationlevel", "s6");
				completeTask(task);
			}
		}

		assertTrue("Net Ended Prematurel", instance.isStarted());
		assertFalse(instance.isComplete());
		checkVariable(instance, "Pregnancy", "true");
		checkVariable(instance, "educationlevel", "s6");
		//**  Completed Task 2**//

		//***======================== child info should be enabled
		Task child_info_7 = instance.getTask("Child_Info_7");
		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(1, currentEnabledTasks.size());
		assertTrue("finish task not found in list", currentEnabledTasks.contains(child_info_7));
		child_info_7.setValue("dateofbirth", "1986-Nov");
		completeTask(child_info_7);



		//**========================= Task 3 **//
		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(1, currentEnabledTasks.size());
		Task task = instance.getTask("finish");
		assertTrue("finish task not found in list", currentEnabledTasks.contains(task));
		completeTask(task);
		assertTrue("Net Ended Prematurely", instance.isComplete());

		currentEnabledTasks = instance.getCurrentEnabledTasks();
		assertEquals(0, currentEnabledTasks.size());

		checkVariable(instance, "dateofbirth", "1986-Nov");
		//** Complete Task 3 **//

		//************ Check instance is complete ****/
		currentEnabledTasks = instance.getCurrentEnabledTasks();

		assertTrue("No Tasks Are Expected to be enabled", currentEnabledTasks.isEmpty());
		assertTrue("Net is expected to be in completed state", instance.isComplete());
		assertFalse(instance.isStarted());

		//***********Complete//

	}

	public void testNetCompletesWhenPregnancyIsFalse() throws IllegalAccessException, InstantiationException, IOException {
		Net net = Resources.getNetWithAnd1();

		net.getTask("finish").withJoinType(Junction.TYPE.OR);
		System.out.println(net);

		net.setValue("Name", "tonny");
		net.setValue("village", "Kiwatule");
		net.setValue("householdID", "HID205");
		net.setValue("Age", "25");

		net.start();

		List<Task> currentEnabledTasks = net.getCurrentEnabledTasks();
		assertEquals(1, currentEnabledTasks.size());
		completeTasks(currentEnabledTasks);

		currentEnabledTasks = net.getCurrentEnabledTasks();
		assertEquals(2, currentEnabledTasks.size());
		Task task = net.getTask("Maternal_Info_6");
		assertTrue(currentEnabledTasks.contains(task));
		task.setValue("Pregnancy", "false");
		completeTasks(currentEnabledTasks);

		currentEnabledTasks = net.getCurrentEnabledTasks();
		assertEquals(1, currentEnabledTasks.size());
		completeTasks(currentEnabledTasks);

		assertTrue("Premature end of net", net.isComplete());

	}

	public void testNetCompletesWithOneTask() {
		Net net = Resources.getNetWithOneTask();
		net.start();
		List<Task> tasks = net.getCurrentEnabledTasks();
		assertEquals("Tasks expected to be only one", 1, tasks.size());
		Task task = tasks.get(0);
		net.complete(task);

		tasks = net.getCurrentEnabledTasks();
		assertEquals("No Enabled tasks are expected", 0, tasks.size());

		assertTrue("Net is expected to be complete", net.isComplete());
	}

	public void testNetWithCustomCondition() {
		Net net = new Net();


		net.setId("SampleNet");
		Task enterdetails = new Task("EnterDetails", "enterdetails");
		net.addFlow().forFlowingIntoTask(enterdetails).
				withParameter("name", Variable.FLOW.OUTPUT).
				withParameter("sex", Variable.FLOW.OUTPUT);

		Task isfather = (Task) enterdetails
				.withSplitType(Junction.TYPE.OR)
				.addOutFlow()
				.withCondition((root, task) -> root.getVariablesTable().get("sex").getValue().equals("m"))
				.forFlowingIntoNewTask("IsFather", "isfather").
						withParameter("name", Variable.FLOW.INPUT).
						withParameter("is_father", Variable.FLOW.OUTPUT);

		Task ispregnant = (Task) enterdetails
				.withSplitType(Junction.TYPE.OR)
				.addOutFlow()
				.withEqualCondition("sex", "f")
				.forFlowingIntoNewTask("IsPregnant", "ispregnant").
						withParameter("name", Variable.FLOW.INPUT).
						withParameter("is_pregnant", Variable.FLOW.OUTPUT);


		isfather.addFlowToEnd();
		ispregnant.addFlowToEnd();

		net.start();

		System.out.println(net.toString());

		Task enabled1 = net.getCurrentEnabledTasks().get(0);

		enabled1.setValue("sex", "m");

		net.complete(enabled1);


		List<Task> currentEnabledTasks = net.getCurrentEnabledTasks();
		assertEquals(1, currentEnabledTasks.size());
		Task enabled2 = currentEnabledTasks.get(0);
		assertEquals("isfather", enabled2.getId());



	}


	public void testNetWithCustomCondition2() {
		Net net = new Net();


		net.setId("SampleNet");
		Task enterdetails = new Task("EnterDetails", "enterdetails");
		net.addFlow().forFlowingIntoTask(enterdetails).
				withParameter("name", Variable.FLOW.OUTPUT).
				withParameter("sex", Variable.FLOW.OUTPUT);

		Task isfather = (Task) enterdetails
				.withSplitType(Junction.TYPE.OR)
				.addOutFlow()
				.withCondition((root, task) -> root.getVariablesTable().get("sex").getValue().equals("m"))
				.forFlowingIntoNewTask("IsFather", "isfather").
						withParameter("name", Variable.FLOW.INPUT).
						withParameter("is_father", Variable.FLOW.OUTPUT);

		Task ispregnant = (Task) enterdetails
				.withSplitType(Junction.TYPE.OR)
				.addOutFlow()
				.withEqualCondition("sex", "f")
				.forFlowingIntoNewTask("IsPregnant", "ispregnant").
						withParameter("name", Variable.FLOW.INPUT).
						withParameter("is_pregnant", Variable.FLOW.OUTPUT);


		isfather.addFlowToEnd();
		ispregnant.addFlowToEnd();

		net.start();

		System.out.println(net.toString());

		Task enabled1 = net.getCurrentEnabledTasks().get(0);

		enabled1.setValue("sex", "f");

		net.complete(enabled1);

		List<Task> currentEnabledTasks = net.getCurrentEnabledTasks();
		assertEquals(1, currentEnabledTasks.size());
		Task enabled2 = currentEnabledTasks.get(0);
		assertEquals("ispregnant", enabled2.getId());



	}
	public void completeTasks(List<Task> tasks) {

		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			System.out.print("Completing task: " + task.getId());
			task.getRootNet().complete(task);
			System.out.println("\n  -> Next tasks: " + task.getRootNet().getCurrentEnabledTasks().stream().map(Element::getId).collect(Collectors.joining(",", "[", "]")));
		}

	}
}
