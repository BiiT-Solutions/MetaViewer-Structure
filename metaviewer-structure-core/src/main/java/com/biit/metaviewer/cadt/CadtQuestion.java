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

public enum CadtQuestion {
    QUESTION1("nhm_card_choice1"),
    QUESTION2("nhm_card_choice2"),
    QUESTION3("nhm_card_choice3"),
    QUESTION4("nhm_card_choice4"),
    QUESTION5("nhm_card_choice5"),
    QUESTION6("nhm_card_choice6"),

    COMPETENCES("competency_choice");

    private final String tag;


    CadtQuestion(String answer) {
        this.tag = answer;
    }

    public String getTag() {
        return tag;
    }
}
