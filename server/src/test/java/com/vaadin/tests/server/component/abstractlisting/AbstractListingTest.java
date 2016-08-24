package com.vaadin.tests.server.component.abstractlisting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vaadin.server.data.BackEndDataSource;
import com.vaadin.server.data.DataSource;
import com.vaadin.server.data.ListDataSource;
import com.vaadin.server.data.Query;
import com.vaadin.server.data.TypedDataGenerator;
import com.vaadin.ui.AbstractListing;

import elemental.json.JsonObject;

public class AbstractListingTest {

    private final class TestListing extends AbstractListing<String> {

        @Override
        public void addDataGenerator(TypedDataGenerator<String> generator) {
            super.addDataGenerator(generator);
        }

        @Override
        public void removeDataGenerator(TypedDataGenerator<String> generator) {
            super.removeDataGenerator(generator);
        }

        /**
         * Used to execute data generation
         */
        public void runDataGeneration() {
            super.getDataCommunicator().beforeClientResponse(true);
        }
    }

    private final class CountGenerator implements TypedDataGenerator<String> {

        int callCount = 0;

        @Override
        public void generateData(String data, JsonObject jsonObject) {
            ++callCount;
        }

        @Override
        public void destroyData(String data) {
        }
    }

    private static final String[] ITEM_ARRAY = new String[] { "Foo", "Bar",
            "Baz" };

    private TestListing listing;
    private List<String> items;

    @Before
    public void setUp() {
        items = new ArrayList<>(Arrays.asList(ITEM_ARRAY));
        listing = new TestListing();
    }

    @Test
    public void testSetItemsWithCollection() {
        listing.setItems(items);
        listing.getDataSource().apply(new Query()).forEach(
                str -> Assert.assertTrue("Unexpected item in data source",
                        items.remove(str)));
        Assert.assertTrue("Not all items from list were in data source",
                items.isEmpty());
    }

    @Test
    public void testSetItemsWithVarargs() {
        listing.setItems(ITEM_ARRAY);
        listing.getDataSource().apply(new Query()).forEach(
                str -> Assert.assertTrue("Unexpected item in data source",
                        items.remove(str)));
        Assert.assertTrue("Not all items from list were in data source",
                items.isEmpty());
    }

    @Test
    public void testSetDataSource() {
        ListDataSource<String> dataSource = DataSource.create(items);
        listing.setDataSource(dataSource);
        Assert.assertEquals("setDataSource did not set data source", dataSource,
                listing.getDataSource());
        listing.setDataSource(new BackEndDataSource<>(q -> Stream.of(ITEM_ARRAY)
                .skip(q.getOffset()).limit(q.getLimit()),
                q -> ITEM_ARRAY.length));
        Assert.assertNotEquals("setDataSource did not replace data source",
                dataSource, listing.getDataSource());
    }

    @Test
    public void testAddDataGeneartorBeforeDataSource() {
        CountGenerator generator = new CountGenerator();
        listing.addDataGenerator(generator);
        listing.setItems("Foo");
        listing.runDataGeneration();
        Assert.assertEquals("Generator should have been called once", 1,
                generator.callCount);
    }

    @Test
    public void testAddDataGeneartorAfterDataSource() {
        CountGenerator generator = new CountGenerator();
        listing.setItems("Foo");
        listing.addDataGenerator(generator);
        listing.runDataGeneration();
        Assert.assertEquals("Generator should have been called once", 1,
                generator.callCount);
    }

    @Test
    public void testRemoveDataGeneartor() {
        listing.setItems("Foo");
        CountGenerator generator = new CountGenerator();
        listing.addDataGenerator(generator);
        listing.removeDataGenerator(generator);
        listing.runDataGeneration();
        Assert.assertEquals("Generator should not have been called", 0,
                generator.callCount);
    }
}
