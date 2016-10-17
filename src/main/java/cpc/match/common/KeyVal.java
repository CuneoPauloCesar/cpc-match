package cpc.match.common;

public final class KeyVal<V> {
    public final Comparable key;
    public final V val;

    public KeyVal(Comparable key, V val) {
        this.key = key;
        this.val = val;
    }

    @Override
    public String toString() {
        return "[" + key + ", " + val + "]";
    }
}
