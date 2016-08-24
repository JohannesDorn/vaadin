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
package com.vaadin.shared.ui.select;

import com.vaadin.shared.AbstractFieldState;

/**
 * Shared state for the AbstractSelect component.
 *
 * @since 7.6
 */
public class AbstractSelectState extends AbstractFieldState {

    {
        primaryStyleName = "v-select";
    }

    /**
     * Is the select in multiselect mode?
     *
     * Note that Table and Tree still use the old communication mechanism for
     * multi-mode support.
     */
    public boolean multiSelect;
}
