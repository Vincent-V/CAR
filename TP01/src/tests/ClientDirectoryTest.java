package tests;

import static org.junit.Assert.*;
import lille1.car.ftpServer.ClientDirectory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClientDirectoryTest {
	
	private ClientDirectory dir;
	
	@Before
	public void setUp() throws Exception {
		dir = new ClientDirectory(".");
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testAbsolutePath() {
		assertEquals(".", dir.getAbsolutePath());
	}

}
