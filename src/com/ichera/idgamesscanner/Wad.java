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
