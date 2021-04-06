package com.kayr.github.liteworkflow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import junit.framework.TestCase;

/**
 *
 * @author kay
 */
public class FlowTest extends TestCase {

	Flow instance;

	public FlowTest(String testName) {
		super(testName);
	}

	@Override
	protected void setUp() throws Exception {
		instance = new Flow("nextTaskName", "prevTaskName", null);
	}

	/**
	 * Test of write method, of class Flow.
	 */
	public void testWriteAndRead() throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bout);
		instance.write(dos);

		Flow in = new Flow();
		in.read(new DataInputStream(new ByteArrayInputStream(bout.toByteArray())));

		assertEquals(in.getNextTaskName(),instance.getNextTaskName());
		assertEquals(in.getPreviousTaskName(),instance.getPreviousTaskName());

	}

	public void testWriteAndReadWitCondn() throws Exception {
		SimpleEqualCondition cond = new SimpleEqualCondition("varid", "expValue", "prevTaskName");
		instance.setCondition(cond);

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bout);
		instance.write(dos);

		Flow in = new Flow();
		in.read(new DataInputStream(new ByteArrayInputStream(bout.toByteArray())));


	}
}
