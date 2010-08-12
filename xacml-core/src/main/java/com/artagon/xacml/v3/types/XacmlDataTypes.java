package com.artagon.xacml.v3.types;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import com.artagon.xacml.v3.AttributeCategoryId;
import com.artagon.xacml.v3.AttributeValue;
import com.artagon.xacml.v3.AttributeValueType;
import com.artagon.xacml.v3.BagOfAttributeValues;
import com.artagon.xacml.v3.XacmlSyntaxException;

public enum XacmlDataTypes 
{
	/** XACML DataType: <b>http://www.w3.org/2001/XMLSchema#anyURI</b> */
	ANYURI(new AnyURITypeImpl("http://www.w3.org/2001/XMLSchema#anyURI")),

	/** XACML DataType: <b>http://www.w3.org/2001/XMLSchema#boolean</b> */
	BOOLEAN(new BooleanTypeImpl("http://www.w3.org/2001/XMLSchema#boolean")),

	/** XACML DataType: <b>http://www.w3.org/2001/XMLSchema#base64Binary</b> */
	BASE64BINARY(new Base64BinaryTypeImpl(
			"http://www.w3.org/2001/XMLSchema#base64Binary")),

	/** XACML DataType: <b>http://www.w3.org/2001/XMLSchema#date</b> */
	DATE(new DateTypeImpl("http://www.w3.org/2001/XMLSchema#date")),

	/** XACML DataType: <b>http://www.w3.org/2001/XMLSchema#dateTime</b> */
	DATETIME(new DateTimeTypeImpl("http://www.w3.org/2001/XMLSchema#dateTime")),

	/** XACML DataType: <b>http://www.w3.org/2001/XMLSchema#dayTimeDuration</b> */
	DAYTIMEDURATION(new DayTimeDurationTypeImpl(
			"http://www.w3.org/2001/XMLSchema#dayTimeDuration")),

	DNSNAME(new DNSNameTypeImpl(
			"urn:oasis:names:tc:xacml:2.0:data-type:dnsName")),

	/** XACML DataType: <b>http://www.w3.org/2001/XMLSchema#double</b> */
	DOUBLE(new DoubleTypeImpl("http://www.w3.org/2001/XMLSchema#double")),

	/** XACML DataType: <b>http://www.w3.org/2001/XMLSchema#hexBinary</b> */
	HEXBINARY(new HexBinaryTypeImpl(
			"http://www.w3.org/2001/XMLSchema#hexBinary")),

	/** XACML DataType: <b>http://www.w3.org/2001/XMLSchema#integer</b> */
	INTEGER(new IntegerTypeImpl("http://www.w3.org/2001/XMLSchema#integer")),

	IPADDRESS(new IPAddressTypeImpl(
			"urn:oasis:names:tc:xacml:2.0:data-type:ipAddress")),

	/** XACML DataType: <b>http://www.w3.org/2001/XMLSchema#string</b> */
	RFC822NAME(new RFC822NameTypeImpl(
			"urn:oasis:names:tc:xacml:1.0:data-type:rfc822Name")),

	/** XACML DataType: <b>http://www.w3.org/2001/XMLSchema#string</b> */
	STRING(new StringTypeImpl("http://www.w3.org/2001/XMLSchema#string")),

	/** XACML DataType: <b>http://www.w3.org/2001/XMLSchema#time</b> */
	TIME(new TimeTypeImpl("http://www.w3.org/2001/XMLSchema#time")),

	X500NAME(new X500NameTypeImpl(
			"urn:oasis:names:tc:xacml:1.0:data-type:x500Name")),

	/**
	 * XACML DataType:
	 * <b>urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression</b>
	 */
	XPATHEXPRESSION(new XPathExpressionTypeImpl(
			"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression")),

	/**
	 * XACML DataType: <b>http://www.w3.org/2001/XMLSchema#yearMonthDuration</b>
	 */
	YEARMONTHDURATION(new YearMonthDurationTypeImpl(
			"http://www.w3.org/2001/XMLSchema#yearMonthDuration"));

	private static final Map<String, AttributeValueType> BY_TYPE_ID = new HashMap<String, AttributeValueType>();

