package springboot.ci.services;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SystemCalls {

	static boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
	
	public static String test() {
		String cmd = "";
		if (isWindows)
			cmd="DIR";
		else
			cmd="ls -l";
		
		test(cmd);
		return "Executed com " + cmd;
	}
		
	public static void test(String cmd) {
		Runtime r = Runtime.getRuntime();

		try {
			/*
			 * Here we are executing the UNIX command ls for directory listing. The format
			 * returned is the long format which includes file information and permissions.
			 */
			Process p = r.exec("ls -l");
			InputStream in = p.getInputStream();
			BufferedInputStream buf = new BufferedInputStream(in);
			InputStreamReader inread = new InputStreamReader(buf);
			BufferedReader bufferedreader = new BufferedReader(inread);

			// Read the ls output
			String line;
			while ((line = bufferedreader.readLine()) != null) {
				System.out.println(line);
			}
			// Check for ls failure
			try {
				if (p.waitFor() != 0) {
					System.err.println("exit value = " + p.exitValue());
				}
			} catch (InterruptedException e) {
				System.err.println(e);
			} finally {
				// Close the InputStream
				bufferedreader.close();
				inread.close();
				buf.close();
				in.close();
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
