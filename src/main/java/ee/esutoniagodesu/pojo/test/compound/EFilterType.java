package ee.esutoniagodesu.pojo.test.compound;

import ee.esutoniagodesu.pojo.entity.IntIDStringTitle;

public enum EFilterType implements IntIDStringTitle {
    grade(1),
    jlpt(2),
    jouyou(3),
    heisig6(4),
    heisig4(5);

    public final int ID;

    EFilterType(int id) {
        ID = id;
    }

    public static EFilterType findById(int id) {
        for (EFilterType p : values()) {
            if (p.ID == id) return p;
        }
        return null;
    }

    public int getId() {
        return ID;
    }

    public String getTitle() {
        return name().toLowerCase();
    }
}
