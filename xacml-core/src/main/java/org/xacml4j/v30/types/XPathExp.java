package org.xacml4j.v30.types;

import org.xacml4j.v30.AttributeCategory;
import org.xacml4j.v30.XPathExpression;

public final class XPathExp extends BaseAttributeExp<XPathExpression>
{
	private static final long serialVersionUID = 8576542145890616101L;

	public XPathExp(String xpath, AttributeCategory categoryId){
		super(XPathExpType.XPATHEXPRESSION,
				new XPathExpression(xpath, categoryId));
	}

	public String getPath(){
		return getValue().getPath();
	}

	public AttributeCategory getCategory(){
		return getValue().getCategory();
	}
}
