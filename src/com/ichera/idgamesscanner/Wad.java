package com.ichera.idgamesscanner;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public class Wad 
{
//	private static final int UNSPECIFIED = -1;
//	private static final int IWAD = 0;
//	private static final int PWAD = 1;
	
//	private byte[] 	m_data;
//	private int		m_type = UNSPECIFIED;
//	
//	private int[]	m_filepos;
//	private int[]	m_size;
	
	public Wad(byte[] data) throws ParseException
	{
		try
		{
			if(data.length < 12)
				throw new ParseException("Wad too short", 0);
			String header = new String(data, 0, 4, "UTF-8");
			
//			int type;
			if(header.equals("PWAD"))
			{
//				type = PWAD;
			}
			else if(header.equals("IWAD"))
			{
//				type = IWAD;
			}
			else
			{
//				type = UNSPECIFIED;
				throw new ParseException("Bad wad type", 0);
			}
			
			int numlumps = Util.readInt32(data, 4);
			int infotableofs = Util.readInt32(data, 8);
			
			if((numlumps > 0 && infotableofs + numlumps * 16 > data.length) || 
					(numlumps > 0 && infotableofs < 12) || numlumps < 0)
				throw new ParseException("Bad wad size", 4);
			
			int[] filepos = new int[numlumps];
			int[] size = new int[numlumps];
			
			for(int i = 0; i < numlumps; ++i)
			{
				filepos[i] = Util.readInt32(data, infotableofs + 16 * i);
				size[i] = Util.readInt32(data, infotableofs + 16 * i + 4);
				
				if((size[i] > 0 && filepos[i] + size[i] > data.length) || 
						(size[i] > 0 && filepos[i] < 12) || size[i] < 0)
					throw new ParseException("Bad lump size", 16 * i);
				
			}
			
//			m_data = data;
//			m_type = type;
//			m_filepos = filepos;
//			m_size = size;
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
			throw new ParseException("Internal error", 0);
		}
		catch(OutOfMemoryError e)
		{
			throw new ParseException("Bad lump size", 0);
		}
	}
}
