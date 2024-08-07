package org.biojava.nbio.structure.chem;

import org.biojava.nbio.core.util.SoftHashMap;
import org.biojava.nbio.structure.AminoAcid;
import org.biojava.nbio.structure.AminoAcidImpl;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.HetatomImpl;
import org.biojava.nbio.structure.NucleotideImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChemCompGroupFactory {
    private static final Logger logger = LoggerFactory.getLogger(ChemCompGroupFactory.class);
    private static ChemCompProvider chemCompProvider = new DownloadChemCompProvider();
    private static final SoftHashMap<String, ChemComp> cache = new SoftHashMap<>(0);

    public static ChemComp getChemComp(String recordName) {
        recordName = recordName.toUpperCase().trim();

        // we are using the cache, to avoid hitting the file system too often.
        ChemComp cc = cache.get(recordName);
        if (cc != null) {
            logger.debug("Chem comp {} read from cache", cc.getThreeLetterCode());
            return cc;
        }

        // not cached, get the chem comp from the provider
        logger.debug("Chem comp {} read from provider {}", recordName, chemCompProvider.getClass().getCanonicalName());
        cc = chemCompProvider.getChemComp(recordName);

        // Note that this also caches null or empty responses
        cache.put(recordName, cc);
        return cc;
    }

    /**
     * The new ChemCompProvider will be set in the static variable,
     * so this provider will be used from now on until it is changed
     * again. Note that this change can have unexpected behavior of
     * code executed afterwards.
     * <p>
     * Changing the provider also resets the cache, so any groups
     * previously accessed will be reread or re-downloaded.
     *
     * @param provider
     */
    public static void setChemCompProvider(ChemCompProvider provider) {
        logger.debug("Setting new chem comp provider to {}", provider.getClass().getCanonicalName());
        chemCompProvider = provider;
        // clear cache
        cache.clear();
    }

    public static ChemCompProvider getChemCompProvider(){
        return chemCompProvider;
    }

    /**
     * Force the in-memory cache to be reset.
     *
     * Note that the ChemCompProvider may have additional memory or disk caches that need to be cleared too.
     */
    public static void clearCache() {
        cache.clear();
    }

    public static Group getGroupFromChemCompDictionary(String recordName) {
        // make sure we work with upper case records
        recordName = recordName.toUpperCase().trim();
        Group g;
        ChemComp cc =  getChemComp(recordName);

        if (cc == null) {
            return null;
        }

        if (PolymerType.PROTEIN_ONLY.contains(cc.getPolymerType())) {
            AminoAcid aa = new AminoAcidImpl();

            String one_letter = cc.getOneLetterCode();
            if (one_letter == null || "X".equals(one_letter) || "?".equals(one_letter) || one_letter.length() == 0) {
                String parent = cc.getMonNstdParentCompId();
                if (parent != null && parent.length() == 3) {
                    String parentid = cc.getMonNstdParentCompId();
                    ChemComp parentCC = getChemComp(parentid);
                    one_letter = parentCC.getOneLetterCode();
                }
            }

            if (one_letter == null || one_letter.length() == 0 || "?".equals(one_letter)) {
                // e.g. problem with PRR, which probably should have a parent of ALA, but as of 20110127 does not.
                logger.warn("Problem with chemical component: {} Did not find one letter code! Setting it to 'X'",
                        recordName);
                aa.setAminoType('X');
            } else  {
                aa.setAminoType(one_letter.charAt(0));
            }

            g = aa;
        } else if (PolymerType.POLYNUCLEOTIDE_ONLY.contains(cc.getPolymerType())) {
            g = new NucleotideImpl();
        } else {
            g = new HetatomImpl();
        }

        g.setChemComp(cc);
        return g;
    }

    public static String getOneLetterCode(ChemComp cc) {
        String oneLetter = cc.getOneLetterCode();
        if (oneLetter == null || "X".equals(oneLetter) || "?".equals(oneLetter)) {
            String parentId = cc.getMonNstdParentCompId();
            if (parentId == null) {
                return oneLetter;
            }
            // cases like OIM have multiple parents (comma separated), we shouldn't try grab a chemcomp for those strings
            if (parentId.length() > 3) {
                return oneLetter;
            }
            ChemComp parentCC = ChemCompGroupFactory.getChemComp(parentId);
            if (parentCC == null) {
                return oneLetter;
            }
            oneLetter = parentCC.getOneLetterCode();
        }
        return oneLetter;
    }
}
