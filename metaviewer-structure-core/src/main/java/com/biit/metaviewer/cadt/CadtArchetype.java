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

public enum CadtArchetype {
    RECEPTIVE("receptive"),
    INNOVATOR("innovator"),
    STRATEGIST("strategist"),
    VISIONARY("visionary"),
    LEADER("leader"),
    BANKER("banker"),
    SCIENTIST("scientist"),
    TRADESMAN("tradesman");

    private final String tag;


    CadtArchetype(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public static CadtArchetype fromTag(String tag) {
        for (CadtArchetype archetype : CadtArchetype.values()) {
            if (archetype.getTag().equalsIgnoreCase(tag)) {
                return archetype;
            }
        }
        return null;
    }
}
