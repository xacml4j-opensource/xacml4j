package com.artagon.xacml.v3.policy.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.artagon.xacml.v3.policy.AttributeValue;
import com.artagon.xacml.v3.policy.type.DoubleType.DoubleValue;

public class DoubleTypeTest 
{
	private DoubleType t1;
	
	@Before
	public void init(){
		this.t1 = DataTypes.DOUBLE.getType();
	}
	
	@Test
	public void testCreateDouble()
	{
		assertEquals(DataTypes.DOUBLE.create(1), DataTypes.DOUBLE.create(Short.valueOf("1")));
		assertEquals(DataTypes.DOUBLE.create(1), DataTypes.DOUBLE.create(Byte.valueOf("1")));
		assertEquals(DataTypes.DOUBLE.create(1), DataTypes.DOUBLE.create(Integer.valueOf("1")));
		assertEquals(DataTypes.DOUBLE.create(1), DataTypes.DOUBLE.create(Long.valueOf("1")));
		assertEquals(DataTypes.DOUBLE.create(1), DataTypes.DOUBLE.create(Float.valueOf("1")));
		assertEquals(DataTypes.DOUBLE.create(1), DataTypes.DOUBLE.create(Double.valueOf("1")));
	}
	
	@Test
	public void testToXacmlString()
	{
		AttributeValue v0 = t1.create(1.0d);
		AttributeValue v1 = t1.create(-2.0d);
		assertEquals("1.0", v0.toXacmlString());
		assertEquals("-2.0", v1.toXacmlString());
	}
	
	@Test
	public void testFromAnyObject()
	{
		Object o = 0.2d;
		DoubleValue a = t1.create(o);
		assertEquals(o, a.getValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromAnyObjectWrongContentType()
	{
		Object o = new byte[10];
		t1.create(o);
	}

	
	@Test
	public void testEquals()
	{
		AttributeValue v0 = t1.create(1.0d);
		AttributeValue v1 = t1.create(2.0d);
		AttributeValue v2 = t1.create(1.0d);
		assertFalse(v0.equals(v1));
		assertTrue(v0.equals(v2));
		AttributeValue v0Nan = t1.create(Double.NaN);
		AttributeValue v1Nan = t1.create(Double.NaN);
		assertFalse(v0.equals(v0Nan));
		assertTrue(v0Nan.equals(v0Nan));
		assertFalse(v1Nan.equals(v0));
	}
}
