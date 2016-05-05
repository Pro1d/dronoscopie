package fr.insatoulouse.stereoshot.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

public class FileManager {
	File directory;
	public FileManager(String directory) {
		File ext= Environment.getExternalStorageDirectory();
		File dir = new File(ext, directory);
		if(!dir.isDirectory() || !dir.exists())
			dir.mkdir();
		this.directory = dir;
	}
	
	public void writeFile(byte[] data, String name) {
		File file = new File(directory, name);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeFileAsync(final byte[] data, final String name) {
		new Thread() {
			@Override
			public void run() {
				writeFile(data, name);
			}
		}.start();
	}
}
