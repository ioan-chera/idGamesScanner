// http://www.mkyong.com/java/how-to-decompress-files-from-a-zip-file/

package com.ichera.idgamesscanner;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzipper implements Closeable
{
	private static final int BUFSIZ = 8192;
	
	private boolean m_silent;
	private String mPath;
	private ZipEntry mZipEntry;
	private ZipInputStream mZipStream;
	
	public Unzipper(String path) throws FileNotFoundException
	{
		mPath = path;
		mZipStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(mPath)));
	}
	
	void setSilent(boolean value)
	{
		m_silent = value;
	}
	
	public Entry getNext()
	{
		try
		{
			mZipEntry = mZipStream.getNextEntry();
			if(mZipEntry != null)
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[BUFSIZ];
				int count;
				while((count = mZipStream.read(buffer)) != -1)
					baos.write(buffer, 0, count);
				return new Entry(mZipEntry.getName(), baos.toByteArray());
			}
		}
		catch(IOException e)
		{
			if(!m_silent)
			{
				System.err.printf("On %s:\n", mPath);
				e.printStackTrace();
			}
		}
		catch(IllegalArgumentException e)
		{
			System.err.printf("On %s:\n", mPath);
			System.err.println("WARNING: Stopped reading ZIP, invalid internal value");
		}
		return null;
	}
	
	public static class Entry
	{
		public String name;
		public byte[] content;
		
		public Entry(String inName, byte[] inContent)
		{
			name = inName;
			content = inContent;
		}
	}

	@Override
	public void close() throws IOException 
	{
		mZipStream.close();
	}
	
	public static Entry[] getContents(String path, boolean silent)
	{
		ArrayList<Entry> entries = new ArrayList<Entry>();
		
		Unzipper uz = null;
		try
		{
			uz = new Unzipper(path);
			uz.setSilent(silent);
			Unzipper.Entry entry;
			// Must have .WAD and .TXT with names matching the path
			
			while((entry = uz.getNext()) != null)
			{
				entries.add(entry);
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			Util.close(uz);
		}
		
		Entry[] dsf = new Entry[entries.size()];
		entries.toArray(dsf);
		return dsf;
	}
}
