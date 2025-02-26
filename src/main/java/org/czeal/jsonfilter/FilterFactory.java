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


/**
 * A factory class for creating filtering strategy implementations.
 *
 * <p>This factory provides instances of filters that can either include or exclude
 * specified JSON elements based on a given filtering strategy.</p>
 *
 * <p>Supported filtering strategies:</p>
 * <ul>
 * <li>{@code INCLUSION} - Retains only the specified JSON elements.</li>
 * <li>{@code EXCLUSION} - Removes the specified JSON elements.</li>
 * </ul>
 *
 * <p>Usage example:</p>
 * <pre style="border:1px solid lightgray; padding:1px;">
 * <code>
 * FilterFactory factory = new FilterFactory();
 *
 * Filter filter = factory.create(FilterType.INCLUSION);
 * </code>
 * </pre>
 *
 * @author Hideki Ikeda
 */
public class FilterFactory
{
    /**
     * Creates a filter based on the given filter type.
     *
     * @param type
     *         The filter type.
     *
     * @return
     *         A filter implementation.
     *
     * @throws NullPointerException
     *         If the filter type is {@code null}.
     *
     * @throws IllegalArgumentException
     *         If the filter type is unknown.
     */
    public Filter create(FilterType type)
    {
        if (type == null)
        {
            // The filter type can't be null.
            throw new NullPointerException("The filter type can't be null.");
        }

        switch (type)
        {
            case INCLUSION:
                // Create an inclusion filter.
                return new InclusionFilter();

            case EXCLUSION:
                // Create an exclusion filter.
                return new ExclusionFilter();

            default:
                // Unknown filter type.
                throw new IllegalArgumentException("Unknown filter type.");
        }
    }
}
