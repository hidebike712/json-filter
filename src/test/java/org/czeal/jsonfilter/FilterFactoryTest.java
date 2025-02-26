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


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class FilterFactoryTest
{
    @Test
    @DisplayName("throw NullPointerException when creating filter with null type")
    void testCreate1()
    {
        assertThrows(NullPointerException.class, () -> { new FilterFactory().create(null); });
    }


    @Test
    @DisplayName("create InclusionFilter when FilterType.INCLUSION is specified")
    void testCreate2()
    {
        Filter filter = new FilterFactory().create(FilterType.INCLUSION);
        assertNotNull(filter);
        assertTrue(filter instanceof InclusionFilter);
    }


    @Test
    @DisplayName("create ExclusionFilter when FilterType.EXCLUSION is specified")
    void testCreate3()
    {
        Filter filter = new FilterFactory().create(FilterType.EXCLUSION);
        assertNotNull(filter);
        assertTrue(filter instanceof ExclusionFilter);
    }
}
