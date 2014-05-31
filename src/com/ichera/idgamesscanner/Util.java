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
	
	public static int readInt32(byte[] buffer, int offset)
	{
		return (buffer[offset] & 0xff) + 
				((buffer[offset + 1] & 0xff) << 8) +
				((buffer[offset + 2] & 0xff) << 16) +
				((buffer[offset + 3] & 0xff) << 24);
	}
}
