package org.xacml4j.v30;

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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xacml4j.v30.types.TypeToString;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;

/**
 * A marker interface for XACML type specific capability/strategy.
 *
 * Exampple: capabilities to/from sting, xml, json
 * 
 * @author Giedrius Trumpickas
 */
public interface TypeCapability
{
	/**
	 * Gets XACML type associated
	 * with this capability
	 *
	 * @return {@link AttributeValueType}
	 */
	AttributeValueType getType();

	static <T extends TypeCapability> Optional<T> forType(AttributeValueType type,
	                                                      Function<AttributeValueType, Optional<T>> systemCapabilities,
	                           Supplier<ServiceLoader<? extends TypeCapabilityFactory<T>>> extensionCapabilities)
	{
		Optional<T> cap = systemCapabilities.apply(type);
		if(cap.isPresent()){
			return cap;
		}
		ServiceLoader<? extends TypeCapabilityFactory<T>> extensions = extensionCapabilities.get();
		if(extensions == null){
			return Optional.empty();
		}
		return extensions.stream()
		                 .filter(v->v.get().forType(type).isPresent())
		                 .findFirst()
		                 .flatMap(v->v.get().forType(type));
	}

	abstract class AbstractCapabilityFactory<T extends TypeCapability>
			implements org.xacml4j.v30.TypeCapabilityFactory<T>
	{
		private static final Logger LOG = LoggerFactory.getLogger(AbstractCapabilityFactory.class);

		private Map<AttributeValueType, T> capabilitiesByType;
		private Class<T> capabilityType;

		/**
		 *
		 * @param providedCapabilities a standard system capabilities for standard types
		 * @param capabilityType a capability type
		 */
		protected AbstractCapabilityFactory(Collection<T> providedCapabilities,
		                                    Class<T> capabilityType){
			this.capabilityType = Objects.requireNonNull(capabilityType, "capabilityType");
			this.capabilitiesByType = providedCapabilities.stream()
			                                            .collect(ImmutableMap.toImmutableMap(e->e.getType(), e->e));
		}

		public final Class<T> getCapabilityType(){
			return capabilityType;
		}

		public final Collection<T> getCapabilities(){
			return capabilitiesByType.values();
		}

		public final Optional<T> forType(AttributeValueType type){
			return Optional.ofNullable(capabilitiesByType.get(type));
		}
	}
}