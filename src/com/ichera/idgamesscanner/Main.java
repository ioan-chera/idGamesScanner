package com.ichera.idgamesscanner;

import java.io.File;
import java.util.List;

public class Main 
{
	private static final int PRINT_STEP = 1024;
	
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
		Analyzer lyzer = new Analyzer();
		int i = 0;
		
		System.out.println("Starting...");
		for(File file : zipFiles)
		{
			if(i % PRINT_STEP == 0)
				System.out.print('.');
			
			entries = Unzipper.getContents(file.getPath(), true);
						
			lyzer.setZipFile(file);
			lyzer.setEntries(entries);
			lyzer.check();
			
			++i;
		}
		System.out.println("Finished.");
	}
	
}
