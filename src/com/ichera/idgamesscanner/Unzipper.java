/*

Copyright (c) 2014, Ioan Chera
All rights reserved.

Redistribution and use in source and binary forms, with or without 
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this 
   list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, 
   this list of conditions and the following disclaimer in the documentation 
   and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its contributors 
   may be used to endorse or promote products derived from this software 
   without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ichera.idgamesscanner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
				if(entry.content != null && Util.hasExtension(entry.name, "zip"))
				{
					Unzipper2 subzip = 
							new Unzipper2(entry.name, 
									new ByteArrayInputStream(entry.content));
					entries.addAll(Arrays.asList(subzip.getEntries()));
				}
				else
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
