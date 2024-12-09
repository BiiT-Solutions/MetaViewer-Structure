package com.biit.metaviewer.tests;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.metaviewer.Collection;
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.TestListener;
import com.biit.metaviewer.cadt.CadtController;
import com.biit.utils.file.FileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@SpringBootTest
@Test(groups = "cadtScore")
@Listeners(TestListener.class)
public class CadtScoreTest extends AbstractTestNGSpringContextTests {
    protected static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "MetaViewer";

    @Autowired
    private CadtController cadtController;

    private static final String DROOLS_FORM_FILE_PATH = "drools/CADT_Score_2.json";

    protected boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }


    @Test
    public void convertScoresToMetaViewer() throws IOException {
        final List<DroolsSubmittedForm> droolsSubmittedForms = List.of(DroolsSubmittedForm.getFromJson(FileReader
                .getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8)));

        final Collection collection = cadtController.createCollection(droolsSubmittedForms);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "cadt-single.cxml")), true)) {
            out.println(ObjectMapperFactory.generateXml(collection));
        }
    }

    @Test
    public void convertFactsToMetaViewer() throws IOException {
        final Collection collection = cadtController.createCollection();
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "cadt.cxml")), true)) {
            out.println(ObjectMapperFactory.generateXml(collection));
        }
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
