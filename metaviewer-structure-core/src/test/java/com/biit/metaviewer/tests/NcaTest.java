package com.biit.metaviewer.tests;

/*-
 * #%L
 * MetaViewer Structure (Core)
 * %%
 * Copyright (C) 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.metaviewer.Collection;
import com.biit.metaviewer.ObjectMapperFactory;
import com.biit.metaviewer.TestListener;
import com.biit.metaviewer.controllers.FormController;
import com.biit.utils.file.FileReader;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@Test(groups = "nca")
@Listeners(TestListener.class)
public class NcaTest extends AbstractTestNGSpringContextTests {
    protected static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "MetaViewer";
    private static final String DROOLS_FORM_FILE_PATH = "drools/NCA_1.json";
    private static final String FORM = "NCA";

    @Autowired
    private FormController formController;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void convertNcaToMetaviewer() throws IOException {
        final List<DroolsSubmittedForm> droolsSubmittedForms = List.of(DroolsSubmittedForm.getFromJson(FileReader
                .getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8)));

        final Collection collection = formController.createCollection(droolsSubmittedForms);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "nca-single.cxml")), true)) {
            out.println(ObjectMapperFactory.generateXml(collection));
        }
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "nca-single.json")), true)) {
            out.println(ObjectMapperFactory.generateJson(collection));
        }
    }

    //Depens on having NCA forms in Testing.
    @Test(enabled = false)
    public void convertFactsToMetaViewer() throws IOException {
        final Collection collection = formController.createCollection(FORM);
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "nca-score.cxml")), true)) {
            out.println(ObjectMapperFactory.generateXml(collection));
        }
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "nca-score.json")), true)) {
            out.println(ObjectMapperFactory.generateJson(collection));
        }
    }

    @Test
    public void jsonSerialization() throws IOException {
        final List<DroolsSubmittedForm> droolsSubmittedForms = List.of(DroolsSubmittedForm.getFromJson(FileReader
                .getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8)));
        final Collection collection = formController.createCollection(droolsSubmittedForms);
        String jsonCode = ObjectMapperFactory.generateJson(collection);
        final Collection importedCollection = objectMapper.readValue(jsonCode, Collection.class);
        Assert.assertEquals(collection.getItems().getItems().size(), importedCollection.getItems().getItems().size());
        Assert.assertEquals(collection, importedCollection);
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }

}
