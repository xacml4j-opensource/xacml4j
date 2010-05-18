package com.artagon.xacml.v3.policy.impl;

import com.artagon.xacml.v3.policy.CompositeDecisionRule;
import com.artagon.xacml.v3.policy.DecisionCombiningAlgorithm;
import com.artagon.xacml.v3.policy.FunctionSpec;
import com.artagon.xacml.v3.policy.PolicyFactory;
import com.artagon.xacml.v3.policy.PolicySyntaxException;
import com.artagon.xacml.v3.policy.Rule;
import com.artagon.xacml.v3.policy.spi.DecisionCombiningAlgorithmProvider;
import com.artagon.xacml.v3.policy.spi.FunctionProvidersRegistry;
import com.google.common.base.Preconditions;

public abstract class BasePolicyFactory implements PolicyFactory {
	private FunctionProvidersRegistry functions;
	private DecisionCombiningAlgorithmProvider combingingAlgorithms;

	public BasePolicyFactory(FunctionProvidersRegistry functions,
			DecisionCombiningAlgorithmProvider combiningAlgorithms) {
		Preconditions.checkNotNull(functions);
		Preconditions.checkNotNull(combiningAlgorithms);
		this.functions = functions;
		this.combingingAlgorithms = combiningAlgorithms;
	}

	protected final FunctionSpec createFunction(String functionId)
			throws PolicySyntaxException 
	{
		FunctionSpec spec = functions.getFunction(functionId);
		if (spec == null) {
			throw new PolicySyntaxException(
					"Function with id=\"%s\" can not be resolved", functionId);
		}
		return spec;
	}

	protected final DecisionCombiningAlgorithm<Rule> createRuleCombingingAlgorithm(
			String algorithmId) throws PolicySyntaxException {
		DecisionCombiningAlgorithm<Rule> algorithm = combingingAlgorithms
				.getRuleAlgorithm(algorithmId);
		if (algorithm == null) {
			throw new PolicySyntaxException(
					"Rile comnbining algorithm=\"%s\" can not be resolved",
					algorithmId);
		}
		return algorithm;
	}

	protected final DecisionCombiningAlgorithm<? extends CompositeDecisionRule> createPolicyCombingingAlgorithm(
			String algorithmId) throws PolicySyntaxException {
		DecisionCombiningAlgorithm<? extends CompositeDecisionRule> algorithm = combingingAlgorithms
				.getPolicyAlgorithm(algorithmId);
		if (algorithm == null) {
			throw new PolicySyntaxException(
					"Policy comnbining algorithm=\"%s\" can not be resolved",
					algorithmId);
		}
		return algorithm;
	}

}
