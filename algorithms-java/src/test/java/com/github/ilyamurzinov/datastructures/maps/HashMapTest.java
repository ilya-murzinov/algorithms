package com.github.ilyamurzinov.datastructures.maps;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * @author Ilya Murzinov
 */
public class HashMapTest {

    @Test
    public void shouldBeAbleToConstructMapWithInitialCapacity() throws Exception {
        HashMap<TestClass0, String> map = new HashMap<>(123);
        assertEquals(128, getCapacity(map));
    }

    @Test
    public void shouldBeAbleToConstructMapWithInitialCapacityGreaterThanMaximum() throws Exception {
        HashMap<TestClass0, String> map = new HashMap<>((1 << 20) + 1);
        assertEquals(1 << 20, getCapacity(map));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotBeAbleToConstructMapWithIncorrectInitialCapacity() throws Exception {
        new HashMap<>(-123);
    }

    @Test
    public void shouldBeAbleToConstructMapWithInitialCapacityAndLoadFactor() throws Exception {
        HashMap<TestClass0, String> map = new HashMap<>(123, 0.5);
        assertEquals(0.5, getLoadFactor(map), 0);
    }

    @Test
    public void shouldBeAbleToConstructMapWithLoadFactorGreaterThanOne() throws Exception {
        HashMap<TestClass0, String> map = new HashMap<>(123, 100500);
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
        HashMap<Integer, Integer> map = new HashMap<>(1 << 10, 0.5);
        assertEquals(0, map.size());
        for (int i = 0; i < (1 << 9) + 1; i++) {
            map.put(i, i);
        }
        assertEquals((1 << 9) + 1, map.size());
    }

    @Test
    public void mapShouldReturnIsEmpty() throws Exception {
        HashMap<Integer, Integer> map = new HashMap<>(1 << 10, 0.5);
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
        HashMap<Integer, Integer> map = new HashMap<>(1 << 10, 0.5);
        for (int i = 0; i < (1 << 9) + 1; i++) {
            map.put(i, i);
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
        String oldValue = map.remove(new TestClass0(12));
        assertEquals("value", oldValue);
    }

    @Test
    public void mapShouldHandleInefficientHashCodeCorrectly() throws Exception {
        Map<TestClass0, String> map = new HashMap<>();
        for (int i = 0; i < 1 << 10; i++) {
            map.put(new TestClass0(i), "value" + i);
        }
        for (int i = 0; i < 1 << 10; i++) {
            assertEquals("value" + i, map.get(new TestClass0(i)));
        }
    }

    private int getCapacity(HashMap map) throws Exception {
        Field field = map.getClass().getDeclaredField("capacity");
        field.setAccessible(true);
        return (int) field.get(map);
    }

    private double getLoadFactor(HashMap map) throws Exception {
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

            TestClass42 testClass42 = (TestClass42) o;

            return i == testClass42.i;

        }

        @Override
        public int hashCode() {
            return 0;
        }
    }
}
