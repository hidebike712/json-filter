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
 * Represents the type of filtering applied to a JSON structure.
 *
 * <p>
 * This enumeration defines the filtering strategies available for processing
 * JSON elements. The filtering can either include or exclude specified elements
 * based on the chosen strategy.
 * </p>
 *
 * @author Hideki Ikeda
 */
public enum FilterType
{
    /**
     * Indicates a filtering type that filters specified JSON elements are included
     * in the resultant JSON.
     */
    INCLUSION,

    /**
     * Indicates a filtering that filters specified JSON elements are excluded from
     * the resultant JSON.
     */
    EXCLUSION,
    ;
}
