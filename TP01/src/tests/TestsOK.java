package tests;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import lille1.car.ftpServer.ClientDirectory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestsOK {
	
	private ClientDirectory dir;
	
	@Before
	public void setUp() throws Exception {
		dir = new ClientDirectory(".");
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
	
	@Test
	public void testMultipleConnexion() throws IOException {
		Socket client1 = new Socket("127.0.0.1", 2121);
		Socket client2 = new Socket("127.0.0.1", 2121);
		BufferedReader cReader1 = new BufferedReader(new InputStreamReader(
				client1.getInputStream()));
		BufferedReader cReader2 = new BufferedReader(new InputStreamReader(
				client2.getInputStream()));
		String line1 = cReader1.readLine();
		String line2 = cReader2.readLine();
		assertEquals(line1, line2);
		client1.close();
		client2.close();
	}

	@Test
	public void testAuthUser() throws IOException{
		Socket client = new Socket("127.0.0.1", 2121);
		
		BufferedReader cReader = new BufferedReader(new InputStreamReader(
				client.getInputStream()));
		BufferedWriter cWritter = new BufferedWriter(new OutputStreamWriter(
				client.getOutputStream()));
		String line = cReader.readLine();
		cWritter.write("USER toto\n");
		cWritter.flush();
		line = cReader.readLine();
		assertEquals("331 user ok", line);
		client.close();
	}
	
	@Test
	public void testAuthUserAndPass() throws IOException{
		Socket client = new Socket("127.0.0.1", 2121);
		
		BufferedReader cReader = new BufferedReader(new InputStreamReader(
				client.getInputStream()));
		BufferedWriter cWritter = new BufferedWriter(new OutputStreamWriter(
				client.getOutputStream()));
		
		String line = cReader.readLine();
		cWritter.write("USER toto\n");
		cWritter.flush();
		line = cReader.readLine();
		cWritter.write("PASS bob\n");
		cWritter.flush();
		line = cReader.readLine();
		assertEquals("230 pass ok", line);
		client.close();
	}
	
	@Test
	public void testAuthUserAndWrongPass() throws IOException{
		Socket client = new Socket("127.0.0.1", 2121);
		
		BufferedReader cReader = new BufferedReader(new InputStreamReader(
				client.getInputStream()));
		BufferedWriter cWritter = new BufferedWriter(new OutputStreamWriter(
				client.getOutputStream()));
		
		String line = cReader.readLine();
		cWritter.write("USER toto\n");
		cWritter.flush();
		line = cReader.readLine();
		cWritter.write("PASS bobo\n");
		cWritter.flush();
		line = cReader.readLine();
		assertEquals("530 pass ko", line);
		client.close();
	}
	
	@Test
	public void testPWD() throws IOException{
		Socket client = new Socket("127.0.0.1", 2121);
		
		BufferedReader cReader = new BufferedReader(new InputStreamReader(
				client.getInputStream()));
		BufferedWriter cWritter = new BufferedWriter(new OutputStreamWriter(
				client.getOutputStream()));
		String line = cReader.readLine();
		File local = new File(".");
		cWritter.write("PWD\n");
		cWritter.flush();
		line = cReader.readLine();
		assertEquals("257 "+local.getAbsolutePath(), line);
		client.close();
	}
	
	@Test
	public void testCWD() throws IOException{
		Socket client = new Socket("127.0.0.1", 2121);
		
		BufferedReader cReader = new BufferedReader(new InputStreamReader(
				client.getInputStream()));
		BufferedWriter cWritter = new BufferedWriter(new OutputStreamWriter(
				client.getOutputStream()));
		String line = cReader.readLine();
		File local = new File(".").getParentFile();
		cWritter.write("CWD ..\n");
		cWritter.flush();
		line = cReader.readLine();
		assertEquals("257 "+local.getAbsolutePath(), line);
		client.close();
	}
}
