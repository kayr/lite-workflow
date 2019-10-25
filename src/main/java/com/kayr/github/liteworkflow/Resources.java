package com.kayr.github.liteworkflow;

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
                withParameter("name", Variable.FLOW.OUTPUT).
                        withParameter("sex", Variable.FLOW.OUTPUT);

		Task isfather = (Task) enterdetails.withSplitType(Junction.TYPE.OR).addOutFlow().withEqualCondition("sex", "m").
			forFlowingIntoNewTask("IsFather", "isfather").
                                                   withParameter("name", Variable.FLOW.INPUT).
                                                   withParameter("is_father", Variable.FLOW.OUTPUT);

		Task ispregnant = (Task) enterdetails.withSplitType(Junction.TYPE.OR).addOutFlow().withEqualCondition("sex", "f").
			forFlowingIntoNewTask("IsPregnant", "ispregnant").
                                                     withParameter("name", Variable.FLOW.INPUT).
                                                     withParameter("is_pregnant", Variable.FLOW.OUTPUT);

		Task confirm = (Task) isfather.addOutFlow().forFlowingIntoNewTask("confirm", "confirm").
                withParameter("name", Variable.FLOW.IO).
                                              withParameter("sex", Variable.FLOW.IO).
                                              withParameter("detail", Variable.FLOW.IO, false).
			withInputMapping("is_father", "detail").
			withOutputMapping("detail", "is_pregnant").
			withOutputMapping("detail", "is_father");

		ispregnant.addOutFlow().forFlowingIntoTask(confirm);

		confirm.addFlowToEnd();

		return instance;

	}

	public static Net getSimpleNet() {
		Net net = new Net();
		net.setId("Net2");
		Task start = new Task("EnterName", "entername");
		net.addFlow().forFlowingIntoTask(start).
                withParameter("name", Variable.FLOW.OUTPUT).
                   withParameter("2name", Variable.FLOW.OUTPUT);

		start.addOutFlow().forFlowingIntoNewTask("Confirm", "confirm").
                withParameter("name", Variable.FLOW.IO).
                     withParameter("2name", Variable.FLOW.IO);

		return net;
	}

	public static Net getNetWithAnd1() {
		Net net = new Net();
		net.setId("exampleoffline");

		Task reprodInfo = (Task) net.addFlow().
			forFlowingIntoNewTask("Reproductive Info", "Reproductive_info_4").
                                            withParameter("Name", Variable.FLOW.INPUT).
                                            withParameter("village", Variable.FLOW.INPUT).
                                            withParameter("householdID", Variable.FLOW.INPUT).
                                            withParameter("Age", Variable.FLOW.INPUT).
                                            withParameter("familyplanningmethod", Variable.FLOW.OUTPUT);

		Task maternalInfo = (Task) reprodInfo.addAndOutFlow().
			forFlowingIntoNewTask("Maternal Info", "Maternal_Info_6").
                                                     withParameter("Name", Variable.FLOW.INPUT).
                                                     withParameter("village", Variable.FLOW.INPUT).
                                                     withParameter("householdID", Variable.FLOW.INPUT).
                                                     withParameter("Age", Variable.FLOW.INPUT).
                                                     withParameter("Pregnancy", Variable.FLOW.OUTPUT);

		Task childInfo = (Task) maternalInfo.withSplitType(Junction.TYPE.OR).addOutFlow().
				withEqualCondition("Pregnancy", "true").
			forFlowingIntoNewTask("Child Info", "Child_Info_7").
                                                    withParameter("Name", Variable.FLOW.INPUT).
                                                    withParameter("village", Variable.FLOW.INPUT).
                                                    withParameter("householdID", Variable.FLOW.INPUT).
                                                    withParameter("Age", Variable.FLOW.INPUT).
                                                    withParameter("dateofbirth", Variable.FLOW.OUTPUT);

		Task updateRegister = (Task) reprodInfo.addAndOutFlow().
			forFlowingIntoNewTask("Update Register", "Update_Register_9").
                                                       withParameter("Name", Variable.FLOW.INPUT).
                                                       withParameter("village", Variable.FLOW.INPUT).
                                                       withParameter("householdID", Variable.FLOW.INPUT).
                                                       withParameter("Age", Variable.FLOW.INPUT).
                                                       withParameter("educationlevel", Variable.FLOW.OUTPUT);

		Task finish = updateRegister.addOutFlow().
			forFlowingIntoNewTask("Finish", "finish").
											withJoinType(Junction.TYPE.AND);

		finish.addFlowToEnd();

		childInfo.addOutFlow().forFlowingIntoTask(finish);
		maternalInfo.addOutFlow().forFlowingIntoTask(finish);

		return net;
	}

	public static Net getNetWithOneTask() {
		Net net = new Net();
		net.addFlow().forFlowingIntoNewTask("SomeTask", "SomeTask").
                withParameter("name", Variable.FLOW.INPUT).
                   withParameter("sex", Variable.FLOW.OUTPUT);
		return net;
	}

	public static Net getNetWithAnd() {
		Net net = new Net();
		net.setId("IgangaDSS_0.10");

		Task newPregnancy = (Task) net.addFlow().
			forFlowingIntoNewTask("New Pregnancy", "New_Pregancy_10").
											  withSplitType(Junction.TYPE.XOR).
                                              withParameter("VillageName", Variable.FLOW.INPUT).
                                              withParameter("VillageLocation", Variable.FLOW.INPUT).
                                              withParameter("VillageCode", Variable.FLOW.INPUT).
                                              withParameter("MotherID_", Variable.FLOW.INPUT).
                                              withParameter("RoundNumber", Variable.FLOW.INPUT).
                                              withParameter("FACode", Variable.FLOW.INPUT).
                                              withParameter("WomanName_", Variable.FLOW.INPUT).
                                              withParameter("enumerator_u", Variable.FLOW.INPUT).
                                              withParameter("newpregnancy", Variable.FLOW.OUTPUT).
                                              withParameter("AnyPreviousBirth", Variable.FLOW.OUTPUT);

		Task registerPregnancy = (Task) newPregnancy.addOutFlow().
				withEqualCondition("newpregnancy", "Yes").
			forFlowingIntoNewTask("Pregnancy Register", "Pregnancy_Registration_4").
															withJoinType(Junction.TYPE.OR).
                                                            withParameter("VillageName", Variable.FLOW.INPUT).
                                                            withParameter("VillageLocation", Variable.FLOW.INPUT).
                                                            withParameter("VillageCode", Variable.FLOW.INPUT).
                                                            withParameter("MotherID_", Variable.FLOW.INPUT).
                                                            withParameter("RoundNumber", Variable.FLOW.INPUT).
                                                            withParameter("FACode", Variable.FLOW.INPUT).
                                                            withParameter("WomanName_", Variable.FLOW.INPUT).
                                                            withParameter("enumerator_u", Variable.FLOW.INPUT);

		Task pregnancyOutcome = (Task) newPregnancy.addOutFlow().
				withEqualCondition("AnyPreviousBirth", "Yes").
			forFlowingIntoNewTask("Pregancy Birth Outcome", "Pregnancy_Birth_Outcome_5").
														   withJoinType(Junction.TYPE.OR).
                                                           withParameter("VillageName", Variable.FLOW.INPUT).
                                                           withParameter("VillageLocation", Variable.FLOW.INPUT).
                                                           withParameter("VillageCode", Variable.FLOW.INPUT).
                                                           withParameter("MotherID_", Variable.FLOW.INPUT).
                                                           withParameter("RoundNumber", Variable.FLOW.INPUT).
                                                           withParameter("FACode", Variable.FLOW.INPUT).
                                                           withParameter("WomanName_", Variable.FLOW.INPUT).
                                                           withParameter("WomanPregnant", Variable.FLOW.INPUT).
                                                           withParameter("enumerator_u", Variable.FLOW.INPUT);

		net.setValue("WomanPregnant", "No");

		Task finTask = registerPregnancy.addOutFlow().forFlowingIntoNewTask("Finish", "finish").
				withJoinType(Junction.TYPE.AND);
		newPregnancy.addOutFlow().forFlowingIntoTask(finTask);
		pregnancyOutcome.addOutFlow().forFlowingIntoTask(finTask);
		return net;
	}
}
