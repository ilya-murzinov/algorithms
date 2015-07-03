package com.github.ilyamurzinov.datastructures.trees;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * @author Ilya Murzinov
 */
@SuppressWarnings("ConstantConditions")
public class BinomialTreeTest {

    BinomialTree<Integer> tree1 = new BinomialTree<>(6);
    BinomialTree<Integer> tree2 = new BinomialTree<>(44);
    BinomialTree<Integer> tree3 = new BinomialTree<>(10);
    BinomialTree<Integer> tree4 = new BinomialTree<>(17);

    BinomialTree<Integer> tree5 = new BinomialTree<>(29);
    BinomialTree<Integer> tree6 = new BinomialTree<>(31);
    BinomialTree<Integer> tree7 = new BinomialTree<>(48);
    BinomialTree<Integer> tree8 = new BinomialTree<>(50);

    @Test
    public void testMerge() throws Exception {
        BinomialTree<Integer> tree1 = new BinomialTree<>(1);
        BinomialTree<Integer> tree2 = new BinomialTree<>(2);

        BinomialTree<Integer> merge = tree1.merge(tree2);

        assertEquals(1, merge.getRootValue(), 0);
    }

    @Test
    public void testMergeTreesOfDegree0() throws Exception {
        BinomialTree<Integer> merged12 = tree1.merge(tree2);
        assertEquals(1, merged12.getDegree());
        assertEquals(6, merged12.getRootValue(), 0);
        assertEquals(44, getChildrenSubtree(merged12).getRootValue(), 0);
        assertNull(getSiblingsSubtree(merged12));
    }

    @Test
    public void testMergeTreesOfDegree1() throws Exception {
        BinomialTree<Integer> merged12 = tree1.merge(tree2);
        BinomialTree<Integer> merged34 = tree3.merge(tree4);

        BinomialTree<Integer> merged1234 = merged12.merge(merged34);

        assertEquals(2, merged1234.getDegree());
        assertEquals(6, merged1234.getRootValue(), 0);
        assertNull(getSiblingsSubtree(merged1234));

        BinomialTree<Integer> childrenSubtree = getChildrenSubtree(merged1234);
        assertEquals(10, childrenSubtree.getRootValue(), 0);
        assertEquals(17, getChildrenSubtree(childrenSubtree).getRootValue(), 0);
        assertEquals(44, getSiblingsSubtree(childrenSubtree).getRootValue(), 0);
    }

    @Test
    public void testMergeTreesOfDegree2() throws Exception {
        BinomialTree<Integer> result = mergeAllTrees();

        assertEquals(3, result.getDegree());
        assertEquals(6, result.getRootValue(), 0);
        assertNull(getSiblingsSubtree(result));

        BinomialTree<Integer> childrenSubtree = getChildrenSubtree(result);
        BinomialTree<Integer> siblingsSubtree = getSiblingsSubtree(childrenSubtree);
        assertEquals(29, childrenSubtree.getRootValue(), 0);
        assertEquals(10, siblingsSubtree.getRootValue(), 0);

        BinomialTree<Integer> childrenSubtree1 = getChildrenSubtree(childrenSubtree);
        BinomialTree<Integer> siblingsSubtree1 = getSiblingsSubtree(childrenSubtree1);
        assertEquals(48, childrenSubtree1.getRootValue(), 0);
        assertEquals(31, siblingsSubtree1.getRootValue(), 0);

        assertEquals(50, getChildrenSubtree(childrenSubtree1).getRootValue(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMergeDifferentDegree() throws Exception {
        BinomialTree<Integer> tree1 = new BinomialTree<>(1);
        BinomialTree<Integer> tree2 = new BinomialTree<>(2);

        BinomialTree<Integer> merged = tree1.merge(tree2);

        merged.merge(tree1);
    }

    @Test
    public void testDeleteRoot() throws Exception {
        BinomialTree<Integer> integerBinomialTree = mergeAllTrees();
        integerBinomialTree.deleteRoot();
    }

    private BinomialTree<Integer> mergeAllTrees() {
        BinomialTree<Integer> merged12 = tree1.merge(tree2);
        BinomialTree<Integer> merged34 = tree3.merge(tree4);

        BinomialTree<Integer> merged1234 = merged12.merge(merged34);

        BinomialTree<Integer> merged56 = tree5.merge(tree6);
        BinomialTree<Integer> merged78 = tree7.merge(tree8);

        BinomialTree<Integer> merged5678 = merged56.merge(merged78);

        return merged1234.merge(merged5678);
    }

    private <T> BinomialTree<T> getChildrenSubtree(BinomialTree<T> tree) throws Exception {
        Field rootField = tree.getClass().getDeclaredField("root");
        rootField.setAccessible(true);

        Object root = rootField.get(tree);
        Field valueField = root.getClass().getDeclaredField("childrenRoot");
        valueField.setAccessible(true);

        Object childrenRoot = valueField.get(root);

        if(childrenRoot == null) {
            return null;
        }

        Constructor constructor = tree.getClass().getDeclaredConstructor(childrenRoot.getClass());
        constructor.setAccessible(true);
        return (BinomialTree<T>) constructor.newInstance(childrenRoot);
    }

    private <T> BinomialTree<T> getSiblingsSubtree(BinomialTree<T> tree) throws Exception {
        Field rootField = tree.getClass().getDeclaredField("root");
        rootField.setAccessible(true);

        Object root = rootField.get(tree);
        Field valueField = root.getClass().getDeclaredField("siblingsRoot");
        valueField.setAccessible(true);

        Object siblingsRoot = valueField.get(root);

        if(siblingsRoot == null) {
            return null;
        }

        Constructor constructor = tree.getClass().getDeclaredConstructor(siblingsRoot.getClass());
        constructor.setAccessible(true);
        return (BinomialTree<T>) constructor.newInstance(siblingsRoot);
    }
}
