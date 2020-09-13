class OrderedMap<K extends Comparable<? super K>, V> {
    private static Random rnd = new Random();
    private RBST<K, V> root;
    public OrderedMap() {}
    private OrderedMap(RBST<K, V> root) {this.root = root;}
    public static <K extends Comparable<? super K>, V> OrderedMap<K, V> merge(OrderedMap<K, V> l, OrderedMap<K, V> r) {
        return l.mergeRight(r);
    }
    public OrderedMap<K, V> mergeLeft(OrderedMap<K, V> l) {
        return new OrderedMap<>(RBST.merge(l.root, root));
    }
    public OrderedMap<K, V> mergeRight(OrderedMap<K, V> r) {
        return new OrderedMap<>(RBST.merge(root, r.root));
    }
    public OrderedMap<K, V> splitRightUsingIndex(int k) {
        Pair<RBST<K, V>, RBST<K, V>> p = RBST.splitUsingIndex(root, k);
        OrderedMap<K, V> fst = new OrderedMap<>(p.fst);
        root = fst.root;
        OrderedMap<K, V> snd = new OrderedMap<>(p.snd);
        return snd;
    }
    public OrderedMap<K, V> splitLeftUsingIndex(int k) {
        Pair<RBST<K, V>, RBST<K, V>> p = RBST.splitUsingIndex(root, k);
        OrderedMap<K, V> fst = new OrderedMap<>(p.fst);
        OrderedMap<K, V> snd = new OrderedMap<>(p.snd);
        root = snd.root;
        return fst;
    }
    public OrderedMap<K, V> splitRightUsingKey(K key) {
        Pair<RBST<K, V>, RBST<K, V>> p = RBST.splitUsingKey(root, key);
        OrderedMap<K, V> fst = new OrderedMap<>(p.fst);
        root = fst.root;
        OrderedMap<K, V> snd = new OrderedMap<>(p.snd);
        return snd;
    }
    public OrderedMap<K, V> splitLeftUsingKey(K key) {
        Pair<RBST<K, V>, RBST<K, V>> p = RBST.splitUsingKey(root, key);
        OrderedMap<K, V> fst = new OrderedMap<>(p.fst);
        OrderedMap<K, V> snd = new OrderedMap<>(p.snd);
        root = snd.root;
        return fst;
    }
    public java.util.AbstractMap.SimpleEntry<K, V> kthEntry(int k) {
        if (k < 0 || k >= size()) return null;
        return RBST.kthEntry(root, k);
    }
    public java.util.AbstractMap.SimpleEntry<K, V> firstEntry() {
        return kthEntry(0);
    }
    public java.util.AbstractMap.SimpleEntry<K, V> lastEntry() {
        return kthEntry(size() - 1);
    }
    public java.util.AbstractMap.SimpleEntry<K, V> lowerEntry(K key) {
        return kthEntry(RBST.ltCount(root, key) - 1);
    }
    public java.util.AbstractMap.SimpleEntry<K, V> floorEntry(K key) {
        return kthEntry(RBST.leqCount(root, key) - 1);
    }
    public java.util.AbstractMap.SimpleEntry<K, V> higherEntry(K key) {
        return kthEntry(RBST.leqCount(root, key));
    }
    public java.util.AbstractMap.SimpleEntry<K, V> ceilingEntry(K key) {
        return kthEntry(RBST.ltCount(root, key));
    }
    public V get(K key) {
        return RBST.get(root, key);
    }
    public V getOrDefault(K key, V defaultValue) {
        V res = RBST.get(root, key);
        return res != null ? res : defaultValue;
    }
    public V put(K key, V val) {
        if (RBST.contains(root, key)) {
            java.util.AbstractMap.SimpleEntry<K, V> e = RBST.getEntry(root, key);
            V oldValue = e.getValue();
            e.setValue(val);
            return oldValue;
        }
        root = RBST.insert(root, key, val);
        return null;
    }
    public V putIfAbsent(K key, V val) {
        java.util.AbstractMap.SimpleEntry<K, V> e = RBST.getEntry(root, key);
        if (e != null) return e.getValue();
        put(key, val);
        return null;
    }
    public java.util.AbstractMap.SimpleEntry<K, V> removeKthEntry(int k) {
        if (k < 0 || k >= size()) return null;
        Pair<RBST<K, V>, java.util.AbstractMap.SimpleEntry<K, V>> nodeAndEntry = RBST.eraseUsingIndex(root, k);
        root = nodeAndEntry.fst;
        return nodeAndEntry.snd;
    }
    public java.util.AbstractMap.SimpleEntry<K, V> remove(K key) {
        if (!containsKey(key)) return null;
        Pair<RBST<K, V>, java.util.AbstractMap.SimpleEntry<K, V>> nodeAndEntry = RBST.eraseUsingKey(root, key);
        root = nodeAndEntry.fst;
        return nodeAndEntry.snd;
    }
    public boolean remove(K key, V value) {
        java.util.AbstractMap.SimpleEntry<K, V> e = RBST.getEntry(root, key);
        if (e == null) return false;
        if (java.util.Objects.equals(value, e.getValue())) {
            Pair<RBST<K, V>, java.util.AbstractMap.SimpleEntry<K, V>> nodeAndEntry = RBST.eraseUsingKey(root, key);
            root = nodeAndEntry.fst;
            return true;
        }
        return false;
    }
    public V replace(K key, V newValue) {
        java.util.AbstractMap.SimpleEntry<K, V> e = RBST.getEntry(root, key);
        if (e == null) return null;
        V oldValue = e.getValue();
        e.setValue(newValue);
        return oldValue;
    }
    public boolean replace(K key, V oldValue, V newValue) {
        java.util.AbstractMap.SimpleEntry<K, V> e = RBST.getEntry(root, key);
        if (e == null) return false;
        V value = e.getValue();
        if (java.util.Objects.equals(value, oldValue)) {
            e.setValue(newValue);
            return true;
        }
        return false;
    }
    public int countLessThan(K key) {
        return RBST.ltCount(root, key);
    }
    public int countLessThanOrEqual(K key) {
        return RBST.leqCount(root, key);
    }
    public int countGreaterThan(K key) {
        return size() - countLessThanOrEqual(key);
    }
    public int countGreaterThanOrEqual(K key) {
        return size() - countLessThan(key);
    }
    public int countRange(K fromKey, K toKey) {
        return countLessThan(toKey) - countLessThan(fromKey);
    }
    public boolean containsKey(K key) {
        return RBST.contains(root, key);
    }
    public int size() {
        return RBST.size(root);
    }
    public boolean isEmpty() {
        return size() == 0;
    }
    public void clear() {
        root = null;
    }
    public java.util.Set<java.util.AbstractMap.SimpleEntry<K, V>> entrySet() {
        return RBST.entrySet(root);
    }
    public java.util.Set<java.util.AbstractMap.SimpleEntry<K, V>> descendingEntrySet() {
        return RBST.descendingEntrySet(root);
    }
    public java.util.Iterator<java.util.AbstractMap.SimpleEntry<K, V>> iterator() {
        return RBST.iterator(root);
    }
    public java.util.Iterator<java.util.AbstractMap.SimpleEntry<K, V>> descendingIterator() {
        return RBST.descendingIterator(root);
    }
    public java.util.Iterator<K> keyIterator() {
        return RBST.keyIterator(root);
    }
    public java.util.Iterator<K> descendingKeyIterator() {
        return RBST.descendingKeyIterator(root);
    }
    public java.util.Set<K> keySet() {
        return RBST.keySet(root);
    }
    public java.util.Set<K> descendingKeySet() {
        return RBST.descendingKeySet(root);
    }
    public java.util.Collection<V> values() {
        return RBST.values(root);
    }
    public java.util.Optional<V> safeGet(K key) {
        V res = get(key);
        return res != null ? java.util.Optional.of(res) : java.util.Optional.empty();
    }
    public java.util.Optional<java.util.AbstractMap.SimpleEntry<K, V>> safeGetFirstEntry() {
        return size() > 0 ? java.util.Optional.of(kthEntry(0)) : java.util.Optional.empty();
    }
    public java.util.Optional<java.util.AbstractMap.SimpleEntry<K, V>> safeGetLastEntry() {
        return size() > 0 ? java.util.Optional.of(kthEntry(size() - 1)) : java.util.Optional.empty();
    }
    public java.util.Optional<java.util.AbstractMap.SimpleEntry<K, V>> safeGetLowerEntry(K key) {
        int k = RBST.ltCount(root, key) - 1;
        return k >= 0 ? java.util.Optional.of(kthEntry(k)) : java.util.Optional.empty();
    }
    public java.util.Optional<java.util.AbstractMap.SimpleEntry<K, V>> safeGetFloorEntry(K key) {
        int k = RBST.leqCount(root, key) - 1;
        return k >= 0 ? java.util.Optional.of(kthEntry(k)) : java.util.Optional.empty();
    }
    public java.util.Optional<java.util.AbstractMap.SimpleEntry<K, V>> safeGetHigherEntry(K key) {
        int k = RBST.leqCount(root, key);
        return k < size() ? java.util.Optional.of(kthEntry(k)) : java.util.Optional.empty();
    }
    public java.util.Optional<java.util.AbstractMap.SimpleEntry<K, V>> safeGetCeilingEntry(K key) {
        int k = RBST.ltCount(root, key);
        return k < size() ? java.util.Optional.of(kthEntry(k)) : java.util.Optional.empty();
    }
    static final class RBST<K extends Comparable<? super K>, V> extends java.util.AbstractMap.SimpleEntry<K, V> {
        private static final long serialVersionUID = 5141683502983027212L;
        private RBST<K, V> l, r;
        private int size;
        private RBST(K key, V val) {super(key, val); this.size = 1;}
        private RBST<K, V> update() {
            size = size(l) + size(r) + 1;
            return this;
        }
        static <K extends Comparable<? super K>, V> java.util.AbstractMap.SimpleEntry<K, V> getEntry(RBST<K, V> t, K key) {
            while (t != null) {
                if (t.getKey().compareTo(key) == 00) return t;
                t = t.getKey().compareTo(key) < 0 ? t.r : t.l;
            }
            return null;
        }
        static <K extends Comparable<? super K>, V> V get(RBST<K, V> t, K key) {
            while (t != null) {
                if (t.getKey().compareTo(key) == 0) return t.getValue();
                t = t.getKey().compareTo(key) < 0 ? t.r : t.l;
            }
            return null;
        }
        static <K extends Comparable<? super K>, V> java.util.AbstractMap.SimpleEntry<K, V> kthEntry(RBST<K, V> t, int k) {
            int c = size(t.l);
            if (k < c) return kthEntry(t.l, k);
            if (k == c) return t;
            return kthEntry(t.r, k - c - 1);
        }
        static <K extends Comparable<? super K>, V> int leqCount(RBST<K, V> t, K key) {
            if (t == null) return 0;
            if (key.compareTo(t.getKey()) < 0) return leqCount(t.l, key);
            return leqCount(t.r, key) + size(t.l) + 1;
        }
        static <K extends Comparable<? super K>, V> int ltCount(RBST<K, V> t, K key) {
            if (t == null) return 0;
            if (key.compareTo(t.getKey()) <= 0) return ltCount(t.l, key);
            return ltCount(t.r, key) + size(t.l) + 1;
        }
        static <K extends Comparable<? super K>, V> RBST<K, V> merge(RBST<K, V> l, RBST<K, V> r) {
            if (l == null) return r;
            if (r == null) return l;
            if (rnd.nextInt() % (l.size + r.size) < l.size) {
                l.r = merge(l.r, r);
                return l.update();
            } else {
                r.l = merge(l, r.l);
                return r.update();
            }
        }
        static <K extends Comparable<? super K>, V> Pair<RBST<K, V>, RBST<K, V>> splitUsingIndex(RBST<K, V> x, int k) {
            if (k < 0 || k > size(x)) {
                throw new IndexOutOfBoundsException(
                    String.format("index %d is out of bounds for the length of %d", k, size(x))
                );
            }
            if (x == null) {
                return new Pair<RBST<K, V>, RBST<K, V>>(null, null);
            } else if (k <= size(x.l)) {
                Pair<RBST<K, V>, RBST<K, V>> p = splitUsingIndex(x.l, k);
                x.l = p.snd;
                p.snd = x.update();
                return p;
            } else {
                Pair<RBST<K, V>, RBST<K, V>> p = splitUsingIndex(x.r, k - size(x.l) - 1);
                x.r = p.fst;
                p.fst = x.update();
                return p;
            }
        }
        static <K extends Comparable<? super K>, V> Pair<RBST<K, V>, RBST<K, V>> splitUsingKey(RBST<K, V> x, K key) {
            if (x == null) {
                return new Pair<RBST<K, V>, RBST<K, V>>(null, null);
            } else if (key.compareTo(x.getKey()) <= 0) {
                Pair<RBST<K, V>, RBST<K, V>> p = splitUsingKey(x.l, key);
                x.l = p.snd;
                p.snd = x.update();
                return p;
            } else {
                Pair<RBST<K, V>, RBST<K, V>> p = splitUsingKey(x.r, key);
                x.r = p.fst;
                p.fst = x.update();
                return p;
            }
        }
        static <K extends Comparable<? super K>, V> RBST<K, V> insert(RBST<K, V> t, K key, V val) {
            Pair<RBST<K, V>, RBST<K, V>> p = splitUsingKey(t, key);
            return RBST.merge(RBST.merge(p.fst, new RBST<>(key, val)), p.snd);
        }
        static <K extends Comparable<? super K>, V> Pair<RBST<K, V>, java.util.AbstractMap.SimpleEntry<K, V>> eraseUsingIndex(RBST<K, V> t, int k) {
            Pair<RBST<K, V>, RBST<K, V>> p = splitUsingIndex(t, k);
            Pair<RBST<K, V>, RBST<K, V>> q = splitUsingIndex(p.snd, 1);
            return new Pair<>(RBST.merge(p.fst, q.snd), q.fst);
        }
        static <K extends Comparable<? super K>, V> Pair<RBST<K, V>, java.util.AbstractMap.SimpleEntry<K, V>> eraseUsingKey(RBST<K, V> t, K key) {
            Pair<RBST<K, V>, RBST<K, V>> p = splitUsingKey(t, key);
            Pair<RBST<K, V>, RBST<K, V>> q = splitUsingIndex(p.snd, 1);
            return new Pair<>(RBST.merge(p.fst, q.snd), q.fst);
        }
        static <K extends Comparable<? super K>, V> boolean contains(RBST<K, V> t, K key) {
            while (t != null) {
                if (t.getKey().compareTo(key) == 0) return true;
                else if (t.getKey().compareTo(key) < 0) t = t.r;
                else t = t.l;
            }
            return false;
        }
        static <K extends Comparable<? super K>, V> int size(RBST<K, V> nd) {
            return nd == null ? 0 : nd.size;
        }
        static <K extends Comparable<? super K>, V> java.util.Set<java.util.AbstractMap.SimpleEntry<K, V>> entrySet(RBST<K, V> t) {
            java.util.LinkedHashSet<java.util.AbstractMap.SimpleEntry<K, V>> set = new java.util.LinkedHashSet<>();
            if (t == null) return set;
            java.util.ArrayDeque<IntObjPair<RBST<K, V>>> stack = new java.util.ArrayDeque<>();
            if (t.r != null) stack.addLast(new IntObjPair<>(0, t.r));
            stack.addLast(new IntObjPair<>(1, t));
            if (t.l != null) stack.addLast(new IntObjPair<>(0, t.l));
            while (stack.size() > 0) {
                IntObjPair<RBST<K, V>> p = stack.pollLast();
                RBST<K, V> u = p.value;
                if (p.key == 1) {
                    set.add(u);
                } else {
                    if (u.r != null) stack.addLast(new IntObjPair<>(0, u.r));
                    stack.addLast(new IntObjPair<>(1, u));
                    if (u.l != null) stack.addLast(new IntObjPair<>(0, u.l));
                }
            }
            return set;
        }
        static <K extends Comparable<? super K>, V> java.util.Set<java.util.AbstractMap.SimpleEntry<K, V>> descendingEntrySet(RBST<K, V> t) {
            java.util.LinkedHashSet<java.util.AbstractMap.SimpleEntry<K, V>> set = new java.util.LinkedHashSet<>();
            if (t == null) return set;
            java.util.ArrayDeque<IntObjPair<RBST<K, V>>> stack = new java.util.ArrayDeque<>();
            if (t.l != null) stack.addLast(new IntObjPair<>(0, t.l));
            stack.addLast(new IntObjPair<>(1, t));
            if (t.r != null) stack.addLast(new IntObjPair<>(0, t.r));
            while (stack.size() > 0) {
                IntObjPair<RBST<K, V>> p = stack.pollLast();
                RBST<K, V> u = p.value;
                if (p.key == 1) {
                    set.add(u);
                } else {
                    if (u.l != null) stack.addLast(new IntObjPair<>(0, u.l));
                    stack.addLast(new IntObjPair<>(1, u));
                    if (u.r != null) stack.addLast(new IntObjPair<>(0, u.r));
                }
            }
            return set;
        }
        static <K extends Comparable<? super K>, V> java.util.Set<K> keySet(RBST<K, V> t) {
            java.util.Set<K> set = new java.util.LinkedHashSet<>();
            for (java.util.AbstractMap.SimpleEntry<K, V> e : entrySet(t)) set.add(e.getKey());
            return set;
        }
        static <K extends Comparable<? super K>, V> java.util.Set<K> descendingKeySet(RBST<K, V> t) {
            java.util.Set<K> set = new java.util.LinkedHashSet<>();
            for (java.util.AbstractMap.SimpleEntry<K, V> e : descendingEntrySet(t)) set.add(e.getKey());
            return set;
        }
        static <K extends Comparable<? super K>, V> java.util.Collection<V> values(RBST<K, V> t) {
            java.util.Collection<V> col = new java.util.ArrayList<>();
            for (java.util.AbstractMap.SimpleEntry<K, V> e : entrySet(t)) col.add(e.getValue());
            return col;
        }
        static <K extends Comparable<? super K>, V> java.util.Iterator<java.util.AbstractMap.SimpleEntry<K, V>> iterator(RBST<K, V> t) {
            return entrySet(t).iterator();
        }
        static <K extends Comparable<? super K>, V> java.util.Iterator<java.util.AbstractMap.SimpleEntry<K, V>> descendingIterator(RBST<K, V> t) {
            return descendingEntrySet(t).iterator();
        }
        static <K extends Comparable<? super K>, V> java.util.Iterator<K> keyIterator(RBST<K, V> t) {
            return new java.util.Iterator<K>(){
                java.util.Iterator<java.util.AbstractMap.SimpleEntry<K, V>> it = iterator(t);
                public boolean hasNext() {return it.hasNext();}
                public K next() {return it.next().getKey();}
            };
        }
        static <K extends Comparable<? super K>, V> java.util.Iterator<K> descendingKeyIterator(RBST<K, V> t) {
            return new java.util.Iterator<K>(){
                java.util.Iterator<java.util.AbstractMap.SimpleEntry<K, V>> it = descendingIterator(t);
                public boolean hasNext() {return it.hasNext();}
                public K next() {return it.next().getKey();}
            };
        }
        public String toString() {
            return "(" + getKey() + " => " + getValue() + ")";
        }
    }

    private static final class Random {
        private int x = 123456789, y = 362436069, z = 521288629, w = 88675123;
        public int nextInt() {
            int t = x ^ (x << 11);
            x = y; y = z; z = w;
            return w = (w ^ (w >> 19)) ^ (t ^ (t >> 8));
        }
    }
    private static final class Pair<E0, E1> {
        E0 fst;
        E1 snd;
        Pair(final E0 fst, final E1 snd) {this.fst = fst; this.snd = snd;}
    }
    private static final class IntObjPair<V> {
        final int key;
        final V value;
        IntObjPair(int key, V value) {this.key = key;this.value = value;}
    }
}

