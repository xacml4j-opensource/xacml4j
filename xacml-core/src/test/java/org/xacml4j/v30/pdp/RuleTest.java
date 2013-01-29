package org.xacml4j.v30.pdp;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createStrictControl;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.easymock.Capture;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.xacml4j.v30.Advice;
import org.xacml4j.v30.Decision;
import org.xacml4j.v30.DecisionRule;
import org.xacml4j.v30.Effect;
import org.xacml4j.v30.EvaluationContext;
import org.xacml4j.v30.EvaluationException;
import org.xacml4j.v30.Expression;
import org.xacml4j.v30.MatchResult;
import org.xacml4j.v30.Obligation;
import org.xacml4j.v30.StatusCode;
import org.xacml4j.v30.types.StringType;


public class RuleTest
{
	private Rule rulePermit;
	private Rule ruleDeny;
	
	private Policy currentPolicy;
	private Condition condition;
	private Target target;
	
	private EvaluationContext context;
	
	private IMocksControl c;
	
	private Expression denyObligationAttributeExp;
	private Expression permitObligationAttributeExp;
	
	private Expression denyAdviceAttributeExp;
	private Expression permitAdviceAttributeExp;
	
	private Rule.Builder builder;
	
	@Before
	public void init()
	{
		this.c = createStrictControl();
		this.context = c.createMock(EvaluationContext.class);
		this.condition = c.createMock(Condition.class);
		this.target = c.createMock(Target.class);
		this.currentPolicy = c.createMock(Policy.class);
			
	
		this.permitAdviceAttributeExp = c.createMock(Expression.class);
		this.denyAdviceAttributeExp = c.createMock(Expression.class);
		
		this.denyObligationAttributeExp = c.createMock(Expression.class);
		this.permitObligationAttributeExp = c.createMock(Expression.class);
		
		this.builder = Rule
				.builder("TestRuleId", Effect.DENY)
				.target(target)
				.condition(condition)
				.obligation(ObligationExpression
						.builder("denyObligation", Effect.DENY)
							.attribute("testId", denyObligationAttributeExp))
				.obligation(ObligationExpression
						.builder("permitObligation", Effect.PERMIT)
						.attribute("testId", permitObligationAttributeExp))
				.advice(AdviceExpression
						.builder("denyAdvice", Effect.DENY)
						.attribute("testId", denyAdviceAttributeExp))
				.advice(AdviceExpression
					.builder("permitAdvice", Effect.PERMIT)
					.attribute("testId", permitAdviceAttributeExp));
		
		this.rulePermit = builder
				.id("testPermitRule")
				.withEffect(Effect.PERMIT)
				.build();
		
		this.ruleDeny = builder
				.id("testDenyRule")
				.withEffect(Effect.DENY)
				.build();
		
	}
	
	@Test
	public void testDenyRuleIsApplicableWithNoTarget() throws EvaluationException
	{
		DecisionRule ruleDenyNoTarget = builder.withoutTarget().withEffect(Effect.DENY).build();
		EvaluationContext ruleContext = ruleDenyNoTarget.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		c.replay();
		assertEquals(MatchResult.MATCH, ruleDenyNoTarget.isMatch(ruleContext));
		c.verify();
	}
	
	@Test
	public void testPermitRuleIsApplicableWithNoTarget() throws EvaluationException
	{
		DecisionRule rulePermitNoTarget = builder.withoutTarget().withEffect(Effect.PERMIT).build();
		EvaluationContext ruleContext = rulePermitNoTarget.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		c.replay();
		assertEquals(MatchResult.MATCH, rulePermitNoTarget.isMatch(ruleContext));
		c.verify();
	}
	
	@Test
	public void testDenyRuleApplicabilityWithTargetMatch() throws EvaluationException
	{
		EvaluationContext ruleContext = ruleDeny.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(target.match(ruleContext)).andReturn(MatchResult.MATCH);
		c.replay();
		assertEquals(MatchResult.MATCH, ruleDeny.isMatch(ruleContext));
		c.verify();
	}
	
