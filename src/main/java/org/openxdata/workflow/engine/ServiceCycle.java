package org.openxdata.workflow.engine;

/**
 * Created by kay on 12/7/2016.
 */
public class ServiceCycle {

	static Net instance() {
		Net net = new Net();

		Task smeOne = (Task) net.addFlow().forFlowingIntoNewTask("First SMS", "first_sms")
				.withParemeter("cowname", Variable.TYPE_INPUT).withParemeter("cowid", Variable.TYPE_INPUT);

		Task sms2 = (Task) smeOne.addOutFlow().forFlowingIntoNewTask("Second SMS", "secon_sms")
				.withParemeter("cowname", Variable.TYPE_INPUT).withParemeter("cowid", Variable.TYPE_INPUT);

		Task sms3 = (Task) sms2.addOutFlow().forFlowingIntoNewTask("Third SMS", "third_sms")
				.withParemeter("cowname", Variable.TYPE_INPUT).withParemeter("cowid", Variable.TYPE_INPUT);

		Task confirmPregnancySMS = (Task) sms3.addOutFlow()
				.forFlowingIntoNewTask("Confirm Pregnancy SMS", "confirm_preg_sms")
				.withParemeter("cowname", Variable.TYPE_INPUT).withParemeter("cowid", Variable.TYPE_INPUT)
				.withParemeter("is_pregnant", Variable.TYPE_OUTPUT);

		Task dryingSMS = (Task) confirmPregnancySMS.addOutFlow().withEqualCondititon("is_pregnant", "yes")
				.forFlowingIntoNewTask("DRYING SMS", "drying_sms").withParemeter("cowname", Variable.TYPE_INPUT)
				.withParemeter("cowid", Variable.TYPE_INPUT);

		dryingSMS.addOutFlow().withEqualCondititon("is_pregnant", "no").forFlowingIntoTask(smeOne);

		Task steamingSms = (Task) dryingSMS.addOutFlow().forFlowingIntoNewTask("Steaming", "steaming");

		return net;
	}
}
