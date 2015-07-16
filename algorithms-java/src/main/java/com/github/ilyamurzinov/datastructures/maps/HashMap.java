package com.github.ilyamurzinov.datastructures.maps;

import java.util.Collection;
import java.util.Set;

/**
 * @author Ilya Murzinov
 */
public class HashMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final int MAXIMUM_CAPACITY = 1 << 20; // not 1 << 30, for testing purposes
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private Entry<K, V>[] buckets;
    private int size;
    private int capacity;
    private int threshold;
    private double loadFactor;

    public HashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int initialCapacity, double loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        } else if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if (loadFactor <= 0 || Double.isNaN(loadFactor)) {
            throw new IllegalArgumentException("LoadFactor must be positive number");
        } else if (loadFactor > 1) {
            loadFactor = DEFAULT_LOAD_FACTOR;
        }

        this.capacity = getCapacity(initialCapacity);
        this.loadFactor = loadFactor;
        this.threshold = (int) (this.capacity * this.loadFactor);
        this.buckets = new Entry[this.capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(K key) {
        return false;
    }

    @Override
    public boolean containsValue(V value) {
        return false;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            return buckets[0] == null ? null : buckets[0].getValue();
        } else {
            int hash = hash(key.hashCode());
            Entry<K, V> entry = buckets[indexFor(hash)];
            while (entry != null) {
                if (key.equals(entry.getKey())) {
                    return entry.getValue();
                }
                entry = entry.next;
            }
            return null;
        }
    }

    @Override
    public V put(K key, V value) {
        if (key == null) {
            return putForNullKey(value);
        } else {
            int hash = hash(key.hashCode());
            int index = indexFor(hash);
            Entry<K, V> entry = buckets[index];
            while (entry != null) {
                if (key.equals(entry.getKey())) {
                    return entry.setValue(value);
                }
                entry = entry.next;
            }
            resize();
            addEntry(key, value, hash, index);
            size++;
            return null;
        }
    }

    @Override
    public V remove(K key) {
        if (key == null) {
            return removeForNullKey();
        } else {
            int hash = hash(key.hashCode());
            int index = indexFor(hash);
            Entry<K, V> entry = buckets[index];

            if (entry == null) {
                return null;
            }

            Entry<K, V> next = entry.next;

            if (key.equals(entry.key)) {
                V result = entry.getValue();
                buckets[index] = next;
                size--;
                return result;
            }

            while (entry != null) {
                if (key.equals(entry.key)) {
                    V result = entry.getValue();
                    entry.next = next == null ? null : next.next;
                    size--;
                    return result;
                }
                entry = next;
                next = next == null ? null : next.next;
            }
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            int hash = hash(entry.hashCode());
            addEntry(entry.getKey(), entry.getValue(), hash, indexFor(hash));
        }
    }

    @Override
    public void clear() {
        capacity = DEFAULT_CAPACITY;
        buckets = new Entry[capacity];
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<? extends Map.Entry<K, V>> entrySet() {
        return null;
    }

    private int getCapacity(int capacity) {
        int result = 1;
        while (result < capacity) {
            result <<= 1;
        }
        return result;
    }

    private void resize() {
        if (size + 1 >= threshold) {
            int newCapacity = capacity << 1;
            Entry[] oldBuckets = buckets;
            buckets = new Entry[newCapacity];
            transfer(oldBuckets);
            capacity = newCapacity;
            threshold = (int) (capacity * loadFactor);
        }
    }

    private void transfer(Entry[] oldBuckets) {
        for (Entry<K, V> entry : oldBuckets) {
            if (entry == null) {
                continue;
            }

            int index = indexFor(entry.hash);
            addEntry(entry, index);
        }
    }

    private void addEntry(K key, V value, int hash, int index) {
        Entry<K, V> entry = buckets[index];
        if (entry == null) {
            buckets[index] = new Entry<>(key, value, hash);
        } else {
            while (entry.next != null) {
                entry = entry.next;
            }
            entry.next = new Entry<>(key, value, hash);
        }
    }

    private void addEntry(Entry<K, V> entry, int index) {
        Entry<K, V> oldEntry = buckets[index];
        if (oldEntry == null) {
            buckets[index] = entry;
        } else {
            while (entry.next != null) {
                entry = entry.next;
            }
            entry.next = entry;
        }
    }

    private V putForNullKey(V value) {
        int index = 0;
        Entry<K, V> entry = buckets[index];
        while (entry != null) {
            if (entry.key == null) {
                V result = entry.getValue();
                entry.setValue(value);
                return result;
            }
            entry = entry.next;
        }

        resize();
        addEntry(null, value, 0, 0);
        size++;
        return null;
    }

    private V removeForNullKey() {
        int index = 0;
        Entry<K, V> entry = buckets[index];

        if (entry == null) {
            return null;
        }

        Entry<K, V> next = entry.next;

        if (entry.key == null) {
            V result = entry.getValue();
            buckets[index] = next;
            size--;
            return result;
        }

        while (entry != null) {
            if (entry.key == null) {
                V result = entry.getValue();
                entry.next = next == null ? null : next.next;
                size--;
                return result;
            }
            entry = next;
            next = next == null ? null : next.next;
        }
        return null;
    }

    private int hash(int hashCode) {
        hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
        return hashCode ^ (hashCode >>> 7) ^ (hashCode >>> 4);
    }

    private int indexFor(int hash) {
        return hash & (buckets.length - 1);
    }

    private static final class Entry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;
        private final int hash;
        private Entry<K, V> next;

        private Entry(K key, V value, int hash) {
            this.key = key;
            this.value = value;
            this.hash = hash;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
}
