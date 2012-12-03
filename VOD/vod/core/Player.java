package vod.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import vod.exe.Processer;

public class Player {

	private static Process process;

	public static BufferedReader errorReader;

	public static BufferedReader inputReader;

	private static BufferedWriter cmdWriter;

	private static boolean isPlaying;

	private Player() {
	}

	public static boolean isPlaying() {
		return Player.isPlaying;
	}

	public static void play(String player, String songs, String... options)
			throws IOException {
		stop();
		String[] cmd = new String[2];

		if (options != null && options.length > 0) {
			cmd = new String[options.length + 2];
			for (int i = 0; i < options.length; i++) {
				cmd[i + 1] = options[i];
			}
		}
		cmd[0] = player;
		cmd[cmd.length - 1] = songs;
		process = Runtime.getRuntime().exec(cmd,null);

		inputReader = new BufferedReader(new InputStreamReader(
				process.getInputStream()));

		errorReader = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));

		cmdWriter = new BufferedWriter(new OutputStreamWriter(
				process.getOutputStream()));

		isPlaying = true;

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String line = null;
					while (Player.inputReader != null
							&& (line = Player.inputReader.readLine()) != null) {
						if (line.startsWith("Exiting")) Player.isPlaying = false;
						if (line.startsWith("Exiting... (End of file)")) {
							Processer.getInstance().next();
							break;
						}
					}
				} catch (IOException e) {
					// closed error message
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (Player.errorReader.readLine() != null) {
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public synchronized static void stop() {
		// if (cmdWriter != null) {
		// cmdWriter.write("quit \n");
		// cmdWriter.flush();
		// cmdWriter.close();
		// }
		// if (inputReader != null) {
		// inputReader.close();
		// }
		// if (errorReader != null) {
		// errorReader.close();
		// }
		if (process != null) {
			process.destroy();
		}
	}

	public synchronized static void runCommand(String cmd) {
		if (isPlaying && cmdWriter != null) {
			try {
				cmdWriter.write(cmd + " \n");
				cmdWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}