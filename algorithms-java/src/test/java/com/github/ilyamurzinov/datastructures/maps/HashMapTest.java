package com.github.ilyamurzinov.datastructures.maps;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Ilya Murzinov
 */
public class HashMapTest {

    private Random random = new Random();

    @Test
    public void shouldBeAbleToConstructMapWithInitialCapacity() throws Exception {
        Map<TestClass0, String> map = new HashMap<>(123);
        assertEquals(128, getCapacity(map));
    }

    @Test
    public void shouldBeAbleToConstructMapWithInitialCapacityGreaterThanMaximum() throws Exception {
        Map<TestClass0, String> map = new HashMap<>((1 << 20) + 1);
        assertEquals(1 << 20, getCapacity(map));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotBeAbleToConstructMapWithIncorrectInitialCapacity() throws Exception {
        new HashMap<>(-123);
    }

    @Test
    public void shouldBeAbleToConstructMapWithInitialCapacityAndLoadFactor() throws Exception {
        Map<TestClass0, String> map = new HashMap<>(123, 0.5);
        assertEquals(0.5, getLoadFactor(map), 0);
    }

    @Test
    public void shouldBeAbleToConstructMapWithLoadFactorGreaterThanOne() throws Exception {
        Map<TestClass0, String> map = new HashMap<>(123, 100500);
        assertEquals(0.75, getLoadFactor(map), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotBeAbleToConstructMapWithIncorrectLoadFactor() throws Exception {
        new HashMap<>(123, Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotBeAbleToConstructMapWithIncorrectLoadFactor2() throws Exception {
        new HashMap<>(123, -1);
    }

    @Test
    public void mapShouldReturnCorrectSize() throws Exception {
        Map<Integer, Integer> map = new HashMap<>(1 << 10, 0.5);
        assertEquals(0, map.size());
        for (int i = 0; i < (1 << 9) + 1; i++) {
            map.put(i, i);
        }
        assertEquals((1 << 9) + 1, map.size());
    }

    @Test
    public void mapShouldReturnIsEmpty() throws Exception {
        Map<Integer, Integer> map = new HashMap<>(1 << 10, 0.5);
        assertTrue(map.isEmpty());
        for (int i = 0; i < (1 << 9) + 1; i++) {
            map.put(i, i);
            assertFalse(map.isEmpty());
        }

        for (int i = 0; i < (1 << 9) + 1; i++) {
            map.remove(i);
        }
        assertTrue(map.isEmpty());
    }

    @Test
    public void mapShouldPutValueWithNullKey() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        map.put(null, "value");
        String oldValue = map.put(null, "new value");
        assertEquals("value", oldValue);
    }

    @Test
    public void mapShouldGetValueWithNullKey() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        map.put(null, "value");
        String oldValue = map.get(null);
        assertEquals("value", oldValue);
    }

    @Test
    public void mapShouldRemoveValueWithNullKey() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        map.put(null, "value");
        String oldValue = map.remove(null);
        assertEquals("value", oldValue);
    }

    @Test
    public void mapShouldGetValueWithNullKeyWhenItsNotPresent() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        String value = map.get(null);
        assertNull(value);
    }

    @Test
    public void mapShouldRemoveValueWithNullKeyWhenItsNotPresent() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        String oldValue = map.remove(null);
        assertNull(oldValue);
    }

