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
