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

import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

public class Md5Key 
{
	private byte[] m_digest;
	
	public Md5Key(byte[] digest)
	{
		setDigest(digest);
	}
	
	public Md5Key(String hexString)
	{
		if(hexString == null || hexString.length() != 32)
			throw new IllegalArgumentException(
					"Digest string must be 32 characters long");
		m_digest = DatatypeConverter.parseHexBinary(hexString);
	}
	
	@Override
	public String toString()
	{
		return m_digest != null ? DatatypeConverter.printHexBinary(m_digest) : "";
	}
	
	public void setDigest(byte[] digest)
	{
		if(digest == null || digest.length != 16)
			throw new IllegalArgumentException("Digest must be 16 bytes long");
		m_digest = digest;
	}
	
	public byte[] getDigest()
	{
		return m_digest;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		
		if(!(o instanceof Md5Key))
			return false;
		
		Md5Key m = (Md5Key)o;
		
		return m_digest == null ? m.m_digest == null : 
			Arrays.equals(m_digest, m.m_digest);
	}
	
	@Override
	public int hashCode()
	{
		int result = 17;
		result = 31 * result + (m_digest == null ? 0 : Arrays.hashCode(m_digest));
		return result;
	}
}
