package org.openxdata.workflow.engine;

import static org.openxdata.workflow.engine.Variable.*;

/**
 *
 * @author kay
 */
public class Resources {

	public static Net getNet() {

		Net instance = new Net();
		instance.setId("SampleNet");
		Task enterdetails = new Task("EnterDetails", "enterdetails");
		instance.addFlow().forFlowingIntoTask(enterdetails).
			withParemeter("name", Variable.TYPE_OUTPUT).
			withParemeter("sex", Variable.TYPE_OUTPUT);

		Task isfather = (Task) enterdetails.addOutFlow().withEqualCondititon("sex", "m").
			forFlowingIntoNewTask("IsFather", "isfather").
			withParemeter("name", Variable.TYPE_INPUT).
			withParemeter("is_father", Variable.TYPE_OUTPUT);

		Task ispregnant = (Task) enterdetails.addOutFlow().withEqualCondititon("sex", "f").
			forFlowingIntoNewTask("IsPregnant", "ispregnant").
			withParemeter("name", Variable.TYPE_INPUT).
			withParemeter("is_pregnant", Variable.TYPE_OUTPUT);

		Task confirm = (Task) isfather.addOutFlow().forFlowingIntoNewTask("confirm", "confirm").
			withParemeter("name", Variable.TYPE_IO).
			withParemeter("sex", Variable.TYPE_IO).
			withParemeter("detail", Variable.TYPE_IO, false).
			withInputMapping("is_father", "detail").
			withOutputMapping("detail", "is_pregnant").
			withOutputMapping("detail", "is_father");

		ispregnant.addOutFlow().forFlowingIntoTask(confirm);

		return instance;

	}

	public static Net getSimpleNet() {
		Net net = new Net();
		net.setId("Net2");
		Task start = new Task("EnterName", "entername");
		net.addFlow().forFlowingIntoTask(start).
			withParemeter("name", Variable.TYPE_OUTPUT).
			withParemeter("2name", Variable.TYPE_OUTPUT);

		start.addOutFlow().forFlowingIntoNewTask("Confirm", "confirm").
			withParemeter("name", Variable.TYPE_IO).
			withParemeter("2name", Variable.TYPE_IO);

		return net;
	}

	public static Net getNetWithAnd1() {
		Net net = new Net();
		net.setId("exampleoffline");

		Task reprodInfo = (Task) net.addFlow().
			forFlowingIntoNewTask("Reproductive Info", "Reproductive_info_4").
			withParemeter("Name", TYPE_INPUT).
			withParemeter("village", TYPE_INPUT).
			withParemeter("householdID", TYPE_INPUT).
			withParemeter("Age", TYPE_INPUT).
			withParemeter("familyplanningmethod", TYPE_OUTPUT);

		Task maternalInfo = (Task) reprodInfo.addAndOutFlow().
			forFlowingIntoNewTask("Maternal Info", "Maternal_Info_6").
			withParemeter("Name", TYPE_INPUT).
			withParemeter("village", TYPE_INPUT).
			withParemeter("householdID", TYPE_INPUT).
			withParemeter("Age", TYPE_INPUT).
			withParemeter("Pregnancy", TYPE_OUTPUT);

		Task childInfo = (Task) maternalInfo.addOutFlow().
			withEqualCondititon("Pregnancy", "true").
			forFlowingIntoNewTask("Child Info", "Child_Info_7").
			withParemeter("Name", TYPE_INPUT).
			withParemeter("village", TYPE_INPUT).
			withParemeter("householdID", TYPE_INPUT).
			withParemeter("Age", TYPE_INPUT).
			withParemeter("dateofbirth", TYPE_OUTPUT);

		Task updateRegister = (Task) reprodInfo.addAndOutFlow().
			forFlowingIntoNewTask("Update Register", "Update_Register_9").
			withParemeter("Name", TYPE_INPUT).
			withParemeter("village", TYPE_INPUT).
			withParemeter("householdID", TYPE_INPUT).
			withParemeter("Age", TYPE_INPUT).
			withParemeter("educationlevel", TYPE_OUTPUT);

		Task finish = updateRegister.addOutFlow().
			forFlowingIntoNewTask("Finish", "finish").
			havingJoinType(Jucntion.TYPE_AND);

		childInfo.addOutFlow().forFlowingIntoTask(finish);
		maternalInfo.addOutFlow().forFlowingIntoTask(finish);

		return net;
	}

	public static Net getNetWithOneTask() {
		Net net = new Net();
		net.addFlow().forFlowingIntoNewTask("SomeTask", "SomeTask").
			withParemeter("name", Variable.TYPE_INPUT).
			withParemeter("sex", Variable.TYPE_OUTPUT);
		return net;
	}

	public static Net getNetWithAnd() {
		Net net = new Net();
		net.setId("IgangaDSS_0.10");

		Task newPregnancy = (Task) net.addFlow().
			forFlowingIntoNewTask("New Pregnancy", "New_Pregancy_10").
			havingSplitType(Jucntion.TYPE_XOR).
			withParemeter("VillageName", TYPE_INPUT).
			withParemeter("VillageLocation", TYPE_INPUT).
			withParemeter("VillageCode", TYPE_INPUT).
			withParemeter("MotherID_", TYPE_INPUT).
			withParemeter("RoundNumber", TYPE_INPUT).
			withParemeter("FACode", TYPE_INPUT).
			withParemeter("WomanName_", TYPE_INPUT).
			withParemeter("enumerator_u", TYPE_INPUT).
			withParemeter("newpregnancy", TYPE_OUTPUT).
			withParemeter("AnyPreviousBirth", TYPE_OUTPUT);

		Task registerPregnancy = (Task) newPregnancy.addOutFlow().
			withEqualCondititon("newpregnancy", "Yes").
			forFlowingIntoNewTask("Pregnancy Register", "Pregnancy_Registration_4").
			havingJoinType(Jucntion.TYPE_OR).
			withParemeter("VillageName", TYPE_INPUT).
			withParemeter("VillageLocation", TYPE_INPUT).
			withParemeter("VillageCode", TYPE_INPUT).
			withParemeter("MotherID_", TYPE_INPUT).
			withParemeter("RoundNumber", TYPE_INPUT).
			withParemeter("FACode", TYPE_INPUT).
			withParemeter("WomanName_", TYPE_INPUT).
			withParemeter("enumerator_u", TYPE_INPUT);

		Task pregnancyOutcome = (Task) newPregnancy.addOutFlow().
			withEqualCondititon("AnyPreviousBirth", "Yes").
			forFlowingIntoNewTask("Pregancy Birth Outcome", "Pregnancy_Birth_Outcome_5").
			havingJoinType(Jucntion.TYPE_OR).
			withParemeter("VillageName", TYPE_INPUT).
			withParemeter("VillageLocation", TYPE_INPUT).
			withParemeter("VillageCode", TYPE_INPUT).
			withParemeter("MotherID_", TYPE_INPUT).
			withParemeter("RoundNumber", TYPE_INPUT).
			withParemeter("FACode", TYPE_INPUT).
			withParemeter("WomanName_", TYPE_INPUT).
			withParemeter("WomanPregnant", TYPE_INPUT).
			withParemeter("enumerator_u", TYPE_INPUT);

		net.setValue("WomanPregnant", "No");

		Task finTask = registerPregnancy.addOutFlow().forFlowingIntoNewTask("Finish", "finish").
			havingJoinType(Jucntion.TYPE_AND);
		newPregnancy.addOutFlow().forFlowingIntoTask(finTask);
		pregnancyOutcome.addOutFlow().forFlowingIntoTask(finTask);
		return net;
	}
}
