package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestsKO {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConnexion() throws IOException {
		Socket client = new Socket("127.0.0.1", 2121);
		BufferedReader cReader = new BufferedReader(new InputStreamReader(
				client.getInputStream()));
		String line = cReader.readLine();
		assertEquals("220 coucou", line);
		client.close();
	}
}
