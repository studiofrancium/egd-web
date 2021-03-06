package ee.esutoniagodesu.service;

import com.google.common.base.Joiner;
import ee.esutoniagodesu.domain.core.table.IHasCoreWord;
import ee.esutoniagodesu.domain.freq.table.NresBase;
import ee.esutoniagodesu.domain.jmen.table.EN_Sens;
import ee.esutoniagodesu.domain.jmet.table.Sens;
import ee.esutoniagodesu.domain.kanjidic2.table.Kanji;
import ee.esutoniagodesu.pojo.test.compound.*;
import ee.esutoniagodesu.repository.domain.heisig.HeisigCoreKwRepository;
import ee.esutoniagodesu.repository.project.CoreDB;
import ee.esutoniagodesu.repository.project.JMDictDB;
import ee.esutoniagodesu.repository.project.JMDictEnDB;
import ee.esutoniagodesu.repository.project.KanjiDB;
import ee.esutoniagodesu.security.AuthoritiesConstants;
import ee.esutoniagodesu.security.SecurityUtils;
import ee.esutoniagodesu.util.JCString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;

/**
 * Kanji sõnade testi mudel
 * Salvestatud testid
 * Download as XLS/ODS/PDF
 */
@Service
@Transactional
public class TestCompoundService {

    private static final Logger log = LoggerFactory.getLogger(TestCompoundService.class);

    @Inject
    private JMDictDB jmDictDB;
    @Inject
    private JMDictEnDB jmDictEnDB;
    @Inject
    private KanjiDB kanjiDB;
    @Inject
    private CoreDB coreDB;

    @Inject
    private HeisigCoreKwRepository heisigCoreKwRepository;

    private static final FilterCompoundParamsDTO _params = new FilterCompoundParamsDTO();

    //------------------------------ enne submitti ------------------------------

    public FilterCompoundParamsDTO params() {
        return _params;
    }

    public FilterCompoundSubmitDTO getFormDefault(int paramId) {
        return FilterCompoundSubmitDefaults.getValueById(paramId).VALUE;
    }

    //------------------------------ peale submitti ------------------------------