	static {
		for (XacmlDataTypes t : EnumSet.allOf(XacmlDataTypes.class)) {
			BY_TYPE_ID.put(t.getTypeId(), t.getType());
		}
		// legacy XACML 2.0 type mappings
		BY_TYPE_ID.put(
				"urn:oasis:names:tc:xacml:2.0:data-type:xpathExpression",
				XacmlDataTypes.XPATHEXPRESSION.getType());
		BY_TYPE_ID.put(
				"urn:oasis:names:tc:xacml:2.0:data-type:xpath-expression",
				XacmlDataTypes.XPATHEXPRESSION.getType());
		BY_TYPE_ID
				.put("http://www.w3.org/TR/2002/WD-xquery-operators-20020816#dayTimeDuration",
						XacmlDataTypes.DAYTIMEDURATION.getType());
		BY_TYPE_ID
				.put("http://www.w3.org/TR/2002/WD-xquery-operators-20020816#yearMonthDuration",
						XacmlDataTypes.YEARMONTHDURATION.getType());
	}

	private AttributeValueType type;

	private XacmlDataTypes(AttributeValueType type) {
		this.type = type;
	}

	/**
	 * Gets type XACML identifier
	 * 
	 * @return XACML identifier for a type
	 */
	public String getTypeId() {
		return type.getDataTypeId();
	}

	/**
	 * Creates type instance value from a given {@link Object}
	 * 
	 * @param <V>
	 *            an attribute value type
	 * @param o
	 *            an attribute value
	 * @return an XACML data type value instance
	 */
	@SuppressWarnings("unchecked")
	public <V extends AttributeValue> V create(Object o, Object... params) {
		return ((V) type.create(o, params));
	}

	/**
	 * Creates a XACML bag of given 
	 * {@link AttributeValue} instances
	 * 
	 * @param <T>
	 * @param attributes an attribute values
	 * @return {@link BagOfAttributeValues} a XACML bag
	 */
	@SuppressWarnings("unchecked")
	public <T extends AttributeValue> BagOfAttributeValues<T> bag(
			AttributeValue... attributes) {
		return (BagOfAttributeValues<T>) type.bagOf().create(attributes);
	}

	@SuppressWarnings("unchecked")
	public <T extends AttributeValue> BagOfAttributeValues<T> bag(
			Collection<AttributeValue> attributes) {
		return (BagOfAttributeValues<T>) type.bagOf().create(attributes);
	}

	@SuppressWarnings("unchecked")
	public <T extends AttributeValue> BagOfAttributeValues<T> emptyBag() {
		return (BagOfAttributeValues<T>) type.bagOf().createEmpty();
	}

	@SuppressWarnings("unchecked")
	public <V extends AttributeValue> V fromXacmlString(String v,
			Object... params) {
		return ((V) type.fromXacmlString(v, params));
	}

	@SuppressWarnings("unchecked")
	public <T extends AttributeValueType> T getType() {
		return (T) type;
	}

	@SuppressWarnings("unchecked")
	public static <T extends AttributeValueType> T getType(String typeId) 
		throws XacmlSyntaxException
	{
		AttributeValueType type = BY_TYPE_ID.get(typeId);
		if(type == null){
			throw new XacmlSyntaxException("Unknow XACML typeId=\"%s\"", typeId);
		}
		return (T) type;
	}

	/**
	 * Creates attribute value of given type
	 * 
	 * @param typeId a type identifier
	 * @param value an attribute value
	 * @return {@link AttributeValue} instance
	 * @throws XacmlSyntaxException if attribute can 
	 * not be created from a given value or given type
	 * identifier does not represent valid XACML data type
	 */
	public static AttributeValue createAttributeValue(String typeId, Object value)
			throws XacmlSyntaxException {
		return createAttributeValue(typeId, value,
				Collections.<QName, String> emptyMap());
	}

	public static AttributeValue createAttributeValue(
			String typeId,
			Object value,
			Object... values) throws XacmlSyntaxException {
		AttributeValueType type = getType(typeId);
		try {
			return type.create(value, values);
		} catch (Exception e) {
			throw new XacmlSyntaxException(e);
		}
	}

	public static AttributeValue createAttributeValue(
			String typeId, 
			Object value,
			Map<QName, String> values) throws XacmlSyntaxException {
		AttributeValueType type = getType(typeId);
		try {
			return type.create(value, getXPathCategory(values));
		} catch (Exception e) {
			throw new XacmlSyntaxException(e);
		}
	}

	private static AttributeCategoryId getXPathCategory(Map<QName, String> attr) 
		throws XacmlSyntaxException
	{
		for (QName n : attr.keySet()) {
			if (n.getLocalPart().equals("XPathCategory")) {
				return AttributeCategoryId.parse(attr.get(n));
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return type.toString();
	}
}
