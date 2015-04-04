package com.edv.game.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;

import static java.nio.file.StandardOpenOption.*;

public class Manipulator {

	private SeekableByteChannel channel;
	private ByteBuffer chunkBuffer;
	
	private Header header;
	
	/**
	 * Create a new empty GameMap file.
	 * 
	 * @param name - name of the GameMap.
	 * @param header - header data for the map.
	 */
	public Manipulator(String name, Header header) {
		
		this.header = header;
		
		String dir = "./res/GameMaps/" + name + "/";
		
		new File(dir).mkdirs();
		
		try {
			
			new FileOutputStream(dir + name + ".map");
			
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		
		header.write(name);
	}	
	/**
	 * Load an existing GameMap file.
	 * 
	 * @param name - name of the GameMap.
	 */
	public Manipulator(String name) {
				
		header = new Header();	
		
		header.read(name);
	}

	public void open(String name) {

		String dir = "./res/GameMaps/" + name + "/";

		Path path = FileSystems.getDefault().getPath(dir, name + ".map");

		try {

			channel = Files.newByteChannel(path, EnumSet.of(READ, WRITE, SPARSE));
			chunkBuffer = ByteBuffer.allocate(Chunk.SIZE);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void dispose() {

		try {

			channel.close();
			chunkBuffer.clear();
			chunkBuffer = null;

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void write(Chunk chunk, int xi, int yi) {

		int index = chunkIndex(xi, yi);

		chunkBuffer.clear();
		chunk.save(chunkBuffer);
		chunkBuffer.flip();

		try {

			channel.position(Header.BYTE_COUNT + Chunk.SIZE * index);
			channel.write(chunkBuffer);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void write(Chunk[][] chunks) {

		for (int x = 0; x < header.getWidth(); x++) {

			for (int y = 0; y < header.getHeight(); y++) {

				write(chunks[x][y], x, y);
			}
		}
	}
	
	public Chunk read(int xi, int yi) {

		int index = chunkIndex(xi, yi);

		chunkBuffer.clear();
		try {

			channel.position(Header.BYTE_COUNT + Chunk.SIZE * index);

			if (channel.read(chunkBuffer) < 0) {

				return null;

			} else {

				chunkBuffer.flip();

				Chunk chunk = Chunk.load(chunkBuffer);
				chunk.setChunkX(xi * Chunk.WIDTH * Tile.SIZE);
				chunk.setChunkY(yi * Chunk.HEIGHT * Tile.SIZE);

				return chunk;
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}
	
	public Header getHeader() {
		
		return header;
	}

	private int chunkIndex(int xIndex, int yIndex) {

		if (0 <= xIndex && xIndex < header.getWidth() && 0 <= yIndex
				&& yIndex < header.getHeight()) {

			return yIndex * header.getWidth() + xIndex;

		} else {

			System.err.println("chunkIndex(" + xIndex + ", " + yIndex + ") out of bounds!");
			return -1;
		}
	}
}
