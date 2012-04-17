package com.artagon.xacml.v30.pdp;

import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.TimeZone;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.artagon.xacml.v30.AttributeCategory;
import com.artagon.xacml.v30.StatusCode;
import com.artagon.xacml.v30.XPathVersion;
import com.google.common.base.Preconditions;

/**
 * An implementation of {@link EvaluationContext} which
 * delegates all invocation to the given context
 * 
 * @author Giedrius Trumpickas
 */
class DelegatingEvaluationContext implements EvaluationContext
{
	private EvaluationContext delegate;
	
	protected DelegatingEvaluationContext(
			EvaluationContext context){
		Preconditions.checkNotNull(context);
		this.delegate = context;
	}
	
	@Override
	public EvaluationContext getParentContext(){
		return delegate;
	}
	
	@Override
	public boolean isValidateFuncParamsAtRuntime() {
		return delegate.isValidateFuncParamsAtRuntime();
	}
	
	@Override
	public void setValidateFuncParamsAtRuntime(boolean validate){
		delegate.setValidateFuncParamsAtRuntime(validate);
	}

	@Override
	public void addAdvices(Decision d, Iterable<Advice> advices){
		delegate.addAdvices(d, advices);
	}
	
	@Override
	public void addObligations(Decision d, Iterable<Obligation> obligations){
		delegate.addObligations(d, obligations);
	}

	/**
	 * Delegates call to {@link EvaluationContext} instance
	 */
	@Override
	public Policy getCurrentPolicy() {
		return delegate.getCurrentPolicy();
	}
	
	@Override
	public StatusCode getEvaluationStatus() {
		return delegate.getEvaluationStatus();
	}

	@Override
	public void setEvaluationStatus(StatusCode code) {
		delegate.setEvaluationStatus(code);
	}

	@Override
	public void addEvaluatedApplicablePolicy(Policy policy, Decision result) {
		delegate.addEvaluatedApplicablePolicy(policy, result);
	}

	@Override
	public void addEvaluatedApplicablePolicySet(PolicySet policySet, Decision result) {
		delegate.addEvaluatedApplicablePolicySet(policySet, result);
	}

	/**
	 * Delegates call to {@link EvaluationContext} instance
	 */
	@Override
	public PolicySet getCurrentPolicySet() {
		return delegate.getCurrentPolicySet();
	}

	/**
	 * Delegates call to {@link EvaluationContext} instance
	 */
	@Override
	public ValueExpression getVariableEvaluationResult(String variableId) {
		return delegate.getVariableEvaluationResult(variableId);
	}
	
	/**
	 * Delegates call to {@link EvaluationContext} instance
	 */
	@Override
	public void setVariableEvaluationResult(String variableId, ValueExpression value) {
		delegate.setVariableEvaluationResult(variableId, value);
	}
	
	/**
	 * Delegates call to {@link EvaluationContext} instance
	 */
	@Override
	public Policy resolve(PolicyIDReference ref) throws PolicyResolutionException 
	{
		return delegate.resolve(ref);
	}

	/**
	 * Delegates call to {@link EvaluationContext} instance
	 */
	@Override
	public PolicySet resolve(PolicySetIDReference ref) throws PolicyResolutionException {
		return delegate.resolve(ref);
	}

	/**
	 * Delegates call to {@link EvaluationContext} instance
	 */
	@Override
	public PolicyIDReference getCurrentPolicyIDReference() {
		return delegate.getCurrentPolicyIDReference();
	}

	/**
	 * Delegates call to {@link EvaluationContext} instance
	 */
	@Override
	public PolicySetIDReference getCurrentPolicySetIDReference() {
		return delegate.getCurrentPolicySetIDReference();
	}

	/**
	 * Delegates call to {@link EvaluationContext} instance
	 */
	@Override
	public TimeZone getTimeZone() {
		return delegate.getTimeZone();
	}
	
	@Override
	public Calendar getCurrentDateTime() {
		return delegate.getCurrentDateTime();
	}
	
	@Override
	public XPathVersion getXPathVersion() {
		return delegate.getXPathVersion();
	}

	/**
	 * Delegates call to {@link EvaluationContext} instance
	 */
	@Override
	public Node evaluateToNode(String path, AttributeCategory categoryId)
			throws EvaluationException {
		return delegate.evaluateToNode(path, categoryId);
	}

	/**
	 * Delegates call to {@link EvaluationContext} instance
	 */
	@Override
	public NodeList evaluateToNodeSet(String path, AttributeCategory categoryId)
			throws EvaluationException {
		return delegate.evaluateToNodeSet(path, categoryId);
	}

	/**
	 * Delegates call to {@link EvaluationContext} instance
	 */
	@Override
	public Number evaluateToNumber(String path, AttributeCategory categoryId)
			throws EvaluationException {
		return delegate.evaluateToNumber(path, categoryId);
	}
	
	/**
	 * Delegates call to {@link EvaluationContext} instance
	 */
	@Override
	public String evaluateToString(String path, AttributeCategory categoryId)
			throws EvaluationException {
		return delegate.evaluateToString(path, categoryId);
	}

	@Override
	public BagOfAttributeExp resolve(
			AttributeDesignatorKey ref)
			throws EvaluationException {
		return delegate.resolve(ref);
	}
	
	@Override
	public BagOfAttributeExp resolve(
			AttributeSelectorKey ref)
			throws EvaluationException {
		return delegate.resolve(ref);
	}

	@Override
	public Collection<CompositeDecisionRuleIDReference> getEvaluatedPolicies() {
		return delegate.getEvaluatedPolicies();
	}

	@Override
	public void setResolvedDesignatorValue(AttributeDesignatorKey ref,
			BagOfAttributeExp v) {
		delegate.setResolvedDesignatorValue(ref, v);
	}

	@Override
	public int getDecisionCacheTTL() {
		return delegate.getDecisionCacheTTL();
	}

	@Override
	public void setDecisionCacheTTL(int ttl) {
		delegate.setDecisionCacheTTL(ttl);
	}
	
	@Override
	public Map<AttributeDesignatorKey, BagOfAttributeExp> getResolvedDesignators() {
		return delegate.getResolvedDesignators();
	}

	public Iterable<Obligation> getMatchingObligations(Decision decision) {
		return delegate.getMatchingObligations(decision);
	}

	public Iterable<Advice> getMatchingAdvices(Decision decision) {
		return delegate.getMatchingAdvices(decision);
	}
}
