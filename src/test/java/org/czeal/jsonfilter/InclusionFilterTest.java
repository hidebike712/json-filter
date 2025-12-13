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
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class InclusionFilterTest
{
    @Test
    @DisplayName("apply filtering to null")
    void testApply1()
    {
        assertEquals(JsonNull.INSTANCE, new InclusionFilter().apply(null, "key"));
    }


    @Test
    @DisplayName("apply filtering with empty node to string")
    void testApply2() {
        JsonElement jsonElement = JsonParser.parseString("\"Hello World\"");
        JsonElement result = new InclusionFilter().apply(jsonElement, "");
        assertEquals("\"Hello World\"", result.toString());
    }


    @Test
    @DisplayName("apply filtering with empty node to number")
    void testApply3()
    {
        JsonElement jsonElement = JsonParser.parseString("42");
        JsonElement result = new InclusionFilter().apply(jsonElement, "");
        assertEquals("42", result.toString());
    }


    @Test
    @DisplayName("apply filtering with empty node to empty object")
    void testApply4()
    {
        JsonElement jsonElement = JsonParser.parseString("{}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "");
        assertEquals("{}", result.toString());
    }


    @Test
    @DisplayName("apply filtering with empty node to empty array")
    void testApply5()
    {
        JsonElement jsonElement = JsonParser.parseString("[]");
        JsonElement result = new InclusionFilter().apply(jsonElement, "");
        assertEquals("[]", result.toString());
    }


    @Test
    @DisplayName("apply filtering with unknown node to empty object")
    void testApply6()
    {
        JsonElement jsonElement = JsonParser.parseString("{}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "name");
        assertEquals("{}", result.toString());
    }


    @Test
    @DisplayName("apply filtering with unknown node to empty array")
    void testApply7()
    {
        JsonElement jsonElement = JsonParser.parseString("[]");
        JsonElement result = new InclusionFilter().apply(jsonElement, "name");
        assertEquals("[]", result.toString());
    }


    @Test
    @DisplayName("apply filtering with empty node to object")
    void testApply8()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"name\":\"John\",\"age\":30}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "");
        assertEquals("{}", result.toString());
    }


    @Test
    @DisplayName("apply filtering with empty node to object")
    void testApply9()
    {
        JsonElement jsonElement = JsonParser.parseString("[ {\"name\":\"John\", \"age\":30}, \"Hello\", null, {\"name\":\"Cali\", \"city\":\"NY\"} ]");
        JsonElement result = new InclusionFilter().apply(jsonElement, "");
        assertEquals("[{},\"Hello\",null,{}]", result.toString());
    }


    @Test
    @DisplayName("apply filtering with unknown node to object")
    void testApply10()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"name\":\"john\",\"type\":0}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "key");
        assertEquals("{}", result.toString());
    }


    @Test
    @DisplayName("extract specified node from double-array object")
    void testApply11()
    {
        JsonElement jsonElement = JsonParser.parseString("[[{\"name\":\"john\",\"type\":0}]]");
        JsonElement result = new InclusionFilter().apply(jsonElement, "name");
        assertEquals("[[{\"name\":\"john\"}]]", result.toString());
    }


    @Test
    @DisplayName("extract specified node from triple-array object")
    void testApply12()
    {
        JsonElement jsonElement = JsonParser.parseString("[[[{\"name\":\"john\",\"type\":0}]]]");
        JsonElement result = new InclusionFilter().apply(jsonElement, "name");
        assertEquals("[[[{\"name\":\"john\"}]]]", result.toString());
    }


    @Test
    @DisplayName("extract specified node from object in array")
    void testApply13()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"arr\":[{\"name\":\"john\",\"type\":0}]}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "arr(name)");
        assertEquals("{\"arr\":[{\"name\":\"john\"}]}", result.toString());
    }


    @Test
    @DisplayName("extract full object in array by parent node key")
    void testApply14()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"arr\":[{\"name\":\"john\",\"type\":0}]}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "arr");
        assertEquals("{\"arr\":[{\"name\":\"john\",\"type\":0}]}", result.toString());
    }


    @Test
    @DisplayName("extract multiple specified nodes from object in array")
    void testApply15()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"arr\":[{\"name\":\"john\",\"type\":0}]}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "arr(name),arr(type)");
        assertEquals("{\"arr\":[{\"name\":\"john\",\"type\":0}]}", result.toString());
    }


    @Test
    @DisplayName("extract multiple specified nodes from multiple objects in arrays")
    void testApply16()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"arr\":[{\"name\":\"john\",\"type\":0}],\"arr2\":[{\"name\":\"cali\",\"type\":1}]}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "arr(name),arr2(name,type)");
        assertEquals("{\"arr\":[{\"name\":\"john\"}],\"arr2\":[{\"name\":\"cali\",\"type\":1}]}", result.toString());
    }


    @Test
    @DisplayName("extract specified nodes from objects in array")
    void testApply17()
    {
        JsonElement jsonElement = JsonParser.parseString("[{\"name\":\"john\",\"type\":0},{\"name\":\"cali\",\"type\":1}]");
        JsonElement result = new InclusionFilter().apply(jsonElement, "name,type");
        assertEquals("[{\"name\":\"john\",\"type\":0},{\"name\":\"cali\",\"type\":1}]", result.toString());
    }


    @Test
    @DisplayName("extract specified node from mixed-type array")
    void testApply18()
    {
        JsonElement jsonElement = JsonParser.parseString("[{\"name\":\"john\",\"type\":0},\"secondElement\",{\"name\":\"cali\",\"type\":1}]");
        JsonElement result = new InclusionFilter().apply(jsonElement, "name");
        assertEquals("[{\"name\":\"john\"},\"secondElement\",{\"name\":\"cali\"}]", result.toString());
    }


    @Test
    @DisplayName("extract specified nodes with unknown node from mixed-type array")
    void testApply19()
    {
        JsonElement jsonElement = JsonParser.parseString("[{\"name\":\"john\",\"type\":0},\"secondElement\",{\"name\":\"cali\",\"type\":1}]");
        JsonElement result = new InclusionFilter().apply(jsonElement, "name,hoge,type");
        assertEquals("[{\"name\":\"john\",\"type\":0},\"secondElement\",{\"name\":\"cali\",\"type\":1}]", result.toString());
    }


    @Test
    @DisplayName("extract specified nodes from objects in mixed-type array")
    void testApply20()
    {
        JsonElement jsonElement = JsonParser.parseString("[{\"name\":\"john\",\"type\":0},null,{\"key\":\"value\"}]");
        JsonElement result = new InclusionFilter().apply(jsonElement, "name,key");
        assertEquals("[{\"name\":\"john\"},null,{\"key\":\"value\"}]", result.toString());
    }


    @Test
    @DisplayName("extract multiple specified nodes, including nested properties, from object")
    void testApply21()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"name\":\"john\",\"desc\":null,\"prop1\":[{\"key\":\"key1\",\"value\":\"value1\"},{\"key\":\"key2\",\"value\":\"value2\"}]}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "name,desc,prop1(key)");
        assertEquals("{\"name\":\"john\",\"desc\":null,\"prop1\":[{\"key\":\"key1\"},{\"key\":\"key2\"}]}", result.toString());
    }


    @Test
    @DisplayName("extract specified sub-node from nested object")
    void testApply22()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"prop\":{\"name\":\"john\",\"type\":0}}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "prop(name)");
        assertEquals("{\"prop\":{\"name\":\"john\"}}", result.toString());
    }


    @Test
    @DisplayName("extract nothing from nested object by empty sub-nodes")
    void testApply23()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"prop\":{\"key1\":\"value1\",\"key2\":\"value2\"}}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "prop()");
        assertEquals("{\"prop\":{}}", result.toString());
    }


    @Test
    @DisplayName("extract specified sub-node from object in array")
    void testApply24()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"prop\":[{\"key1\":\"value1\",\"key2\":\"value2\"}]}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "prop(key1)");
        assertEquals("{\"prop\":[{\"key1\":\"value1\"}]}", result.toString());
    }


    @Test
    @DisplayName("extract nothing from object in array by empty sub-nodes")
    void testApply25()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"prop\":[{\"key1\":\"value1\",\"key2\":\"value2\"},null]}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "prop()");
        assertEquals("{\"prop\":[{},null]}", result.toString());
    }


    @Test
    @DisplayName("extract entire value from object by key")
    void testApply26()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"prop\":{\"key1\":\"value1\",\"key2\":\"value2\"}}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "prop");
        assertEquals("{\"prop\":{\"key1\":\"value1\",\"key2\":\"value2\"}}", result.toString());
    }


    @Test
    @DisplayName("extract specified sub-node from objects in array")
    void testApply27()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"arr\":[{\"remove\":\"yes\"},{\"keep\":\"yes\"}]}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "arr(keep)");
        assertEquals("{\"arr\":[{},{\"keep\":\"yes\"}]}", result.toString());
    }


    @Test
    @DisplayName("extract specified sub-node from nested object")
    void testApply28()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"deep\":{\"nested\":{\"object\":{\"remove\":\"yes\",\"keep\":\"yes\"}}}}");
        JsonElement result = new InclusionFilter().apply(jsonElement, "deep(nested(object(keep)))");
        assertEquals("{\"deep\":{\"nested\":{\"object\":{\"keep\":\"yes\"}}}}", result.toString());
    }
}