	@Test
	public void testPermitRuleIsApplicableWithTargetIndeterminate() throws EvaluationException
	{
		EvaluationContext ruleContext = rulePermit.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(target.match(ruleContext)).andReturn(MatchResult.INDETERMINATE);
		c.replay();
		assertEquals(MatchResult.INDETERMINATE, rulePermit.isMatch(ruleContext));
		c.verify();
	}
	
	@Test
	public void testDenyRuleIsApplicableWithTargetIndeterminate() throws EvaluationException
	{
		EvaluationContext ruleContext = ruleDeny.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(target.match(ruleContext)).andReturn(MatchResult.INDETERMINATE);
		c.replay();
		assertEquals(MatchResult.INDETERMINATE, ruleDeny.isMatch(ruleContext));
		c.verify();
	}
	
	@Test
	public void testPermitRuleIsApplicableWithTargetMatch() throws EvaluationException
	{
		EvaluationContext ruleContext = rulePermit.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(target.match(ruleContext)).andReturn(MatchResult.MATCH);
		c.replay();
		assertEquals(MatchResult.MATCH, rulePermit.isMatch(ruleContext));
		c.verify();
	}
	
	@Test
	public void testDenyRuleIsApplicableWithTargetNoMatch() throws EvaluationException
	{
		EvaluationContext ruleContext = ruleDeny.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(target.match(ruleContext)).andReturn(MatchResult.NOMATCH);
		c.replay();
		assertEquals(MatchResult.NOMATCH, ruleDeny.isMatch(ruleContext));
		c.verify();		
	}
	
	@Test
	public void testDenyRuleConditionTrue() throws EvaluationException
	{
		EvaluationContext ruleContext = ruleDeny.createContext(context);
		
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(condition.evaluate(ruleContext)).andReturn(ConditionResult.TRUE);
		
		Capture<Collection<Advice>> advices = new Capture<Collection<Advice>>();
		Capture<Collection<Obligation>> obligations = new Capture<Collection<Obligation>>();
		
		expect(denyAdviceAttributeExp.evaluate(ruleContext)).andReturn(
				StringType.STRING.create("testVal1"));
		
		
		expect(denyObligationAttributeExp.evaluate(ruleContext)).andReturn(
				StringType.STRING.create("testVal1"));
		
		Capture<Decision> advicesD = new Capture<Decision>();
		Capture<Decision> obligationsD = new Capture<Decision>();
		
		context.addAdvices(capture(advicesD), capture(advices));
		context.addObligations(capture(obligationsD), capture(obligations));
				
		c.replay();
		
		assertEquals(Decision.DENY, ruleDeny.evaluate(ruleContext));
		
		c.verify();
		
		assertTrue(
				advices.getValue().contains(Advice
						.builder("denyAdvice", Effect.DENY)
						.attribute(
								"testId", 
								StringType.STRING.create("testVal1"))
								.create()));
		
		assertTrue(
				obligations.getValue().contains(Obligation
						.builder("denyObligation", Effect.DENY)
						.attribute(
								"testId", 
								StringType.STRING.create("testVal1"))
								.build()));
		
	}
	
	@Test
	public void testDenyRuleObligationOrAdviceEvaluationFails() throws EvaluationException
	{
		EvaluationContext ruleContext = ruleDeny.createContext(context);
		
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(condition.evaluate(ruleContext)).andReturn(ConditionResult.TRUE);
			
		expect(denyAdviceAttributeExp.evaluate(ruleContext)).andReturn(
				StringType.STRING.create("testVal1"));
		
		expect(denyObligationAttributeExp.evaluate(ruleContext)).andThrow(
				new EvaluationException(StatusCode.createProcessingError(), ruleContext, new NullPointerException()));
		
		c.replay();
		assertEquals(Decision.INDETERMINATE, ruleDeny.evaluate(ruleContext));
		c.verify();
	}
	
