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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class NodeTest
{
    @Test
    @DisplayName("convert single node to string")
    void testToString1()
    {
        Node node = new Node("a", null);
        assertEquals("a", node.toString());
    }


    @Test
    @DisplayName("convert node with empty sub-nodes to string")
    void testToString2()
    {
        Node node = new Node("a", Collections.emptyList());
        assertEquals("a()", node.toString());
    }


    @Test
    @DisplayName("convert node with one sub-node to string")
    void testToString3()
    {
        Node node = new Node("a", Collections.singletonList(new Node("b", null)));
        assertEquals("a(b)", node.toString());
    }


    @Test
    @DisplayName("convert node with multiple sub-nodes to string")
    void testToString4()
    {
        Node node = new Node(
            "a",
            Arrays.asList(new Node("b", null), new Node("c", null))
        );
        assertEquals("a(b,c)", node.toString());
    }


    @Test
    @DisplayName("convert nested node structure to string")
    void testToString5()
    {
        Node node = new Node(
            "a",
            Arrays.asList(new Node("b", Collections.singletonList(new Node("c", null))), new Node("d", null))
        );
        assertEquals("a(b(c),d)", node.toString());
    }


    @Test
    @DisplayName("compare node with itself")
    void testToEquals1()
    {
        Node node = new Node("a", null);
        assertTrue(node.equals(node));
    }


    @Test
    @DisplayName("compare identical nodes with sub-nodes")
    void testToEquals2()
    {
        Node node1 = new Node("a", Collections.singletonList(new Node("b", null)));
        Node node2 = new Node("a", Collections.singletonList(new Node("b", null)));
        assertTrue(node1.equals(node2));
    }


    @Test
    @DisplayName("compare nodes with different keys")
    void testToEquals3()
    {
        Node node1 = new Node("a", null);
        Node node2 = new Node("b", null);
        assertFalse(node1.equals(node2));
    }


    @Test
    @DisplayName("compare nodes with different sub-node keys")
    void testToEquals4()
    {
        Node node1 = new Node("a", Collections.singletonList(new Node("b", null)));
        Node node2 = new Node("a", Collections.singletonList(new Node("c", null)));
        assertFalse(node1.equals(node2));
    }


    @Test
    @DisplayName("generate identical hash codes for identical nodes")
    void testHashCode1()
    {
        Node node1 = new Node("a", Arrays.asList(new Node("b", null), new Node("c", null)));
        Node node2 = new Node("a", Arrays.asList(new Node("b", null), new Node("c", null)));
        assertEquals(node1.hashCode(), node2.hashCode());
    }


    @Test
    @DisplayName("retrieve key from node")
    void testGetKey1()
    {
        Node node = new Node("a", null);
        assertEquals("a", node.getKey());
    }


    @Test
    @DisplayName("retrieve sub-nodes from node")
    void testGetSubNodes1()
    {
        List<Node> subNodes = Collections.singletonList(new Node("b", null));
        Node node = new Node("a", subNodes);
        assertEquals(subNodes, node.getSubNodes());
    }
}
