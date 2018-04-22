package org.minimalj.rest;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.Test;
import org.minimalj.backend.Backend;
import org.minimalj.backend.RestDbBackend;
import org.minimalj.rest.EntityJsonTest.TestClass;
import org.minimalj.rest.EntityJsonTest.TestEnum;

public class RestDbSandboxTest {

	@Test
	public void test() {
		System.setProperty("https.protocols", "TLSv1.2");
		
		TestClass input = new TestClass();
		input.id = UUID.randomUUID().toString();
		input.s = "test";
		input.integer = 42;
		input.i = 4711;
		input.l = 424242424242L;
		input.b = true;
		input.b2 = null;
		input.primitivBoolean = false;
		input.bigDecimal = new BigDecimal("47.11");
		input.testEnum = TestEnum.B;
		input.testEnums.add(TestEnum.A);
		input.testEnums.add(TestEnum.D);

		Backend.setInstance(new RestDbBackend("sandbox-2e88.restdb.io", 80));
		Backend.insert(input);
		
	}

}
