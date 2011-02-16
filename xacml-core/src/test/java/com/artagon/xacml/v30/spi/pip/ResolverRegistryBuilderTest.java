package com.artagon.xacml.v30.spi.pip;

import static org.easymock.EasyMock.createControl;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;

public class ResolverRegistryBuilderTest 
{
	private ResolverRegistryBuilder b;
	private ResolverRegistry r;
	private IMocksControl c;
	
	@Before
	public void init(){
		this.c = createControl();
		this.r = c.createMock(ResolverRegistry.class);
		this.b = ResolverRegistryBuilder.builder();
		
	}
	
	@Test
	public void testBuildEmpty(){
		c.replay();
		assertNotNull(b.build());
		c.verify();
	}
	
	@Test
	public void testBuildEmptyWithRegistry(){
		c.replay();
		assertSame(r, b.build(r));
		c.verify();
	}
}
