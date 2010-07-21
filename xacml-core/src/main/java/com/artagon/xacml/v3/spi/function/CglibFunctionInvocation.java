package com.artagon.xacml.v3.spi.function;

import java.lang.reflect.Method;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import com.artagon.xacml.v3.ValueExpression;
import com.google.common.base.Preconditions;

public class CglibFunctionInvocation extends BaseReflectionFunctionInvocation 
{
	private Object instance;
	private FastClass fastClass;
	private FastMethod fastMethod;
	
	public CglibFunctionInvocation(
			Method m, 
			boolean evalContextRequired)
	{
		this(m, null ,evalContextRequired);
	}
	
	public CglibFunctionInvocation(
			Method m,
			Object instance,
			boolean evalContextRequired)
	{
		super(evalContextRequired);
		Preconditions.checkNotNull(m);
		this.fastClass = FastClass.create(m.getDeclaringClass());
		this.fastMethod = fastClass.getMethod(m);
		this.instance = instance;
		Preconditions.checkState(fastMethod != null);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected <T extends ValueExpression> T invoke(Object ...params) throws Exception
	{
		return (T)fastMethod.invoke(instance, params);
	}
}
