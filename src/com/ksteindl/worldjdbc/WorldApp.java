package com.ksteindl.worldjdbc;

public class WorldApp {
	
	public static void main(String[] args) {
		try (Controller controller = new Controller()) {
			controller.start();
		}
	}

}
