package org.openxdata.workflow.engine;

/**
 * Created by kay on 12/7/2016.
 */
public class ServiceCycle {

	static Net instance() {
		Net net = new Net();

		Task smeOne = (Task) net.addFlow().forFlowingIntoNewTask("First SMS", "first_sms")
								.withParameter("cowname", Variable.TYPE.INPUT).withParameter("cowid", Variable.TYPE.INPUT);

		Task sms2 = (Task) smeOne.addOutFlow().forFlowingIntoNewTask("Second SMS", "secon_sms")
								 .withParameter("cowname", Variable.TYPE.INPUT).withParameter("cowid", Variable.TYPE.INPUT);

		Task sms3 = (Task) sms2.addOutFlow().forFlowingIntoNewTask("Third SMS", "third_sms")
							   .withParameter("cowname", Variable.TYPE.INPUT).withParameter("cowid", Variable.TYPE.INPUT);

		Task confirmPregnancySMS = (Task) sms3.addOutFlow()
											  .forFlowingIntoNewTask("Confirm Pregnancy SMS", "confirm_preg_sms")
											  .withParameter("cowname", Variable.TYPE.INPUT).withParameter("cowid", Variable.TYPE.INPUT)
											  .withParameter("is_pregnant", Variable.TYPE.OUTPUT);

		Task dryingSMS = (Task) confirmPregnancySMS.addOutFlow().withEqualCondititon("is_pregnant", "yes")
				.forFlowingIntoNewTask("DRYING SMS", "drying_sms").withParameter("cowname", Variable.TYPE.INPUT)
				.withParameter("cowid", Variable.TYPE.INPUT);

		dryingSMS.addOutFlow().withEqualCondititon("is_pregnant", "no").forFlowingIntoTask(smeOne);

		Task steamingSms = (Task) dryingSMS.addOutFlow().forFlowingIntoNewTask("Steaming", "steaming");

		return net;
	}
}