class OrderedSet<K extends Comparable<? super K>> implements Iterable<K> {
    private OrderedMap<K, Object> m = new OrderedMap<>();
    private static final Object PRESENT = new Object();
    public OrderedSet() {}
    private OrderedSet(OrderedMap<K, Object> m) {this.m = m;}
    public static <K extends Comparable<? super K>> OrderedSet<K> merge(OrderedSet<K> l, OrderedSet<K> r) {
        return l.mergeRight(r);
    }
    public OrderedSet<K> mergeLeft(OrderedSet<K> l) {
        return new OrderedSet<K>(OrderedMap.merge(l.m, m));
    }
    public OrderedSet<K> mergeRight(OrderedSet<K> r) {
        return new OrderedSet<K>(OrderedMap.merge(m, r.m));
    }
    public OrderedSet<K> splitRightUsingIndex(int k) {
        OrderedMap<K, Object> r = m.splitRightUsingIndex(k);
        return new OrderedSet<K>(r);
    }
    public OrderedSet<K> splitLeftUsingIndex(int k) {
        OrderedMap<K, Object> l = m.splitLeftUsingIndex(k);
        return new OrderedSet<K>(l);
    }
    public OrderedSet<K> splitRightUsingKey(K key) {
        OrderedMap<K, Object> r = m.splitRightUsingKey(key);
        return new OrderedSet<K>(r);
    }
    public OrderedSet<K> splitLeftUsingKey(K key) {
        OrderedMap<K, Object> l = m.splitLeftUsingKey(key);
        return new OrderedSet<K>(l);
    }
    public boolean contains(K key) {
        return m.containsKey(key);
    }
    public boolean add(K key) {
        return m.put(key, PRESENT) == null;
    }
    public boolean remove(K key) {
        return m.remove(key) == PRESENT;
    }
    public java.util.Optional<K> removeKthElement(int k) {
        return convertEntryToOptional(m.removeKthEntry(k));
    }
    public java.util.Optional<K> kthElement(int k) {
        return convertEntryToOptional(m.kthEntry(k));
    }
    public java.util.Optional<K> first() {
        return convertEntryToOptional(m.firstEntry());
    }
    public java.util.Optional<K> last() {
        return convertEntryToOptional(m.lastEntry ());
    }
    public java.util.Optional<K> lower(K e) {
        return convertEntryToOptional(m.lowerEntry(e));
    }
    public java.util.Optional<K> floor(K e) {
        return convertEntryToOptional(m.floorEntry(e));
    }
    public java.util.Optional<K> ceiling(K e) {
        return convertEntryToOptional(m.ceilingEntry(e));
    }
    public java.util.Optional<K> higher(K e) {
        return convertEntryToOptional(m.higherEntry (e));
    }
    public java.util.Optional<K> removeFirst() {
        return convertEntryToOptional(m.removeKthEntry(0));
    }
    public java.util.Optional<K> removeLast() {
        return convertEntryToOptional(m.removeKthEntry(size() - 1));
    }
    private static <K> java.util.Optional<K> convertEntryToOptional(java.util.AbstractMap.SimpleEntry<K, ?> e) {
        return e == null ? java.util.Optional.empty() : java.util.Optional.of(e.getKey());
    }
    public int countLessThan(K key) {
        return m.countLessThan(key);
    }
    public int countLessThanOrEqual(K key) {
        return m.countLessThanOrEqual(key);
    }
    public int countGreaterThan(K key) {
        return m.countGreaterThan(key);
    }
    public int countGreaterThanOrEqual(K key) {
        return m.countGreaterThanOrEqual(key);
    }
    public int countRange(K fromKey, K toKey) {
        return m.countRange(fromKey, toKey);
    }
    public int size() {
        return m.size();
    }
    public boolean isEmpty() {
        return m.size() == 0;
    }
    public void clear() {
        m.clear();
    }
    public java.util.Iterator<K> iterator() {
        return m.keyIterator();
    }
    public java.util.Iterator<K> descendingIterator() {
        return m.descendingKeyIterator();
    }
}