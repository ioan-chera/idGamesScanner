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
