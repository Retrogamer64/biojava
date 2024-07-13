package org.biojava.nbio.alignment.routines;

import java.io.Serializable;
import java.util.Comparator;

public class AlignerHelperQueryIndexComparator {
    public static class QueryIndexComparator implements Comparator<AlignerHelperAnchor.Anchor>, Serializable {
        private static final long serialVersionUID = 1;

        @Override
        public int compare(AlignerHelperAnchor.Anchor o1, AlignerHelperAnchor.Anchor o2) {
            return o1.getQueryIndex() - o2.getQueryIndex();
        }
    }
}
