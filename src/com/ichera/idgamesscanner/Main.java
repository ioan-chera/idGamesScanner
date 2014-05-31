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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main 
{
	private static final int PRINT_STEP = 1024;
	
	private final Map<Md5Key, String> m_md5Map = new HashMap<Md5Key, String>();
	
	public Map<Md5Key, String> getMd5Map()
	{
		return m_md5Map;
	}
	
	public static void main(String[] args)
	{
		if(args.length < 1)
		{
			System.err.println("Please specify the path to the idgames/levels/ root.");
			return;
		}
		
		RecursiveFileGetter rfg = new RecursiveFileGetter(args[0]);
		rfg.setNameRegex(".+\\.zip");
		rfg.collect();
		List<File> zipFiles = rfg.getFiles();
		
		Unzipper.Entry[] entries;
		Main global = new Main();
		
		Analyzer lyzer = new Analyzer(global);
		int i = 0;
		
		System.out.println("Starting...");
		for(File file : zipFiles)
		{
			if(i % PRINT_STEP == 0)
				System.out.print('.');
			
//			try
//			{
//				Unzipper2 unzipper = new Unzipper2(file.getPath());
				
//				entries = unzipper.getEntries();
				entries = Unzipper.getContents(file.getPath(), false);
				
				lyzer.setZipFile(file);
				lyzer.setEntries(entries);
				lyzer.check();
//			}
//			catch(FileNotFoundException e)
//			{
//				System.err.println(file.getPath() + " not found!");
//			}
			
			++i;
		}
		System.out.println("Finished.");
	}
	
}
