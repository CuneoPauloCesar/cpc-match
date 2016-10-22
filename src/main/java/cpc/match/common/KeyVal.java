package cpc.match.common;

public final class KeyVal<K, V> {
    public final K key;
    public final V val;

    public KeyVal(K key, V val) {
        this.key = key;
        this.val = val;
    }

    @Override
    public String toString() {
        return "[" + key + ", " + val + "]";
    }

}
