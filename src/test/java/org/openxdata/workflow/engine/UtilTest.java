package org.openxdata.workflow.engine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import junit.framework.TestCase;

/**
 *
 * @author kay
 */
public class UtilTest extends TestCase {

	Hashtable<String, IdPersistent> table = new Hashtable<String, IdPersistent>(0);

	@Override
	protected void setUp() throws Exception {
		table.put("1", new TestClass("1"));
		table.put("2", new TestClass("2"));
		table.put("3", new TestClass("3"));
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test of readFromStrem method, of class Util.
	 */
	public void testReadFromStrem() throws Exception {
		DataInputStream din = getInputStream();
		Hashtable result = Util.readFromStrem(din, TestClass.class);
		assertEquals(3, result.size());

	}

	public void testReadFromStremEmptyTable() throws Exception {
		table.clear();
		DataInputStream din = getInputStream();
		Hashtable result = Util.readFromStrem(din, TestClass.class);
		assertEquals(0, result.size());

	}

	public void testReadFromStremNullTable() throws Exception {
		table = null;
		DataInputStream din = getInputStream();
		Hashtable result = Util.readFromStrem(din, TestClass.class);
		assertEquals(0, result.size());

	}

	private DataInputStream getInputStream() throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bout);
		Util.writeToStream(dout, table);
		DataInputStream din = new DataInputStream(new ByteArrayInputStream(bout.toByteArray()));
		return din;
	}

	public static class TestClass implements IdPersistent {

		private String id;

		public TestClass() {
		}

		public TestClass(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}

		public void write(DataOutputStream dos) throws IOException {
			dos.writeUTF(id);
		}

		public void read(DataInputStream dis) throws IOException, InstantiationException, IllegalAccessException {
			id = dis.readUTF();
		}
	}
}
