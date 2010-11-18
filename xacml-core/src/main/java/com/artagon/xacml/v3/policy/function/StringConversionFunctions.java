package com.artagon.xacml.v3.policy.function;

import com.artagon.xacml.v3.sdk.function.XacmlFuncParam;
import com.artagon.xacml.v3.sdk.function.XacmlFuncReturnType;
import com.artagon.xacml.v3.sdk.function.XacmlFuncSpec;
import com.artagon.xacml.v3.sdk.function.XacmlFunctionProvider;
import com.artagon.xacml.v3.types.StringType;
import com.artagon.xacml.v3.types.StringValue;

@XacmlFunctionProvider(description="XACML string conversion functions")
public class StringConversionFunctions 
{
	@XacmlFuncSpec(id="urn:oasis:names:tc:xacml:1.0:function:string-normalize-space")
	@XacmlFuncReturnType(typeId="http://www.w3.org/2001/XMLSchema#string")
	public static StringValue normalizeSpace(
			@XacmlFuncParam(typeId="http://www.w3.org/2001/XMLSchema#string")StringValue v)
	{
		return StringType.STRING.create(v.getValue().trim());
	}
	
	@XacmlFuncSpec(id="urn:oasis:names:tc:xacml:1.0:function:string-normalize-to-lower-case")
	@XacmlFuncReturnType(typeId="http://www.w3.org/2001/XMLSchema#string")
	public static StringValue normalizeToLowerCase(
			@XacmlFuncParam(typeId="http://www.w3.org/2001/XMLSchema#string")StringValue v)
	{
		return StringType.STRING.create(v.getValue().toLowerCase());
	}
	
	@XacmlFuncSpec(id="urn:artagon:names:tc:xacml:1.0:function:string-normalize-to-upper-case")
	@XacmlFuncReturnType(typeId="http://www.w3.org/2001/XMLSchema#string")
	public static StringValue normalizeToUpperCase(
			@XacmlFuncParam(typeId="http://www.w3.org/2001/XMLSchema#string")StringValue v)
	{
		return StringType.STRING.create(v.getValue().toUpperCase());
	}
}