    public List<KanjiCompound> submit(FilterCompoundSubmitDTO s) {
        log.info("generate: s=" + s);
        long ms = System.currentTimeMillis();

        //kanji indeksi intervall
        int ivfrom = s.kanjiInterval[0];
        int ivto = s.kanjiInterval[1];

        //kanjide nimekiri
        Map<Character, Kanji> kanjis = getKanjis(s.getEFilterType(), ivfrom, ivto, s.getEKanjiIntervalType(), s.radicalHintVisible);
        Assert.isTrue(kanjis.size() > 0);

        //Mitu kanjit võib olla sõnas. Intervall
        int compdlfrom = s.compLengthInterval[0];
        int compdlto = s.compLengthInterval[1];

        //leiame kandidaadid
        List<KanjiCompound> candidates = getCompounds(s.getEDictionary(), kanjis.values(), compdlfrom, compdlto);
        Assert.isTrue(candidates.size() > 0);

        //jagab sõnad kanjide arvu kaupa gruppidesse
        Map<Integer, List<KanjiCompound>> groupedByKanjiCount = divideIntoGroups(candidates, compdlfrom, compdlto);
        Assert.isTrue(groupedByKanjiCount.size() > 0);

        //1 kanjiga = 20 sõna, 2 kanjiga = 20 sõna,jne. Lineaarselt, ruutfunktsioon
        Map<Integer, Integer> drawCounts = getDrawCounts(s.generateCount, compdlfrom, compdlto, EDrawCountStrategy.LINEAR, groupedByKanjiCount);
        Assert.isTrue(drawCounts.size() > 0);

        //leiab igast grupist suvalised n sõna. sum(n) = testi sõnade arv
        List<KanjiCompound> result = drawBallots(groupedByKanjiCount, drawCounts);
        Assert.isTrue(result.size() > 0);

        //nüüd on testi minevad sõnad leitud, asume täiendava info lisamise kallale

        for (KanjiCompound p : result) {
            //eestikeelsed tähendused
            Sens sens = jmDictDB.getFirstSensByKanjAndRdng(p.answer, p.reading);

            //peab olema Jim Breeni sõnaraamatus olemas
            if (sens == null) {
                log.warn("submit: sens==null, jp={}, reading={}", p.answer, p.reading);
                continue;
            }

            if (p.et == null) {//ilo sõnastik määrab ET ise
                p.et = join(sens.getGlosses());
            }

            //inglisekeelsed tähendused lisatakse alati kui et puudub või en pole keelatud
            if (p.en == null && (p.et == null || !s.noEnIfHasEt)) {//CORE sõnastik määrab en ise
                EN_Sens sensEn = jmDictEnDB.getFirstSensByKanjAndRdng(p.answer, p.reading);
                Assert.notNull(sensEn);
                p.en = join(sensEn.getGlosses());
            }

            if (s.notesVisible) {
                String poses = join(sens.getPoses());
                String flds = join(sens.getFlds());
                p.notes = JCString.join(", ", poses, flds);
            }

            p.signs.stream().filter(q -> q.kanji).forEach(q -> {
                if (s.strokeCountHintVisible)
                    q.strokeCountHint = kanjis.get(q.sign).getStrokeCount();
                if (s.radicalHintVisible)
                    q.radicalHint = kanjis.get(q.sign).getRadicalHint();

            });

            if (SecurityUtils.isUserInRole(AuthoritiesConstants.ADMIN)) {
                String kanji = null;
                for (KanjiCompound.Calligraphy sign : p.signs) {
                    if (sign.kanji) {
                        kanji = String.valueOf(sign.sign);
                        break;
                    }
                }
                heisigCoreKwRepository.findOneByKanji(kanji).ifPresent(item -> {
                    if (item.getWord() != null && item.getWord().equals(p.answer) &&
                        item.getWordReading() != null && item.getWordReading().equals(p.reading)) {
                        p.heisigEquals = true;
                    }
                    p.heisigCoreKw = item.getId() + "-" + item.getKeywordEn() + "-" +
                        item.getWord() + "-" + item.getWordReading() + "-" + item.getWordTranslation();
                });

            }
        }

        log.info("generate: time=" + (System.currentTimeMillis() - ms) + ", result.size=" + result.size() + ", s=" + s);
        return result;
    }

    //------------------------------ kanjide leidmine ------------------------------

    private Map<Character, Kanji> getKanjis(EFilterType kanjidic, int ivfrom, int ivto, EKanjiIntervalType intervalType, boolean radicalHintVisible) {
        long ms = System.currentTimeMillis();
        List<Kanji> kanjis;

        switch (kanjidic) {
            case grade: {
                kanjis = kanjiDB.getGradeKanjisByLevel(ivfrom, ivto);
                break;
            }
            case jlpt: {
                kanjis = kanjiDB.getJLPTKanjisByLevel(ivfrom, ivto);
                break;
            }
            case jouyou: {
                kanjis = kanjiDB.getJouyouKanjisByGrade(ivfrom, ivto);
                break;
            }
            case heisig6: {
                kanjis = intervalType == EKanjiIntervalType.level ?
                    kanjiDB.getHeisig6KanjisByLesson(ivfrom, ivto) :
                    kanjiDB.getHeisig6KanjisByIndex(ivfrom, ivto);
                break;
            }
            case heisig4: {
                kanjis = intervalType == EKanjiIntervalType.level ?
                    kanjiDB.getHeisig4KanjisByLesson(ivfrom, ivto) :
                    kanjiDB.getHeisig4KanjisByIndex(ivfrom, ivto);
                break;
            }
            default:
                throw new RuntimeException("not implemented");
        }

        Map<Character, Kanji> result = new LinkedHashMap<>();

        for (Kanji p : kanjis) {
            char kanj = p.getLiteral().charAt(0);

            if (radicalHintVisible) {
                p.setRadicalHint(kanjiDB.getPrimitiveHint(kanj, _primitiveHintDelim));
            }

            result.put(kanj, p);
        }

        log.info("getKanjis: time=" + (System.currentTimeMillis() - ms) + ", result.size=" + result.size());
        return result;
    }

