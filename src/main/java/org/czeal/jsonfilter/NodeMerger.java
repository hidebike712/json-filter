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


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * <i>NOTE: This class is intended for internal use only.</i>
 * </p>
 *
 * A class for merging {@link Node} objects.
 *
 * <p>
 * This class provides methods to merge nodes and their sub-nodes, ensuring that
 * duplicate root nodes are consolidated while preserving the relationships between
 * parent and child paths.
 * </p>
 *
 * <p>
 * The merging process follows these principles:
 * </p>
 * <ul>
 * <li>
 * If a path has no sub-paths ({@code null}), it is retained in the merged
 * structure.
 * </li>
 * <li>
 * If a root appears multiple times with different sub-paths, they are combined
 * into one.
 * </li>
 * </ul>
 *
 * <p>
 * This ensures a well-structured and non-redundant hierarchical JSON representation.
 * </p>
 *
 * @author Hideki Ikeda
 */
class NodeMerger
{
    /**
     * Recursively merges the sub nodes of the given {@code Node}.
     *
     * <p>
     * This method ensures that if multiple sub nodes exist under the same root,
     * they are properly merged into a single hierarchical structure.
     * </p>
     *
     * @param node
     *         The {@link Node} object whose sub nodes need to be merged.
     *
     * @return A new {@link Node} instance with its sub nodes merged.
     */
    Node merge(Node node)
    {
        return node == null ? null : new Node(node.getKey(), merge(node.getSubNodes()));
    }


    /**
     * Merges a list of paths by grouping sub-paths under their respective root
     * names.
     *
     * <p>
     * This method ensures:
     * <ul>
     * <li>
     * If a path has no sub-paths ({@code null}), it is retained in the merged
     * structure.
     * </li>
     * <li>
     * If a root appears multiple times with different sub-paths, they are
     * combined into one.
     * </li>
     * </ul>
     * </p>
     *
     * <p>
     * The merging process maintains a hierarchical tree structure, preventing
     * duplication of root nodes while preserving the relationships between parent
     * and child paths.
     * </p>
     *
     * @param nodes
     *         The list of {@code Path} objects to be merged.
     *
     * @return A new {@code List<Path>} containing merged paths where duplicate
     *         roots are consolidated into one.
     */
    private List<Node> merge(List<Node> nodes)
    {
        if (nodes == null)
        {
            return nodes;
        }

        Map<String, List<Node>> map = new LinkedHashMap<>();

        for (Node node : nodes)
        {
            String key          = node.getKey();
            List<Node> subNodes = node.getSubNodes();

            if (subNodes == null)
            {
                if (!map.containsKey(key))
                {
                    map.put(key, null);
                }
            }
            else
            {
                map.putIfAbsent(key, new ArrayList<Node>());
                map.get(key).addAll(subNodes);
            }
        }

        return map.entrySet()
                  .stream()
                  .map(e -> new Node(e.getKey(), merge(e.getValue())))
                  .collect(Collectors.toList());
    }
}
