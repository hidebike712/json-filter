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


import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;


/**
 * A filter implementation that includes only specified JSON elements from a given
 * JSON element. The inclusion rules are defined using a comma-separated string
 * where nested fields can be specified using parentheses.
 *
 * <p>Example of node specification:</p>
 * <ul>
 * <li>{@code "a,b(c,d)"} - Includes only the top-level elements {@code a} and
 * sub-elements {@code c} and {@code d} inside {@code b}.</li>
 * <li>{@code "x(y)"} - Includes only {@code x.y} but keeps other elements in
 * {@code x}.</li>
 * </ul>
 *
 * <p>
 * If the input JSON is a primitive value or {@code null}, it is returned as is.
 * </p>
 *
 * @author Hideki Ikeda
 */
public class InclusionFilter implements Filter
{
    /**
     * Creates a JSON element by extracting JSON elements from the given JSON element
     * based on the nodes string. The nodes are provided as a comma-separated string,
     * where each node defines a portion of the JSON structure to include in the result.
     * Nested fields can be specified using parentheses. For example, a nodes string
     * might be {@code "a,b(c,d)"}, indicating that the output should include the top-level
     * element {@code a} and the element {@code b} with only the {@code c} and {@code d} sub-nodes.
     *
     * <p><b>Examples:</b></p>
     *
     * <p><b>Example 1: For a flat JSON object</b></p>
     * <pre><code>
     * Gson gson = new Gson();
     *
     * JsonElement jsonElement = gson.fromJson("{\"a\":1,\"b\":2,\"c\":3}");
     *
     * JsonElement filtered = new InclusionFilter().apply(jsonElement, "a,b");
     *
     * System.out.println(filtered); // {"a":1,"b":2}
     * </code></pre>
     *
     * <p><b>Example 2: For a nested JSON object</b></p>
     * <pre><code>
     * Gson gson = new Gson();
     *
     * JsonElement jsonElement = gson.fromJson("{\"x\":{\"y\":{\"z\":5},\"w\":10},\"v\":20}");
     *
     * JsonElement filtered = new InclusionFilter().apply(jsonElement, "x(y)");
     *
     * System.out.println(filtered); // {"x":{"y":{"z":5}}}
     * </code></pre>
     *
     * <p><b>Example 3: For nested arrays</b></p>
     * <pre><code>
     * Gson gson = new Gson();
     *
     * JsonElement jsonElement = gson.fromJson("[[[{\"name\":\"john\",\"type\":0}]]]");
     *
     * JsonElement filtered = new InclusionFilter().apply(jsonElement, "name");
     *
     * System.out.println(filtered); // [[[{"name":"john"}]]]
     * </code></pre>
     *
     * <p>
     * If the source JSON is a primitive or {@code null}, filtering is not applicable
     * and the deep copy of the source is returned unmodified.
     * </p>
     *
     * @param source
     *         The source JSON element to filter, which should be either a JSON
     *         object or an array.
     *
     * @param nodes
     *         A comma-separated string defining allowed JSON nodes to include.
     *         Each node can specify nested fields using parentheses.
     *
     * @return
     *         A new {@link JsonElement} instance built by filtering the {@code source}
     *         based on the given {@code nodes}.
     *
     * @throws IllegalArgumentException
     *         If the source JSON element is an invalid JSON element.
     */
    @Override
    public JsonElement apply(JsonElement source, String nodes)
    {
        return apply(source, new NodeParser().parse(nodes));
    }


    private JsonElement apply(JsonElement source, Node node)
    {
        if (source == null || source instanceof JsonNull)
        {
            // The source is null.
            return JsonNull.INSTANCE;
        }

        if (source.isJsonObject())
        {
            // The source is a JSON object.
            return filterJsonObject((JsonObject)source, node);
        }

        if (source.isJsonArray())
        {
            // The source is a JSON array.
            return filterJsonArray((JsonArray)source, node);
        }

        // JsonPrimitive values can't be filtered. Then, in this case, the deep
        // copy of the original element is returned.
        return source.deepCopy();
    }


    private JsonElement filterJsonObject(JsonObject source, Node node)
    {
        // Sub nodes of the given node.
        List<Node> subNodes = node.getSubNodes();

        if (subNodes == null)
        {
            // Sub nodes are empty. This means there are no more nodes to look into.
            // Just return the deep-copy of the original source object.
            return source.deepCopy();
        }

        // Sub nodes are not empty. Then, we filter JSON elements from the source
        // based on the specified sub nodes.

        // The target JSON object we copy filtered JSON elements into.
        JsonObject filteredObject = new JsonObject();

        for (Node subNode : subNodes)
        {
            String keyOfSubNode = subNode.getKey();

            if (source.has(keyOfSubNode))
            {
                // Get the JSON element for the key from the source and then filter
                // it based on the sub node. Then, add the filtered element to the
                // target JSON object.
                JsonElement newSource       = source.get(keyOfSubNode);
                JsonElement filteredElement = apply(newSource, subNode);
                filteredObject.add(keyOfSubNode, filteredElement);
            }
        }

        return filteredObject;
    }


    private JsonElement filterJsonArray(JsonArray source, Node node)
    {
        // The target JSON array we copy JSON elements into.
        JsonArray target = new JsonArray();

        // For each JSON element in the JSON array.
        for (JsonElement e : source)
        {
            // Filter the JSON element based on the node. Then, add the filtered
            // element to the target JSON object.
            JsonElement filtered = apply(e, node);
            target.add(filtered);
        }

        return target;
    }
}
