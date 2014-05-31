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
