package aston.tests;

import aston.code.lesson5.MyArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class MyArrayListTest {
    private MyArrayList<Integer> integerList;
    private MyArrayList<String> stringList;
    private MyArrayList<Integer> sortedInts;
    private MyArrayList<String> sortedStrings;

    @BeforeEach
    public void beforeAll() {
        integerList = new MyArrayList<>();
        stringList = new MyArrayList<>();
    }

    @Test
    public void testAdd() {
        integerList.add(999);
        integerList.add(18);
        integerList.add(180_000);

        stringList.add("three nines");
        stringList.add("adult age");
        stringList.add("too much to write");

        assertThat(integerList.toString()).isEqualTo("[999, 18, 180000]");
        assertThat(stringList.toString()).isEqualTo("[three nines, adult age, too much to write]");
    }

    @Test
    public void testAddAll() {
        integerList.addAll(List.of(1, 2, 3, 4, 5));
        assertThat(integerList.toString()).isEqualTo("[1, 2, 3, 4, 5]");

        stringList.addAll(List.of("one", "two", "three"));
        assertThat(stringList.toString()).isEqualTo("[one, two, three]");
    }

    @Test
    public void testGet() {
        integerList.addAll(List.of(2, 3, 4, 5));
        assertThat(integerList.get(0)).isEqualTo(2);
        assertThat(integerList.get(2)).isEqualTo(4);

        stringList.addAll(List.of("one", "two", "three"));
        assertThat(stringList.get(0)).isEqualTo("one");
        assertThat(stringList.get(2)).isEqualTo("three");
    }

    @Test
    public void testRemove() {
        integerList.addAll(List.of(2, 3, 4, 5));
        integerList.remove(0);
        assertThat(integerList.get(0)).isEqualTo(3);
        assertThat(integerList.size()).isEqualTo(3);
        integerList.remove((Integer) 3);
        assertThat(integerList.get(0)).isEqualTo(4);
        assertThat(integerList.size()).isEqualTo(2);

        stringList.addAll(List.of("one", "two", "three"));
        stringList.remove("one");
        assertThat(stringList.get(0)).isEqualTo("two");
    }

    @Test
    public void testSort() {
        sortedInts = new MyArrayList<>();
        sortedStrings = new MyArrayList<>();

        integerList.addAll(List.of(1, 77, 53, 2, 3, 4, 10, 48, 0));
        stringList.addAll(List.of("xyz", "abc", "abb", "aston", "hello", "якут", "карась"));

        sortedInts.addAll(List.of(0, 1, 2, 3, 4, 10, 48, 53, 77));
        sortedStrings.addAll(List.of("abb", "abc", "aston", "hello", "xyz", "карась", "якут"));

        integerList.sort();
        stringList.sort();

        assertThat(integerList.toString()).isEqualTo(sortedInts.toString());
        assertThat(stringList.toString()).isEqualTo(sortedStrings.toString());
    }

    @Test
    public void testStaticSort() {
        var ints = new ArrayList<Integer>();
        ints.add(5);
        ints.add(1);
        ints.add(4);
        ints.add(0);
        ints.add(2);
        ints.add(3);
        MyArrayList.sort(ints);
        assertThat(ints).isEqualTo(List.of(0, 1, 2, 3, 4, 5));

        var chars = new ArrayList<Character>();
        chars.add('z');
        chars.add('a');
        chars.add('m');
        MyArrayList.sort(chars);
        assertThat(chars).isEqualTo(List.of('a', 'm', 'z'));
    }
}
