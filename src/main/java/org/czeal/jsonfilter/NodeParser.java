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


import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * <p>
 * <i>NOTE: This class is intended for internal use only.</i>
 * </p>
 *
 * A class for parsing a given string into a {@link Node} object.
 *
 * <p>
 * This class processes a structured string representing a hierarchy of nodes
 * and converts it into a corresponding {@link Node} tree. The input format is
 * expected to be a comma-separated list where nested nodes are enclosed in parentheses.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <h3>Example 1: A normal node.</h3>
 *
 * <pre>
 * Node rootNode = new NodeParser().parse("a,b(c)");
 *
 * System.out.println(rootNode.getKey());      // "ROOT"
 * Node nodeA = rootNode.getSubNodes().get(0);
 * System.out.println(nodeA.getKey());         // "a"
 * System.out.println(nodeA.getSubNodes());    // null
 * Node nodeB = rootNode.getSubNodes().get(1);
 * System.out.println(nodeB.getKey());         // "b"
 * Node nodeC = nodeB.getSubNodes().get(0);
 * System.out.println(nodeC.getKey());         // "c"
 * System.out.println(nodeC.getSubNodes());    // null
 * </pre>
 *
 * <h3>Example 2: Nodes having the same key are merged.</h3>
 *
 * <pre>
 * Node rootNode = new NodeParser().parse("a(b(c),b(d))");
 *
 * System.out.println(rootNode.getKey());      // "ROOT"
 * Node nodeA = rootNode.getSubNodes().get(0);
 * System.out.println(nodeA.getKey());         // "a"
 * Node nodeB = rootNode.getSubNodes().get(1);
 * System.out.println(nodeB.getKey());         // "b"
 * Node nodeC = nodeB.getSubNodes().get(0);
 * System.out.println(nodeC.getKey());         // "c"
 * System.out.println(nodeC.getSubNodes());    // null
 * Node nodeD = nodeB.getSubNodes().get(1);
 * System.out.println(nodeD.getKey());         // "d"
 * System.out.println(nodeD.getSubNodes());    // null
 * </pre>
 *
 * @author Hideki Ikeda
 */
class NodeParser
{
    /**
     * The pattern that expresses a node.
     */
    private static final Pattern PATTERN = Pattern.compile("^([a-zA-Z0-9_\\s]*)(\\(([a-zA-Z0-9,()_\\s]*)\\))?$");


    /**
     * Helper method to split the input by the top-level commas.
     */
    private static List<String> splitByTopLevelCommas(String input)
    {
        List<String> tokens = new LinkedList<>();

        int depth      = 0;
        int tokenStart = 0;

        // Process tokens.
        for (int i = 0; i < input.length(); i++)
        {
            char c = input.charAt(i);

            if (c == '(')
            {
                depth++;
            }
            else if (c == ')')
            {
                depth--;
            }
            else if (c == ',' && depth == 0)
            {
                tokens.add(input.substring(tokenStart, i).trim());
                tokenStart = i + 1;
            }
        }

        // Process the last token.
        tokens.add(input.substring(tokenStart).trim());

        return tokens;
    }


    /**
     * Parses the given input string into a {@link Node} object.
     *
     * <p>
     * The input should be a comma-separated string with optional nested nodes
     * enclosed in parentheses.
     * </p>
     *
     * <h3>Example Usage</h3>
     *
     * <h4>Example 1: A normal node.</h4>
     * <pre>
     * Node rootNode = new NodeParser().parse("a,b(c)");
     *
     * System.out.println(rootNode.getKey());      // "ROOT"
     * Node nodeA = rootNode.getSubNodes().get(0);
     * System.out.println(nodeA.getKey());         // "a"
     * System.out.println(nodeA.getSubNodes());    // null
     * Node nodeB = rootNode.getSubNodes().get(1);
     * System.out.println(nodeB.getKey());         // "b"
     * Node nodeC = nodeB.getSubNodes().get(0);
     * System.out.println(nodeC.getKey());         // "c"
     * System.out.println(nodeC.getSubNodes());    // null
     * </pre>
     *
     * <h4>Example 2: Nodes having the same key are merged.</h4>
     * <pre>
     * Node rootNode = new NodeParser().parse("a(b(c),b(d))");
     *
     * System.out.println(rootNode.getKey());      // "ROOT"
     * Node nodeA = rootNode.getSubNodes().get(0);
     * System.out.println(nodeA.getKey());         // "a"
     * Node nodeB = rootNode.getSubNodes().get(1);
     * System.out.println(nodeB.getKey());         // "b"
     * Node nodeC = nodeB.getSubNodes().get(0);
     * System.out.println(nodeC.getKey());         // "c"
     * System.out.println(nodeC.getSubNodes());    // null
     * Node nodeD = nodeB.getSubNodes().get(1);
     * System.out.println(nodeD.getKey());         // "d"
     * System.out.println(nodeD.getSubNodes());    // null
     * </pre>
     *
     * @param input
     *         The input string representing nodes. This should be a comma-separated
     *         string with optional nested nodes enclosed in parentheses (e.g. {@code
     *         "a,b,c(d,e(f))"}).
     *
     * @return
     *         A parsed {@link Node} object, or {@code null} if the input is {@code
     *         null}.
     */
    Node parse(String input)
    {
        // Create the top-level node.
        //
        // [Node Info]
        //   - key: "ROOT"
        //   - sub nodes: Nodes generated by parsing the input.
        Node parsed = createNode("ROOT", input);

        // Merge the parsed node if necessary.
        return new NodeMerger().merge(parsed);
    }


    private Node createNode(String key, String nodesAsString)
    {
        return new Node(key.trim(), parseNodes(nodesAsString));
    }


    private Node parseNode(String input)
    {
        // Matcher for a single node.
        Matcher m = PATTERN.matcher(input);

        // If the input does not match the pattern, throw an exception.
        if (!m.matches())
        {
            throw new IllegalArgumentException(
                    String.format("The node format is invalid: '%s'.", input));
        }

        // Create the top-level node.
        //
        // [Node Info]
        //   - key: The first matched group.
        //   - sub nodes: Nodes generated by parsing the the third matched group.
        return createNode(m.group(1), m.group(3));
    }


    private List<Node> parseNodes(String input)
    {
        if (input == null)
        {
            return null;
        }

        // Split the input string by the top-level commas after trimming it.
        List<String> topLevelNodes = splitByTopLevelCommas(input);

        // Parse each string in the list into a Node object and collect them as
        // a list.
        return topLevelNodes.stream()
                            .map(this::parseNode)
                            .collect(Collectors.toList());
    }
}
