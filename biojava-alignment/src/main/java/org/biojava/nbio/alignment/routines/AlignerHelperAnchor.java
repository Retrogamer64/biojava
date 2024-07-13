package org.biojava.nbio.alignment.routines;

import java.io.Serializable;
import java.util.Comparator;

public class AlignerHelperAnchor {
    /**
     * Compounds in query and target sequences that must align
     * @author Daniel Cameron
     */
    public static class Anchor {
        public int getQueryIndex() {
            return queryIndex;
        }
        public int getTargetIndex() {
            return targetIndex;
        }
        public final int queryIndex;
        public final int targetIndex;
        public Anchor(int queryIndex, int targetIndex) {
            this.queryIndex = queryIndex;
            this.targetIndex = targetIndex;
        }
    }
}
