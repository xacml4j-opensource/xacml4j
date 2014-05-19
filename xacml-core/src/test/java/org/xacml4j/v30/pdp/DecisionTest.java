package org.xacml4j.v30.pdp;

/*
 * #%L
 * Artagon XACML 3.0 Core Engine Implementation
 * %%
 * Copyright (C) 2009 - 2014 Artagon
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.xacml4j.v30.Decision;


public class DecisionTest
{
	@Test
	public void testIndeterminate()
	{
		assertTrue(Decision.INDETERMINATE_D.isIndeterminate());
		assertTrue(Decision.INDETERMINATE_P.isIndeterminate());
		assertTrue(Decision.INDETERMINATE_DP.isIndeterminate());
		assertTrue(Decision.INDETERMINATE.isIndeterminate());
		assertFalse(Decision.DENY.isIndeterminate());
		assertFalse(Decision.PERMIT.isIndeterminate());
		assertFalse(Decision.NOT_APPLICABLE.isIndeterminate());

	}

}
