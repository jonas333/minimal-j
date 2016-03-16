package org.minimalj.util.mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import org.minimalj.util.mock.MockPrename.NameWithFrequency;

// http://www.heise.de/ct/ftp/07/17/182/
public class PrenameGenerator {
	public static final Logger LOG = Logger.getLogger(MockPrename.class.getName());
	
	private static List<NameWithFrequency> readPrenames(int localePos) throws IOException {
		List<NameWithFrequency> names = new ArrayList<>();
		InputStream inputStream = MockPrename.class.getResourceAsStream("/org/minimalj/util/mock/prenames.txt");
		try (Scanner scanner = new Scanner(inputStream, "ISO-8859-1")) {
			scanner.useDelimiter("\n");
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.charAt(0) == '#') continue;
				NameWithFrequency nameWithFrequency = new NameWithFrequency();
				
				String name = line.substring(3, 29).trim();
				int pos = name.indexOf(' ');
				if (pos < 0) {
					nameWithFrequency.name = name;
				} else {
					nameWithFrequency.name = name.substring(pos + 1);
					nameWithFrequency.callName = name.substring(0, pos);
				}
				
				char frequency = line.charAt(localePos);
				if (Character.isDigit(frequency)) nameWithFrequency.frequency = frequency - '0';
				else nameWithFrequency.frequency = frequency - 'A' + 10;

				String type = line.substring(0, 3);
				if (!type.contains("M")) nameWithFrequency.male = Boolean.FALSE;
				if (!type.contains("F")) nameWithFrequency.male = Boolean.TRUE;
				
				if (nameWithFrequency.frequency > 5) {
					names.add(nameWithFrequency);
				}
			}
		}
		return names;
	}
 
	public static void main(String[] args) throws IOException {
		Map<String, List<NameWithFrequency>> namesByLocale = new HashMap<>();
		namesByLocale.put("ch", readPrenames(44));
		namesByLocale.put("de", readPrenames(42));
		namesByLocale.put("us", readPrenames(31));
		NameWithFrequency.write(namesByLocale, "prenames.zip");
	}
}
