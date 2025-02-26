/*
 * Copyright (C) 2025 Hideki Ikeda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.czeal.jsonfilter;


import com.google.gson.JsonElement;


/**
 * A filter interface for processing JSON elements by either including or excluding
 * specified nodes.
 *
 * <p>
 * Implementations of this interface define how filtering is applied to a given
 * JSON structure based on a set of rules provided in a string format.
 * </p>
 *
 * <p>
 * The filtering rules are specified as a comma-separated string where nested
 * fields can be defined using parentheses. Implementations may use this structure
 * to determine whether elements should be retained or removed from the resulting
 * JSON.
 * </p>
 *
 * <h2>Example</h2>
 * <ul>
 * <li>{@code "a,b(c,d)"} - Includes/excludes top-level element {@code a} and the
 * sub-elements {@code c} and {@code d} inside {@code b}.</li>
 * <li>{@code "x(y)"} - Includes/excludes only {@code x.y}, keeping other elements
 * in {@code x}.</li>
 * </ul>
 *
 * <p>
 * Implementing classes may vary in behavior, such as including only specified
 * elements (inclusion filtering) or removing specified elements (exclusion filtering).
 * </p>
 *
 * @author Hideki Ikeda
 */
public interface Filter
{
    /**
     * Applies the filtering strategy to the target {@code source} based on the
     * given {@code nodes}.
     *
     * @param source
     *         The source JSON element to filter.
     *
     * @param nodes
     *         A comma-separated string defining allowed JSON nodes to include.
     *         Each node can specify nested fields using parentheses.
     *
     * @return
     *         A new {@link JsonElement} instance built by filtering the {@code
     *         source} object based on the given {@code nodes} string.
     */
    public JsonElement apply(JsonElement source, String nodes);
}