    //------------------------------ jp sõnade leidmine ------------------------------

    private static String toJoinedKanjis(Collection<Kanji> kanjis) {
        StringBuilder result = new StringBuilder();
        for (Kanji p : kanjis) {
            if (result.length() > 0) result.append(",");
            result.append(p.getLiteral().charAt(0));
        }
        return result.toString();
    }

    private List<KanjiCompound> getCompounds(EDictionary dictionary, Collection<Kanji> kanjis, int compdlfrom, int compdlto) {
        long ms = System.currentTimeMillis();
        List<KanjiCompound> result;
        String joinedKanjis = toJoinedKanjis(kanjis);

        switch (dictionary) {
            case ilo_yellow_jp_et: {
                result = coreWordToCompoundList(coreDB.getIloWordsByKanjis(joinedKanjis, compdlfrom, compdlto));
                break;
            }
            case core6k: {
                result = coreWordToCompoundList(coreDB.getCore6KWordsByKanjis(joinedKanjis, compdlfrom, compdlto));
                break;
            }
            case core10k: {
                result = coreWordToCompoundList(coreDB.getCore10KWordsByKanjis(joinedKanjis, compdlfrom, compdlto));
                break;
            }
            case tofu: {
                result = coreWordToCompoundList(coreDB.getTofuSentencesByKanjis(joinedKanjis, compdlfrom, compdlto));
                break;
            }
            default:
                throw new RuntimeException("not implemented");
        }

        log.info("getCompounds: result.size=" + result.size() + ", time=" + (System.currentTimeMillis() - ms));
        return result;
    }

    //------------------------------ lotomasin ------------------------------

    private Map<Integer, Integer> getDrawCounts(int generateCount, int compdlfrom, int compdlto, EDrawCountStrategy strategy, Map<Integer, List<KanjiCompound>> candicates) {
        switch (strategy) {
            case LINEAR:
                return getDrawCountsLinear(generateCount, compdlfrom, compdlto, candicates);
            default:
                throw new RuntimeException("not implemented");
        }
    }

    private Map<Integer, Integer> getDrawCountsLinear(int generateCount, int compdlfrom, int compdlto, Map<Integer, List<KanjiCompound>> candicates) {
        int countWish = generateCount / (compdlto - compdlfrom + 1);//iga kanjide arvu kohta võiks olla nii palju sõnu

        Map<Integer, Integer> result = new HashMap<>();
        int deficit = 0;
        int count;
        int countAll = 0;
        for (int p = compdlto; p >= compdlfrom; p--) {
            //selle kanjide arvuga sõnu kui palju on saadaval
            int available = candicates.get(p).size();

            if (p == compdlfrom) {
                deficit = generateCount - countAll - countWish;
            }

            if (countWish > available) {//kui soovitakse rohkem saada kui on saadaval
                //siis võtame nii palju kui saada on ja määrame defitsiidi
                deficit += countWish - available;
                count = available;
            } else if (deficit > 0 && available > countWish) {//kui eksisteerib defitsiit ja saadaval on rohkem kui soovitud
                //siis võtame nii palju kui on saadaval, aga mitte rohkem kui defitsiit
                int even = Math.min(deficit, (available - countWish));
                count = countWish + even;
                //kui ei saanud siit hulgast ikka piisavalt, siis kanname defitsiidi edasi
                deficit -= even;
            } else {
                count = countWish;
            }

            result.put(p, count);
            countAll += count;
        }

        if (deficit > 0) {
            log.warn("getDrawCountsLinear: deficit=" + deficit);
        }

        return result;
    }

