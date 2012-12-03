package vod.exe;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import vod.client.Client;
import vod.core.Player;
import vod.display.Window;
import vod.entities.Song;
import vod.entities.Star;
import vod.resources.Resource;
import vod.util.Store;

public class Processer {

	private String screenWid;

	public Client client;

	private String[] options;

	private Store store = new Store();

	private static Processer instance = new Processer();

	private int currentVolume = 50;

	private List<Song> songList;

	private List<Star> starList;

	public static Processer getInstance() {
		return instance;
	}

	private Processer() {
		this.init();

		// Screen Type
		if ("1".equals(Resource.SCREEN_TYPE)) {
			this.setScreenWid(Window.getInstance().getHandleId());
		}
	}

	@SuppressWarnings("unchecked")
	public void init() {

		File songsFile = new File("songs.db");
		File starsFile = new File("stars.db");

		if (!songsFile.exists() || !starsFile.exists()) {
			songList = new ArrayList<Song>();
			starList = new ArrayList<Star>();

			ObjectOutputStream songs = null, stars = null;
			try {
				this.searchResourceOnDisk(songList, starList);
				songs = new ObjectOutputStream(new FileOutputStream(songsFile));
				stars = new ObjectOutputStream(new FileOutputStream(starsFile));

				songs.writeObject(songList);
				songs.flush();

				stars.writeObject(starList);
				stars.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (songs != null) {
					try {
						songs.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (stars != null) {
					try {
						stars.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {

			ObjectInputStream songsReader = null;
			ObjectInputStream starsReader = null;
			try {
				songsReader = new ObjectInputStream(new FileInputStream(
						songsFile));
				songList = (List<Song>) songsReader.readObject();

				starsReader = new ObjectInputStream(new FileInputStream(
						starsFile));
				starList = (List<Star>) starsReader.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (songsReader != null) {
					try {
						songsReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (starsReader != null) {
					try {
						starsReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void searchResourceOnDisk(List<Song> songList, List<Star> starList) {
		final String[] types = Resource.TYPE_PATH.split(",");

		List<File> songs = new ArrayList<File>();
		String[] path = Resource.SONG_PATH.split("\\|");
		int splitIndex = 0;
		for (int i = 0; i < path.length; i++) {

			File folder = new File(path[i]);
			File[] files = folder.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					int dot = pathname.getName().lastIndexOf('.');
					if (pathname.isFile() && (dot > -1)
							&& (dot < (pathname.getName().length() - 1))) {
						for (String type : types) {
							if (type.equalsIgnoreCase(pathname.getName()
									.substring(dot + 1)))
								return true;
						}
					}
					return false;
				}
			});
			if (i == 0)
				splitIndex = files.length;
			songs.addAll(Arrays.asList(files));
		}

		Song song = null;
		Star star = null;

		for (int i = 0; i < songs.size(); i++) {
			File songFile = songs.get(i);
			String fileName = songFile.getName();

			// cut prefix
			java.util.regex.Matcher m = java.util.regex.Pattern.compile(
					Resource.NAMING_PREFIX).matcher(fileName);
			if (m.find() && m.start() == 0) {
				fileName = fileName.substring(m.end());
			}

			m = java.util.regex.Pattern.compile(Resource.NAMING_PATTERN)
					.matcher(fileName);

			int dot = fileName.lastIndexOf('.');
			if (m.find()) {

				String songName = null;
				String starName = null;

				if (i < splitIndex) {
					songName = fileName.substring(0, m.start());
					starName = fileName.substring(m.end(), dot);
				} else {
					songName = fileName.substring(m.end(), dot);
					starName = fileName.substring(0, m.start());
				}

				String songPy = getPinYin(songName);
				String starPy = getPinYin(starName);

				song = new Song();
				song.setKey(String.valueOf(songList.size()));
				song.setName(songName);
				song.setStar(starName);
				song.setPinyin(songPy);
				song.setUri(songFile.getAbsolutePath());
				songList.add(song);

				boolean isExist = false;
				for (Star exist : starList) {
					if (exist.getName().equals(starName)) {
						isExist = true;
						exist.addSong(song);
						break;
					}
				}

				if (!isExist) {
					star = new Star();
					star.setKey(String.valueOf(starList.size()));
					star.setName(starName);
					star.setPinyin(starPy);
					star.addSong(song);
					starList.add(star);
				}
			}
		}
	}

	private String getPinYin(String str) {
		String res = "";
		int num[] = new int[10];
		num[0] = num[6] = 'L';
		num[1] = 'Y';
		num[2] = 'E';
		num[3] = num[4] = 'S';
		num[5] = 'W';
		num[7] = 'Q';
		num[8] = 'B';
		num[9] = 'J';
		char cc;
		for (int i = 0; i < str.length(); i++) {
			cc = str.charAt(i);
			if (cc > 255)
				res += CapitalLetter(PinyinHelper.toHanyuPinyinStringArray(cc));
			else if (cc >= 'a' && cc <= 'z')
				res += (char) (cc + 'A' - 'a');
			else if (cc >= 'A' && cc <= 'Z')
				res += cc;
			else if (cc >= '0' && cc <= '9')
				res += (char) num[cc - '0'];
		}
		return res;
	}

	private char CapitalLetter(String[] pinyinArray) {
		// int charlist[] = new int[26];
		// int i, max = 0, pos = -1;
		// for (i = 0; i < pinyinArray.length; i++) {
		// int x = pinyinArray[i].charAt(0) - 'a';
		// charlist[x]++;
		// }
		// for (i = 0; i < 26; i++) {
		// if (charlist[i] > max) {
		// max = charlist[i];
		// pos = i;
		// }
		// }
		// return (char) (pos + 'A');
		return (char) (pinyinArray[0].charAt(0) - 'a' + 'A');
	}

	public List<Star> findStarsByPinyin(String pinyin) {
		List<Star> stars = new ArrayList<Star>();
		for (Star star : this.starList) {
			if (star.getPinyin().startsWith(pinyin)) {
				stars.add(star);
			}
		}
		return stars;
	}

	public List<Song> findSongsByPinyin(String pinyin) {
		List<Song> songs = new ArrayList<Song>();
		for (Song song : this.songList) {
			if (song.getPinyin().startsWith(pinyin.toUpperCase())) {
				songs.add(song);
			}
		}
		return songs;
	}

	public List<Song> findSongsByStarPinyin(String pinyin) {
		List<Song> songs = new ArrayList<Song>();
		for (Star star : this.starList) {
			if (star.getPinyin().startsWith(pinyin.toUpperCase())) {
				return star.getSongs();
			}
		}
		return songs;
	}

	public List<Song> findSongsByStarName(String name) {
		List<Song> songs = new ArrayList<Song>();
		for (Star star : this.starList) {
			if (star.getName().contains(name)) {
				return star.getSongs();
			}
		}
		return songs;
	}

	public List<Star> getAllStars() {
		return this.starList;
	}

	private void play(Song song) {

		if (this.screenWid == null) {
			options = new String[] { "-volume",
					String.valueOf(this.currentVolume) };
		} else {
			options = new String[] { "-volume",
					String.valueOf(this.currentVolume), "-wid", this.screenWid };
		}

		try {
			Player.play(Resource.PLAYER_PATH, song.getUri(), this.options);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void addToStore(Song song) {
		if (this.store.isEmpty() && !Player.isPlaying()) {
			this.play(song);
		} else {
			this.store.add(song);
		}
	}

	public Store getSelectedList() {
		return this.store;
	}

	public synchronized void next() {
		if (!this.store.isEmpty()) {
			this.play(store.pop());
			client.refreshSelectedList();
		}
	}

	public synchronized void audio() {
		this.execCommand("switch_audio");
		this.volume(currentVolume);
	}

	public synchronized void balance(String balance) {
		this.execCommand("balance " + balance + " 1");
	}
	
	public synchronized void pause() {
		this.execCommand("pause");
	}

	public synchronized void mute() {
		this.execCommand("mute");
	}

	public synchronized void volume(int value) {
		this.execCommand("volume " + value + " 1");
		this.currentVolume = value;
	}

	public void execCommand(String cmd) {
		Player.runCommand(cmd);
	}

	/**
	 * @return the screenWid
	 */
	public String getScreenWid() {
		return screenWid;
	}

	/**
	 * @param screenWid
	 *            the screenWid to set
	 */
	public void setScreenWid(String screenWid) {
		this.screenWid = screenWid;
	}
}
