package org.openxdata.workflow.engine;

/**
 * Created by kay on 12/7/2016.
 */
public class ServiceCycle {

	static Net instance() {
		Net net = new Net();

		Task smeOne = (Task) net.addFlow().forFlowingIntoNewTask("First SMS", "first_sms")
								.withParameter("cowname", Variable.FLOW.INPUT).withParameter("cowid", Variable.FLOW.INPUT);

		Task sms2 = (Task) smeOne.addOutFlow().forFlowingIntoNewTask("Second SMS", "secon_sms")
								 .withParameter("cowname", Variable.FLOW.INPUT).withParameter("cowid", Variable.FLOW.INPUT);

		Task sms3 = (Task) sms2.addOutFlow().forFlowingIntoNewTask("Third SMS", "third_sms")
							   .withParameter("cowname", Variable.FLOW.INPUT).withParameter("cowid", Variable.FLOW.INPUT);

		Task confirmPregnancySMS = (Task) sms3.addOutFlow()
											  .forFlowingIntoNewTask("Confirm Pregnancy SMS", "confirm_preg_sms")
											  .withParameter("cowname", Variable.FLOW.INPUT).withParameter("cowid", Variable.FLOW.INPUT)
											  .withParameter("is_pregnant", Variable.FLOW.OUTPUT);

		Task dryingSMS = (Task) confirmPregnancySMS.addOutFlow().withEqualCondition("is_pregnant", "yes")
				.forFlowingIntoNewTask("DRYING SMS", "drying_sms").withParameter("cowname", Variable.FLOW.INPUT)
				.withParameter("cowid", Variable.FLOW.INPUT);

		dryingSMS.addOutFlow().withEqualCondition("is_pregnant", "no").forFlowingIntoTask(smeOne);

		Task steamingSms = dryingSMS.addOutFlow().forFlowingIntoNewTask("Steaming", "steaming");

		return net;
	}
}
