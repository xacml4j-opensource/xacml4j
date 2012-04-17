package com.artagon.xacml.v30.marshall.jaxb;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.oasis.xacml.v20.jaxb.context.DecisionType;
import org.oasis.xacml.v20.jaxb.context.ResponseType;
import org.oasis.xacml.v20.jaxb.context.ResultType;
import org.oasis.xacml.v20.jaxb.context.StatusCodeType;
import org.oasis.xacml.v20.jaxb.context.StatusType;
import org.oasis.xacml.v20.jaxb.policy.AttributeAssignmentType;
import org.oasis.xacml.v20.jaxb.policy.EffectType;
import org.oasis.xacml.v20.jaxb.policy.ObligationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artagon.xacml.util.Xacml20XPathTo30Transformer;
import com.artagon.xacml.v30.AttributeCategories;
import com.artagon.xacml.v30.Status;
import com.artagon.xacml.v30.StatusCode;
import com.artagon.xacml.v30.StatusCodeIds;
import com.artagon.xacml.v30.StatusDetail;
import com.artagon.xacml.v30.marshall.ResponseUnmarshaller;
import com.artagon.xacml.v30.pdp.Attribute;
import com.artagon.xacml.v30.pdp.AttributeAssignment;
import com.artagon.xacml.v30.pdp.AttributeExp;
import com.artagon.xacml.v30.pdp.Attributes;
import com.artagon.xacml.v30.pdp.Decision;
import com.artagon.xacml.v30.pdp.Effect;
import com.artagon.xacml.v30.pdp.Obligation;
import com.artagon.xacml.v30.pdp.RequestSyntaxException;
import com.artagon.xacml.v30.pdp.ResponseContext;
import com.artagon.xacml.v30.pdp.Result;
import com.artagon.xacml.v30.pdp.XacmlSyntaxException;
import com.artagon.xacml.v30.types.DataTypeRegistry;
import com.artagon.xacml.v30.types.DataTypeRegistryBuilder;
import com.artagon.xacml.v30.types.StringType;
import com.artagon.xacml.v30.types.XPathExpType;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

public class Xacml20ResponseContextUnmarshaller 
	extends BaseJAXBUnmarshaller<ResponseContext> 