	@Test
	public void testDenyRuleConditionFalse() throws EvaluationException
	{
		EvaluationContext ruleContext = ruleDeny.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(condition.evaluate(ruleContext)).andReturn(ConditionResult.FALSE);	
		c.replay();
		
		assertEquals(Decision.NOT_APPLICABLE, ruleDeny.evaluate(ruleContext));
		c.verify();	
	}
	
	@Test
	public void testDenyRuleConditionIndeterminate() throws EvaluationException
	{
		EvaluationContext ruleContext = ruleDeny.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(condition.evaluate(ruleContext)).andReturn(ConditionResult.INDETERMINATE);	
		c.replay();
		assertEquals(Decision.INDETERMINATE_D, ruleDeny.evaluate(ruleContext));
		c.verify();
	}
	
		
	@Test
	public void testPermitRuleConditionTrue() throws EvaluationException
	{
		EvaluationContext ruleContext = rulePermit.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(condition.evaluate(ruleContext)).andReturn(ConditionResult.TRUE);
		
		Capture<Collection<Advice>> advices = new Capture<Collection<Advice>>();
		Capture<Collection<Obligation>> obligations = new Capture<Collection<Obligation>>();
		
		expect(permitAdviceAttributeExp.evaluate(context)).andReturn(StringType.STRING.create("testVal1"));
		expect(permitObligationAttributeExp.evaluate(context)).andReturn(StringType.STRING.create("testVal1"));
		
		Capture<Decision> advicesD = new Capture<Decision>();
		Capture<Decision> obligationsD = new Capture<Decision>();
		
		context.addAdvices(capture(advicesD), capture(advices));
		context.addObligations(capture(obligationsD), capture(obligations));
		
		c.replay();
		assertEquals(Decision.PERMIT, rulePermit.evaluate(ruleContext));
		c.verify();
		
		assertTrue(
				advices.getValue().contains(Advice
						.builder("permitAdvice", Effect.PERMIT)
						.attribute(
								"testId", 
								StringType.STRING.create("testVal1"))
								.create()));
		
		assertTrue(
				obligations.getValue().contains(Obligation
						.builder("permitObligation", Effect.PERMIT)
						.attribute(
								"testId", 
								StringType.STRING.create("testVal1"))
								.build()));
	}
	
	@Test
	public void testPermitRuleConditionFalse() throws EvaluationException
	{
		EvaluationContext ruleContext = rulePermit.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(condition.evaluate(ruleContext)).andReturn(ConditionResult.FALSE);	
		c.replay();
		
		assertEquals(Decision.NOT_APPLICABLE, ruleDeny.evaluate(ruleContext));
		c.verify();
	}
	
	@Test
	public void testPermitRuleConditionIndeterminate() throws EvaluationException
	{
		EvaluationContext ruleContext = rulePermit.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(condition.evaluate(ruleContext)).andReturn(ConditionResult.INDETERMINATE);	
		c.replay();
		assertEquals(Decision.INDETERMINATE_D, ruleDeny.evaluate(ruleContext));
		c.verify();	
	}

	@Test
	public void testDenyRuleEvaluateIfApplicableWithTargetIndeterminate() 
		throws EvaluationException
	{
		EvaluationContext ruleContext = ruleDeny.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(target.match(ruleContext)).andReturn(MatchResult.INDETERMINATE);
		c.replay();
		assertEquals(Decision.INDETERMINATE_D, ruleDeny.evaluateIfMatch(ruleContext));
		c.verify();
	}
	
	@Test
	public void testDenyRuleEvaluateIfApplicableWithTargetNoMatch()
		throws EvaluationException
	{
		EvaluationContext ruleContext = ruleDeny.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(target.match(ruleContext)).andReturn(MatchResult.NOMATCH);
		c.replay();
		assertEquals(Decision.NOT_APPLICABLE, ruleDeny.evaluateIfMatch(ruleContext));
		c.verify();
	}
	
