/*
 * Copyright 2000-2016 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.tests.components.grid.basicfeatures.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.vaadin.tests.components.grid.basicfeatures.GridBasicFeaturesTest;
import com.vaadin.v7.testbench.customelements.GridElement;

public class GridScrollTest extends GridBasicFeaturesTest {

    @Test
    public void testCorrectItemRequestsOnScroll() {
        openTestURL();

        assertTrue("Initial push from server not found",
                getLogRow(1).equals("0. Requested items 0 - 40"));
        // Client response varies a bit between browsers as different amount of
        // rows is cached.
        assertTrue("First row request from client not found",
                getLogRow(0).startsWith("1. Requested items 0 - "));

        selectMenuPath("Component", "Size", "HeightMode Row");

        selectMenuPath("Settings", "Clear log");
        $(GridElement.class).first().scrollToRow(40);
        assertEquals("Log row did not contain expected item request",
                "0. Requested items 0 - 86", getLogRow(0));
        assertEquals("There should be only one log row", " ", getLogRow(1));
        selectMenuPath("Settings", "Clear log");
        $(GridElement.class).first().scrollToRow(100);
        assertEquals("Log row did not contain expected item request",
                "0. Requested items 47 - 146", getLogRow(0));
        assertEquals("There should be only one log row", " ", getLogRow(1));
        selectMenuPath("Settings", "Clear log");
        $(GridElement.class).first().scrollToRow(300);
        assertEquals("Log row did not contain expected item request",
                "0. Requested items 247 - 346", getLogRow(0));
        assertEquals("There should be only one log row", " ", getLogRow(1));
    }
}
