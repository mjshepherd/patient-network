/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/
 */
package org.phenotips.data.similarity.configuration;

import org.xwiki.component.annotation.Role;
import org.xwiki.stability.Unstable;

/**
 * Configuration for the patient similarity feature.
 *
 * @version $Id$
 * @since 1.0M1
 */
@Unstable
@Role
public interface SimilarityConfiguration
{
    /**
     * The configured {@link FeatureSimilarityScorer feature scorer} to use.
     *
     * @return the name (component implementation hint) of the selected feature scorer, or {@code default} if none is
     *         configured
     */
    String getScorerType();
}
