package com.ichera.idgamesscanner;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;

public class Main 
{
	public static void main(String[] args)
	{
		File folder = new File(args[0]);
		File[] zipFiles = folder.listFiles(new FileFilter() 
		{
			@Override
			public boolean accept(File pathname) 
			{
				if(!pathname.isFile())
					return false;
				String path = pathname.getPath();
				if(path.length() < 5)
					return false;
				path = path.substring(path.length() - 4);
				return path.equalsIgnoreCase(".zip");
			}
		});
		
		Unzipper.Entry[] entries;
		for(File file : zipFiles)
		{
			entries = Unzipper.getContents(file.getPath());
			System.out.printf("%s\n\t", file.getName());
			for(Unzipper.Entry entry : entries)
				if(Util.getExtension(entry.name).equalsIgnoreCase(".wad"))
					System.out.print(entry.name + " ");
			System.out.print('\n');
			
		}
	}
	
	private static void zipOperation(File file)
	{
		Unzipper uz = null;
		try
		{
			uz = new Unzipper(file.getPath());
			Unzipper.Entry entry;
			// Must have .WAD and .TXT with names matching the path
			String bare = Util.getNameWithoutExtension(file.getName());
			
			boolean textFound = false, wadFound = false;
			StringBuilder builder = new StringBuilder();
			while((entry = uz.getNext()) != null)
			{
				if(entry.name.equalsIgnoreCase(bare + ".wad"))
					wadFound = true;
				else if(entry.name.equalsIgnoreCase(bare + ".txt"))
					textFound = true;
				builder.append(entry.name);
				builder.append(' ');
			}
			if(!wadFound || !textFound)
			{
				System.out.println("Bad .txt/.wad name or not included: " + file.getPath());
				System.out.println("Contents is: " + builder.toString());
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
	}
}
