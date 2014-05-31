package com.ichera.idgamesscanner;

import java.io.Closeable;
import java.io.IOException;

public class Util 
{
	/**
	 * Close a stream while containing its exception
	 * @param item
	 */
	public static void close(Closeable item)
	{
		try
		{
			item.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static String getNameWithoutExtension(String name)
	{
		int index = name.indexOf('.');
		if(index == -1)
			return name;
		return name.substring(0, index);
	}
	
	public static String getExtension(String name)
	{
		int index = name.indexOf('.');
		if(index == -1)
			return "";
		return name.substring(index);
	}
	
	public static boolean hasExtension(String name, String extension)
	{
		return name.toLowerCase().matches(".*\\." + extension);
	}
}
