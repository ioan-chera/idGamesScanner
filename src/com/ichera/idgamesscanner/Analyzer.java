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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyzer 
{
	private static final long	SUSPICIOUS_SIZE = 100000000;	// 100M
	
	private File				m_zipFile;
	private Unzipper.Entry[]	m_entries;
	
	private final Main			m_global;
	
	private static final Set<Md5Key> s_commonMd5 = new HashSet<Md5Key>();
	
	static
	{
		// Dehacked as in idgames/levels/doom/s-u/sr388b1.zip
		s_commonMd5.add(new Md5Key("0c17f541b5b246081a79a9dd4bc17508"));
		// Dehacked as in idgames/levels/doom/d-f/drtyhary.zip
		s_commonMd5.add(new Md5Key("e531ee4c5e2f42aa3b7b23f992cf525f"));
		// Dehacked as in idgames/levels/doom2/deathmatch/g-i/hcdm.zip
		s_commonMd5.add(new Md5Key("6cc9e0b0cf981365a8a14a33d18d4514"));
		// Dehacked as in idgames/levels/doom/g-i/hulkdoom.zip
		s_commonMd5.add(new Md5Key("dc7a40bf6865e5951d78fdc2e201f77e"));
		// Dehacked as in idgames/levels/doom/megawads/inva_19.zip
		s_commonMd5.add(new Md5Key("7f4b6dde1aad5565d38cdd805d72af3b"));
		// Dehacked as in idgames/levels/doom/s-u/ultradm.zip
		s_commonMd5.add(new Md5Key("aaf348b7079fa437dccfb725bdf992a5"));
		// Dehacked as in idgames/levels/doom/s-u/urban.zip
		s_commonMd5.add(new Md5Key("a0a810ee87ad3d9b5d1be0e33a1d99b9"));
		// Dehacked as in idgames/levels/doom/s_u/uac_trn.zip
		s_commonMd5.add(new Md5Key("50e3e155d7e6854adc24bbcd614971cb"));
		// Dehacked as in idgames/levels/doom/p-r/quest12.zip
		s_commonMd5.add(new Md5Key("d1e0ac9bf0e2c347f4b1b503db4f2f31"));
		
		// DeuSF as in idgames/levels/doom/megawads/inva_19.zip
		s_commonMd5.add(new Md5Key("f681f3ccdaf48cc4e66a7a2a0a9567bf"));
		// DeuSF as in idgames/levels/hexen/d-f/fury_ch2.zip
		s_commonMd5.add(new Md5Key("d3cf544b57f49a8bbc01eef084ce929c"));
		// DeuSF as in idgames/levels/doom/d-f/drtyhary.zip
		s_commonMd5.add(new Md5Key("89be00455dec01df32e1df8a6bf191f9"));
		// DeuSF as in idgames/levels/doom/d-f/disgrun5.zip
		s_commonMd5.add(new Md5Key("1588d55f30c6622c37f7a867b6aaaaa5"));
		// DeuSF as in idgames/levels/heretic/0-9/7thsign2.zip
		s_commonMd5.add(new Md5Key("06be303acbae938be316281a4733b798"));
		// DeuSF as in idgames/levels/doom/s-u/toondm.zip
		s_commonMd5.add(new Md5Key("f1a1a64d56b0df3bcd607db8494fc00a"));
		// DeuSF as in idgames/levels/doom/p-r/quest12.zip
		s_commonMd5.add(new Md5Key("271df1bcbc9a6cfaeb00bb04e988d927"));
		// DeuSF as in idgames/levels/doom/g_i/hulkdoom.zip
		s_commonMd5.add(new Md5Key("e8c334616482f9029960691e76371a30"));
		// DeuSF as in idgames/levels/doom/s_u/tmall.zip
		s_commonMd5.add(new Md5Key("aeb2fd69396272f591c3936d91fc60ae"));
		// DeuSF as in idgames/levels/doom2/deathmatch/j-l/j2doom.zip
		s_commonMd5.add(new Md5Key("28e8176a053f9d3c17dc2997306eabaf"));
		// DeuSF as in idgames/levels/doom2/megawads/doom2099.zip
		s_commonMd5.add(new Md5Key("c68026bdc5e7a30619148547e44e76d1"));
				
		// Dmadds11 as in idgames/levels/doom/deathmatch/s-u/tlr.zip
		s_commonMd5.add(new Md5Key("4ff42528232c08b8fb81f4a98b9abaf4"));
		
		// Dmaud as in idgames/levels/doom/a-c/ambient.zip
		s_commonMd5.add(new Md5Key("51099ebba4414fb5977e438491ff8bab"));
		// Dmaud as in idgames/levels/doom/deathmatch/a-c/citywar.zip
		s_commonMd5.add(new Md5Key("86b0a8288ade94cde37a33278adeb96f"));
		
		// Dmgraph as in idgames/levels/doom/deathmatch/d-f/frozen.zip
		s_commonMd5.add(new Md5Key("7fda7103b42721e082874803c723ab18"));
		// Dmgraph as in idgames/levels/doom/deathmatch/v-z/zrat.zip
		s_commonMd5.add(new Md5Key("39eb02fa7a2a7f59518acfc395fd5e9b"));
		
		// HHE as in idgames/levels/heretic/p-r/redkey.zip
		s_commonMd5.add(new Md5Key("65433871fda033afbc6b743e1ab80e70"));
		
		// NWT as in idgames/levels/doom/a-c/basilica.zip
		s_commonMd5.add(new Md5Key("7495992951f3f650b58d7a2af891ecfc"));
		// NWT as in idgames/levels/doom2/d-f/edna.zip
		s_commonMd5.add(new Md5Key("269fc7cc589ee65a5779756cf14c6d96"));
		
		// Stuffbuf as in idgames/levels/doom2/s-u/try_me.zip
		s_commonMd5.add(new Md5Key("68a63ad3e4954bca156bfad45592ea0d"));
		
		
	}
	
	Analyzer(Main global)
	{
		m_global = global;
	}
	
	void setZipFile(File zipFile)
	{
		m_zipFile = zipFile;
	}
	
	void setEntries(Unzipper.Entry[] entries)
	{
		m_entries = entries;
	}
	
	void check()
	{
		checkSuspiciousSize();
		checkMissingWad();
		checkDuplicateEntries();
		harvestEmails();
	}
	
	private void checkSuspiciousSize()
	{
		long expandedSize = 0;
		for(Unzipper.Entry entry : m_entries)
		{
			if(entry.getContent() != null)
				expandedSize += entry.getContent().length;
		}
		long zipSize = m_zipFile.length();
		if(expandedSize >= SUSPICIOUS_SIZE && expandedSize >= 100 * zipSize)
			warn("Expanded size too large (" + expandedSize + ") from zip of size " + zipSize);
	}
	
	private void checkMissingWad()
	{
		boolean foundWad = false;
		String nonstandard = null;
		for(Unzipper.Entry entry : m_entries)
		{
			if(Util.hasExtension(entry.getName(), "wad") || 
					Util.hasExtension(entry.getName(), "pk3") ||
					Util.hasExtension(entry.getName(), "pk7") || 
					Util.hasExtension(entry.getName(), "pke"))
			{
				foundWad = true;
				break;
			}
			else if(entry.getContent() != null)
			{
				try
				{
					new Wad(entry.getContent());
					nonstandard = entry.getName();
				}
				catch(ParseException e)
				{
				}
			}
		}
		if(!foundWad)
		{
			if(nonstandard != null && nonstandard.length() > 0)
				info("Nonstandard wad extension: " + nonstandard);
			else
				warn("Missing .wad file!");
		}
	}
	
	private void checkDuplicateEntries()
	{
		try
		{
			for(Unzipper.Entry entry : m_entries)
			{
				if(entry.getContent() == null || entry.getContent().length == 0)
					continue;
				Md5Key key = 
						new Md5Key(
								MessageDigest.getInstance("MD5")
								.digest(entry.getContent()));
				
				if(s_commonMd5.contains(key))	// skip common utility files
					continue;
				
				if(m_global.getMd5Map().containsKey(key) && 
						!m_global.getMd5Map().get(key).equals(m_zipFile.getPath()))
				{
					warn("File " + entry.getName() + " same as in " + 
							m_global.getMd5Map().get(key));
				}
				else
					m_global.getMd5Map().put(key, m_zipFile.getPath());
			}
		}
		catch(NoSuchAlgorithmException e)
		{
			System.err.println("Internal error on MD5 calculator");
		}
	}
	
	private void warn(String s)
	{
		System.out.println("WARNING: " + m_zipFile + ": " + s);
	}
	
	private void info(String s)
	{
		System.out.println("INFO: " + m_zipFile + ": " + s);
	}
	
	private void harvestEmails()
	{
		final Pattern pattern = Pattern.compile("[\\.a-zA-Z0-9\\-]+@[\\.a-zA-Z0-9\\-]+");

		for(Unzipper.Entry entry : m_entries)
		{
			if(entry.getContent() != null && 
					(Util.hasExtension(entry.getName(), "txt") ||
					Util.hasExtension(entry.getName(), "doc")))
			{
				// assume text file
				try
				{
					String string = new String(entry.getContent(), "UTF-8");
					Matcher m = pattern.matcher(string);
					while(m.find())
					{
						String s = m.group();
						m_global.getEmailSet().add(s);
					}
				}
				catch(UnsupportedEncodingException e)
				{
					System.err.println("Internal error");
				}
			}
		}
	}
}
