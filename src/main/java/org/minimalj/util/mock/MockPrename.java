package org.minimalj.util.mock;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.minimalj.util.LoggingRuntimeException;

// http://www.heise.de/ct/ftp/07/17/182/
public class MockPrename {
	public static final Logger LOG = Logger.getLogger(MockPrename.class.getName());
	
	public static class NameWithFrequency implements Serializable {
		private static final long serialVersionUID = 4174820307622957597L;
		
		public int frequency;
		public String name;
		public String callName;
		public Boolean male;
		
		public boolean male() {
			return male != null ? male : true;
		}
		
		public static void write(Map<String, List<NameWithFrequency>> namesByLocale, String filename) throws IOException {
			try (FileOutputStream fos = new FileOutputStream(filename)) {
				try (DeflaterOutputStream dos = new DeflaterOutputStream(fos)) {
					try (ObjectOutput oos = new ObjectOutputStream(dos)) {
						oos.writeObject(namesByLocale);
					}
				}
			}
		}

		public static Map<String, List<NameWithFrequency>> read(InputStream inputStream) {
			try (InflaterInputStream iis = new InflaterInputStream(inputStream)) {
				try (ObjectInputStream ois = new ObjectInputStream(iis)) {
					return (Map<String, List<NameWithFrequency>>) ois.readObject();
				}
			} catch (Exception x) {
				throw new LoggingRuntimeException(x, LOG, "Read of demo names failed");
			}
		}

		// eclipse
		
		@Override
		public String toString() {
			return "NameWithFrequency [callName=" + callName + ", frequency=" + frequency + ", name=" + name + "]";
		}
		
	}
	
	private static Map<String, List<NameWithFrequency>> namesByLocale = new HashMap<>();
	
	public static String getFirstName() {
		return getName().name;
	}
	
	public static NameWithFrequency getName() {
		Locale locale = Locale.getDefault();
		String country = locale.getCountry();
		if (!namesByLocale.containsKey(country)) {
			country = "CH";
		}
		return choose(namesByLocale.get(country));
	}
	
	private static NameWithFrequency choose(List<NameWithFrequency> list) {
		Collections.shuffle(list);
		for (NameWithFrequency name : list) {
			if (Math.random() > (0.95 / name.frequency)) return name;
		}
		return null;
 	}
	
}
