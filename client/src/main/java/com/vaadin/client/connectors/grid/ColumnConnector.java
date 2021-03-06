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
package com.vaadin.client.connectors.grid;

import com.vaadin.client.ServerConnector;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.widgets.Grid.Column;
import com.vaadin.shared.data.DataCommunicatorConstants;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.grid.ColumnState;

import elemental.json.JsonObject;
import elemental.json.JsonValue;

/**
 * A connector class for columns of the Grid component.
 *
 * @author Vaadin Ltd
 * @since
 */
@Connect(com.vaadin.ui.Grid.Column.class)
public class ColumnConnector extends AbstractExtensionConnector {

    private Column<JsonValue, JsonObject> column;

    /* This parent is needed because it's no longer available in onUnregister */
    private GridConnector parent;

    @Override
    protected void extend(ServerConnector target) {
        parent = getParent();
        column = new Column<JsonValue, JsonObject>() {

            @Override
            public JsonValue getValue(JsonObject row) {
                return row.getObject(DataCommunicatorConstants.DATA)
                        .get(getState().id);
            }
        };
        getParent().addColumn(column, getState().id);
    }

    @OnStateChange("caption")
    void updateCaption() {
        column.setHeaderCaption(getState().caption);
    }

    @OnStateChange("sortable")
    void updateSortable() {
        column.setSortable(getState().sortable);
    }

    @Override
    public void onUnregister() {
        super.onUnregister();

        parent.removeColumn(column);
        column = null;
    }

    @Override
    public GridConnector getParent() {
        return (GridConnector) super.getParent();
    }

    @Override
    public ColumnState getState() {
        return (ColumnState) super.getState();
    }

}
