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
                withParameter("name", TYPE.OUTPUT).
                        withParameter("sex", TYPE.OUTPUT);

		Task isfather = (Task) enterdetails.addOutFlow().withEqualCondititon("sex", "m").
			forFlowingIntoNewTask("IsFather", "isfather").
                                                   withParameter("name", TYPE.INPUT).
                                                   withParameter("is_father", TYPE.OUTPUT);

		Task ispregnant = (Task) enterdetails.addOutFlow().withEqualCondititon("sex", "f").
			forFlowingIntoNewTask("IsPregnant", "ispregnant").
                                                     withParameter("name", TYPE.INPUT).
                                                     withParameter("is_pregnant", TYPE.OUTPUT);

		Task confirm = (Task) isfather.addOutFlow().forFlowingIntoNewTask("confirm", "confirm").
                withParameter("name", TYPE.IO).
                                              withParameter("sex", TYPE.IO).
                                              withParameter("detail", TYPE.IO, false).
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
                withParameter("name", TYPE.OUTPUT).
                   withParameter("2name", TYPE.OUTPUT);

		start.addOutFlow().forFlowingIntoNewTask("Confirm", "confirm").
                withParameter("name", TYPE.IO).
                     withParameter("2name", TYPE.IO);

		return net;
	}

	public static Net getNetWithAnd1() {
		Net net = new Net();
		net.setId("exampleoffline");

		Task reprodInfo = (Task) net.addFlow().
			forFlowingIntoNewTask("Reproductive Info", "Reproductive_info_4").
                                            withParameter("Name", TYPE.INPUT).
                                            withParameter("village", TYPE.INPUT).
                                            withParameter("householdID", TYPE.INPUT).
                                            withParameter("Age", TYPE.INPUT).
                                            withParameter("familyplanningmethod", TYPE.OUTPUT);

		Task maternalInfo = (Task) reprodInfo.addAndOutFlow().
			forFlowingIntoNewTask("Maternal Info", "Maternal_Info_6").
                                                     withParameter("Name", TYPE.INPUT).
                                                     withParameter("village", TYPE.INPUT).
                                                     withParameter("householdID", TYPE.INPUT).
                                                     withParameter("Age", TYPE.INPUT).
                                                     withParameter("Pregnancy", TYPE.OUTPUT);

		Task childInfo = (Task) maternalInfo.addOutFlow().
			withEqualCondititon("Pregnancy", "true").
			forFlowingIntoNewTask("Child Info", "Child_Info_7").
                                                    withParameter("Name", TYPE.INPUT).
                                                    withParameter("village", TYPE.INPUT).
                                                    withParameter("householdID", TYPE.INPUT).
                                                    withParameter("Age", TYPE.INPUT).
                                                    withParameter("dateofbirth", TYPE.OUTPUT);

		Task updateRegister = (Task) reprodInfo.addAndOutFlow().
			forFlowingIntoNewTask("Update Register", "Update_Register_9").
                                                       withParameter("Name", TYPE.INPUT).
                                                       withParameter("village", TYPE.INPUT).
                                                       withParameter("householdID", TYPE.INPUT).
                                                       withParameter("Age", TYPE.INPUT).
                                                       withParameter("educationlevel", TYPE.OUTPUT);

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
                withParameter("name", TYPE.INPUT).
                   withParameter("sex", TYPE.OUTPUT);
		return net;
	}

	public static Net getNetWithAnd() {
		Net net = new Net();
		net.setId("IgangaDSS_0.10");

		Task newPregnancy = (Task) net.addFlow().
			forFlowingIntoNewTask("New Pregnancy", "New_Pregancy_10").
			havingSplitType(Jucntion.TYPE.XOR).
                                              withParameter("VillageName", TYPE.INPUT).
                                              withParameter("VillageLocation", TYPE.INPUT).
                                              withParameter("VillageCode", TYPE.INPUT).
                                              withParameter("MotherID_", TYPE.INPUT).
                                              withParameter("RoundNumber", TYPE.INPUT).
                                              withParameter("FACode", TYPE.INPUT).
                                              withParameter("WomanName_", TYPE.INPUT).
                                              withParameter("enumerator_u", TYPE.INPUT).
                                              withParameter("newpregnancy", TYPE.OUTPUT).
                                              withParameter("AnyPreviousBirth", TYPE.OUTPUT);

		Task registerPregnancy = (Task) newPregnancy.addOutFlow().
			withEqualCondititon("newpregnancy", "Yes").
			forFlowingIntoNewTask("Pregnancy Register", "Pregnancy_Registration_4").
			havingJoinType(Jucntion.TYPE.OR).
                                                            withParameter("VillageName", TYPE.INPUT).
                                                            withParameter("VillageLocation", TYPE.INPUT).
                                                            withParameter("VillageCode", TYPE.INPUT).
                                                            withParameter("MotherID_", TYPE.INPUT).
                                                            withParameter("RoundNumber", TYPE.INPUT).
                                                            withParameter("FACode", TYPE.INPUT).
                                                            withParameter("WomanName_", TYPE.INPUT).
                                                            withParameter("enumerator_u", TYPE.INPUT);

		Task pregnancyOutcome = (Task) newPregnancy.addOutFlow().
			withEqualCondititon("AnyPreviousBirth", "Yes").
			forFlowingIntoNewTask("Pregancy Birth Outcome", "Pregnancy_Birth_Outcome_5").
			havingJoinType(Jucntion.TYPE.OR).
                                                           withParameter("VillageName", TYPE.INPUT).
                                                           withParameter("VillageLocation", TYPE.INPUT).
                                                           withParameter("VillageCode", TYPE.INPUT).
                                                           withParameter("MotherID_", TYPE.INPUT).
                                                           withParameter("RoundNumber", TYPE.INPUT).
                                                           withParameter("FACode", TYPE.INPUT).
                                                           withParameter("WomanName_", TYPE.INPUT).
                                                           withParameter("WomanPregnant", TYPE.INPUT).
                                                           withParameter("enumerator_u", TYPE.INPUT);

		net.setValue("WomanPregnant", "No");

		Task finTask = registerPregnancy.addOutFlow().forFlowingIntoNewTask("Finish", "finish").
			havingJoinType(Jucntion.TYPE.AND);
		newPregnancy.addOutFlow().forFlowingIntoTask(finTask);
		pregnancyOutcome.addOutFlow().forFlowingIntoTask(finTask);
		return net;
	}
}
