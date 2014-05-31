package com.ichera.idgamesscanner;

import java.io.File;
import java.text.ParseException;

public class Analyzer 
{
	private static final long	SUSPICIOUS_SIZE = 100000000;	// 100M
	
	private File				m_zipFile;
	private Unzipper.Entry[]	m_entries;
	
	Analyzer()
	{
	}
	
	Analyzer(File zipFile, Unzipper.Entry[] entries)
	{
		m_zipFile = zipFile;
		m_entries = entries;
	}
	
	void setZipFile(File zipFile)
	{
		m_zipFile = zipFile;
	}
	
	void setEntries(Unzipper.Entry[] entries)
	{
		m_entries = entries;
	}
	
	void check()
	{
		checkSuspiciousSize();
		checkMissingWad();
	}
	
	private void checkSuspiciousSize()
	{
		long expandedSize = 0;
		for(Unzipper.Entry entry : m_entries)
		{
			if(entry.content != null)
				expandedSize += entry.content.length;
		}
		long zipSize = m_zipFile.length();
		if(expandedSize >= SUSPICIOUS_SIZE && expandedSize >= 100 * zipSize)
			warn("Expanded size too large (" + expandedSize + ") from zip of size " + zipSize);
	}
	
	private void checkMissingWad()
	{
		boolean foundWad = false;
		String nonstandard = null;
		for(Unzipper.Entry entry : m_entries)
		{
			if(Util.hasExtension(entry.name, "wad") || 
					Util.hasExtension(entry.name, "pk3") ||
					Util.hasExtension(entry.name, "pk7") || 
					Util.hasExtension(entry.name, "pke"))
			{
				foundWad = true;
				break;
			}
			else if(entry.content != null)
			{
				try
				{
					new Wad(entry.content);
					nonstandard = entry.name;
				}
				catch(ParseException e)
				{
				}
			}
		}
		if(!foundWad)
		{
			if(nonstandard != null && nonstandard.length() > 0)
				info("Nonstandard wad extension: " + nonstandard);
			else
				warn("Missing .wad file!");
		}
	}
	
	private void warn(String s)
	{
		System.out.println("WARNING: " + m_zipFile + ": " + s);
	}
	
	private void info(String s)
	{
		System.out.println("INFO: " + m_zipFile + ": " + s);
	}
}
