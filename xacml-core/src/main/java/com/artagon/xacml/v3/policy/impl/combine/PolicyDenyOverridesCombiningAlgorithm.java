package com.artagon.xacml.v3.policy.impl.combine;

import com.artagon.xacml.v3.policy.Policy;

final class PolicyDenyOverridesCombiningAlgorithm extends DenyOverrides<Policy>
{
	public final static String ID = "urn:oasis:names:tc:xacml:3.0:policy-combining-algorithm:deny-overrides";
	
	public PolicyDenyOverridesCombiningAlgorithm(){
		super(ID);
	}
	
	private PolicyDenyOverridesCombiningAlgorithm(String algorithmId){
		super(algorithmId);
	}
	
	public static PolicyDenyOverridesCombiningAlgorithm getLegacyInstance(){
		return new PolicyDenyOverridesCombiningAlgorithm("urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:deny-overrides");
	}	
}
