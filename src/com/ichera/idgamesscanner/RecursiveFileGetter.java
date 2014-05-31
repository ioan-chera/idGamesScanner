package com.ichera.idgamesscanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecursiveFileGetter 
{
	private final File m_rootDir;
	
	private ArrayList<File> m_files;
	private String			m_nameRegex;
	
	RecursiveFileGetter(File rootDir)
	{
		m_rootDir = rootDir;
	}
	
	RecursiveFileGetter(String rootPath)
	{
		m_rootDir = new File(rootPath);
	}
	
	void setNameRegex(String value)
	{
		m_nameRegex = value;
	}
	
	List<File> getFiles()
	{
		return m_files;
	}
	
	void collect()
	{
		// Reset content
		if(m_files == null)
			m_files = new ArrayList<File>();
		else 
			m_files.clear();
		
		workCollect(m_rootDir);
	}
	
	private void workCollect(File rootDir)
	{
		File[] files = rootDir.listFiles();
		
		if(files == null)
			return;
		
		for(File file: files)
		{
			if(file.isDirectory())
				workCollect(file);
			else if(m_nameRegex == null || m_nameRegex.length() == 0 
					|| file.getName().matches(m_nameRegex))
				m_files.add(file);
		}
	}
}
