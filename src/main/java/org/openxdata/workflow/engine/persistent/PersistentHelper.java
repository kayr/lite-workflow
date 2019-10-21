package org.openxdata.workflow.engine.persistent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * Helper class to write and read collection to and from streams. This class also
 * writes the built in types taking care of nulls if any.
 * 
 * @author Daniel Kayiwa
 *
 */
public class PersistentHelper {
	
	/**
	 * Writes a string to the stream.
	 * 
	 * @param dos - the stream for writing.
	 * @param data - the string to write.
	 * @throws IOException - thrown when a problem occurs during the writing to stream.
	 */
	public static void writeUTF(DataOutputStream dos,String data) throws IOException{
		if(data != null){
			dos.writeBoolean(true);
			dos.writeUTF(data);
		}
		else
			dos.writeBoolean(false);
	}
	
	/**
	 * Writes an Integer to the stream.
	 * 
	 * @param dos - the stream for writing.
	 * @param data - the Integer to write.
	 * @throws IOException - thrown when a problem occurs during the writing to stream.
	 */
	public static void writeInteger(DataOutputStream dos,Integer data) throws IOException{
		if(data != null){
			dos.writeBoolean(true);
			dos.writeInt(data);
		}
		else
			dos.writeBoolean(false);
	} 
	
	/**
	 * Writes a Date to a stream.
	 * 
	 * @param dos - the stream to write to.
	 * @param data - the Date to write.
	 * @throws IOException - thrown when a problem occurs during the writing to stream.
	 */
	public static void writeDate(DataOutputStream dos,Date data) throws IOException{
		if(data != null){
			dos.writeBoolean(true);
			dos.writeLong(data.getTime());
		}
		else
			dos.writeBoolean(false);
	} 
	
	/**
	 * Writes a boolean to a stream.
	 * 
	 * @param dos - the stream to write to.
	 * @param data - the boolean to write.
	 * @throws IOException - thrown when a problem occurs during the writing to stream.
	 */
	public static void writeBoolean(DataOutputStream dos,Boolean data) throws IOException{
		if(data != null){
			dos.writeBoolean(true);
			dos.writeBoolean(data);
		}
		else
			dos.writeBoolean(false);
	} 
	
	/**
	 * Reads a string from a stream.
	 * 
	 * @param dis - the stream to read from.
	 * @return - the read string or null if none.
	 * @throws IOException - thrown when a problem occurs during the reading from stream.
	 */
	public static String readUTF(DataInputStream dis) throws IOException {
		if(dis.readBoolean())
			return dis.readUTF();
		return null;
	}
	
	/**
	 * Reads an Integer from a stream.
	 * 
	 * @param dis - the stream to read from.
	 * @return - the read Integer or null of none.
	 * @throws IOException - thrown when a problem occurs during the reading from stream.
	 */
	public static Integer readInteger(DataInputStream dis) throws IOException {
		if(dis.readBoolean())
			return dis.readInt();
		return null;
	}
	
	/**
	 * Reads a Date from a stream.
	 * 
	 * @param dis - the stream to read from.
	 * @return - the read Date or null if none.
	 * @throws IOException - thrown when a problem occurs during the reading from stream.
	 */
	public static Date readDate(DataInputStream dis) throws IOException {
		if(dis.readBoolean())
			return new Date(dis.readLong());
		return null;
	}
	
	/**
	 * Reads a boolean from a stream.
	 * 
	 * @param dis - the stream to read from.
	 * @return - the read boolean or null if none.
	 * @throws IOException - thrown when a problem occurs during the reading from stream.
	 */
	public static Boolean readBoolean(DataInputStream dis) throws IOException {
		if(dis.readBoolean())
			return dis.readBoolean();
		return null;
	}
	
	/**
	 * Writes a small vector (byte size) of Persistent objects to a stream.
	 *
	 * @param persistentList - the vector of persistent objects.
	 * @param dos - the stream to write to.
	 * @throws IOException - thrown when a problem occurs during the writing to stream.
	 */
	public static void write(List persistentList, DataOutputStream dos) throws IOException {
		if (persistentList != null) {
			dos.writeByte(persistentList.size());
			for (Object o : persistentList) {
				((Persistent) o).write(dos);
			}
		}
		else
			dos.writeByte(0);
	}
	
	/**
	 * Writes a big vector (of int size) of persistent objects from a stream.
	 *
	 * @param persistentList
	 * @param dos
	 * @throws IOException
	 */
	public static void writeBig(List persistentList, DataOutputStream dos) throws IOException {
		if (persistentList != null) {
			dos.writeInt(persistentList.size());
			for (Object o : persistentList) {
				((Persistent) o).write(dos);
			}
		}
		else
			dos.writeInt(0);
	}

