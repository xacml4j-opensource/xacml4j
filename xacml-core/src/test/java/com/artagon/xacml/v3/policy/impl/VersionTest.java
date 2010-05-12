package com.artagon.xacml.v3.policy.impl;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.artagon.xacml.v3.Version;

public class VersionTest 
{
	@Test
	public void testCreateVersion()
	{
		Version v1 = Version.valueOf("1.0");
		Version v2 = Version.valueOf("1.0");
		assertTrue(v1.equals(v1));
		assertTrue(v1.equals(v2));
		assertFalse(v1.equals(null));
		assertFalse(v1.equals("1.0"));
	}
	
	@Test
	public void testLessThanVersion()
	{
		Version v1 = Version.valueOf("1.1");
		Version v2 = Version.valueOf("1.0");
		Version v3 = Version.valueOf("1.0.1");
		Version v4 = Version.valueOf("1.0.0");
		assertTrue(v1.compareTo(v2) > 0);
		assertTrue(v3.compareTo(v1) < 0);
		assertTrue(v3.compareTo(v2) > 0);
		assertTrue(v2.compareTo(v4) == 0);
		assertTrue(v2.compareTo(v2) == 0);
		assertTrue(v4.compareTo(v2) == 0);
	}
}
