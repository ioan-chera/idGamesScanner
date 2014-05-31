package com.ichera.idgamesscanner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

import com.ichera.idgamesscanner.Unzipper.Entry;

public class Unzipper2 
{
	private static final int BUFSIZ = 8192;
	
	private Unzipper.Entry[]	mEntries;
	
	public Unzipper2(String path) throws FileNotFoundException
	{
		FileInputStream fis = null;
//		BufferedInputStream bis = null;
		try
		{
			fis = new FileInputStream(path);
//			bis = new BufferedInputStream(fis);
			construct("", fis);
		}
		finally
		{
			if(fis != null)
				Util.close(fis);
//			if(bis != null)
//				Util.close(bis);
		}
	}
	
	public Unzipper2(InputStream stream)
	{
		construct("", stream);
	}
	
	public Unzipper2(String prefix, InputStream stream)
	{
		construct(prefix, stream);
	}
	
	private void construct(String prefix, InputStream stream)
	{
		ArrayList<Entry> entries = new ArrayList<Entry>();
		
		ZipInputStream zipStream = null;
		try
		{
			zipStream = new ZipInputStream(stream);
			for(;;)
			{
				try
				{
					ZipEntry entry = zipStream.getNextEntry();
	
					if(entry == null)
						break;
									
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[BUFSIZ];
					int count;
					while((count = zipStream.read(buffer)) != -1)
						baos.write(buffer, 0, count);
					
					if(Util.hasExtension(entry.getName(), "zip"))
					{
						Unzipper2 subzip = 
								new Unzipper2(entry.getName(), 
										new ByteArrayInputStream(baos.toByteArray()));
						entries.addAll(Arrays.asList(subzip.getEntries()));
					}
					else
						entries.add(new Entry(prefix + '/' + entry.getName(), 
								baos.toByteArray()));
					
				}
				finally
				{
					try
					{
						zipStream.closeEntry();
					}
					catch(IOException e)
					{
					}
				}
			}
		}
		catch(IOException e)
		{
			
			System.err.println("Failed nested " + prefix);
		}
		finally
		{
			if(zipStream != null)
				Util.close(zipStream);
		}
		
		mEntries = new Entry[entries.size()];
		entries.toArray(mEntries);
	}
	
	public Unzipper.Entry[] getEntries()
	{
		return mEntries;
	}
}
