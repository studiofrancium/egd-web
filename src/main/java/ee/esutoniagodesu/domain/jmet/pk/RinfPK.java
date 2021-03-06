package ee.esutoniagodesu.domain.jmet.pk;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;


public final class RinfPK implements Serializable {

    private static final long serialVersionUID = 4819993041403363875L;
    private int entr;
    private short rdng;
    private short kw;

    @Column(name = "entr", nullable = false, insertable = true, updatable = true)
    @Id
    public int getEntr() {
        return entr;
    }

    public void setEntr(int entr) {
        this.entr = entr;
    }

    @Column(name = "kw", nullable = false, insertable = true, updatable = true)
    @Id
    public short getKw() {
        return kw;
    }

    public void setKw(short kw) {
        this.kw = kw;
    }

    @Column(name = "rdng", nullable = false, insertable = true, updatable = true)
    @Id
    public short getRdng() {
        return rdng;
    }

    public void setRdng(short rdng) {
        this.rdng = rdng;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RinfPK rinfPK = (RinfPK) o;

        if (entr != rinfPK.entr) return false;
        if (kw != rinfPK.kw) return false;
        if (rdng != rinfPK.rdng) return false;

        return true;
    }

    public int hashCode() {
        int result = entr;
        result = 31 * result + (int) rdng;
        result = 31 * result + (int) kw;
        return result;
    }
}
