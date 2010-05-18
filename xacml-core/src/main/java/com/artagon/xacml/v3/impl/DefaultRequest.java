package com.artagon.xacml.v3.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;

import com.artagon.xacml.v3.Attribute;
import com.artagon.xacml.v3.AttributeCategoryId;
import com.artagon.xacml.v3.Attributes;
import com.artagon.xacml.v3.AttributesReference;
import com.artagon.xacml.v3.Request;
import com.artagon.xacml.v3.RequestReference;
import com.artagon.xacml.v3.XacmlObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class DefaultRequest extends XacmlObject implements Request
{	
	private boolean returnPolicyIdList;
	private Multimap<AttributeCategoryId, Attributes> attributes;
	private Map<String, Attributes> attributesByXmlId;
	private Collection<RequestReference> multipleRequests;
	
	/**
	 * Constructs a request with a given attributes
	 * @param attributes
	 */
	public DefaultRequest(boolean returnPolicyIdList, 
			Collection<Attributes> attributes, 
			Collection<RequestReference> requestReferences)
	{
		this.returnPolicyIdList = returnPolicyIdList;
		this.attributes = HashMultimap.create();
		this.multipleRequests = new ArrayList<RequestReference>(requestReferences);
		this.attributesByXmlId = new HashMap<String, Attributes>();
		for(Attributes attr : attributes)
		{
			// index attributes by category
			this.attributes.put(attr.getCategoryId(), attr);
			// index attributes
			// by id for fast lookup
			if(attr.getId() != null){
				this.attributesByXmlId.put(attr.getId(), attr);
			}
		}
	}
	
	/**
	 * Constructs a request with a given attributes
	 * 
	 * @param attributes a collection of {@link Attributes}
	 * instances
	 */
	public DefaultRequest(boolean returnPolicyIdList, 
			Collection<Attributes> attributes)
	{
		this(returnPolicyIdList, attributes, 
				Collections.<RequestReference>emptyList());
	}
	
	@Override
	public boolean isReturnPolicyIdList(){
		return returnPolicyIdList;
	}
	
	@Override
	public int getCategoryOccuriences(AttributeCategoryId category){
		Collection<Attributes> attr = attributes.get(category);
		return (attr == null)?0:attr.size();
	}
	
	@Override
	public Collection<RequestReference> getRequestReferences(){
		return Collections.unmodifiableCollection(multipleRequests);
	}

	@Override
	public boolean hasMultipleRequests(){
		return !multipleRequests.isEmpty();
	}
	
	@Override
	public Set<AttributeCategoryId> getCategories(){
		return Collections.unmodifiableSet(attributes.keySet());
	}
	
	@Override
	public Attributes getReferencedAttributes(AttributesReference reference){
		Preconditions.checkNotNull(reference);
		return attributesByXmlId.get(reference.getReferenceId());
	}
	
	@Override
	public Map<AttributeCategoryId, Collection<Attributes>> getAttributes(){
		return Multimaps.unmodifiableMultimap(attributes).asMap();
	}
	
	@Override
	public Collection<Attributes> getAttributes(AttributeCategoryId categoryId){
		Preconditions.checkNotNull(categoryId);
		Collection<Attributes> attr =  attributes.get(categoryId);
		return (attr == null)?Collections.<Attributes>emptyList():attr;
	}
	
	public Collection<Node> getContent(AttributeCategoryId categoryId)
	{
		Preconditions.checkNotNull(categoryId);
		Collection<Attributes> byCategory =  attributes.get(categoryId);
		if(byCategory == null || 
				byCategory.isEmpty()){
			return Collections.<Node>emptyList();
		}
		Collection<Node> content = new ArrayList<Node>(byCategory.size());
		for(Attributes a : byCategory){
			content.add(a.getContent());
		}
		return content;
	}
	
	@Override
	public boolean hasRepeatingCategories(){
		for(AttributeCategoryId category : getCategories()){
			if(getCategoryOccuriences(category) > 1){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public Collection<Attributes> getIncludeInResultAttributes() 
	{
		Collection<Attributes> resultAttr = new LinkedList<Attributes>();
		for(Attributes a : attributes.values()){
			Collection<Attribute> includeInResult =  a.getIncludeInResultAttributes();
			if(!includeInResult.isEmpty()){
				resultAttr.add(new DefaultAttributes(a.getCategoryId(), includeInResult));
			}
		}
		return resultAttr;
	}	
	
}
