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


import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class FilterTypeTest
{
    @Test
    @DisplayName("retrieve all enum values")
    void testValues1()
    {
        FilterType[] expected = { FilterType.INCLUSION, FilterType.EXCLUSION };
        FilterType[] actual = FilterType.values();
        assertArrayEquals(expected, actual);
    }


    @Test
    @DisplayName("retrieve name of INCLUSION enum")
    void testName1()
    {
        assertEquals("INCLUSION", FilterType.INCLUSION.name());
    }


    @Test
    @DisplayName("retrieve name of EXCLUSION enum")
    void testName2()
    {
        assertEquals("EXCLUSION", FilterType.EXCLUSION.name());
    }


    @Test
    @DisplayName("retrieve enum by name - INCLUSION")
    void testValueOf1()
    {
        assertEquals(FilterType.INCLUSION, FilterType.valueOf("INCLUSION"));
    }


    @Test
    @DisplayName("retrieve enum by name - EXCLUSION")
    void testValueOf2()
    {
        assertEquals(FilterType.EXCLUSION, FilterType.valueOf("EXCLUSION"));
    }


    @Test
    @DisplayName("throw exception for invalid enum name")
    void testValueOf3()
    {
        assertThrows(IllegalArgumentException.class, () -> FilterType.valueOf("INVALID"));
    }


    @Test
    @DisplayName("retrieve ordinal of INCLUSION")
    void testOrdinal1()
    {
        assertEquals(0, FilterType.INCLUSION.ordinal());
    }


    @Test
    @DisplayName("Retrieve ordinal of EXCLUSION")
    void testOrdinal2()
    {
        assertEquals(1, FilterType.EXCLUSION.ordinal());
    }
}
