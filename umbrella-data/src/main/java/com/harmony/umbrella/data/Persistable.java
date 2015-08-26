/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.harmony.umbrella.data;

import java.io.Serializable;

/**
 * Simple interface for entities.
 * 
 * @param <ID>
 *            the type of the identifier
 * @author Oliver Gierke
 */
public interface Persistable<ID extends Serializable> extends Serializable {

	/**
	 * Returns the id of the entity.
	 * 
	 * @return the id
	 */
	ID getId();

	/**
	 * Returns if the {@code Persistable} is new or was persisted already.
	 * 
	 * @return if the object is new
	 */
	boolean isNew();
}