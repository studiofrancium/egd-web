package ee.esutoniagodesu.domain.jmen.view;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Immutable
@Table(name = "vt_kanj", schema = "jmen")
public final class EN_VtKanj implements Serializable {

    private static final long serialVersionUID = 5608681427222727210L;
    private Integer entr;
    private String ktxt;

    @Id
    @Basic
    @Column(name = "entr", nullable = true, insertable = true, updatable = true)
    public Integer getEntr() {
        return entr;
    }

    public void setEntr(Integer entr) {
        this.entr = entr;
    }

    @Basic
    @Column(name = "ktxt", nullable = true, insertable = true, updatable = true, length = 2147483647)
    public String getKtxt() {
        return ktxt;
    }

    public void setKtxt(String ktxt) {
        this.ktxt = ktxt;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EN_VtKanj vtKanj = (EN_VtKanj) o;

        if (entr != null ? !entr.equals(vtKanj.entr) : vtKanj.entr != null) return false;
        if (ktxt != null ? !ktxt.equals(vtKanj.ktxt) : vtKanj.ktxt != null) return false;

        return true;
    }

    public int hashCode() {
        int result = entr != null ? entr.hashCode() : 0;
        result = 31 * result + (ktxt != null ? ktxt.hashCode() : 0);
        return result;
    }
}
