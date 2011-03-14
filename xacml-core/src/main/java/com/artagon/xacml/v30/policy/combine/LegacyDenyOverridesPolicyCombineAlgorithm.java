package com.artagon.xacml.v30.policy.combine;

import static com.artagon.xacml.v30.spi.combine.DecisionCombingingAlgorithms.evaluateIfApplicable;

import java.util.List;

import com.artagon.xacml.v30.CompositeDecisionRule;
import com.artagon.xacml.v30.Decision;
import com.artagon.xacml.v30.EvaluationContext;
import com.artagon.xacml.v30.spi.combine.BaseDecisionCombiningAlgorithm;
import com.artagon.xacml.v30.spi.combine.XacmlPolicyDecisionCombingingAlgorithm;

public class LegacyDenyOverridesPolicyCombineAlgorithm extends BaseDecisionCombiningAlgorithm<CompositeDecisionRule>
{
	private final static String ID = "urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:deny-overrides";

	public LegacyDenyOverridesPolicyCombineAlgorithm() {
		super(ID);
	}
	
	protected LegacyDenyOverridesPolicyCombineAlgorithm(String algorithmId) {
		super(algorithmId);
	}
	
	public Decision combine(EvaluationContext context, 
			List<CompositeDecisionRule> rules) {
		return doCombine(context, rules);
	}
	@XacmlPolicyDecisionCombingingAlgorithm("urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:deny-overrides")
	public static <D extends CompositeDecisionRule> Decision doCombine(EvaluationContext context, 
			List<D> rules) {
		boolean atLeastOnePermit = false;
		for(CompositeDecisionRule r : rules){
			Decision d = evaluateIfApplicable(context, r);
			if(d == Decision.DENY){
				return d;
			}
			if(d == Decision.PERMIT){
				atLeastOnePermit = true;
				continue;
			}
			if(d == Decision.NOT_APPLICABLE){
				continue;
			}
			if(d.isIndeterminate()){
				return Decision.DENY;
			}
		}
		if(atLeastOnePermit){
			return Decision.PERMIT;
		}
		return Decision.NOT_APPLICABLE;
	}	
}
