package vod.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import vod.entities.Song;
import vod.entities.Star;
import vod.exe.Processer;
import vod.resources.Resource;

public class SocketServer {

	public final void init() {
		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		ServerSocket server = null;
		try {
			server = new ServerSocket(Resource.SOCKET_PORT);
			while (server != null && !server.isClosed()) {
				acceptSockets(server.accept());
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (server != null)
					server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// }
		// }).start();
	}

	private void acceptSockets(final Socket socket) {
		new Thread(new Runnable() {
			@Override
			public void run() {

				BufferedReader br = null;
				BufferedWriter bw = null;
				try {
					br = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));

					bw = new BufferedWriter(new OutputStreamWriter(
							socket.getOutputStream()));

					String line = br.readLine();// null;
//					while ((line = br.readLine()) != null) {
						// && !"OVER".equals(line)) {
					System.out.println(line);
						if (line != null)
							switchProcess(line, bw);
//					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (bw != null)
							bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						if (br != null)
							br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						if (socket != null)
							socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public static void main(String[] args) {
		new SocketServer().init();
	}

	private void switchProcess(String line, BufferedWriter bw)
			throws IOException {
		if (line.startsWith("SELECTSONG")) {
			Processer.getInstance().addToStore(
					line.substring("SELECTSONG".length()));
			bw.write("OK");
			bw.newLine();
		}
		if (line.startsWith("STARSSPINYIN-")) {
			List<Star> stars = Processer.getInstance().findStarsByPinyin(
					line.substring("STARSSPINYIN-".length()));
			for (Star star : stars) {
				bw.write(star.getKey() + "|" + star.getName());
				bw.newLine();
			}
		}
		if (line.startsWith("SONGSPINYIN-")) {
			List<Song> songs = Processer.getInstance().findSongsByPinyin(
					line.substring("SONGSPINYIN-".length()));
			for (Song song : songs) {
				bw.write(song.getKey() + "|" + song.getName() + "________"
						+ song.getStar());
				bw.newLine();
			}
		}

		if (line.startsWith("GETSELECTEDLIST")) {
			List<Song> songs = Processer.getInstance().getSelectedList();
			for (Song song : songs) {
				bw.write(song.getKey() + "            " + song.getName()
						+ "            " + song.getStar());
				bw.newLine();
			}
		}
		if (line.startsWith("NEXT")) {
			Processer.getInstance().next();
		}
		if (line.startsWith("COMMAND")) {
			String[] param = line.substring("COMMAND".length()).split(",");
			Processer.getInstance().execCommand(param[0],
					Integer.parseInt(param[1]));
		}
		bw.write("OVER");
		bw.flush();
	}

	public void mullticate() {

		// new java.net.MulticastSocket().
	}
}
