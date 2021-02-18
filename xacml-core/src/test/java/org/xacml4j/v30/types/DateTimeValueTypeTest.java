package org.xacml4j.v30.types;

/*
 * #%L
 * Xacml4J Core Engine Implementation
 * %%
 * Copyright (C) 2009 - 2014 Xacml4J.org
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.xacml4j.v30.TypeCapability;


public class DateTimeValueTypeTest
{
	private TypeToString toString;

	@Before
	public void init() throws Exception{
		this.toString = TypeToString.Types.DATETIME;
	}

	@Test
	public void testFromXacmlString(){
		DateTimeValue value = (DateTimeValue)toString.fromString("2002-05-30T09:30:10-06:00");
		assertEquals(2002, value.value().getYear());
		assertEquals(5, value.value().getMonth());
		assertEquals(30, value.value().getDay());
		assertEquals(9, value.value().getHour());
		assertEquals(30, value.value().getMinute());
		assertEquals(10, value.value().getSecond());
		assertEquals(-360, value.value().getTimezoneOffset());
		assertEquals("2002-05-30T09:30:10-06:00", toString.toString(value));
	}

	@Test
	public void testFromXacmlStringNoTimeZone(){
		DateTimeValue value = XacmlTypes.DATETIME.of("2002-05-30T09:30:10");
		assertEquals(2002, value.value().getYear());
		assertEquals(5, value.value().getMonth());
		assertEquals(30, value.value().getDay());
		assertEquals(9, value.value().getHour());
		assertEquals(30, value.value().getMinute());
		assertEquals(10, value.value().getSecond());
		assertEquals(0, value.value().getTimezoneOffset());
		assertEquals("2002-05-30T09:30:10Z", toString.toString(value));

	}

	@Test(expected=IllegalArgumentException.class)
	public void testFromXacmlStringJustDate(){
		toString.fromString("2002-09-24Z");
	}

	@Test
	public void addDayTimeDurationTest()
	{
		DateTimeValue dateTime1 = XacmlTypes.DATETIME.of("2002-03-22T08:23:47-05:00");
		DateTimeValue dateTime2 = XacmlTypes.DATETIME.of("2002-03-27T10:23:47-05:00");
		DayTimeDurationValue duration = DayTimeDurationValue.of("P5DT2H0M0S");
		assertEquals(dateTime2, dateTime1.add(duration));
	}

	@Test
	public void compareTest()
	{
		DateTimeValue dateTime1 = XacmlTypes.DATETIME.of("2002-03-22T08:23:47-05:00");
		DateTimeValue dateTime2 = XacmlTypes.DATETIME.of("2002-03-22T10:23:47-05:00");
		assertEquals(-1, dateTime1.compareTo(dateTime2));
		dateTime2 = DateTimeValue.of("2002-03-22T08:23:47-05:00");
		assertEquals(0, dateTime1.compareTo(dateTime2));
		dateTime2 = DateTimeValue.of("2002-03-22T08:22:47-05:00");
		assertEquals(1, dateTime1.compareTo(dateTime2));
	}

	@Test
	public void addYearMonthDuration()
	{
		DateTimeValue dateTime1 = XacmlTypes.DATETIME.of("2002-03-22T08:23:47-05:00");
		DateTimeValue dateTime2 = XacmlTypes.DATETIME.of("2001-01-22T08:23:47-05:00");
		YearMonthDurationValue duration = YearMonthDurationValue.of("-P1Y2M");
		assertEquals(dateTime2, dateTime1.add(duration));
	}

	@Test
	public void substractYearMonthDuration()
	{

		DateTimeValue dateTime1 = XacmlTypes.DATETIME.of("2002-07-22T08:23:47-05:00");
		DateTimeValue dateTime2 = XacmlTypes.DATETIME.of("2006-08-22T08:23:47-05:00");
		YearMonthDurationValue duration = YearMonthDurationValue.of("-P4Y1M");
		assertEquals(dateTime2, dateTime1.subtract(duration));
	}

}