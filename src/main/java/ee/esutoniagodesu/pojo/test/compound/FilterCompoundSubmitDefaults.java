package ee.esutoniagodesu.pojo.test.compound;

import java.util.LinkedHashMap;
import java.util.Map;

public enum FilterCompoundSubmitDefaults {

    ilo_heisig6_1_100(1, getIloHeisig6()),
    ilo_heisig6_100_300(4, getIloHeisig6Intermediate()),
    core6k_heisig6_1_900(2, getCore6KHeisig6()),
    core10k_jlpt_1_3(3, getCore10KJLPT()),
    tofu_jlpt_1_3(5, getTofuJLPT());

    public final int ID;
    public final FilterCompoundSubmitDTO VALUE;

    FilterCompoundSubmitDefaults(int id, FilterCompoundSubmitDTO form) {
        ID = id;
        VALUE = form;
    }

    public static FilterCompoundSubmitDefaults getValueById(int id) {
        for (FilterCompoundSubmitDefaults p : values()) {
            if (p.ID == id) return p;
        }
        throw new NullPointerException("id=" + id);
    }

    public static Map<Integer, String> asMap() {
        Map<Integer, String> result = new LinkedHashMap<>();
        for (FilterCompoundSubmitDefaults p : values()) {
            result.put(p.ID, p.name().toLowerCase());
        }
        return result;
    }

    //------------------------------ vormid ------------------------------

    public static FilterCompoundSubmitDTO getIloHeisig6Intermediate() {
        FilterCompoundSubmitDTO submit = new FilterCompoundSubmitDTO();

        submit.dictionary = EDictionary.ilo_yellow_jp_et.ID;
        submit.generateCount = 500;

        submit.notesVisible = true;
        submit.compLengthInterval[0] = 1;
        submit.compLengthInterval[1] = 3;
        submit.filterType = EFilterType.heisig6.ID;//heisig

        submit.kanjiIntervalType = "index";
        submit.kanjiInterval[0] = 100;//heisig frame from
        submit.kanjiInterval[1] = 300;

        submit.orderByType = EOrderByType.random.ID;//filter_id

        submit.strokeCountHintVisible = false;
        submit.radicalHintVisible = false;
        submit.noEnIfHasEt = true;

        return submit;
    }

    public static FilterCompoundSubmitDTO getIloHeisig6() {
        FilterCompoundSubmitDTO submit = new FilterCompoundSubmitDTO();

        submit.dictionary = EDictionary.ilo_yellow_jp_et.ID;
        submit.generateCount = 50;

        submit.notesVisible = true;
        submit.compLengthInterval[0] = 1;
        submit.compLengthInterval[1] = 3;
        submit.filterType = EFilterType.heisig6.ID;//heisig

        submit.kanjiIntervalType = "index";
        submit.kanjiInterval[0] = 1;//heisig frame from
        submit.kanjiInterval[1] = 100;

        submit.orderByType = EOrderByType.random.ID;//filter_id

        submit.strokeCountHintVisible = false;
        submit.radicalHintVisible = false;
        submit.noEnIfHasEt = true;

        return submit;
    }

    public static FilterCompoundSubmitDTO getCore6KHeisig6() {
        FilterCompoundSubmitDTO submit = new FilterCompoundSubmitDTO();

        submit.dictionary = EDictionary.core6k.ID;
        submit.generateCount = 50;

        submit.notesVisible = true;
        submit.compLengthInterval[0] = 1;
        submit.compLengthInterval[1] = 4;
        submit.filterType = EFilterType.heisig6.ID;//heisig

        submit.kanjiIntervalType = "index";
        submit.kanjiInterval[0] = 1;//heisig frame from
        submit.kanjiInterval[1] = 900;

        submit.orderByType = EOrderByType.random.ID;//filter_id

        submit.strokeCountHintVisible = false;
        submit.radicalHintVisible = false;
        submit.noEnIfHasEt = true;

        return submit;
    }

    public static FilterCompoundSubmitDTO getCore10KJLPT() {
        FilterCompoundSubmitDTO submit = new FilterCompoundSubmitDTO();

        submit.dictionary = EDictionary.core10k.ID;
        submit.generateCount = 50;

        submit.notesVisible = true;
        submit.compLengthInterval[0] = 1;
        submit.compLengthInterval[1] = 4;
        submit.filterType = EFilterType.jlpt.ID;//

        submit.kanjiIntervalType = "level";
        submit.kanjiInterval[0] = 1;//frame from
        submit.kanjiInterval[1] = 3;

        submit.orderByType = EOrderByType.random.ID;//

        submit.strokeCountHintVisible = false;
        submit.radicalHintVisible = false;
        submit.noEnIfHasEt = true;

        return submit;
    }

    public static FilterCompoundSubmitDTO getTofuJLPT() {
        FilterCompoundSubmitDTO submit = new FilterCompoundSubmitDTO();

        submit.dictionary = EDictionary.tofu.ID;
        submit.generateCount = 50;

        submit.notesVisible = true;
        submit.compLengthInterval[0] = 1;
        submit.compLengthInterval[1] = 4;
        submit.filterType = EFilterType.jlpt.ID;//

        submit.kanjiIntervalType = "level";
        submit.kanjiInterval[0] = 1;//frame from
        submit.kanjiInterval[1] = 3;

        submit.orderByType = EOrderByType.random.ID;//

        submit.strokeCountHintVisible = false;
        submit.radicalHintVisible = false;
        submit.noEnIfHasEt = true;

        return submit;
    }
}
