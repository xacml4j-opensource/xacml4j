package com.artagon.xacml.v30.pdp;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artagon.xacml.v30.EvaluationContext;
import com.artagon.xacml.v30.MatchResult;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;


public class Target implements PolicyElement
{
	private final static Logger log = LoggerFactory.getLogger(Target.class);
	
	private Collection<MatchAnyOf> matches;
	
	private int hashCode;
	
	/**
	 * Creates target with a collection
	 * of {@link MatchAnyOf}
	 * 
	 * @param matches a collection of {@link MatchAnyOf}
	 */
	public Target(Collection<MatchAnyOf> matches){
		this.matches = ImmutableList.copyOf(matches);
		this.hashCode = Objects.hashCode(matches);
	}
	
	public Target(){
		this(Collections.<MatchAnyOf>emptyList());
	}
	
	public Collection<MatchAnyOf> getAnyOf(){
		return Collections.unmodifiableCollection(matches);
	}
	
	public static Builder builder(){
		return new Builder();
	}
	
	/**
	 * Evaluates a target against given {@link EvaluationContext}.
	 * During target evaluation {@link AttributeDesignator} or
	 * {@link AttributeSelector} are resolved only from request
	 * context
	 * 
	 * @param context an evaluation context
	 * @return {@link MatchResult}
	 */
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
		if(log.isDebugEnabled()){
			log.debug("Target " +
					"match state=\"{}\"", state);
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
	
	@Override
	public int hashCode(){
		return hashCode;
	}
	
	public boolean equals(Object o){
		if(o == this){
			return false;
		}
		if(o == null){
			return false;
		}
		Target t = (Target)o;
		return matches.equals(t.matches);
	}
	
	@Override
	public String toString(){
		return Objects
				.toStringHelper(this)
				.add("matches", matches)
				.toString();
	}
	
	
	public static class Builder
	{
		private Collection<MatchAnyOf> allAnyOf = new LinkedList<MatchAnyOf>();
		
		private Builder(){
		}
		
		public Builder withAnyOf(MatchAnyOf anyOf){
			Preconditions.checkNotNull(anyOf);
			allAnyOf.add(anyOf);
			return this;
		}
		
		public Builder withAnyOf(MatchAnyOf.Builder anyOf){
			Preconditions.checkNotNull(anyOf);
			allAnyOf.add(anyOf.create());
			return this;
		}
		
		/**
		 * Adds {@link MatchAnyOf} created from a given
		 * array of {@link MatchAllOf} instances
		 * @param allOfs a matches
		 */
		public Builder withAnyOf(MatchAllOf ...allOfs){
			Preconditions.checkNotNull(allOfs);
			allAnyOf.add(new MatchAnyOf(allOfs));
			return this;
		}
		
		public Target build(){
			return new Target(allAnyOf);
		}
		
	}
}