    /**
     * @param candidates <mitme kanjiga sõna, sõnad>
     * @param drawCounts <selle kanjide arvuga sõnu, vali n tükki>
     * @param <U>        sõna klass
     * @return lotomasinast välja valitud sõnad
     */
    private <U> List<U> drawBallots(Map<Integer, List<U>> candidates, Map<Integer, Integer> drawCounts) {
        long ms = System.currentTimeMillis();
        Assert.isTrue(candidates.keySet().size() == drawCounts.keySet().size());
        log.debug("drawBallots: candidates.size=" + candidates.size() + ", drawCounts=" + drawCounts);

        List<U> result = new ArrayList<>();
        BallotEngine<U> ballotEngine = new BallotEngine<>();

        for (int kanjiCount : candidates.keySet()) {
            ballotEngine.put(candidates.get(kanjiCount));
            result.addAll(ballotEngine.draw(drawCounts.get(kanjiCount)));
        }

        log.info("drawBallots: result.size=" + result.size() + ", time=" + (System.currentTimeMillis() - ms));
        return result;
    }

    //------------------------------ cmpd konvertimine ------------------------------

    private <U extends IHasCoreWord> List<KanjiCompound> coreWordToCompoundList(List<U> words) {
        List<KanjiCompound> result = new ArrayList<>();
        for (IHasCoreWord p : words) {
            KanjiCompound compound = new KanjiCompound();
            compound.answer = p.getWord();
            compound.reading = p.getWordReading();
            compound.et = p.getWordTranslation();
            setCalligraphy(compound);
            result.add(compound);
        }
        return result;
    }

    public void setCalligraphy(KanjiCompound compound) {
        for (char q : compound.answer.toCharArray()) {
            KanjiCompound.Calligraphy sign = new KanjiCompound.Calligraphy(q);
            compound.signs.add(sign);
        }
    }

    /**
     * Saadud nimekiri sisaldab ainult kirjapilti, aga meil on vaja leida sõnastikust
     * hääldus, tähendused ja märgendid.
     */
    private List<KanjiCompound> nresToCompoundList(List<NresBase> nresBases) {
        List<KanjiCompound> result = new ArrayList<>();
        for (NresBase p : nresBases) {
            KanjiCompound compound = new KanjiCompound();
            String jp = p.getJp();
            compound.answer = jp;
            compound.reading = kanjiDB.getFirstReading(jp);
            setCalligraphy(compound);
            result.add(compound);
        }
        return result;
    }

    //------------------------------ abi ------------------------------

    private static <U extends Serializable> String join(Collection<U> collection) {
        return collection != null && collection.size() > 0 ? Joiner.on(", ").join(collection) : null;
    }

    private <U extends ICountKanjis> Map<Integer, List<U>> divideIntoGroups(List<U> candicates, int compdlfrom, int compdlto) {
        log.debug("divideIntoGroups: candicates.size=" + candicates.size() + ", compdlfrom=" + compdlfrom + ", compdlto=" + compdlto);
        Map<Integer, List<U>> result = getNewCompdLenMap(compdlfrom, compdlto);
        for (U p : candicates) {
            if (p.getCountKanjis() < 1) {
                log.error("no kanjis {}", p);
                continue;
            }

            result.get(p.getCountKanjis()).add(p);
        }
        return result;
    }

    private <U> Map<Integer, List<U>> getNewCompdLenMap(int compdlfrom, int compdlto) {
        Map<Integer, List<U>> bresByKanjiCount = new HashMap<>();
        for (int p = compdlfrom; p <= compdlto; p++) {
            bresByKanjiCount.put(p, new ArrayList<>());
        }
        return bresByKanjiCount;
    }

    //------------------------------ kanjile lisateabe leidmine ------------------------------

    private static final String _primitiveHintDelim = "";
}
