package com.biit.metaviewer.cadt;

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

import com.biit.metaviewer.Collection;
import com.biit.metaviewer.controllers.FormController;
import com.biit.metaviewer.providers.FormProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public abstract class CadtController extends FormController {

    public static final String FORM_NAME = "CADT_Score";

    private static final String FORM_SCORE_VARIABLE = "Score";

    private static final int RED_COLOR_LIMIT = 100;
    private static final int ORANGE_COLOR_LIMIT = 250;
    private static final int YELLOW_COLOR_LIMIT = 350;
    private static final int LIGHT_GREEN_COLOR_LIMIT = 450;

    protected CadtController(ObjectMapper objectMapper, FormProvider formProvider) {
        super(objectMapper, formProvider);
    }

    public Collection createCollection() {
        return createCollection(FORM_NAME);
    }

    @Override
    protected String getColor(String formName, Map<String, Object> formVariables) {
        final Object color = formVariables.get(FORM_SCORE_VARIABLE);
        if (color == null) {
            return null;
        }
        return getScoreColor(formName, (Double) color);
    }

    @Override
    protected int getRedColorLimit(String formName) {
        return RED_COLOR_LIMIT;
    }

    @Override
    protected int getOrangeColorLimit(String formName) {
        return ORANGE_COLOR_LIMIT;
    }

    @Override
    protected int getYellowColorLimit(String formName) {
        return YELLOW_COLOR_LIMIT;
    }

    @Override
    protected int getLightGreenColorLimit(String formName) {
        return LIGHT_GREEN_COLOR_LIMIT;
    }
}
