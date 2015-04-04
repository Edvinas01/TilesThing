package com.edv.game.map;

import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.SPARSE;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;

public class Header {

	// Total byte count in class.
	public static final int BYTE_COUNT = 12;
	
	// Version of the map.
	private float version;

	// Width and height in chunks.
	private int width;
	private int height;

	public Header() {

		this.version = -1;
		this.width = -1;
		this.height = -1;
	}

	public Header(float version, int width, int height) {

		this.version = version;
		this.width = width;
		this.height = height;
	}

	public float getVersion() {

		return version;
	}

	public void setVersion(float version) {

		this.version = version;
	}

	public int getWidth() {

		return width;
	}

	public void setWidth(int width) {

		this.width = width;
	}

	public int getHeight() {

		return height;
	}

	public void setHeight(int height) {

		this.height = height;
	}

	public void write(String gameMapName) {

		String dir = "./res/GameMaps/" + gameMapName + "/";

		Path path = FileSystems.getDefault().getPath(dir, gameMapName + ".map");

		try {
			SeekableByteChannel channel = Files.newByteChannel(path, EnumSet.of(WRITE, SPARSE));
			ByteBuffer buffer = ByteBuffer.allocate(BYTE_COUNT);

			buffer.putFloat(version);
			buffer.putInt(width);
			buffer.putInt(height);

			buffer.flip();

			channel.write(buffer);

			channel.close();
			buffer.clear();

			buffer = null;
			channel = null;

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void read(String gameMapName) {

		String dir = "./res/GameMaps/" + gameMapName + "/";

		Path path = FileSystems.getDefault().getPath(dir, gameMapName + ".map");

		try {			
			SeekableByteChannel channel = Files.newByteChannel(path, EnumSet.of(READ, SPARSE));
			ByteBuffer buffer = ByteBuffer.allocate(BYTE_COUNT);

			channel.read(buffer);

			buffer.flip();

			version = buffer.getFloat();
			width = buffer.getInt();
			height = buffer.getInt();

			channel.close();
			buffer.clear();

			buffer = null;
			channel = null;
			
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
