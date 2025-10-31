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

public enum CadtCompetence {
    DISCIPLINE("discipline"),
    CLIENT_ORIENTED("client-oriented"),
    ENGAGEMENT("engagement"),
    COOPERATION("cooperation"),
    LEADERSHIP("leadership"),
    RELATIONSHIPS("building-and-maintaining"),
    DIRECTION("direction"),
    MULTICULTURAL_SENSITIVITY("multicultural-sensitivity"),
    JUDGEMENT("judgement"),
    INDEPENDENCE("independence"),
    INITIATIVE("initiative"),
    GOAL_SETTING("goal-setting"),
    DECISIVENESS("decisiveness"),
    FUTURE("future"),
    COMMUNICATION_SKILLS("communication-skills"),
    BUSINESS_MINDED("business-minded"),
    TENACITY("tenacity"),
    CONSCIENTIOUSNESS("conscientiousness"),
    INTERPERSONAL_SENSITIVITY("interpersonal-sensitivity"),
    FLEXIBILITY("flexibility"),
    PERSUASIVENESS("persuasiveness"),
    INNOVATION("innovation"),
    PROBLEM_ANALYSIS("problem-analysis"),
    PLANIFICATION("planification");


    private final String tag;

    CadtCompetence(String answer) {
        this.tag = answer;
    }

    public String getTag() {
        return tag;
    }

    public static CadtCompetence fromTag(String tag) {
        for (CadtCompetence archetype : CadtCompetence.values()) {
            if (archetype.getTag().equalsIgnoreCase(tag)) {
                return archetype;
            }
        }
        return null;
    }

}
