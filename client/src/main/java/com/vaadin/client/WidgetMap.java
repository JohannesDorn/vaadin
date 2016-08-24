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
package com.vaadin.client;

import java.util.HashMap;

/**
 * Abstract class mapping between {@link ComponentConnector} instances and their
 * instances.
 *
 * A concrete implementation of this class is generated by WidgetMapGenerator or
 * one of its subclasses during widgetset compilation.
 */
abstract class WidgetMap {

    protected static HashMap<Class<? extends ServerConnector>, WidgetInstantiator> instmap = new HashMap<Class<? extends ServerConnector>, WidgetInstantiator>();

    /**
     * Create a new instance of a connector based on its type.
     *
     * @param classType
     *            {@link ComponentConnector} class to instantiate
     * @return new instance of the connector
     */
    public ServerConnector instantiate(
            Class<? extends ServerConnector> classType) {
        return instmap.get(classType).get();
    }

    /**
     * Return the connector class to use for a fully qualified server side
     * component class name.
     *
     * @param fullyqualifiedName
     *            fully qualified name of the server side component class
     * @return component connector class to use
     */
    public abstract Class<? extends ServerConnector> getConnectorClassForServerSideClassName(
            String fullyqualifiedName);

    /**
     * Return the connector classes to load after the initial widgetset load and
     * start.
     *
     * @return component connector class to load after the initial widgetset
     *         loading
     */
    public abstract Class<? extends ServerConnector>[] getDeferredLoadedConnectors();

    /**
     * Make sure the code for a (deferred or lazy) component connector type has
     * been loaded, triggering the load and waiting for its completion if
     * necessary.
     *
     * @param classType
     *            component connector class
     */
    public abstract void ensureInstantiator(
            Class<? extends ServerConnector> classType);

}
