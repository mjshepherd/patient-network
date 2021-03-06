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
package org.phenotips.data.similarity.internal;

import org.phenotips.data.Feature;
import org.phenotips.data.similarity.AccessType;
import org.phenotips.vocabulary.VocabularyTerm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * Implementation of {@link org.phenotips.data.similarity.FeatureClusterView} that reveals the full patient information
 * if the user has full access to the patient, and only matching reference information for similar features if the
 * patient is matchable.
 *
 * @version $Id$
 * @since 1.0M1
 */
public class RestrictedFeatureClusterView extends DefaultFeatureClusterView
{
    /**
     * Constructor passing the {@link #match matched feature} and the {@link #reference reference feature}.
     *
     * @param match the features in the matched patient, can be empty
     * @param reference the features in the reference patient, can be empty
     * @param access the access level of the match
     * @param root the root/shared ancestor for the cluster, can be {@code null} to represent unmatched features
     * @param score the score of the feature matching, or 0.0 if unmatched
     * @throws IllegalArgumentException if match or reference are null
     */
    public RestrictedFeatureClusterView(Collection<Feature> match, Collection<Feature> reference, AccessType access,
        VocabularyTerm root, double score) throws IllegalArgumentException
    {
        super(match, reference, access, root, score);
    }

    /**
     * {@inheritDoc} If access is matchable and there is exactly one reference phenotype that is identical to the
     * category (ancestor), report the category as the parent of the ancestor for privacy.
     *
     * @see org.phenotips.data.similarity.internal.DefaultFeatureClusterView#getRoot()
     */
    @Override
    public VocabularyTerm getRoot()
    {
        if (this.access.isPrivateAccess()) {
            return null;
        } else if (this.access.isLimitedAccess() && this.ancestor != null && this.reference.size() == 1) {
            String refId = this.reference.iterator().next().getId();
            String ancestorId = this.ancestor.getId();
            // Check if the reference and ancestor terms are the same term
            if (refId != null && StringUtils.equals(refId, ancestorId)) {
                Set<VocabularyTerm> parents = this.ancestor.getParents();
                if (parents.isEmpty()) {
                    return null;
                } else if (parents.size() == 1) {
                    return parents.iterator().next();
                } else {
                    // Get an arbitrary parent in a deterministic manner (lowest ID)
                    VocabularyTerm parent = Collections.min(parents, new Comparator<VocabularyTerm>()
                    {
                        @Override
                        public int compare(VocabularyTerm o1, VocabularyTerm o2)
                        {
                            String id1 = o1.getId();
                            String id2 = o2.getId();
                            return id1 == null || id2 == null ? 0 : id1.compareTo(id2);
                        }
                    });
                    return parent;
                }
            }
        }
        return super.getRoot();
    }

    @Override
    public double getScore()
    {
        if (this.access.isPrivateAccess()) {
            return 0.0;
        } else {
            return super.getScore();
        }
    }

    @Override
    public Collection<Feature> getMatch()
    {
        if (this.access.isPrivateAccess()) {
            return Collections.emptySet();
        } else if (this.access.isOpenAccess()) {
            return super.getMatch();
        } else {
            List<Feature> hiddenFeatures = new ArrayList<Feature>(this.match.size());
            for (int i = 0; i < this.match.size(); i++) {
                hiddenFeatures.add(null);
            }
            return Collections.unmodifiableCollection(hiddenFeatures);
        }
    }
}
