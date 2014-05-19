package org.xacml4j.v30.policy.function;

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

import static org.easymock.EasyMock.createControl;
import static org.easymock.EasyMock.expect;

import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Node;
import org.xacml4j.util.DOMUtil;
import org.xacml4j.v30.Categories;
import org.xacml4j.v30.EvaluationContext;
import org.xacml4j.v30.pdp.FunctionSpec;
import org.xacml4j.v30.spi.function.AnnotiationBasedFunctionProvider;
import org.xacml4j.v30.spi.function.FunctionProvider;
import org.xacml4j.v30.spi.xpath.XPathProvider;
import org.xacml4j.v30.types.AnyURIExp;
import org.xacml4j.v30.types.BooleanExp;
import org.xacml4j.v30.types.XacmlTypes;

public class AttributeDesignatorFunctionTest 
{
	
	
	private String testXml = "<md:record xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
			"xmlns:md=\"urn:example:med:schemas:record\">" +
			"<md:patient>" +
			"<md:patientDoB>1992-03-21</md:patientDoB>" +
			"<md:patient-number>555555</md:patient-number>" +
			"</md:patient>" +
			"</md:record>";

	private EvaluationContext context;
	private XPathProvider xpathProvider;
	private FunctionProvider provider;
	private IMocksControl c;
	private Node content;
	
	@Before
	public void init() throws Exception{
		this.c = createControl();
		this.context = c.createMock(EvaluationContext.class);
		this.content = DOMUtil.stringToNode(testXml);
		this.provider = new AnnotiationBasedFunctionProvider(AttributeDesignatorFunctions.class);
	}
	
	@Test
	@Ignore
	public void testDesignatorFunction(){
		FunctionSpec func = provider.getFunction("urn:oasis:names:tc:xacml:3.0:function:attribute-designator");
	 
		expect(context.isValidateFuncParamsAtRuntime()).andReturn(false);
		c.replay();
		func.invoke(context, 
				AnyURIExp.valueOf(Categories.SUBJECT_ACCESS.getId()),
				AnyURIExp.valueOf("testId"),
				AnyURIExp.valueOf(XacmlTypes.STRING.getDataTypeId()),
				BooleanExp.valueOf(false),
				null);
		c.verify();
	}
}
