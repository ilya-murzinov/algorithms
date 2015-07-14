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
        HashMap<TestClass, String> map = new HashMap<>(123);
        assertEquals(128, getCapacity(map));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotBeAbleToConstructMapWithIncorrectInitialCapacity() throws Exception {
        new HashMap<>(-123);
    }

    @Test
    public void shouldBeAbleToConstructMapWithInitialCapacityAndLoadFactor() throws Exception {
        HashMap<TestClass, String> map = new HashMap<>(123, 0.5);
        assertEquals(0.5, getLoadFactor(map), 0);
    }

    @Test
    public void shouldBeAbleToConstructMapWithLoadFactorGreaterThanOne() throws Exception {
        HashMap<TestClass, String> map = new HashMap<>(123, 100500);
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
    public void mapShouldPutValueWithNullKey() throws Exception {
        Map<TestClass, String> map = new HashMap<>();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());

        map.put(null, "value");
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());

        String oldValue = map.put(null, "new value");
        assertEquals("value", oldValue);
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
    }

    @Test
    public void mapShouldGetValueWithNullKey() throws Exception {
        Map<TestClass, String> map = new HashMap<>();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());

        map.put(null, "value");
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());

        String oldValue = map.get(null);
        assertEquals("value", oldValue);
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
    }

    @Test
    public void mapShouldRemoveValueWithNullKey() throws Exception {
        Map<TestClass, String> map = new HashMap<>();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());

        map.put(null, "value");
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());

        String oldValue = map.remove(null);
        assertEquals("value", oldValue);
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
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

    class TestClass {
        private int i;

        TestClass(int i) {
            this.i = i;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            TestClass testClass = (TestClass) o;

            return i == testClass.i;

        }

        @Override
        public int hashCode() {
            return 0;
        }
    }
}
