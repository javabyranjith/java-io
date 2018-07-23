package jbr.object.output.stream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class WriteObjectIntoFile {

	public static void main(String[] args) throws Exception {
		File inputFile = new File("person.ser");
		generateInputFile(inputFile);
		ObjectInputStream objInputStream = new ObjectInputStream(new FileInputStream(inputFile));
		Person personObj = (Person) objInputStream.readObject();

		usingObjectOutputStream(personObj);
		usingObjectOutputStreamNew(personObj);
		// usingRandomAccessFile(personObj);
		// usingKryo(personObj);

		objInputStream.close();
	}

	static void usingKryo(Person obj) throws Exception {
		System.out.println("start - kryo");
		long writeStart = System.currentTimeMillis();
		Kryo kryo = new Kryo();

		RandomAccessFile writeFile = new RandomAccessFile("person-kryo.ser", "rw");
		Output output = new Output(new FileOutputStream(writeFile.getFD()), 10);
		kryo.writeObject(output, obj);
		output.close();
		writeFile.close();
		System.out.println("Kryo write Time: " + (System.currentTimeMillis() - writeStart));

		long readStart = System.currentTimeMillis();
		RandomAccessFile readFile = new RandomAccessFile("person-kryo.ser", "r");
		Input input = new Input(new FileInputStream(readFile.getFD()), 100000);
		Person pp = (Person) kryo.readObject(input, Person.class);
		System.out
				.println("Kryo read Time: " + (System.currentTimeMillis() - readStart) + " : " + pp.getNames().size());
		input.close();
		readFile.close();
	}

	static void usingRandomAccessFile(Person obj) throws Exception {
		System.out.println("start - random");

		long start = System.currentTimeMillis();
		RandomAccessFile raf = new RandomAccessFile("person-random.ser", "rw");
		FileOutputStream fos = new FileOutputStream(raf.getFD());
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(obj);

		System.out.println("RandomAccess write Time: " + (System.currentTimeMillis() - start));

		oos.close();
		raf.close();
	}

	static void usingObjectOutputStream(Person obj) {
		System.out.println("start - oos");

		long start = System.currentTimeMillis();
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("person-oos.ser")));) {
			oos.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("ObjectOutputStream write Time: " + (System.currentTimeMillis() - start));

	}

	static void usingObjectOutputStreamNew(Person obj) {
		System.out.println("start - oos - new");

		long start = System.currentTimeMillis();
		/*
		 * try (ObjectOutputStream oos = new ObjectOutputStream( new
		 * BufferedOutputStream(Files.newOutputStream(Paths.get("oos-new.ser"),
		 * StandardOpenOption.CREATE_NEW)))) {
		 */

		try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(
				Files.newOutputStream(Paths.get("oos-new1.ser"), StandardOpenOption.CREATE_NEW)))) {
			out.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("ObjectOutputStream New write Time: " + (System.currentTimeMillis() - start));

	}

	static void generateInputFile(File inputFile) throws Exception {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 1000000; i++) {
			list.add("test");
		}

		Person p = new Person();
		p.setNames(list);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(inputFile));
		oos.writeObject(p);
		oos.close();

	}
}

class Person implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<String> names;

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

}
