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
                withParameter("name", FLOW.OUTPUT).
                        withParameter("sex", FLOW.OUTPUT);

		Task isfather = (Task) enterdetails.addOutFlow().withEqualCondititon("sex", "m").
			forFlowingIntoNewTask("IsFather", "isfather").
                                                   withParameter("name", FLOW.INPUT).
                                                   withParameter("is_father", FLOW.OUTPUT);

		Task ispregnant = (Task) enterdetails.addOutFlow().withEqualCondititon("sex", "f").
			forFlowingIntoNewTask("IsPregnant", "ispregnant").
                                                     withParameter("name", FLOW.INPUT).
                                                     withParameter("is_pregnant", FLOW.OUTPUT);

		Task confirm = (Task) isfather.addOutFlow().forFlowingIntoNewTask("confirm", "confirm").
                withParameter("name", FLOW.IO).
                                              withParameter("sex", FLOW.IO).
                                              withParameter("detail", FLOW.IO, false).
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
                withParameter("name", FLOW.OUTPUT).
                   withParameter("2name", FLOW.OUTPUT);

		start.addOutFlow().forFlowingIntoNewTask("Confirm", "confirm").
                withParameter("name", FLOW.IO).
                     withParameter("2name", FLOW.IO);

		return net;
	}

	public static Net getNetWithAnd1() {
		Net net = new Net();
		net.setId("exampleoffline");

		Task reprodInfo = (Task) net.addFlow().
			forFlowingIntoNewTask("Reproductive Info", "Reproductive_info_4").
                                            withParameter("Name", FLOW.INPUT).
                                            withParameter("village", FLOW.INPUT).
                                            withParameter("householdID", FLOW.INPUT).
                                            withParameter("Age", FLOW.INPUT).
                                            withParameter("familyplanningmethod", FLOW.OUTPUT);

		Task maternalInfo = (Task) reprodInfo.addAndOutFlow().
			forFlowingIntoNewTask("Maternal Info", "Maternal_Info_6").
                                                     withParameter("Name", FLOW.INPUT).
                                                     withParameter("village", FLOW.INPUT).
                                                     withParameter("householdID", FLOW.INPUT).
                                                     withParameter("Age", FLOW.INPUT).
                                                     withParameter("Pregnancy", FLOW.OUTPUT);

		Task childInfo = (Task) maternalInfo.addOutFlow().
			withEqualCondititon("Pregnancy", "true").
			forFlowingIntoNewTask("Child Info", "Child_Info_7").
                                                    withParameter("Name", FLOW.INPUT).
                                                    withParameter("village", FLOW.INPUT).
                                                    withParameter("householdID", FLOW.INPUT).
                                                    withParameter("Age", FLOW.INPUT).
                                                    withParameter("dateofbirth", FLOW.OUTPUT);

		Task updateRegister = (Task) reprodInfo.addAndOutFlow().
			forFlowingIntoNewTask("Update Register", "Update_Register_9").
                                                       withParameter("Name", FLOW.INPUT).
                                                       withParameter("village", FLOW.INPUT).
                                                       withParameter("householdID", FLOW.INPUT).
                                                       withParameter("Age", FLOW.INPUT).
                                                       withParameter("educationlevel", FLOW.OUTPUT);

		Task finish = updateRegister.addOutFlow().
			forFlowingIntoNewTask("Finish", "finish").
			havingJoinType(Jucntion.TYPE.AND);

		childInfo.addOutFlow().forFlowingIntoTask(finish);
		maternalInfo.addOutFlow().forFlowingIntoTask(finish);

		return net;
	}

	public static Net getNetWithOneTask() {
		Net net = new Net();
		net.addFlow().forFlowingIntoNewTask("SomeTask", "SomeTask").
                withParameter("name", FLOW.INPUT).
                   withParameter("sex", FLOW.OUTPUT);
		return net;
	}

	public static Net getNetWithAnd() {
		Net net = new Net();
		net.setId("IgangaDSS_0.10");

		Task newPregnancy = (Task) net.addFlow().
			forFlowingIntoNewTask("New Pregnancy", "New_Pregancy_10").
			havingSplitType(Jucntion.TYPE.XOR).
                                              withParameter("VillageName", FLOW.INPUT).
                                              withParameter("VillageLocation", FLOW.INPUT).
                                              withParameter("VillageCode", FLOW.INPUT).
                                              withParameter("MotherID_", FLOW.INPUT).
                                              withParameter("RoundNumber", FLOW.INPUT).
                                              withParameter("FACode", FLOW.INPUT).
                                              withParameter("WomanName_", FLOW.INPUT).
                                              withParameter("enumerator_u", FLOW.INPUT).
                                              withParameter("newpregnancy", FLOW.OUTPUT).
                                              withParameter("AnyPreviousBirth", FLOW.OUTPUT);

		Task registerPregnancy = (Task) newPregnancy.addOutFlow().
			withEqualCondititon("newpregnancy", "Yes").
			forFlowingIntoNewTask("Pregnancy Register", "Pregnancy_Registration_4").
			havingJoinType(Jucntion.TYPE.OR).
                                                            withParameter("VillageName", FLOW.INPUT).
                                                            withParameter("VillageLocation", FLOW.INPUT).
                                                            withParameter("VillageCode", FLOW.INPUT).
                                                            withParameter("MotherID_", FLOW.INPUT).
                                                            withParameter("RoundNumber", FLOW.INPUT).
                                                            withParameter("FACode", FLOW.INPUT).
                                                            withParameter("WomanName_", FLOW.INPUT).
                                                            withParameter("enumerator_u", FLOW.INPUT);

		Task pregnancyOutcome = (Task) newPregnancy.addOutFlow().
			withEqualCondititon("AnyPreviousBirth", "Yes").
			forFlowingIntoNewTask("Pregancy Birth Outcome", "Pregnancy_Birth_Outcome_5").
			havingJoinType(Jucntion.TYPE.OR).
                                                           withParameter("VillageName", FLOW.INPUT).
                                                           withParameter("VillageLocation", FLOW.INPUT).
                                                           withParameter("VillageCode", FLOW.INPUT).
                                                           withParameter("MotherID_", FLOW.INPUT).
                                                           withParameter("RoundNumber", FLOW.INPUT).
                                                           withParameter("FACode", FLOW.INPUT).
                                                           withParameter("WomanName_", FLOW.INPUT).
                                                           withParameter("WomanPregnant", FLOW.INPUT).
                                                           withParameter("enumerator_u", FLOW.INPUT);

		net.setValue("WomanPregnant", "No");

		Task finTask = registerPregnancy.addOutFlow().forFlowingIntoNewTask("Finish", "finish").
			havingJoinType(Jucntion.TYPE.AND);
		newPregnancy.addOutFlow().forFlowingIntoTask(finTask);
		pregnancyOutcome.addOutFlow().forFlowingIntoTask(finTask);
		return net;
	}
}
