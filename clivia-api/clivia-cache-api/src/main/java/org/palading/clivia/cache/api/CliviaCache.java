/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.palading.clivia.cache.api;

import java.util.Set;

/** @ClassName Cache @Description TODO @Author palading_cr @Version 1.0 */
public interface CliviaCache<K, V> {

    public void put(K k, V v);

    public V get(K k);

    public V getDefault(K k, V v1);

    public void clear();

    public boolean isEmpty();

    public Set<K> keySet();

    public void remove(K k);

    public Object get();

    public int size();

    public void putIfAbsent(K k,V v);
}
