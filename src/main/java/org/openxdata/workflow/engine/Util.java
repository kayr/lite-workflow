package org.openxdata.workflow.engine;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.openxdata.db.util.Serializer;

/**
 *
 * @author kay
 */
public class Util {

	public static void writeToStream(DataOutputStream out, Hashtable<String, ? extends IdPersistent> table) throws IOException {

//		
//				if(persistentVector != null){
//			dos.writeByte(persistentVector.size());
//			for(int i=0; i<persistentVector.size(); i++ ){
//				((Persistent)persistentVector.elementAt(i)).write(dos);
//			}
//		}
//		else
//			dos.writeByte(0);

		if (table != null) {
			Enumeration<? extends IdPersistent> values = table.elements();
			out.writeShort(table.size());
			while (values.hasMoreElements()) {
				IdPersistent idPersistent = values.nextElement();
				idPersistent.write(out);
			}
		} else {
			out.writeShort(0);
		}
	}

	public static <T extends IdPersistent> Hashtable<String, T> readFromStrem(DataInputStream din, Class clazz) throws IOException, InstantiationException, IllegalAccessException {
		Hashtable<String, T> table = new Hashtable<String, T>();
		short len = din.readShort();
		//		byte len = dis.readByte();
		//
		//		/*if(len == 0)
		//			return null;*/
		//
		//		Vector persistentVector = new Vector(len);
		//
		//		for(byte i=0; i<len; i++ ){
		//			Object obj = (Persistent)cls.newInstance();
		//			((Persistent)obj).read(dis);
		//			persistentVector.addElement(obj);
		//		}
		//
		//		return persistentVector;


		for (short i = 0; i < len; i++) {
			T obj = (T) clazz.newInstance();
			obj.read(din);
			table.put(obj.getId(), obj);
		}

		return table;

	}

	public static <T> Vector<T> toVector(Hashtable<? extends Object, T> table) {
		Enumeration<T> elements = table.elements();
		Vector<T> vcs = new Vector<T>(0);
		while (elements.hasMoreElements()) {
			T t = elements.nextElement();
			vcs.addElement(t);
		}
		return vcs;
	}

	public static Net copyNet(Net net) throws IllegalAccessException, InstantiationException, IOException {
		System.out.println("Copying net" + net.getId());
		DataInputStream din = getInputStream(net);

		Net copy = new Net();
		copy.read(din);
		return copy;
	}

	public static DataInputStream getInputStream(Element elem) throws IOException {
		byte[] bytes = Serializer.serialize(elem);
		DataInputStream din = new DataInputStream(new ByteArrayInputStream(bytes));
		return din;
	}
}