    @Test
    public void mapShouldGetValueWithNullKeyWhenItsNotPresentAndBucketIsNotEmpty() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        map.put(new TestClass0(0), "value1");
        map.put(new TestClass0(1), "value2");
        String value = map.get(null);
        assertNull(value);
    }

    @Test
    public void mapShouldRemoveValueWithNullKeyWhenItsNotPresentAndBucketIsNotEmpty() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        map.put(new TestClass0(0), "value1");
        map.put(new TestClass0(1), "value2");
        String oldValue = map.remove(null);
        assertNull(oldValue);
    }

    @Test
    public void mapShouldGetValueWithNullKeyWhenItsPresentAndBucketIsNotEmpty() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        map.put(new TestClass0(0), "value1");
        map.put(new TestClass0(1), "value2");
        map.put(null, "value");
        String value = map.get(null);
        assertEquals("value", value);
    }

    @Test
    public void mapShouldRemoveValueWithNullKeyWhenItsPresentAndBucketIsNotEmpty() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        map.put(new TestClass0(0), "value1");
        map.put(new TestClass0(1), "value2");
        map.put(null, "value");
        String oldValue = map.remove(null);
        assertEquals("value", oldValue);
    }

    @Test
    public void mapShouldPutValueWithNonNullKey() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        String oldValue = map.put("key", "new value");
        assertEquals("value", oldValue);
    }

    @Test
    public void mapShouldGetValueWithNonNullKey() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        String oldValue = map.get("key");
        assertEquals("value", oldValue);
    }

    @Test
    public void mapShouldRemoveValueWithNonNullKey() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        String oldValue = map.remove("key");
        assertEquals("value", oldValue);
    }

    @Test
    public void mapShouldResizeAfterReachedThreshold() throws Exception {
        Map<Integer, Integer> map = new HashMap<>(1 << 10, 0.5);
        for (int i = 0; i < (1 << 9) + 1; i++) {
            int integer = random.nextInt(1000);
            map.put(i, integer);
        }
        assertEquals(1 << 11, getCapacity(map));
    }

    @Test
    public void mapShouldGetValueWithNonNullKeyWhenItsNotPresent() throws Exception {
        Map<String, String> map = new HashMap<>();
        String value = map.get("key");
        assertNull(value);
    }

    @Test
    public void mapShouldRemoveValueWithNonNullKeyWhenItsNotPresent() throws Exception {
        Map<String, String> map = new HashMap<>();
        String oldValue = map.remove("key");
        assertNull(oldValue);
    }

    @Test
    public void mapShouldGetValueWithNonNullKeyWhenItsNotPresentAndBucketIsNotEmpty() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        map.put(new TestClass0(0), "value1");
        map.put(new TestClass0(1), "value2");
        String value = map.get(new TestClass0(12));
        assertNull(value);
    }

    @Test
    public void mapShouldRemoveValueWithNonNullKeyWhenItsNotPresentAndBucketIsNotEmpty() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        map.put(new TestClass0(0), "value1");
        map.put(new TestClass0(1), "value2");
        map.put(new TestClass0(1), "value3");
        map.put(new TestClass0(1), "value4");
        String oldValue = map.remove(new TestClass0(12));
        assertNull(oldValue);
    }

    @Test
    public void mapShouldGetValueWithNonNullKeyWhenItsPresentAndBucketIsNotEmpty() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        map.put(new TestClass0(0), "value1");
        map.put(new TestClass0(1), "value2");
        map.put(new TestClass0(12), "value");
        String value = map.get(new TestClass0(12));
        assertEquals("value", value);
    }

    @Test
    public void mapShouldRemoveValueWithNonNullKeyWhenItsPresentAndBucketIsNotEmpty() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        map.put(new TestClass0(0), "value1");
        map.put(new TestClass0(1), "value2");
        map.put(new TestClass0(12), "value");
        map.put(new TestClass0(3), "value3");
        map.put(new TestClass0(4), "value4");
        String oldValue = map.remove(new TestClass0(12));
        assertEquals("value", oldValue);
    }

    @Test
    public void mapShouldAddGetRemoveMultipleValues() throws Exception {
        Map<String, String> map = new HashMap<>();
        Set<String> values = new HashSet<>();
        for (int i = 0; i < 1 << 4; i++) {
            String value = String.valueOf(random.nextDouble());
            if (!values.contains(value)) {
                map.put(value, value);
                values.add(value);
            } else {
                assertEquals(value, map.put(value, value));
            }
            System.out.println(value);
        }

        for (String value : values) {
            assertEquals(value, map.get(value));
        }
    }

    @Test
    public void mapShouldHandleConstantHashCodeCorrectly() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        for (int i = 0; i < 1 << 10; i++) {
            map.put(new TestClass0(i), "value" + i);
        }
        for (int i = 0; i < 1 << 10; i++) {
            assertEquals("value" + i, map.get(new TestClass0(i)));
        }
    }

    @Test
    public void mapShouldReturnCorrectEntrySet() throws Exception {
        Map<Integer, Integer> map = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 1 << 10; i++) {
            map.put(i, i);
            set.add(i);
        }

        assertEquals(1 << 10, map.entrySet().size());

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            assertTrue(set.contains(entry.getValue()));
            assertTrue(set.contains(entry.getKey()));
            set.remove(entry.getValue());
        }

        assertTrue(set.isEmpty());
    }

    @Test
    public void mapShouldReturnCorrectValues() throws Exception {
        Map<Integer, Integer> map = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 1 << 10; i++) {
            map.put(i, i);
            set.add(i);
        }

        assertEquals(1 << 10, map.values().size());
        assertTrue(set.containsAll(map.values()) && map.values().containsAll(set));
    }

    @Test
    public void mapShouldReturnCorrectKeySet() throws Exception {
        Map<Integer, Integer> map = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 1 << 10; i++) {
            map.put(i, i);
            set.add(i);
        }

        assertEquals(1 << 10, map.keySet().size());
        assertTrue(set.containsAll(map.keySet()) && map.keySet().containsAll(set));
    }

    @Test
    public void mapShouldReturnCorrectEntrySetWithConstantHashCode() throws Exception {
        Map<TestClass42, Integer> map = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 1 << 10; i++) {
            map.put(new TestClass42(i), i);
            set.add(i);
        }

        assertEquals(1 << 10, map.entrySet().size());

        for (Map.Entry<TestClass42, Integer> entry : map.entrySet()) {
            assertTrue(set.contains(entry.getValue()));
            set.remove(entry.getValue());
        }

        assertTrue(set.isEmpty());
    }

    @Test
    public void mapShouldReturnCorrectValuesWithConstantHashCode() throws Exception {
        Map<TestClass0, Integer> map = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 1 << 10; i++) {
            map.put(new TestClass0(i), i);
            set.add(i);
        }

        assertEquals(1 << 10, map.values().size());
        assertTrue(set.containsAll(map.values()) && map.values().containsAll(set));
    }

    @Test
    public void mapShouldReturnCorrectKeySetWithConstantHashCode() throws Exception {
        Map<TestClass0, Integer> map = new HashMap<>();
        Set<TestClass0> set = new HashSet<>();
        for (int i = 0; i < 1 << 10; i++) {
            map.put(new TestClass0(i), i);
            set.add(new TestClass0(i));
        }

        assertEquals(1 << 10, map.keySet().size());
        assertTrue(set.containsAll(map.keySet()) && map.keySet().containsAll(set));
    }

    @Test
    public void mapShouldReturnCorrectSetsWhenEmpty() throws Exception {
        Map<Integer, Integer> map = new HashMap<>();
        assertTrue(map.entrySet().isEmpty());
        assertTrue(map.keySet().isEmpty());
        assertTrue(map.values().isEmpty());
    }

    @Test
    public void mapShouldPutAllElementsFromAnotherMap() throws Exception {
        Map<Integer, Integer> map1 = new HashMap<>();
        Map<Integer, Integer> map2 = new HashMap<>();

        for (int i = 0; i < 1 << 6; i++) {
            if (i % 2 == 0)
                map1.put(i, i);
            else
                map2.put(i, i);
        }

        map1.putAll(map2);

        for (int i = 0; i < 1 << 6; i++) {
            Integer actual = map1.get(i);
            assertNotNull(String.valueOf(i), actual);
            assertEquals(i, actual, 0);
        }
    }

    @Test
    public void mapShouldClearCorrectly() throws Exception {
        Map<Integer, Integer> map = new HashMap<>();
        int capacity = getCapacity(map);
        map.clear();
        assertEquals(0, map.size(), 0);
        assertTrue(map.isEmpty());
        assertEquals(capacity, getCapacity(map), 0);

        for (int i = 0; i < 1 << 10; i++) {
            map.put(i, i);
        }
        capacity = getCapacity(map);
        map.clear();
        assertEquals(0, map.size(), 0);
        assertTrue(map.isEmpty());
        assertEquals(capacity, getCapacity(map), 0);
    }

    private int getCapacity(Map map) throws Exception {
        Field field = map.getClass().getDeclaredField("capacity");
        field.setAccessible(true);
        return (int) field.get(map);
    }

    private double getLoadFactor(Map map) throws Exception {
        Field field = map.getClass().getDeclaredField("loadFactor");
        field.setAccessible(true);
        return (double) field.get(map);
    }

    class TestClass0 {
        private int i;

        TestClass0(int i) {
            this.i = i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            TestClass0 testClass0 = (TestClass0) o;

            return i == testClass0.i;

        }

        @Override
        public int hashCode() {
            return 0;
        }
    }

    class TestClass42 {
        private int i;

        TestClass42(int i) {
            this.i = i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            TestClass42 testClass0 = (TestClass42) o;

            return i == testClass0.i;

        }

        @Override
        public int hashCode() {
            return 42;
        }
    }
}
