package org.xacml4j.v30.spi.function;

/*
 * #%L
 * Artagon XACML 3.0 Core Engine Implementation
 * %%
 * Copyright (C) 2009 - 2014 Artagon
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.xacml4j.util.CglibInvocationFactory;
import org.xacml4j.util.DefaultInvocationFactory;
import org.xacml4j.util.InvocationFactory;
import org.xacml4j.util.Reflections;
import org.xacml4j.v30.XacmlSyntaxException;
import org.xacml4j.v30.pdp.FunctionSpec;

import com.google.common.base.Preconditions;

public final class AnnotiationBasedFunctionProvider extends BaseFunctionProvider
{
	private JavaMethodToFunctionSpecConverter converter;
	
	public AnnotiationBasedFunctionProvider(
			Class<?> factoryClass,
			InvocationFactory invocationFactory)
		throws Exception
	{
		Preconditions.checkNotNull(factoryClass);
		Preconditions.checkNotNull(invocationFactory);
		this.converter = new JavaMethodToFunctionSpecConverter(invocationFactory);
		List<FunctionSpec> functions = findFunctions(factoryClass, null);
		for(FunctionSpec spec : functions){
			add(spec);
		}
	}

	public AnnotiationBasedFunctionProvider(Class<?> clazz)
		throws Exception{
		this(clazz, new CglibInvocationFactory());
	}
	
	public AnnotiationBasedFunctionProvider(
			Object instance,
			InvocationFactory invocationFactory)
		throws Exception
	{
		Preconditions.checkNotNull(instance);
		Preconditions.checkNotNull(invocationFactory);
		this.converter = new JavaMethodToFunctionSpecConverter(invocationFactory);
		List<FunctionSpec> functions = findFunctions(instance.getClass(), instance);
		for(FunctionSpec spec : functions){
			add(spec);
		}
	}

	public AnnotiationBasedFunctionProvider(Object instance) throws Exception{
		this(instance, new DefaultInvocationFactory());
	}

	private List<FunctionSpec> findFunctions(Class<?> clazz, Object instance)
		throws XacmlSyntaxException
	{
		Preconditions.checkArgument(clazz.getAnnotation(XacmlFunctionProvider.class) != null,
				"Function provider=\"%s\" must have provider annotiation", clazz.getName());
		List<FunctionSpec> specs = new LinkedList<FunctionSpec>();
		List<Method> methods  = Reflections.getAnnotatedMethods(clazz, XacmlFuncSpec.class);
		for(final Method m : methods){
			specs.add(converter.createFunctionSpec(m, instance));
		}
		return specs;
	}
}
