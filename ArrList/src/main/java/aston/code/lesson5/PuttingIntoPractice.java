package aston.code.lesson5;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PuttingIntoPractice {

    public static void main(String... args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );


//      1. Найти все транзакции за 2011 год и отсортировать их по сумме (от меньшейк большей).
        var list1 = transactions.stream()
                .filter(t -> t.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .toList();
        System.out.println("Все транзакции на 2011 год (отсортированные): " + list1);

//      2. Вывести список неповторяющихся городов, в которых работают трейдеры.
        transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getCity)
                .distinct()
                .forEach(System.out::println);

//      3. Найти всех трейдеров из Кембриджа и отсортировать их по именам.
        var list2 = transactions.stream()
                .map(Transaction::getTrader)
                .filter(t -> t.getCity().equals("Cambridge"))
                .map(Trader::getName)
                .distinct()
                .sorted(Comparator.comparing(t -> t))
                .toList();
        System.out.println("Все трейдеры из Кембриджа (отсортированные): " + list2);

//      4. Вернуть строку со всеми именами трейдеров, отсортированными в алфавитном порядке.
        var string = transactions.stream()
                .map(Transaction::getTrader)
                .distinct()
                .map(Trader::getName)
                .sorted()
                .collect(Collectors.joining(", "));
        System.out.println("Имена всех трейдеров (отсортированные): " + string);

//      5. Выяснить, существует ли хоть один трейдер из Милана.
        var result = transactions.stream()
                .map(Transaction::getTrader)
                .anyMatch(t -> t.getCity().equals("Milan"));
        System.out.println("Существует ли трейдер из милана? Answer is " + result);

//      6. Вывести суммы всех транзакций трейдеров из Кембриджа.
        var sum = transactions.stream()
                .filter(t -> t.getTrader().getCity().equals("Cambridge"))
                .collect(Collectors.groupingBy(t -> t.getTrader().getName(),
                        Collectors.summingInt(Transaction::getValue))
                );
        System.out.println("Сумма транзакций для каждого трейдера из Кембриджа: " + sum);

//      7. Какова максимальная сумма среди всех транзакций?
        var maxSum = transactions.stream()
                .map(Transaction::getValue)
                .max(Comparator.comparing(v -> v))
                .orElseThrow();
        System.out.println("Максимальная сумма среди всех транзакций = " + maxSum);

//      8. Найти транзакцию с минимальной суммой.
        var minSum = transactions.stream()
                .min(Comparator.comparing(Transaction::getValue))
                .orElseThrow();
        System.out.println("Транзакция с минимальной суммой = " + minSum);
    }
}