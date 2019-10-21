package org.openxdata.workflow.engine;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kay
 */
public class Util {

	public static void writeToStream(DataOutputStream out, Map<String, ? extends IdPersistent> table) throws IOException {

		if (table != null) {
			out.writeShort(table.size());
			for (IdPersistent idPersistent : table.values()) {
				idPersistent.write(out);
			}
		} else {
			out.writeShort(0);
		}
	}

	public static <T extends IdPersistent> Map<String, T> readFromStrem(DataInputStream din, Class clazz) throws IOException, InstantiationException, IllegalAccessException {
		Map<String, T> table = new HashMap<>();
		short len = din.readShort();

		for (short i = 0; i < len; i++) {
			T obj = (T) clazz.newInstance();
			obj.read(din);
			table.put(obj.getId(), obj);
		}

		return table;

	}

	public static Net copyNet(Net net) throws IllegalAccessException, InstantiationException, IOException {
		System.out.println("Copying net" + net.getId());
		DataInputStream din = getInputStream(net);

		Net copy = new Net();
		copy.read(din);
		return copy;
	}

	public static DataInputStream getInputStream(Element elem) throws IOException {
		byte[] bytes = org.openxdata.workflow.engine.persistent.Serializer.serialize(elem);
		return new DataInputStream(new ByteArrayInputStream(bytes));
	}
}