implements ResponseUnmarshaller
{
	private Mapper mapper;
	
	public Xacml20ResponseContextUnmarshaller(DataTypeRegistry dataTypes){
		super(JAXBContextUtil.getInstance());
		Preconditions.checkNotNull(dataTypes);
		this.mapper = new Mapper(dataTypes);
	}
	
	public Xacml20ResponseContextUnmarshaller(){
		this(DataTypeRegistryBuilder.builder()
				.withDefaultTypes()
				.build());
	}
	
	@Override
	protected ResponseContext create(JAXBElement<?> jaxbInstance)
			throws XacmlSyntaxException {
		return mapper.create(
				(ResponseType)jaxbInstance.getValue());
	}
	
	static class Mapper
	{
		private final static Logger log = LoggerFactory.getLogger(Mapper.class);
		
		private final static String RESOURCE_ID = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
		
		private final static Map<Decision, DecisionType> v30ToV20DecisionMapping = new HashMap<Decision, DecisionType>();
		private final static Map<DecisionType, Decision> v20ToV30DecisionMapping = new HashMap<DecisionType, Decision>();
		
		private final static Map<EffectType, Effect> v20ToV30EffectnMapping = new HashMap<EffectType, Effect>();
		private final static Map<Effect, EffectType> v30ToV20EffectnMapping = new HashMap<Effect, EffectType>();
		
		private DataTypeRegistry dataTypes;
		
		static
		{
			v30ToV20DecisionMapping.put(Decision.DENY, DecisionType.DENY);
			v30ToV20DecisionMapping.put(Decision.PERMIT, DecisionType.PERMIT);
			v30ToV20DecisionMapping.put(Decision.NOT_APPLICABLE, DecisionType.NOT_APPLICABLE);
			v30ToV20DecisionMapping.put(Decision.INDETERMINATE, DecisionType.INDETERMINATE);
			v30ToV20DecisionMapping.put(Decision.INDETERMINATE_D, DecisionType.INDETERMINATE);
			v30ToV20DecisionMapping.put(Decision.INDETERMINATE_P, DecisionType.INDETERMINATE);
			v30ToV20DecisionMapping.put(Decision.INDETERMINATE_DP, DecisionType.INDETERMINATE);
			
			v20ToV30DecisionMapping.put(DecisionType.DENY, Decision.DENY);
			v20ToV30DecisionMapping.put(DecisionType.PERMIT, Decision.PERMIT);
			v20ToV30DecisionMapping.put(DecisionType.NOT_APPLICABLE, Decision.NOT_APPLICABLE);
			v20ToV30DecisionMapping.put(DecisionType.INDETERMINATE, Decision.INDETERMINATE);
			
			
			v20ToV30EffectnMapping.put(EffectType.DENY, Effect.DENY);
			v20ToV30EffectnMapping.put(EffectType.PERMIT, Effect.PERMIT);
			
			v30ToV20EffectnMapping.put(Effect.DENY, EffectType.DENY);
			v30ToV20EffectnMapping.put(Effect.PERMIT, EffectType.PERMIT);
		
		}
		
		public Mapper(DataTypeRegistry dataTypes){
			Preconditions.checkNotNull(dataTypes);
			this.dataTypes = dataTypes;
		}
	
		public ResponseContext create(ResponseType response) throws XacmlSyntaxException
		{
			Preconditions.checkNotNull(response);
			Collection<Result> results = new LinkedList<Result>();
			for(ResultType result : response.getResult()){
				results.add(create(result));
			}
			return new ResponseContext(results);
		}
		
		private Result create(ResultType result) throws XacmlSyntaxException
		{
			Preconditions.checkArgument(result.getDecision() != null);
			Decision d = v20ToV30DecisionMapping.get(result.getDecision());
			Status status = create(result.getStatus());
			if(d.isIndeterminate()){
				return new Result(d, status);
			}
			Collection<Attributes> attrs = new LinkedList<Attributes>();
			if(result.getResourceId() != null){
				attrs.add(new Attributes(AttributeCategories.RESOURCE, 
						new Attribute(RESOURCE_ID, StringType.STRING.create(result.getResourceId()))));
			}
			return new Result(d, status, getObligations(result), attrs);
		}
		
		private Collection<Obligation> getObligations(ResultType r) throws XacmlSyntaxException
		{
			if(r.getObligations() == null){
				return Collections.<Obligation>emptyList();
			}
			Collection<Obligation> obligations = new LinkedList<Obligation>();
			for(ObligationType o  : r.getObligations().getObligation()){
				if(log.isDebugEnabled()){
					log.debug("Unmarshalling obligationId=\"{}\"", o.getObligationId());
				}
				obligations.add(create(o));
			}
			return obligations;
		}
		
		private Obligation create(ObligationType o) throws XacmlSyntaxException
		{
			Collection<AttributeAssignment> attrs = new LinkedList<AttributeAssignment>();
			for(AttributeAssignmentType a : o.getAttributeAssignment()){
				attrs.add(new AttributeAssignment(a.getAttributeId(), 
						createValue(a.getDataType(), a.getContent(), a.getOtherAttributes())));
			}
			return Obligation
					.builder(o.getObligationId(), v20ToV30EffectnMapping.get(o.getFulfillOn()))
					.attributes(attrs)
					.create();
		}
		
		private Status create(StatusType status) throws XacmlSyntaxException
		{
			StatusDetail detail = null;
			if(status.getStatusDetail() != null && 
					!status.getStatusDetail().getAny().isEmpty()){
				detail =  new StatusDetail(status.getStatusDetail().getAny());
			}
			return new Status(create(status.getStatusCode()), status.getStatusMessage(), detail);
		}
		
		private StatusCode create(StatusCodeType code) throws XacmlSyntaxException
		{
			if(code == null){
				return null;
			}
			return new StatusCode(StatusCodeIds.parse(code.getValue()), create(code.getStatusCode()));
		}
		
		private AttributeExp createValue(String dataTypeId, 
				List<Object> any, Map<QName, String> other) 
			throws XacmlSyntaxException
		{
			if(any == null || 
					any.isEmpty()){
				throw new RequestSyntaxException("Attribute does not have content");
			}
			com.artagon.xacml.v30.pdp.AttributeExpType dataType = dataTypes.getType(dataTypeId);
			if(dataType == null){
				throw new RequestSyntaxException(
						"DataTypeId=\"%s\" can be be " +
						"resolved to valid XACML type", dataTypeId);
			}
			Object o = Iterables.getOnlyElement(any);
			if(log.isDebugEnabled()){
				log.debug("Creating typeId=\"{}\" value=\"{}\"", dataType, o);
			}
			if(dataType.equals(XPathExpType.XPATHEXPRESSION)){
				String xpath = Xacml20XPathTo30Transformer.transform20PathTo30((String)o);
				return dataType.create(xpath, other);
			}
			return dataType.create(o);
		}
	}
}
