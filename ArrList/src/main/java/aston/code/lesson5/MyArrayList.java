package aston.code.lesson5;

import java.util.Arrays;
import java.util.Collection;

/*
Необходимо написать свою реализацию коллекции на выбор LinkedList или ArrayList(можно оба варианта).
Должны быть основные методы add, get, remove, addAll(ДругаяКоллекция параметр), остальное на ваше усмотрение
Плюс написать реализацию сортировки пузырьком с флагом, который прекращает сортировку, если коллекция уже отсортирована.
Задание с *: На тему дженериков.
Для этих коллекций сделать конструктор который будет принимать другую коллекцию в качестве параметров и
инициализироваться с элементами из этой коллекции. Вторая часть - сделать метод сортировки статическим,
этот метод также будет принимать какую-то коллекцию и сортировать ее. (Аналогия Collections.sort()).
 */

public class MyArrayList<T extends Comparable<T>> {

    private T[] ARRAY;
    private int size = 0;
    private boolean sorted;
    private final int INIT_CAPACITY = 1;

    public MyArrayList() {
        this.ARRAY = (T[]) new Comparable[INIT_CAPACITY];
    }

    public MyArrayList(Collection<T> collection) {
        this.ARRAY = (T[]) new Comparable[collection.toArray().length];
        this.addAll(collection);
    }


    public void add(T el) {
        ARRAY = size + 1 > ARRAY.length ? Arrays.copyOf(ARRAY, size + 1) : ARRAY;
        ARRAY[size] = el;
        size++;
        sorted = false;
    }

    public int size() {
        return size;
    }

    public T get(int index) {
        return ARRAY[index];
    }

    public T[] remove(int index) {
        ARRAY[index] = null;
        var newArray = (T[]) new Comparable[size - 1];
        var newIndex = 0;
        for (int i = 0; i < size; i++) {
            if (i != index) {
                newArray[newIndex] = ARRAY[i];
                newIndex++;
            }
        }
        ARRAY = newArray;
        size = ARRAY.length;
        sorted = false;
        return ARRAY;
    }

    public Integer indexOf(T obj) {
        for (int i = 0; i < size; i++) {
            if (ARRAY[i].equals(obj)) {
                return i;
            }
        }
        return null;
    }

    public T[] remove(T obj) {
        var index = indexOf(obj);
        return index == null ? ARRAY : remove(index);
    }

    public T[] addAll(Collection<T> collection) {
        var arr = collection.toArray();
        var tempArr = Arrays.copyOf(ARRAY, arr.length + size);
        System.arraycopy(arr, 0, tempArr, size, arr.length);
        ARRAY = tempArr;
        size = ARRAY.length;
        sorted = false;
        return ARRAY;
    }

    public boolean sort() {
        if (sorted) {
            return true;
        }

        while (!sorted) {
            sorted = true;
            for (int i = 0; i < size - 1; i++) {
                if (ARRAY[i].compareTo(ARRAY[i + 1]) > 0) {
                    var tmp = ARRAY[i + 1];
                    ARRAY[i + 1] = ARRAY[i];
                    ARRAY[i] = tmp;
                    sorted = false;
                }
            }
        }
        return true;
    }

    public static <A extends Comparable<A>> void sort(Collection<A> collection) {
        var arr = new MyArrayList<>(collection);
        arr.sort();
        collection.clear();
        collection.addAll(Arrays.asList(arr.ARRAY));
    }

    @Override
    public String toString() {
        return Arrays.toString(ARRAY);
    }

}
