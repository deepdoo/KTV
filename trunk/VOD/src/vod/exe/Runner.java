package vod.exe;

import vod.client.Client;

public class Runner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Processer.getInstance().client = new Client(Processer.getInstance());
	}

}
