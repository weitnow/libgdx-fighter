package com.weitnow.sfs;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.weitnow.sfs.SFS;
import com.weitnow.sfs.ressources.GlobalVariables;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Gemetzel");
		config.setWindowedMode(GlobalVariables.WINDOW_WIDTH,GlobalVariables.WINDOW_HEIGHT);
		new Lwjgl3Application(new SFS(), config);
	}
}
