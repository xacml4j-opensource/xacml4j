package com.artagon.xacml.v3.policy.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artagon.xacml.v3.policy.EvaluationContext;
import com.artagon.xacml.v3.policy.MatchAnyOf;
import com.artagon.xacml.v3.policy.MatchResult;
import com.artagon.xacml.v3.policy.Matchable;
import com.artagon.xacml.v3.policy.PolicyVisitor;
import com.artagon.xacml.v3.policy.Target;

final class DefaultTarget implements Target
{
	private final static Logger log = LoggerFactory.getLogger(DefaultTarget.class);
	
	private Collection<Matchable> matches;
	
	public DefaultTarget(Collection<MatchAnyOf> matches){
		this.matches = new LinkedList<Matchable>(matches);
	}
	
	public DefaultTarget(){
		this(Collections.<MatchAnyOf>emptyList());
	}
	
	public MatchResult match(EvaluationContext context) 
	{
		MatchResult state = MatchResult.MATCH;
		for(Matchable m : matches){
			MatchResult r = m.match(context);
			if(r == MatchResult.NOMATCH){
				state = r;
				log.debug("Found AnyOf with match result=\"{}\", " +
						"no need to evaluate target further", r);
				break;
			}
			if(r == MatchResult.INDETERMINATE){
				state = r;
				log.debug("Found AnyOf with match result=\"{}\", " +
						"no need to evaluate target further", r);
				break;
			}
		}
		return state;
	}

	@Override
	public void accept(PolicyVisitor v) {
		v.visitEnter(this);
		for(Matchable m : matches){
			m.accept(v);
		}
		v.visitLeave(this);
	}
		
}
