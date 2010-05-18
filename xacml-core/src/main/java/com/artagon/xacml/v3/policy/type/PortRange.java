package com.artagon.xacml.v3.policy.type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artagon.xacml.v3.XacmlObject;
import com.google.common.base.Preconditions;

public final class PortRange extends XacmlObject
{
	private final static Logger log = LoggerFactory.getLogger(PortRange.class);
	
	private Integer lowerBound;
	private Integer upperBound;

	/**
	 * Default constructor used to represent an unbound range. This is typically
	 * used when an address has no port information.
	 */
	PortRange() {
		this(null, null);
	}
	
	/**
	 * Creates a <code>PortRange</code> with upper and lower bounds. Either of
	 * the parameters may have the value <code>UNBOUND</code> meaning that there
	 * is no bound at the respective end.
	 * 
	 * @param lowerBound
	 *            the lower-bound port number or <code>UNBOUND</code>
	 * @param upperBound
	 *            the upper-bound port number or <code>UNBOUND</code>
	 */
	PortRange(Integer lowerBound, Integer upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	
	/**
	 * Creates a <code>PortRange</code> that represents a single port value
	 * instead of a range of values.
	 * 
	 * @param singlePort
	 *            the single port number
	 */
	PortRange(Integer singlePort) {
		this(singlePort, singlePort);
	}
	
	public static PortRange getAnyPort(){
		return new PortRange();
	}
	
	public static PortRange getSinglePort(Integer port){
		return new PortRange(port);
	}
	
	public static PortRange getRangeFrom(Integer port){
		return new PortRange(port, null);
	}
	
	public static PortRange getRangeUntil(Integer port){
		return new PortRange(null, port);
	}
	
	public static PortRange getRange(Integer lower, Integer upper){
		return new PortRange(lower, upper);
	}

	
	/**
	 * Creates an instance of <code>PortRange</code> based on the given value.
	 * 
	 * @param value
	 *            a <code>String</code> representing the range
	 * 
	 * @return a new <code>PortRange</code>
	 * 
	 * @throws NumberFormatException
	 *             if a port value isn't an integer
	 */
	public static PortRange valueOf(int index, String value) 
	{	
		if ((value.length() == 0) || 
				(value.equals("-"))){
			log.debug("Found any port range in the given value=\"{}\" at index=\"{}\"", value, index);
			return getAnyPort();
		}
		int dashPos = value.indexOf('-', index);
		if (dashPos == -1) {
			log.debug("Found single port in the given value=\"{}\" at index=\"{}\"", value, index);
			return getSinglePort(
					Integer.parseInt(value.substring(index, value.length())));
		}
		if (dashPos == index) {
			log.debug("Found range until in the given value=\"{}\" at index=\"{}\"", value, index);
			return getRangeUntil(
					Integer.parseInt(value.substring(index + 1)));
		} 
		if (dashPos == (value.length() - 1)) {
			log.debug("Found range from in the given value=\"{}\" at index=\"{}\"", value, index);
			return getRangeFrom(Integer.parseInt(value.substring(index, dashPos)));
		}
		Integer lowerBound = Integer.parseInt(value.substring(index, dashPos));
		Integer upperBound = Integer.parseInt(value.substring(dashPos + 1, value.length()));
		return PortRange.getRange(lowerBound, upperBound);
	}
	
	public static PortRange valueOf(String v){
		return valueOf(0, v);
	}

	/**
	 * Returns the lower-bound port value. If the range is not lower-bound, then
	 * this returns <code>UNBOUND</code>. If the range is actually a single port
	 * number, then this returns the same value as <code>getUpperBound</code>.
	 * 
	 * @return the upper-bound
	 */
	public int getLowerBound() {
		Preconditions.checkState(lowerBound != null);
		return lowerBound;
	}

	/**
	 * Returns the upper-bound port value. If the range is not upper-bound, then
	 * this returns <code>UNBOUND</code>. If the range is actually a single port
	 * number, then this returns the same value as <code>getLowerBound</code>.
	 * 
	 * @return the upper-bound
	 */
	public int getUpperBound() {
		Preconditions.checkState(upperBound != null);
		return upperBound;
	}

	/**
	 * Returns whether the range is bounded by a lower port number.
	 * 
	 * @return true if lower-bounded, false otherwise
	 */
	public boolean isLowerBounded() {
		return (lowerBound != null);
	}

	/**
	 * Returns whether the range is bounded by an upper port number.
	 * 
	 * @return true if upper-bounded, false otherwise
	 */
	public boolean isUpperBounded() {
		return (upperBound != null);
	}
	
	/**
	 * Tests if a given port is in this range
	 * 
	 * @param port a positive number indicating port
	 * @return <code>true</code> if a given port is
	 * in this range
	 */
	public boolean contains(int port){
		Preconditions.checkArgument(port > 0);
		return (isLowerBounded()?lowerBound <= port:true) && 
		(isUpperBounded()?port <= upperBound:true);
	}
	
	public boolean contains(PortRange range){
		return contains(range.getLowerBound()) && 
		contains(range.getUpperBound());
	}

	/**
	 * Returns whether the range is actually a single port number.
	 * 
	 * @return true if the range is a single port number, false otherwise
	 */
	public boolean isSinglePort() {
		return ((lowerBound == upperBound) && (isLowerBounded() && isUpperBounded()));
	}

	/**
	 * Returns whether the range is unbound, which means that it specifies no
	 * port number or range. This is typically used with addresses that include
	 * no port information.
	 * 
	 * @return true if the range is unbound, false otherwise
	 */
	public boolean isUnbound() {
		return ((lowerBound == null) && (upperBound == null));
	}

	/**
	 * Returns true if the input is an instance of this class and if its value
	 * equals the value contained in this class.
	 * 
	 * @param o
	 *            the object to compare
	 * 
	 * @return true if this object and the input represent the same value
	 */
	public boolean equals(Object o) {
		if(o == this){
			return true;
		}
		if (!(o instanceof PortRange)){
			return false;
		}
		PortRange other = (PortRange) o;
		return lowerBound == other.lowerBound && 
		upperBound == other.upperBound;
	}
	

	public String toString() 
	{
		if (isUnbound()){
			return "";
		}
		StringBuilder b = new StringBuilder();
		if (isSinglePort()){
			b.append(getLowerBound());
			return b.toString();
		}
		if(isLowerBounded()){
			b.append(lowerBound).append('-');
		}
		if(!isLowerBounded()){
			b.append('-');
		}
		if (isUpperBounded()){
			b.append(getUpperBound());
		}
		return b.toString();
	}
}
