package com.artagon.xacml.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.artagon.xacml.policy.function.XacmlFunction;
import com.artagon.xacml.policy.type.BooleanType;
import com.artagon.xacml.policy.type.XacmlDataType;
import com.artagon.xacml.policy.type.IntegerType;

public class ApplyTest extends XacmlPolicyTestCase 
{
	private FunctionSpec function;
	private IntegerType paramType;
	private BooleanType booleanType;
	
	@Before
	public void init(){
		this.paramType = XacmlDataType.INTEGER.getType();
		this.booleanType = XacmlDataType.BOOLEAN.getType();


		ExplicitFunctionSpecBuilder b = new ExplicitFunctionSpecBuilder(XacmlFunction.INTEGER_EQUAL);
		b.withParam(paramType).withParam(paramType);
		this.function = b.build(new MockFunctionImplementation(booleanType.create(Boolean.FALSE)));
	}
	
	@Test
	public void testApplyWithValidFunctionAndValidParameters() throws PolicyEvaluationException
	{
		Apply apply = function.createApply(paramType.create(10L), paramType.create(11L));
		Value v = apply.evaluate(context);
		assertNotNull(v);
		assertEquals(booleanType.create(Boolean.FALSE), v);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testApplyWithValidFunctionAndInValidParameters() throws PolicyEvaluationException
	{
		Apply apply = function.createApply(paramType.create(10L));
		apply.evaluate(context);
	}
}
