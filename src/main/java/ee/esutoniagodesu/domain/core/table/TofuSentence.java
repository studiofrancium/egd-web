package ee.esutoniagodesu.domain.core.table;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Immutable
@Entity
@Table(name = "tofu_sentence", schema = "core")
public final class TofuSentence implements IHasCoreWord, Serializable {

    private static final long serialVersionUID = -1044326675149260942L;

    @Column(name = "id")
    @Id
    private Integer id;
    @Column(name = "word", nullable = false, insertable = false, updatable = false, length = 2147483647, precision = 0)
    @Basic
    private String word;
    @Column(name = "sentence", nullable = false, insertable = false, updatable = false, length = 2147483647, precision = 0)
    @Basic
    private String sentence;

    @Transient
    private TofuSentenceTranslation translation;

    @Column(name = "with_jmdict", nullable = false, insertable = false, updatable = false, length = 1, precision = 0)
    @Basic
    private boolean withJmdict;

    @Transient
    private int wordKanjiCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonGetter
    public TofuSentenceTranslation getTranslation() {
        return translation;
    }

    @JsonSetter
    public void setTranslation(TofuSentenceTranslation translation) {
        this.translation = translation;
    }

    public String getWord() {
        return word;
    }

    @Transient
    public String getWordReading() {
        return null;
    }

    @Transient
    public String getWordTranslation() {
        return null;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public boolean isWithJmdict() {
        return withJmdict;
    }

    public void setWithJmdict(boolean withJmdict) {
        this.withJmdict = withJmdict;
    }

    public int getWordKanjiCount() {
        return wordKanjiCount;
    }

    public void setWordKanjiCount(int wordKanjiCount) {
        this.wordKanjiCount = wordKanjiCount;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TofuSentence that = (TofuSentence) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (sentence != null ? !sentence.equals(that.sentence) : that.sentence != null) return false;
        if (word != null ? !word.equals(that.word) : that.word != null) return false;

        return true;
    }

    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (sentence != null ? sentence.hashCode() : 0);
        return result;
    }
}
