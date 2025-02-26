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
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;


public class NodeMergerTest
{
    private final NodeMerger merger = new NodeMerger();


    @Test
    @DisplayName("merge() should return null when input is null")
    void testMerge1()
    {
        assertNull(merger.merge(null));
    }


    @Test
    @DisplayName("merge single node 'a' without sub-nodes")
    void testMerge2()
    {
        Node node = new Node("a", null);
        Node merged = merger.merge(node);
        assertEquals("a", merged.toString());
    }


    @Test
    @DisplayName("merge single node 'a' with empty sub-nodes")
    void testMerge3()
    {
        Node node = new Node("a", Collections.emptyList());
        Node merged = merger.merge(node);
        assertEquals("a()", merged.toString());
    }


    @Test
    @DisplayName("merge duplicate 'a' nodes with different sub-nodes 'b' and 'c'")
    void testMerge4()
    {
        Node a1 = new Node("a", Collections.singletonList(new Node("b", null)));
        Node a2 = new Node("a", Collections.singletonList(new Node("c", null)));
        Node root = new Node("ROOT", Arrays.asList(a1, a2));
        Node merged = merger.merge(root);
        assertEquals("ROOT(a(b,c))", merged.toString());
    }


    @Test
    @DisplayName("merge duplicate nodes 'a', one with null sub-nodes and one with sub-node 'b'")
    void testMerge5()
    {
        Node a1 = new Node("a", null);
        Node a2 = new Node("a", Collections.singletonList(new Node("b", null)));
        Node root = new Node("ROOT", Arrays.asList(a1, a2));
        Node merged = merger.merge(root);
        assertEquals("ROOT(a(b))", merged.toString());
    }


    @Test
    @DisplayName("merge duplicate 'b' nodes under 'a' with different sub-nodes 'c' and 'd'")
    void testMerge6()
    {
        Node b1 = new Node("b", Collections.singletonList(new Node("c", null)));
        Node b2 = new Node("b", Collections.singletonList(new Node("d", null)));
        Node a = new Node("a", Arrays.asList(b1, b2));
        Node merged = merger.merge(a);
        assertEquals("a(b(c,d))", merged.toString());
    }


    @Test
    @DisplayName("merge complex scenario: ROOT with mixed duplicate and single nodes")
    void testMerge7()
    {
        Node a1 = new Node("a", null);
        Node a2 = new Node("a", Collections.singletonList(new Node("d", null)));
        Node b  = new Node("b", Collections.singletonList(new Node("c", null)));
        Node root = new Node("ROOT", Arrays.asList(a1, b, a2));
        Node merged = merger.merge(root);
        assertEquals("ROOT(a(d),b(c))", merged.toString());
    }
}
