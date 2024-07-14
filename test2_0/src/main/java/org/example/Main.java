package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            CsvAnalyzer csvAnalyzer = new CsvAnalyzer();
            JsonAnalyzer jsonAnalyzer = new JsonAnalyzer();

            System.out.println("Введите путь до файла (.csv или .json) или 'exit' для завершения:");
            String input = scanner.nextLine();

            while (!input.equalsIgnoreCase("exit")) {
                try {
                    if (input.endsWith(".csv")) {
                        System.out.println("Обработка (CSV):");
                        csvAnalyzer.analyzeCsvFile(input);
                        // Вывод статистики для CSV
                        System.out.println("Дубликаты объектов (CSV):");
                        csvAnalyzer.getDuplicatesCsv().forEach((key, value) ->
                                System.out.println(key + " : " + value)
                        );
                        System.out.println();
                        System.out.println("Максимальный вес (CSV): " + csvAnalyzer.getMaxWeightCsv());
                        System.out.println("Минимальный вес (CSV): " + csvAnalyzer.getMinWeightCsv());
                        System.out.println();
                        System.out.println("Общий вес в группах (CSV):");
                        System.out.println();
                        csvAnalyzer.getTotalWeightPerGroupCsv().forEach((group, totalWeight) ->
                                System.out.println(group + " : " + totalWeight)
                        );
                    } else if (input.endsWith(".json")) {
                        System.out.println("Обработка (JSON):");
                        jsonAnalyzer.analyzeJsonFile(input);
                        // Вывод статистики для JSON
                        System.out.println("Дубликаты объектов (JSON):");
                        jsonAnalyzer.getDuplicatesJson().forEach((key, value) ->
                                System.out.println(key + " : " + value)
                        );
                        System.out.println();
                        System.out.println("Максимальный вес (JSON): " + jsonAnalyzer.getMaxWeightJson());
                        System.out.println("Минимальный вес (JSON): " + jsonAnalyzer.getMinWeightJson());
                        System.out.println();
                        System.out.println("Общий вес в группах (JSON):");
                        System.out.println();
                        jsonAnalyzer.getTotalWeightPerGroupJson().forEach((group, totalWeight) ->
                                System.out.println(group + " : " + totalWeight)
                        );
                    } else {
                        System.out.println("Неподдерживаемый формат файла. Пожалуйста, введите путь до файла с расширением .csv или .json.");
                    }
                } catch (Exception e) {
                    System.err.println("Ошибка при анализе файла: " + e.getMessage());
                }

                System.out.println("Введите путь до файла (.csv или .json) или 'exit' для завершения:");
                input = scanner.nextLine();
            }
        }
    }
}




