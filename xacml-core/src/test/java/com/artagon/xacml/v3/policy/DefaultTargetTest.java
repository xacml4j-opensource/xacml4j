package com.artagon.xacml.v3.policy;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class DefaultTargetTest extends XacmlPolicyTestCase
{
	private Collection<MatchAnyOf> matches;
	
	@Before
	public void init(){
		this.matches = new LinkedList<MatchAnyOf>();
	}
	
	@Test
	public void testAllMatch(){
		MatchAnyOf m1 = createStrictMock(MatchAnyOf.class);
		MatchAnyOf m2 = createStrictMock(MatchAnyOf.class);
		matches.add(m1);
		matches.add(m2);
		expect(m1.match(context)).andReturn(MatchResult.MATCH);
		expect(m2.match(context)).andReturn(MatchResult.MATCH);
		replay(m1, m2);
		Target t = new DefaultTarget(matches);
		assertEquals(MatchResult.MATCH, t.match(context));
		verify(m1, m2);
	}
	
	@Test
	public void testEmptyTarget(){
		Target t = new DefaultTarget(matches);
		assertEquals(MatchResult.MATCH, t.match(context));
		t = new DefaultTarget();
		assertEquals(MatchResult.MATCH, t.match(context));
	}
	
		
	@Test
	public void testAtLeastOneNoMatch()
	{
		MatchAnyOf m1 = createStrictMock(MatchAnyOf.class);
		MatchAnyOf m2 = createStrictMock(MatchAnyOf.class);
		MatchAnyOf m3 = createStrictMock(MatchAnyOf.class);
		matches.add(m1);
		matches.add(m2);
		matches.add(m3);
		expect(m1.match(context)).andReturn(MatchResult.MATCH);
		expect(m2.match(context)).andReturn(MatchResult.INDETERMINATE);
		replay(m1, m2, m3);
		Target t = new DefaultTarget(matches);
		assertEquals(MatchResult.INDETERMINATE, t.match(context));
		verify(m1, m2, m3);
	}
	
	@Test
	public void testAllMatchAndIndeterminate()
	{
		MatchAnyOf m1 = createStrictMock(MatchAnyOf.class);
		MatchAnyOf m2 = createStrictMock(MatchAnyOf.class);
		MatchAnyOf m3 = createStrictMock(MatchAnyOf.class);
		matches.add(m1);
		matches.add(m2);
		matches.add(m3);
		expect(m1.match(context)).andReturn(MatchResult.MATCH);
		expect(m2.match(context)).andReturn(MatchResult.INDETERMINATE);
		replay(m1, m2, m3);
		Target t = new DefaultTarget(matches);
		assertEquals(MatchResult.INDETERMINATE, t.match(context));
		verify(m1, m2, m3);
	}
	
	@Test
	public void testFirstIsNoMatch()
	{
		MatchAnyOf m1 = createStrictMock(MatchAnyOf.class);
		MatchAnyOf m2 = createStrictMock(MatchAnyOf.class);
		MatchAnyOf m3 = createStrictMock(MatchAnyOf.class);
		matches.add(m1);
		matches.add(m2);
		matches.add(m3);
		expect(m1.match(context)).andReturn(MatchResult.NOMATCH);
		replay(m1, m2, m3);
		Target t = new DefaultTarget(matches);
		assertEquals(MatchResult.NOMATCH, t.match(context));
		verify(m1, m2, m3);
	}
	
}
