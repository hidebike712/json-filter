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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class NodeParserTest
{
    @Test
    @DisplayName("parse null input")
    void testParse1()
    {
        NodeParser parser = new NodeParser();
        Node result = parser.parse(null);
        assertEquals("ROOT", result.toString());
    }


    @Test
    @DisplayName("parse empty input")
    void testParse2()
    {
        NodeParser parser = new NodeParser();
        Node result = parser.parse("");
        assertEquals("ROOT()", result.toString());
    }


    @Test
    @DisplayName("parse simple nodes with single level nesting correctly")
    void testParse3()
    {
        NodeParser parser = new NodeParser();
        Node result = parser.parse("a,b(c)");
        assertEquals("ROOT(a,b(c))", result.toString());
    }


    @Test
    @DisplayName("merge duplicate nodes during parsing")
    void testParse4()
    {
        NodeParser parser = new NodeParser();
        Node result = parser.parse("a(b(c),b(d))");
        assertEquals("ROOT(a(b(c,d)))", result.toString());
    }


    @Test
    @DisplayName("parse nodes with extra whitespace")
    void testParse5()
    {
        NodeParser parser = new NodeParser();
        Node result = parser.parse("  a (  b ( c ) , b ( d ) )  ,  e  ");
        assertEquals("ROOT(a(b(c,d)),e)", result.toString());
    }


    @Test
    @DisplayName("throw IllegalArgumentException when input has invalid syntax")
    void testParse6()
    {
        NodeParser parser = new NodeParser();
        assertThrows(IllegalArgumentException.class, () -> { parser.parse("a(b"); });
    }
}