	public static void write(List persistentList, DataOutputStream dos, int len) throws IOException {
		if (persistentList != null) {
			dos.writeInt(persistentList.size());
			for (Object o : persistentList) {
				((Persistent) o).write(dos);
			}
		}
		else
			dos.writeInt(0);
	}

	public static void writeIntegers(List intList, DataOutputStream dos) throws IOException {
		if (intList != null) {
			dos.writeByte(intList.size());
			for (Object o : intList) dos.writeInt((Integer) o);
		}
		else
			dos.writeByte(0);
	}
	
	/**
	 * Writes a list of bytes a stream.
	 *
	 * @param byteList - the Byte vector.
	 * @param dos  - the stream.
	 * @throws IOException
	 */
	public static void writeBytes(List byteList, DataOutputStream dos) throws IOException {
		if (byteList != null) {
			dos.writeByte(byteList.size());
			for (Object o : byteList) dos.writeByte((Byte) o);
		}
		else
			dos.writeByte(0);
	}
	
	/**
	 * Reads a small vector (byte size) of persistent objects of a certain class from a stream.
	 * 
	 * @param dis - the stream to read from.
	 * @param cls - the class of the persistent objects contained in the vector.
	 * @return - the List of persistent objects or null if none.
	 * @throws IOException - thrown when a problem occurs during the reading from stream.
	 * @throws InstantiationException - thrown when a problem occurs during the persistent object creation.
	 * @throws IllegalAccessException - thrown when a problem occurs when setting values of the persistent object.
	 */
	public static List read(DataInputStream dis, Class cls) throws IOException, InstantiationException, IllegalAccessException {
		
		byte len = dis.readByte();
		

		List persistentList = new ArrayList(len);
		
		for(byte i=0; i<len; i++ ){
			Object obj = cls.newInstance();
			((Persistent)obj).read(dis);
			persistentList.add(obj);
		}

		return persistentList;
	}
	
	/**
	 * Reads a big vector (with int size) of a persistent class from stream.
	 * 
	 * @param dis
	 * @param cls
	 * @return
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static List readBig(DataInputStream dis, Class cls) throws IOException, InstantiationException, IllegalAccessException {
		
		int len = dis.readInt();

		List persistentList = new ArrayList(len);
		
		for(int i=0; i<len; i++ ){
			Object obj = cls.newInstance();
			((Persistent)obj).read(dis);
			persistentList.add(obj);
		}

		return persistentList;
	}

	public static List read(DataInputStream dis, Class cls, int len) throws IOException, InstantiationException, IllegalAccessException {
		

		List persistentList = new ArrayList(len);
		
		for(int i=0; i<len; i++ ){
			Object obj = cls.newInstance();
			((Persistent)obj).read(dis);
			persistentList.add(obj);
		}

		return persistentList;
	}

	public static List readIntegers(DataInputStream dis) throws IOException {
		
		byte len = dis.readByte();


		List intList = new ArrayList(len);
		
		for(byte i=0; i<len; i++ )
			intList.add(dis.readInt());

		return intList;
	}
	
	/**
	 * Reads a list of bytes from the stream.
	 * 
	 * @param dis
	 * @return
	 * @throws IOException
	 */
	public static List readBytes(DataInputStream dis) throws IOException {
		
		byte len = dis.readByte();

		List byteList = new ArrayList(len);
		
		for(byte i=0; i<len; i++ )
			byteList.add(dis.readByte());

		return byteList;
	}
	
	/**
	 * Write a hashtable of string keys and values to a stream.
	 *
	 * @param stringHashMap - a hashtable of string keys and values.
	 * @param dos - that stream to write to.
	 * @throws IOException - thrown when a problem occurs during the writing to stream.
	 */
	public static void write(Map stringHashMap, DataOutputStream dos) throws IOException {
		if (stringHashMap != null) {
			dos.writeByte(stringHashMap.size());
			for (Object o : stringHashMap.keySet()) {
				String key = (String) o;
				dos.writeUTF(key);
				dos.writeUTF((String) stringHashMap.get(key));
			}
		} else {
			dos.writeByte(0);
		}
	}
	
	/**
	 * Reads a hashtable of string keys and values from a stream.
	 * 
	 * @param dis - the stream to read from.
	 * @return - the hashtable of string keys and values or null if none.
	 * @throws IOException - thrown when a problem occurs during the reading from stream.
	*/
	public static HashMap read(DataInputStream dis) throws IOException {
		
		byte len = dis.readByte();
		/*if(len == 0)
			return null;*/

		HashMap stringHashMap = new HashMap();

		for(byte i=0; i<len; i++ )
			stringHashMap.put(dis.readUTF(), dis.readUTF());

		return stringHashMap;
	}
}
