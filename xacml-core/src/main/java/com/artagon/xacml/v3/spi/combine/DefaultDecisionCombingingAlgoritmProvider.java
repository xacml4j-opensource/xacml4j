package com.artagon.xacml.v3.spi.combine;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.artagon.xacml.v3.policy.CompositeDecisionRule;
import com.artagon.xacml.v3.policy.DecisionCombiningAlgorithm;
import com.artagon.xacml.v3.policy.Rule;
import com.artagon.xacml.v3.spi.DecisionCombiningAlgorithmProvider;

public class DefaultDecisionCombingingAlgoritmProvider implements DecisionCombiningAlgorithmProvider
{
	private Map<String, DecisionCombiningAlgorithm<Rule>> ruleAlgo;
	private Map<String, DecisionCombiningAlgorithm<CompositeDecisionRule>> policyAlgo;
	
	protected DefaultDecisionCombingingAlgoritmProvider(){
		this.ruleAlgo = new ConcurrentHashMap<String, DecisionCombiningAlgorithm<Rule>>();
		this.policyAlgo = new ConcurrentHashMap<String, DecisionCombiningAlgorithm<CompositeDecisionRule>>();
	}
	
	public DefaultDecisionCombingingAlgoritmProvider(
			Collection<DecisionCombiningAlgorithm<Rule>> ruleAlgorithms,
			Collection<DecisionCombiningAlgorithm<CompositeDecisionRule>> policyAlgorithms){
		this.ruleAlgo = new ConcurrentHashMap<String, DecisionCombiningAlgorithm<Rule>>();
		for(DecisionCombiningAlgorithm<Rule> algo : ruleAlgorithms){
			addRuleCombineAlgorithm(algo);
		}
		this.policyAlgo = new ConcurrentHashMap<String, DecisionCombiningAlgorithm<CompositeDecisionRule>>();
		for(DecisionCombiningAlgorithm<CompositeDecisionRule> algo : policyAlgorithms){
			addCompositeRuleCombineAlgorithm(algo);
		}
	}
	
	
	@Override
	public final DecisionCombiningAlgorithm<CompositeDecisionRule> getPolicyAlgorithm(
			String algorithmId) {
		return policyAlgo.get(algorithmId);
	}
	
	@Override
	public final DecisionCombiningAlgorithm<Rule> getRuleAlgorithm(String algorithmId) {
		return ruleAlgo.get(algorithmId);
	}


	public final boolean isRuleAgorithmProvided(String algorithmId){
		return ruleAlgo.containsKey(algorithmId);
	}
	
	public final boolean isPolicyAgorithmProvided(String algorithmId){
		return policyAlgo.containsKey(algorithmId);
	}
	
	public final void addRuleCombineAlgorithm(
			DecisionCombiningAlgorithm<Rule> algorithm)
	{
		DecisionCombiningAlgorithm<Rule> oldAlgo = ruleAlgo.put(algorithm.getId(), algorithm);
		if(oldAlgo != null){
			throw new IllegalArgumentException(
					String.format("Rule algorithm with identifier=\"%\" already exist", algorithm));
		}
	}
	
	public final void addCompositeRuleCombineAlgorithm(
			DecisionCombiningAlgorithm<CompositeDecisionRule> algorithm)
	{
		DecisionCombiningAlgorithm<CompositeDecisionRule> oldAlgo = policyAlgo.put(
				algorithm.getId(), algorithm);
		if(oldAlgo != null){
			throw new IllegalArgumentException(
					String.format("Policy decision combining" +
							" algorithm with identifier=\"%\" already exist", algorithm));
		}
	}
	
	@Override
	public final Set<String> getSupportedPolicyAlgorithms(){
		return Collections.unmodifiableSet(policyAlgo.keySet());
	}

	@Override
	public final Set<String> getSupportedRuleAlgorithms(){
		return Collections.unmodifiableSet(ruleAlgo.keySet());
	}
	
}
