package com.artagon.xacml.v3.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.easymock.EasyMock.*;

import org.junit.Before;
import org.junit.Test;

import com.artagon.xacml.v3.policy.type.DataTypes;

public class DefaultApplyTest extends XacmlPolicyTestCase 
{
	private FunctionSpec function;
	
	@Before
	public void init(){
		this.function = createStrictMock(FunctionSpec.class);
	}
	
	@Test
	public void testApplyWithValidFunctionAndValidParameters() throws EvaluationException
	{
		assertTrue(context.isValidateFuncParamAtRuntime());
		Expression[] params = {DataTypes.INTEGER.create(10L), DataTypes.INTEGER.create(11L)};
		expect(function.validateParameters(params)).andReturn(true);
		replay(function);
		Apply apply = new DefaultApply(function, params);
		verify(function);
		reset(function);
		expect(function.invoke(context, params)).andReturn(DataTypes.BOOLEAN.create(Boolean.FALSE));
		replay(function);
		Value v = apply.evaluate(context);
		assertNotNull(v);
		assertEquals(DataTypes.BOOLEAN.create(Boolean.FALSE), v);
		verify(function);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testApplyWithValidFunctionAndInValidParameters() throws EvaluationException
	{
		assertTrue(context.isValidateFuncParamAtRuntime());
		expect(function.validateParameters(DataTypes.INTEGER.create(10L))).andReturn(false);
		replay(function);
		new DefaultApply(function, DataTypes.INTEGER.create(10L));
		verify(function);
	}
}
