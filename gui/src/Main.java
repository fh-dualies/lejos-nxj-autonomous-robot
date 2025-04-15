import lejos.pc.comm.*;
import java.io.*;

public class Main {

	public static void main(String[] args) throws NXTCommException, IOException {
		
		System.out.println("Connecting ...");
		
		NXTInfo info = new NXTInfo(NXTCommFactory.BLUETOOTH, "megaman", "00:16:53:1B:F8:ED");
		NXTComm com = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		boolean status = com.open(info);
		
		System.out.println("Connected !");
		
		OutputStream output = com.getOutputStream();
		InputStream input = com.getInputStream();
		
		PrintWriter p = new PrintWriter(output, true);
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				int line;
				try {
					while (true) {
						line = input.read();
						System.out.print((char)line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
		
		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		
		while(true) {
			userInput = consoleReader.readLine();
			if (userInput == null) continue;
			p.println(userInput);
			p.flush();
			System.out.println("SEND: " + userInput);
		}
	}

}
 