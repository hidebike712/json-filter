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
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * <p>
 * <i>NOTE: This class is intended for internal use only.</i>
 * </p>
 *
 * A class representing a hierarchical node.
 *
 * <h2>Node Properties</h2>
 * <p>A node has two properties: {@code key} and {@code subNodes}.</p>
 *
 * <table border="1" cellspacing="0" cellpadding="5">
 * <tr>
 * <th>Property</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td><code>key</code></td>
 * <td>The unique identifier or label of this node.</td>
 * </tr>
 * <tr>
 * <td><code>subNodes</code></td>
 * <td>A list of child nodes representing the hierarchical structure.</td>
 * </tr>
 * </table>
 *
 * <h2>String Representation</h2>
 * <p>
 * The string representation of a node follows this regular expression:
 * </p>
 *
 * <pre style="border:1px solid lightgray; padding:5px;">{@code
 * ^([a-zA-Z0-9_]*)(\(([a-zA-Z0-9,()_]*)\))?$
 * }</pre>
 *
 * <h2>Examples</h2>
 * <p>Here are some example node structures:</p>
 *
 * <h3>Example 1: A node with a single sub-node.</h3>
 * <pre style="border:1px solid lightgray; padding:5px;">{@code
 * a(b)
 * }</pre>
 *
 * <h3>Example 2: A node with multiple sub-nodes.</h3>
 * <pre style="border:1px solid lightgray; padding:5px;">{@code
 * a(b,c,d)
 * }</pre>
 *
 * <h3>Example 3: A node with a nested sub-node and a non-nested sub-node.</h3>
 * <pre style="border:1px solid lightgray; padding:5px;">{@code
 * a(b(c),d)
 * }</pre>
 *
 * @author Hideki Ikeda
 */
class Node
{
    private final String key;
    private final List<Node> subNodes;


    /**
     * Constructs a {@link Node} instance from the given key and sub nodes.
     *
     * @param key
     *         The key of the node.
     *
     * @param subNodes
     *         The sub nodes of the node.
     */
    Node(String key, List<Node> subNodes)
    {
        this.key      = key;
        this.subNodes = subNodes;
    }


    /**
     * Returns the key of the node.
     *
     * @return
     *         The key of the node.
     */
    String getKey()
    {
        return key;
    }


    /**
     * Returns the sub nodes of the node.
     *
     * @return
     *         The sub nodes of the node.
     */
    List<Node> getSubNodes()
    {
        return subNodes;
    }


    /**
     * Converts this node to the string representation.
     *
     * <p>
     * For instance, if a node meets the following conditions:
     *
     * <ul>
     * <li>The key of the node is {@code "a"}.</li>
     * <li>The first sub node of the node has {@code "c"} as its key and {@code
     * "d"} as its sub node.</li>
     * <li>The second sub node of the node has {@code "d"} as its key and no sub
     * nodes.</li>
     * </ul>
     *
     * then, the string representation of the node is {@code "a(b(c),d)"}.
     * </p>
     *
     * @return
     *         The string representation of this node.
     */
    @Override
    public String toString()
    {
        if (subNodes == null)
        {
            return key;
        }

        // The string representation of the node.
        return new StringBuilder(key)
                .append('(')
                .append(subNodes.stream().map(Node::toString).collect(Collectors.joining(",")))
                .append(')')
                .toString();
    }


    /**
     * Compares this {@link Node} object with the specified object for equality.
     * The comparison is based on the values of {@code key} and {@code subNodes}.
     *
     * @param obj
     *         The object to be compared for equality with this {@link Node} object.
     *
     * @return
     *         {@code true} if the specified object is equal to this {@link Node}
     *         object.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }

        Node other = (Node)obj;

        // Compare all components for equality.
        return Objects.equals(this.key, other.key) &&
               Objects.equals(this.subNodes, other.subNodes);
    }


    /**
     * Returns the hash code value for this {@link Node} object. The hash code is
     * generated based on the values of {@code key} and {@code sunNodes} components.
     *
     * @return
     *         The hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(key, subNodes);
    }
}
