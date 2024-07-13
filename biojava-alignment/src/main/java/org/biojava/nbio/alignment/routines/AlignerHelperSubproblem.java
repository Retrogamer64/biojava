package org.biojava.nbio.alignment.routines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlignerHelperSubproblem {
    /**
     * Alignment subproblem. The bounds of the subproblem are the
     * indicies representing the inclusive bounds of the dynamic programming
     * alignment problem.
     * @author Daniel Cameron
     */
    public static class Subproblem {
        public int getTargetStartIndex() {
            return targetStartIndex;
        }
        public int getQueryEndIndex() {
            return queryEndIndex;
        }
        public int getTargetEndIndex() {
            return targetEndIndex;
        }
        public int getQueryStartIndex() {
            return queryStartIndex;
        }
        /**
         * Indicates whether the start query and start target index compounds
         * are anchored to each other
         * @return true if the compounds are anchored in the alignment, false otherwise
         */
        public boolean isStartAnchored() {
            return isAnchored;
        }
        private int queryStartIndex; // [0]
        private int targetStartIndex; // [1]
        private int queryEndIndex; // [2]
        private int targetEndIndex; // [3]
        private boolean isAnchored;
        public Subproblem(int queryStartIndex, int targetStartIndex, int queryEndIndex, int targetEndIndex) {
            this(queryStartIndex, targetStartIndex, queryEndIndex, targetEndIndex, false);
        }
        public Subproblem(int queryStartIndex, int targetStartIndex, int queryEndIndex, int targetEndIndex, boolean isAnchored) {
            this.queryStartIndex = queryStartIndex;
            this.targetStartIndex = targetStartIndex;
            this.queryEndIndex = queryEndIndex;
            this.targetEndIndex = targetEndIndex;
            this.isAnchored = isAnchored;
        }
        /**
         * Convert a list of anchors into a subproblem list.
         * @param anchors anchored read pairs
         * @param querySequenceLength length of query sequence
         * @param targetSequenceLength length of target sequence
         * @return list alignment subproblems
         */
        public static List<Subproblem> getSubproblems(List<AlignerHelperAnchor.Anchor> anchors, int querySequenceLength, int targetSequenceLength) {
            Collections.sort(anchors, new AlignerHelperQueryIndexComparator.QueryIndexComparator());
            List<Subproblem> list = new ArrayList<>();
            AlignerHelperAnchor.Anchor last = new AlignerHelperAnchor.Anchor(-1, -1); // sentinal anchor
            boolean isAnchored = false;
            for (int i = 0; i < anchors.size(); i++) {
                if (anchors.get(i).targetIndex <= last.targetIndex ||
                    anchors.get(i).queryIndex <= last.queryIndex) {
                    throw new IllegalArgumentException("Anchor set must allow at least one possible alignment.");
                }
                list.add(new Subproblem(
                        last.queryIndex + 1,
                        last.targetIndex + 1,
                        anchors.get(i).queryIndex,
                        anchors.get(i).targetIndex,
                        isAnchored));
                last = anchors.get(i);
                isAnchored = true;
            }
            list.add(new Subproblem(
                    last.queryIndex + 1,
                    last.targetIndex + 1,
                    querySequenceLength,
                    targetSequenceLength,
                    isAnchored));
            return list;
        }
    }
}
