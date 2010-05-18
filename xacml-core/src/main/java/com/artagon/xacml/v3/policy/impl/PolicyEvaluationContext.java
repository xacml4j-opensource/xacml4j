package com.artagon.xacml.v3.policy.impl;

import com.artagon.xacml.v3.policy.ContextHandler;
import com.artagon.xacml.v3.policy.Policy;
import com.artagon.xacml.v3.policy.PolicyDefaults;
import com.artagon.xacml.v3.policy.PolicyReferenceResolver;
import com.artagon.xacml.v3.policy.XPathVersion;
import com.artagon.xacml.v3.policy.spi.XPathProvider;
import com.google.common.base.Preconditions;

final class PolicyEvaluationContext extends BaseEvaluationContext
{	
	private Policy policy;
	private XPathVersion xpathVersion;
	
	/**
	 * Creates policy evaluation context without parent evaluation context
	 * @param policy a target policy
	 * @param service an attribute resolver
	 * @param policyResolver a policy resolver
	 */
	PolicyEvaluationContext(
			Policy policy, 
			ContextHandler service, 
			XPathProvider xpathProvider,
			PolicyReferenceResolver policyResolver)
	{
		super(service, xpathProvider, policyResolver);
		Preconditions.checkNotNull(policy);
		this.policy = policy;
		this.xpathVersion = XPathVersion.XPATH1;
	}
	
	@Override
	public Policy getCurrentPolicy() {
		return policy;
	}
	
	@Override
	public XPathVersion getXPathVersion() {
		PolicyDefaults defaults = policy.getDefaults();
		return (defaults == null)?xpathVersion:((
				defaults.getXPathVersion() == null)?
						xpathVersion:defaults.getXPathVersion());
	}

}
