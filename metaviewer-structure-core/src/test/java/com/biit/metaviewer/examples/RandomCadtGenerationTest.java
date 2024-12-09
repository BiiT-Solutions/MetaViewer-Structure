package com.biit.metaviewer.examples;

import com.biit.metaviewer.Collection;
import com.biit.metaviewer.Facet;
import com.biit.metaviewer.FacetCategory;
import com.biit.metaviewer.Item;
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.TestListener;
import com.biit.metaviewer.types.DateTimeType;
import com.biit.metaviewer.types.NumberType;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Test(groups = "cadt")
@Listeners(TestListener.class)
public class RandomCadtGenerationTest {
    protected static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "MetaViewer";

    public static final Random RANDOM = new Random();
    private final static int TOTAL_ITEMS = 1000 + RANDOM.nextInt(300);
    private final static int MAX_SCORE = 520;
    private final static String[] ARCHETYPES = {"universal", "society", "vision", "strength", "material attachment", "communication", "self aware", "analysis"};
    private final static String[] BALANCES = {"structure-inspiration", "adaptability-action"};
    private final static int[] ARCHETYPES_VALUES = {101, 101, 101, 101, 89, 89, 89, 89, 55, 55, 55, 49, 49, 49, 49, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -20, -20, -20, -26, -26, -26, -61, -61};
    private final static int[] BALANCES_VALUES = {110, 110, 110, 110, 85, 85, 85, 85, 56, 56, 56, 33, 33, 33, 33, 33, 16, 16, 16, 0, 0, 0, 0, 0, 0, 0};
    private int items = 0;


    protected boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    private Collection createCollection() {
        final Collection collection = new Collection("CADT", "./dz_haagse_passage/haagse_passage.dzc");
        collection.getFacetCategories().add(new FacetCategory("createdAt", DateTimeType.PIVOT_VIEWER_DEFINITION));
        for (String archetype : ARCHETYPES) {
            collection.getFacetCategories().add(new FacetCategory(archetype, NumberType.PIVOT_VIEWER_DEFINITION));
        }
        for (String balance : BALANCES) {
            collection.getFacetCategories().add(new FacetCategory(balance, NumberType.PIVOT_VIEWER_DEFINITION));
        }
        return collection;
    }

    private int getValue(int[] values) {
        //Gaussian * sqrt(variance) + mean;
        int selected = (int) (RANDOM.nextGaussian() * (values.length / 4) + (values.length / 2));
        if (selected >= values.length) {
            selected = values.length - 1;
        }
        if (selected < 0) {
            selected = 0;
        }
        return values[selected];
    }

    private String getColor(int score) {
        if (score < 100) {
            return "#1";
        }
        if (score < 250) {
            return "#2";
        }
        if (score < 350) {
            return "#3";
        }
        if (score < 450) {
            return "#4";
        }
        return "#5";
    }

    private LocalDateTime getCreatedAt() {
        LocalDateTime randomDate;
        do {
            long minDay = LocalDateTime.of(2022, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
            long maxDay = LocalDateTime.of(2024, 9, 30, 23, 59).toEpochSecond(ZoneOffset.UTC);
            //Gaussian * sqrt(variance) + mean;
            long randomTime = (long) (RANDOM.nextGaussian() * (maxDay - minDay) / 4 + ((maxDay - minDay) / 2 + minDay));
            randomDate = LocalDateTime.ofEpochSecond(randomTime, 0, ZoneOffset.UTC);
        } while (!randomDate.query(new DateQuery()));
        if (randomDate.isAfter(LocalDateTime.now())) {
            randomDate = randomDate.withYear(LocalDateTime.now().getYear());
            randomDate = randomDate.withMonth(LocalDateTime.now().getMonthValue() - 1);
        }
        return randomDate;
    }

    private Item createCadtItem() {
        //Score by archetypes
        final List<Facet<?>> facets = new ArrayList<>();
        double total = 0;

        facets.add(new Facet<>("createdAt", new DateTimeType(getCreatedAt())));

        int selectedArchetypes = 0;
        for (int i = 0; i < ARCHETYPES.length; i++) {
            double value;
            do {
                value = getValue(ARCHETYPES_VALUES);
            } while (total + value > MAX_SCORE && value != 0);
            if (i == ARCHETYPES.length / 2 - 1) {
                //No feminine, update counter.
                selectedArchetypes = 0;
            }
            //Only two archetype selected.
            if (selectedArchetypes > 2 && value > 0) {
                value = 0;
            }
            if (value > 0) {
                selectedArchetypes++;
            }
            total += value;
            facets.add(new Facet<>(ARCHETYPES[i], new NumberType(value)));
        }

        for (String balance : BALANCES) {
            double value;
            do {
                value = getValue(BALANCES_VALUES);
            } while (total + value > MAX_SCORE && value != 0);
            total += value;
            facets.add(new Facet<>(balance, new NumberType(value)));
        }

        //Total Score by rules
        final Item item = new Item(getColor((int) total), "/cadt", "Test " + items++);
        item.getFacets().addAll(facets);
        return item;
    }

    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }

    @Test
    public void generateData() throws IOException {
        final Collection collection = createCollection();
        for (int i = 0; i < TOTAL_ITEMS; i++) {
            collection.getItems().getItems().add(createCadtItem());
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "cadt.cxml")), true)) {
            out.println(ObjectMapperFactory.generateXml(collection));
        }

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "cadt.json")), true)) {
            out.println(ObjectMapperFactory.generateJson(collection));
        }
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }


    static class DateQuery implements TemporalQuery<Boolean> {

        @Override
        public Boolean queryFrom(TemporalAccessor temporal) {
            //Discard weekends.
            if (temporal.get(ChronoField.DAY_OF_WEEK) >= 5) {
                return false;
            }
            //Discard invalid time.
            if (temporal.get(ChronoField.HOUR_OF_DAY) < 9 || temporal.get(ChronoField.HOUR_OF_DAY) > 17) {
                return false;
            }
            return true;
        }
    }

}
