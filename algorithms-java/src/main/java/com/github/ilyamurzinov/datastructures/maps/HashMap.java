package com.github.ilyamurzinov.datastructures.maps;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Ilya Murzinov
 */
public class HashMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_CAPACITY = 1 << 4;
    private static final int MAXIMUM_CAPACITY = 1 << 20; // not 1 << 30, for testing purposes
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private HashMapEntry<K, V>[] buckets;
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
        this.buckets = new HashMapEntry[this.capacity];
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
        return keySet().contains(key);
    }

    @Override
    public boolean containsValue(V value) {
        return values().contains(value);
    }

    @Override
    public V get(K key) {
        int hash = key == null ? 0 : hash(key.hashCode());
        int index = indexFor(hash);

        HashMapEntry<K, V> hashMapEntry = buckets[index];

        while (hashMapEntry != null) {
            if (key == null ? hashMapEntry.key == null : key.equals(hashMapEntry.getKey())) {
                return hashMapEntry.getValue();
            }
            hashMapEntry = hashMapEntry.next;
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (key == null) {
            return putForNullKey(value);
        } else {
            int hash = hash(key.hashCode());
            int index = indexFor(hash);
            HashMapEntry<K, V> hashMapEntry = buckets[index];
            while (hashMapEntry != null) {
                if (key.equals(hashMapEntry.getKey())) {
                    hashMapEntry.hash = hash;
                    return hashMapEntry.setValue(value);
                }
                hashMapEntry = hashMapEntry.next;
            }
            resize();
            index = indexFor(hash);
            addEntry(key, value, hash, index);
            size++;
            return null;
        }
    }

    @Override
    public V remove(K key) {
        int hash = key == null ? 0 : hash(key.hashCode());
        int index = indexFor(hash);
        HashMapEntry<K, V> hashMapEntry = buckets[index];

        if (hashMapEntry == null) {
            return null;
        }

        HashMapEntry<K, V> previous;
        HashMapEntry<K, V> next = hashMapEntry.next;

        if (key == null ? hashMapEntry.key == null : key.equals(hashMapEntry.key)) {
            V result = hashMapEntry.getValue();
            buckets[index] = next;
            size--;
            return result;
        }

        previous = hashMapEntry;
        hashMapEntry = next;
        next = next.next;

        while (hashMapEntry != null) {
            if (key == null ? hashMapEntry.key == null : key.equals(hashMapEntry.key)) {
                V result = hashMapEntry.getValue();
                previous.next = next;
                size--;
                return result;
            }
            previous = hashMapEntry;
            hashMapEntry = next;
            next = next == null ? null : next.next;
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        if (size > 0) {
            size = 0;
            for (int i = 0; i < buckets.length; i++) {
                buckets[i] = null;
            }
        }
    }

    @Override
    public Set<K> keySet() {
        return new AbstractSet<K>() {
            @Override
            public Iterator<K> iterator() {
                return new KeysIterator<>(buckets);
            }

            @Override
            public int size() {
                return size;
            }
        };
    }

    @Override
    public Collection<V> values() {
        return new AbstractCollection<V>() {
            @Override
            public Iterator<V> iterator() {
                return new ValuesIterator<>(buckets);
            }

            @Override
            public int size() {
                return size;
            }
        };
    }

    @Override
    public Set<? extends Map.Entry<K, V>> entrySet() {
        return new AbstractSet<HashMapEntry<K, V>>() {
            @Override
            public Iterator iterator() {
                return new EntryIterator<>(buckets);
            }

            @Override
            public int size() {
                return size;
            }
        };
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
            HashMapEntry<K, V>[] oldBuckets = buckets;
            buckets = new HashMapEntry[newCapacity];
            size = 0;
            transfer(oldBuckets);
            capacity = newCapacity;
            threshold = (int) (capacity * loadFactor);
        }
    }

    private void transfer(HashMapEntry<K, V>[] oldBuckets) {
        for (HashMapEntry<K, V> hashMapEntry : oldBuckets) {
            while (hashMapEntry != null) {
                put(hashMapEntry.key, hashMapEntry.value);
                hashMapEntry = hashMapEntry.next;
            }
        }
    }

    private void addEntry(K key, V value, int hash, int index) {
        HashMapEntry<K, V> hashMapEntry = buckets[index];
        if (hashMapEntry == null) {
            buckets[index] = new HashMapEntry<>(key, value, hash);
        } else {
            while (hashMapEntry.next != null) {
                hashMapEntry = hashMapEntry.next;
            }
            hashMapEntry.next = new HashMapEntry<>(key, value, hash);
        }
    }

    private V putForNullKey(V value) {
        int index = 0;
        HashMapEntry<K, V> hashMapEntry = buckets[index];
        while (hashMapEntry != null) {
            if (hashMapEntry.key == null) {
                V result = hashMapEntry.getValue();
                hashMapEntry.setValue(value);
                return result;
            }
            hashMapEntry = hashMapEntry.next;
        }

        resize();
        addEntry(null, value, 0, 0);
        size++;
        return null;
    }

    private int hash(int hashCode) {
        hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
        return hashCode ^ (hashCode >>> 7) ^ (hashCode >>> 4);
    }

    private int indexFor(int hash) {
        return hash & (buckets.length - 1);
    }

    private static final class HashMapEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;
        private int hash;
        private HashMapEntry<K, V> next;

        private HashMapEntry(K key, V value, int hash) {
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

    private static abstract class AbstractEntryIterator<K, V> {
        private final HashMapEntry<K, V>[] buckets;
        private int currentBucket = 0;
        private HashMapEntry<K, V> next;

        public AbstractEntryIterator(HashMapEntry<K, V>[] buckets) {
            this.buckets = buckets;
            while (currentBucket < buckets.length && buckets[currentBucket] == null)
                currentBucket++;
            next = currentBucket < buckets.length ? buckets[currentBucket] : null;
        }

        public boolean hasNext() {
            return next != null;
        }

        public HashMapEntry<K, V> nextEntry() {
            HashMapEntry<K, V> tmp = next;

            if (next.next != null) {
                next = next.next;
            } else {
                currentBucket++;

                while (currentBucket < buckets.length && buckets[currentBucket] == null)
                    currentBucket++;
                next = currentBucket < buckets.length ? buckets[currentBucket] : null;
            }

            return tmp;
        }
    }

    private static final class EntryIterator<K, V> extends AbstractEntryIterator<K, V> implements
            Iterator<HashMapEntry<K, V>> {
        public EntryIterator(HashMapEntry<K, V>[] buckets) {
            super(buckets);
        }

        @Override
        public boolean hasNext() {
            return super.hasNext();
        }

        @Override
        public HashMapEntry<K, V> next() {
            return super.nextEntry();
        }
    }

    private static final class ValuesIterator<K, V> extends AbstractEntryIterator<K, V> implements Iterator<V> {
        public ValuesIterator(HashMapEntry<K, V>[] buckets) {
            super(buckets);
        }

        @Override
        public boolean hasNext() {
            return super.hasNext();
        }

        @Override
        public V next() {
            return super.nextEntry().value;
        }
    }

    private static final class KeysIterator<K, V> extends AbstractEntryIterator<K, V> implements Iterator<K> {
        public KeysIterator(HashMapEntry<K, V>[] buckets) {
            super(buckets);
        }

        @Override
        public boolean hasNext() {
            return super.hasNext();
        }

        @Override
        public K next() {
            return super.nextEntry().key;
        }
    }
}
