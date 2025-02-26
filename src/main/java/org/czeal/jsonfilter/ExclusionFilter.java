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
 * A filter implementation that excludes specified JSON elements from a given JSON
 * element. The exclusion rules are defined using a comma-separated string where
 * nested fields can be specified using parentheses.
 *
 * <p>Example of node specification:</p>
 * <ul>
 * <li>{@code "a,b(c,d)"} - Excludes top-level elements {@code a} and sub-elements
 * {@code c} and {@code d} inside {@code b}.</li>
 * <li>{@code "x(y)"} - Excludes the sub-element {@code y} inside {@code x}, while
 * keeping other elements in {@code x}.</li>
 * </ul>
 *
 * <p>
 * If the input JSON is a primitive value or {@code null}, it is returned as is.
 * </p>
 *
 * @author Hideki Ikeda
 */
public class ExclusionFilter implements Filter
{
    /**
     * Creates a JSON element by excluding JSON elements from the given JSON element
     * based on the nodes string. The nodes string is provided as a comma-separated
     * string, where each node defines a portion of the JSON structure to exclude
     * from the result. Nested fields can be specified using parentheses. For example,
     * a nodes string might be {@code "a,b(c,d)"}, indicating that the output should
     * not include the top-level element {@code a} and sub-elements {@code c} and
     * {@code d} inside {@code b}.
     *
     * <p><b>Examples:</b></p>
     *
     * <p><b>Example 1: For a flat JSON object</b></p>
     * <pre>
     * Gson gson = new Gson();
     *
     * JsonElement jsonElement = gson.fromJson("{\"a\":1,\"b\":2,\"c\":3}");
     *
     * JsonElement filtered = new ExclusionFilter().apply(jsonElement, "a,b");
     *
     * System.out.println(filtered); // {"c":3}
     * </pre>
     *
     * <p><b>Example 2: For a nested JSON object</b></p>
     * <pre>
     * Gson gson = new Gson();
     *
     * JsonElement jsonElement = gson.fromJson("{\"x\":{\"y\":{\"z\":5},\"w\":10},\"v\":20}");
     *
     * JsonElement filtered = new ExclusionFilter().apply(jsonElement, "x(y)");
     *
     * System.out.println(filtered); // {"x":{"w":10},"v":20}
     * </pre>
     *
     * <p><b>Example 3: For nested arrays</b></p>
     * <pre>
     * Gson gson = new Gson();
     *
     * JsonElement jsonElement = gson.fromJson("[[[{\"name\":\"john\",\"type\":0}]]]");
     *
     * JsonElement filtered = new ExclusionFilter().apply(jsonElement, "name");
     *
     * System.out.println(filtered); // [[[{"type":0}]]]
     * </pre>
     *
     * <p>
     * If the source JSON is a primitive or {@code null}, filtering is not applicable
     * and the deep copy of the source is returned unmodified.
     * </p>
     *
     * <p>
     * Invoking this method is equivalent to invoking {@link #filter(JsonElement, Node)}{@code
     * (source, new NodeParser.parse(nodes))}.
     * </p>
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
     *         source} based on the given {@code nodes}.
     *
     * @throws IllegalArgumentException
     *         If the source JSON element is an invalid JSON element.
     */
    @Override
    public JsonElement apply(JsonElement source, String nodes)
    {
        return filter(source, new NodeParser().parse(nodes));
    }


    private JsonElement filter(JsonElement source, Node node)
    {
        if (source == null || source instanceof JsonNull)
        {
            // The source is null.
            return JsonNull.INSTANCE;
        }

        if (node == null)
        {
            // If no path is specified, we don't need to filter out anything from
            // the original source.
            return source.deepCopy();
        }

        List<Node> subNodes = node.getSubNodes();

        if (subNodes == null)
        {
            // If the sub nodes are not specified, a JsonNull instance is returned.
            // This indicates the source element should be removed from its parent
            // node.
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
        // Deep copy the source object.
        JsonObject copiedSource = source.deepCopy();

        for (Node subNode : node.getSubNodes())
        {
            String keyOfSubNode = subNode.getKey();

            if (copiedSource.has(keyOfSubNode))
            {
                // Get the JSON element for the key from the source and then filter
                // it based on the sub node.
                JsonElement newSource = copiedSource.get(keyOfSubNode);
                JsonElement filtered = filter(newSource, subNode);

                // If the filtered element is an JsonNull instance, it indicates that
                // the element should be removed from the source. Otherwise, it should
                // replace the existing element for the key.
                if (filtered instanceof JsonNull)
                {
                    copiedSource.remove(keyOfSubNode);
                }
                else
                {
                    copiedSource.add(keyOfSubNode, filtered);
                }
            }
        }

        return copiedSource;
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
            JsonElement filtered = filter(e, node);
            target.add(filtered);
        }

        return target;
    }
}
