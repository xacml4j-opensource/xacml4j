package com.artagon.xacml.v30.pdp.profiles;

import java.util.Collection;
import java.util.Collections;

import com.artagon.xacml.v30.Advice;
import com.artagon.xacml.v30.Obligation;
import com.artagon.xacml.v30.RequestContext;
import com.artagon.xacml.v30.Result;
import com.artagon.xacml.v30.Status;
import com.artagon.xacml.v30.spi.pdp.RequestContextHandlerChain;


public class MultipleResourcesHandler extends RequestContextHandlerChain
{	
	public MultipleResourcesHandler()
	{
		super(new MultipleResourcesViaRequestReferencesHandler(), 
				new MultipleResourcesViaRepeatingAttributesHandler(),
				new MultipleResourcesViaXPathExpressionLegacyHandler(),
				new MultipleResourcesViaXPathExpressionHandler());
	}

	@Override
	protected Collection<Result> postProcessResults(RequestContext req,
			Collection<Result> results) {
		if(!req.isCombinedDecision() || 
				results.size() == 1){
			return results;
		}
		Result prev = null;
		for(Result r : results)
		{
			Collection<Advice> advice = r.getAssociatedAdvice();
			if(!advice.isEmpty()){
				return Collections.singleton(
						Result.createIndeterminate(
								Status.createProcessingError()));
			}
			Collection<Obligation> obligation = r.getObligations();
			if(!obligation.isEmpty()){
				return Collections.singleton(
						Result.createIndeterminate(
								Status.createProcessingError()));
			}
			if(prev != null){
				if(r.getDecision().isIndeterminate()){
					if(!prev.getDecision().isIndeterminate()){
						return Collections.singleton(
								Result.createIndeterminate(
										Status.createProcessingError()));
					}
				}
				if(!r.getDecision().equals(prev.getDecision())){
					return Collections.singleton(
							Result.createIndeterminate(
									Status.createProcessingError()));
				}
			}
			prev = r;
		}
		if(prev == null){
			return Collections.singleton(
					Result.createIndeterminate(
							Status.createProcessingError()));
		}
		return Collections.singleton(new Result(
				prev.getDecision(), Status.createSuccess()));
	}
}
