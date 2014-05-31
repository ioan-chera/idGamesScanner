// http://www.mkyong.com/java/how-to-decompress-files-from-a-zip-file/

package com.ichera.idgamesscanner;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;


public class Unzipper implements Closeable
{
	private static final int BUFSIZ = 8192;
	
	private final String mPath;
	private final ZipFile mZipFile;
	private final List<?> mHeaders;
	
	private int mCurIndex;
	
	public Unzipper(String path) throws ZipException
	{
		mPath = path;
		mZipFile = new ZipFile(path);
		mHeaders = mZipFile.getFileHeaders();
	}
		
	public Entry getNext()
	{
		if(mCurIndex >= mHeaders.size())
			return null;
		
		ZipInputStream stream = null;
		FileHeader header = null;
		try
		{
			header = (FileHeader)mHeaders.get(mCurIndex);
			stream = mZipFile.getInputStream(header);
	
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[BUFSIZ];
			int count;
			while((count = stream.read(buffer)) != -1)
				baos.write(buffer, 0, count);
			return new Entry(header.getFileName(), baos.toByteArray());
		}
		catch(ZipException e)
		{
//			System.err.println(e.getClass().getSimpleName() +  " on " + mPath);
			// commented out because it happens too often
		}
		catch(IOException e)
		{
			System.err.println(e.getClass().getSimpleName() +  " on " + mPath);
		}
		finally
		{
			++mCurIndex;
			if(stream != null)
				Util.close(stream);
		}
		if(header != null)
			return new Entry(header.getFileName(), null);
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
//		mZipFile.close();
	}
	
	public static Entry[] getContents(String path, boolean silent)
	{
		ArrayList<Entry> entries = new ArrayList<Entry>();
		
		Unzipper uz = null;
		try
		{
			uz = new Unzipper(path);
			Unzipper.Entry entry;
			// Must have .WAD and .TXT with names matching the path
			
			while((entry = uz.getNext()) != null)
			{
				entries.add(entry);
			}
		}
		catch(ZipException e)
		{
			System.err.println("On " + path + ":");
			e.printStackTrace();
		}
		finally
		{
			if(uz != null)
				Util.close(uz);
		}
		
		Entry[] dsf = new Entry[entries.size()];
		entries.toArray(dsf);
		return dsf;
	}
}