	@Test
	public void testDenyRuleEvaluateIfApplicableWithTargetMatchConditionTrue() throws EvaluationException
	{
		
		EvaluationContext ruleContext = ruleDeny.createContext(context);
		
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		
		expect(target.match(ruleContext)).andReturn(MatchResult.MATCH);
	
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		
		expect(condition.evaluate(ruleContext)).andReturn(ConditionResult.TRUE);
			
		expect(denyAdviceAttributeExp.evaluate(context)).andReturn(StringType.STRING.create("testVal1"));
		expect(denyObligationAttributeExp.evaluate(context)).andReturn(StringType.STRING.create("testVal1"));
		
		Capture<Collection<Advice>> advices = new Capture<Collection<Advice>>();
		Capture<Decision> advicesD = new Capture<Decision>();
		Capture<Collection<Obligation>> obligations = new Capture<Collection<Obligation>>();
		Capture<Decision> obligationsD = new Capture<Decision>();
		
		context.addAdvices(capture(advicesD), capture(advices));
		context.addObligations(capture(obligationsD), capture(obligations));
		
		c.replay();
		
		assertEquals(Decision.DENY, ruleDeny.evaluateIfMatch(ruleContext));
		c.verify();
		
		assertTrue(
				advices.getValue().contains(Advice
						.builder("denyAdvice", Effect.DENY)
						.attribute(
								"testId", 
								StringType.STRING.create("testVal1"))
								.create()));
		
		assertTrue(
				obligations.getValue().contains(Obligation
						.builder("denyObligation", Effect.DENY)
						.attribute(
								"testId", 
								StringType.STRING.create("testVal1"))
								.build()));
	}
	
	
	@Test
	public void testPermitRuleEvaluateIfApplicableWithTargetIndeterminate() 
		throws EvaluationException
	{
		EvaluationContext ruleContext = rulePermit.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(target.match(ruleContext)).andReturn(MatchResult.INDETERMINATE);
		c.replay();
		assertEquals(Decision.INDETERMINATE_P, rulePermit.evaluateIfMatch(ruleContext));
		c.verify();
	}
	
	@Test
	public void testPermitRuleEvaluateIfApplicableWithTargetNoMatch() 
		throws EvaluationException
	{
		EvaluationContext ruleContext = rulePermit.createContext(context);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(target.match(ruleContext)).andReturn(MatchResult.NOMATCH);
		c.replay();
		assertEquals(Decision.NOT_APPLICABLE, rulePermit.evaluateIfMatch(ruleContext));
		c.verify();
	}	
	
	@Test
	public void testPermitRuleEvaluateIfApplicableWithTargetMatchConditionTrue() throws EvaluationException
	{
		
		EvaluationContext ruleContext = rulePermit.createContext(context);
		
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		expect(target.match(ruleContext)).andReturn(MatchResult.MATCH);
		expect(context.getCurrentPolicy()).andReturn(currentPolicy);
		
		expect(condition.evaluate(ruleContext)).andReturn(ConditionResult.TRUE);
			
		expect(permitAdviceAttributeExp.evaluate(context)).andReturn(StringType.STRING.create("testVal1"));
		expect(permitObligationAttributeExp.evaluate(context)).andReturn(StringType.STRING.create("testVal1"));
			
		Capture<Collection<Advice>> advices = new Capture<Collection<Advice>>();
		Capture<Decision> advicesD = new Capture<Decision>();
		Capture<Collection<Obligation>> obligations = new Capture<Collection<Obligation>>();
		Capture<Decision> obligationsD = new Capture<Decision>();
		
		context.addAdvices(capture(advicesD), capture(advices));
		context.addObligations(capture(obligationsD), capture(obligations));
		
		
		c.replay();
		
		assertEquals(Decision.PERMIT, rulePermit.evaluateIfMatch(ruleContext));
		c.verify();
	}
}
