package com.examples.ffmpeg4android_demo;

import android.os.Environment;

import com.examples.ffmpeg4android_demo.AlbumStorageDirFactory;

import java.io.File;

public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

	// Standard storage location for digital camera files
	private static final String CAMERA_DIR = "/dcim/";

	@Override
	public File getAlbumStorageDir(String albumName) {
		return new File (
				Environment.getExternalStorageDirectory()
				+ CAMERA_DIR
				+ albumName
		);
	}
}
