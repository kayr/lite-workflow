package com.kayr.github.liteworkflow;

import com.kayr.github.liteworkflow.persistent.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kay
 */
public class StreamUtil {

	private StreamUtil() {
	}

	public static void writeMap(DataOutputStream out, Map<String, ? extends IdPersistent> table) throws IOException {

		if (table != null) {
			out.writeShort(table.size());
			for (IdPersistent idPersistent : table.values()) {
				idPersistent.write(out);
			}
		} else {
			out.writeShort(0);
		}
	}

	public static <T extends IdPersistent> Map<String, T> readMap(DataInputStream din, Class<T> clazz) throws IOException, InstantiationException, IllegalAccessException {
		Map<String, T> table = new HashMap<>();
		short len = din.readShort();

		for (short i = 0; i < len; i++) {
			T obj = clazz.newInstance();
			obj.read(din);
			table.put(obj.getId(), obj);
		}

		return table;

	}

	public static Net copyNet(Net net) throws IllegalAccessException, InstantiationException, IOException {
		final byte[] serialize = Serializer.serialize(net);
		return Serializer.deserialize(serialize,Net.class);
	}

}
