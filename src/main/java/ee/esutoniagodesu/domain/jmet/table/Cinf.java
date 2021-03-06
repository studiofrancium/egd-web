package ee.esutoniagodesu.domain.jmet.table;

import ee.esutoniagodesu.domain.jmet.pk.CinfPK;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Immutable
@Table(name = "cinf", schema = "jmet")
@IdClass(CinfPK.class)
public final class Cinf implements Serializable {

    private static final long serialVersionUID = -4799968254892260629L;
    private int entr;
    private short kw;
    private String value;
    private String mctype;
    private Chr chrByEntr;
    private Kwcinf kwcinfByKw;

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false, name = "entr", referencedColumnName = "entr", nullable = false)
    public Chr getChrByEntr() {
        return chrByEntr;
    }

    public void setChrByEntr(Chr chrByEntr) {
        this.chrByEntr = chrByEntr;
    }

    @Id
    @Column(name = "entr", nullable = false, insertable = true, updatable = true)
    public int getEntr() {
        return entr;
    }

    public void setEntr(int entr) {
        this.entr = entr;
    }

    @Id
    @Column(name = "kw", nullable = false, insertable = true, updatable = true)
    public short getKw() {
        return kw;
    }

    public void setKw(short kw) {
        this.kw = kw;
    }

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false, name = "kw", referencedColumnName = "id", nullable = false)
    public Kwcinf getKwcinfByKw() {
        return kwcinfByKw;
    }

    public void setKwcinfByKw(Kwcinf kwcinfByKw) {
        this.kwcinfByKw = kwcinfByKw;
    }

    @Id
    @Column(name = "mctype", nullable = false, insertable = true, updatable = true, length = 50)
    public String getMctype() {
        return mctype;
    }

    public void setMctype(String mctype) {
        this.mctype = mctype;
    }

    @Id
    @Column(name = "value", nullable = false, insertable = true, updatable = true, length = 50)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cinf cinf = (Cinf) o;

        if (entr != cinf.entr) return false;
        if (kw != cinf.kw) return false;
        if (mctype != null ? !mctype.equals(cinf.mctype) : cinf.mctype != null) return false;
        if (value != null ? !value.equals(cinf.value) : cinf.value != null) return false;

        return true;
    }

    public int hashCode() {
        int result = entr;
        result = 31 * result + (int) kw;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (mctype != null ? mctype.hashCode() : 0);
        return result;
    }
}
