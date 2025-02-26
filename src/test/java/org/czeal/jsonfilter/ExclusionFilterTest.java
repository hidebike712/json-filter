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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;


public class ExclusionFilterTest
{
    @Test
    @DisplayName("apply filtering to null")
    void testApply1()
    {
        assertEquals(JsonNull.INSTANCE, new ExclusionFilter().apply(null, "key"));
    }


    @Test
    @DisplayName("apply filtering with empty node to string")
    void testApply2()
    {
        JsonElement jsonElement = JsonParser.parseString("\"Hello World\"");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "");
        assertEquals("\"Hello World\"", result.toString());
    }


    @Test
    @DisplayName("apply filtering with empty node to number")
    void testApply3()
    {
        JsonElement jsonElement = JsonParser.parseString("42");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "");
        assertEquals("42", result.toString());
    }


    @Test
    @DisplayName("apply filtering with empty node to empty object")
    void testApply4()
    {
        JsonElement jsonElement = JsonParser.parseString("{}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "");
        assertEquals("{}", result.toString());
    }


    @Test
    @DisplayName("apply filtering with empty node to empty array")
    void testApply5()
    {
        JsonElement jsonElement = JsonParser.parseString("[]");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "");
        assertEquals("[]", result.toString());
    }

    @Test
    @DisplayName("apply filtering with unknown node to empty object")
    void testApply6()
    {
        JsonElement jsonElement = JsonParser.parseString("{}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "name");
        assertEquals("{}", result.toString());
    }

    @Test
    @DisplayName("apply filtering with unknown node to empty array")
    void testApply7()
    {
        JsonElement jsonElement = JsonParser.parseString("[]");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "name");
        assertEquals("[]", result.toString());
    }


    @Test
    @DisplayName("apply filtering with empty node to object")
    void testApply8()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"name\":\"John\",\"age\":30}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "");
        assertEquals("{\"name\":\"John\",\"age\":30}", result.toString());
    }


    @Test
    @DisplayName("apply filtering with empty node to object")
    void testApply9()
    {
        JsonElement jsonElement = JsonParser.parseString("[{\"name\":\"John\",\"age\":30},\"Hello\",null,{\"name\":\"Cali\",\"city\":\"NY\"}]");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "");
        assertEquals("[{\"name\":\"John\",\"age\":30},\"Hello\",null,{\"name\":\"Cali\",\"city\":\"NY\"}]", result.toString());
    }

    @Test
    @DisplayName("apply filtering with unknown node to object")
    void testApply10()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"name\":\"john\",\"type\":0}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "key");
        assertEquals("{\"name\":\"john\",\"type\":0}", result.toString());
    }


    @Test
    @DisplayName("filter out specified node from nested double-array object")
    void testApply11()
    {
        JsonElement jsonElement = JsonParser.parseString("[[{\"name\":\"john\",\"type\":0}]]");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "name");
        assertEquals("[[{\"type\":0}]]", result.toString());
    }


    @Test
    @DisplayName("filter out specified node from triple-array object")
    void testApply12()
    {
        JsonElement jsonElement = JsonParser.parseString("[[[{\"name\":\"john\",\"type\":0}]]]");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "name");
        assertEquals("[[[{\"type\":0}]]]", result.toString());
    }


    @Test
    @DisplayName("filter out specified sub-node from object in array")
    void testApply13()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"arr\":[{\"name\":\"john\",\"type\":0}]}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "arr(name)");
        assertEquals("{\"arr\":[{\"type\":0}]}", result.toString());
    }


    @Test
    @DisplayName("filter out specified sub-node from multiple objects in array")
    void testApply14()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"arr\":[{\"name\":\"john\",\"type\":0},{\"name\":\"cali\",\"type\":1}]}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "arr(name)");
        assertEquals("{\"arr\":[{\"type\":0},{\"type\":1}]}", result.toString());
    }


    @Test
    @DisplayName("filter out entire array by parent array key")
    void testApply15()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"arr\":[{\"name\":\"john\",\"type\":0}]}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "arr");
        assertEquals("{}", result.toString());
    }


    @Test
    @DisplayName("filter out all sub-nodes from object in array")
    void testApply16()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"arr\":[{\"name\":\"john\",\"type\":0}]}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "arr(name),arr(type)");
        assertEquals("{\"arr\":[{}]}", result.toString());
    }


    @Test
    @DisplayName("filter sub-nodes from objects in multiple arrays")
    void testApply17()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"arr\":[{\"name\":\"john\",\"type\":0}],\"arr2\":[{\"name\":\"cali\",\"type\":1}]}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "arr(name),arr2(name,type)");
        assertEquals("{\"arr\":[{\"type\":0}],\"arr2\":[{}]}", result.toString());
    }


    @Test
    @DisplayName("filter out specified nodes from objects in array")
    void testApply18()
    {
        JsonElement jsonElement = JsonParser.parseString("[{\"name\":\"john\",\"type\":0},{\"name\":\"cali\",\"type\":1}]");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "name,type");
        assertEquals("[{},{}]", result.toString());
    }


    @Test
    @DisplayName("filter out specified node from array with mixed types")
    void testApply19()
    {
        JsonElement jsonElement = JsonParser.parseString("[{\"name\":\"john\",\"type\":0},\"secondElement\",{\"name\":\"cali\",\"type\":1}]");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "name");
        assertEquals("[{\"type\":0},\"secondElement\",{\"type\":1}]", result.toString());
    }


    @Test
    @DisplayName("filter out specified and unknown nodes from mixed array")
    void testApply20()
    {
        JsonElement jsonElement = JsonParser.parseString("[{\"name\":\"john\",\"type\":0},\"secondElement\",{\"name\":\"cali\",\"type\":1}]");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "name,hoge,type");
        assertEquals("[{},\"secondElement\",{}]", result.toString());
    }


    @Test
    @DisplayName("filter out specified nodes from array containing null")
    void testApply21()
    {
        JsonElement jsonElement = JsonParser.parseString("[{\"name\":\"john\",\"type\":0},null,{\"key\":\"value\"}]");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "name,key");
        assertEquals("[{\"type\":0},null,{}]", result.toString());
    }


    @Test
    @DisplayName("filter out specified sub-node from nested object")
    void testApply22()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"prop\":{\"name\":\"john\",\"type\":0}}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "prop(name)");
        assertEquals("{\"prop\":{\"type\":0}}", result.toString());
    }


    @Test
    @DisplayName("filter by empty sub-nodes")
    void testApply23()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"prop\":{\"key1\":\"value1\",\"key2\":\"value2\"}}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "prop()");
        assertEquals("{\"prop\":{\"key1\":\"value1\",\"key2\":\"value2\"}}", result.toString());
    }


    @Test
    @DisplayName("filter out specified sub-node from objects inside array")
    void testApply24()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"prop\":[{\"key1\":\"value1\",\"key2\":\"value2\"}]}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "prop(key1)");
        assertEquals("{\"prop\":[{\"key2\":\"value2\"}]}", result.toString());
    }


    @Test
    @DisplayName("filter mixed array containing null by empty sub-nodes")
    void testApply25()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"prop\":[{\"key1\":\"value1\",\"key2\":\"value2\"},null]}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "prop()");
        assertEquals("{\"prop\":[{\"key1\":\"value1\",\"key2\":\"value2\"},null]}", result.toString());
    }


    @Test
    @DisplayName("filter out entire object by parent node")
    void testApply26()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"prop\":{\"key1\":\"value1\",\"key2\":\"value2\"}}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "prop");
        assertEquals("{}", result.toString());
    }


    @Test
    @DisplayName("filter out specified key from array elements")
    void testApply27()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"arr\":[{\"remove\":\"yes\"},{\"keep\":\"yes\"}]}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "arr(remove)");
        assertEquals("{\"arr\":[{},{\"keep\":\"yes\"}]}", result.toString());
    }


    @Test
    @DisplayName("filter out specified sub-node from nested object")
    void testApply28()
    {
        JsonElement jsonElement = JsonParser.parseString("{\"deep\":{\"nested\":{\"object\":{\"remove\":\"yes\",\"keep\":\"no\"}}}}");
        JsonElement result = new ExclusionFilter().apply(jsonElement, "deep(nested(object(remove)))");
        assertEquals("{\"deep\":{\"nested\":{\"object\":{\"keep\":\"no\"}}}}", result.toString());
    }
}
