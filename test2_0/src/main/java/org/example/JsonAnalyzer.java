package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class JsonAnalyzer {
    private Map<String, Integer> duplicatesJson = new HashMap<>();
    private long maxWeightJson = Long.MIN_VALUE;
    private long minWeightJson = Long.MAX_VALUE;
    private final Map<String, BigDecimal> totalWeightPerGroupJson = new HashMap<>();

    public void analyzeJsonFile(String filePath) throws IOException {
        clearDataJson();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            processJson(jsonContent.toString());
        }
    }

    private void processJson(String json) {
        int index = 0;
        while (index < json.length()) {
            char currentChar = json.charAt(index);
            if (currentChar == '{') {
                int endIndex = findEndIndex(json, index);
                String jsonItem = json.substring(index, endIndex + 1);
                processJsonObject(jsonItem);
                index = endIndex + 1;
            } else {
                index++;
            }
        }
    }

    private void processJsonObject(String json) {
        if (json.contains("\"weight\"")) {
            int weightStartIndex = json.indexOf("\"weight\"");
            int colonIndex = json.indexOf(':', weightStartIndex);
            int valueStartIndex = -1;
            for (int i = colonIndex + 1; i < json.length(); i++) {
                if (Character.isDigit(json.charAt(i)) || json.charAt(i) == '-') {
                    valueStartIndex = i;
                    break;
                }
            }
            if (valueStartIndex != -1) {
                int valueEndIndex = valueStartIndex + 1;
                while (valueEndIndex < json.length() &&
                        (Character.isDigit(json.charAt(valueEndIndex)) || json.charAt(valueEndIndex) == '.')) {
                    valueEndIndex++;
                }
                String weightStr = json.substring(valueStartIndex, valueEndIndex);
                try {
                    BigDecimal weight = new BigDecimal(weightStr);
                    if (weight.compareTo(new BigDecimal(minWeightJson)) < 0) {
                        minWeightJson = weight.longValue();
                    }
                    if (weight.compareTo(new BigDecimal(maxWeightJson)) > 0) {
                        maxWeightJson = weight.longValue();
                    }
                    // Расчет общего веса в группах
                    int groupStartIndex = json.indexOf("\"group\"");
                    int groupColonIndex = json.indexOf(':', groupStartIndex);
                    int groupValueStartIndex = -1;
                    for (int i = groupColonIndex + 1; i < json.length(); i++) {
                        if (json.charAt(i) == '"') {
                            groupValueStartIndex = i + 1;
                            break;
                        }
                    }
                    if (groupValueStartIndex != -1) {
                        int groupValueEndIndex = json.indexOf('"', groupValueStartIndex);
                        String group = json.substring(groupValueStartIndex, groupValueEndIndex);
                        BigDecimal totalGroupWeight = totalWeightPerGroupJson.getOrDefault(group, BigDecimal.ZERO);
                        totalWeightPerGroupJson.put(group, totalGroupWeight.add(weight));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка при анализе веса: " + e.getMessage());
                }
            }
        }
        int commaCount = 0;
        int index = 0;
        while (index < json.length()) {
            char currentChar = json.charAt(index);
            if (currentChar == ',') {
                commaCount++;
                if (commaCount == 2) {
                    json = json.substring(0, index + 1); // Обрезаем до второй запятой включительно
                    break;
                }
            }
            index++;
        }
        String key = json;
        int count = duplicatesJson.getOrDefault(key, 0) + 1;
        duplicatesJson.put(key, count);
    }

    private int findEndIndex(String json, int startIndex) {
        int count = 1;
        int index = startIndex + 1;
        while (count > 0 && index < json.length()) {
            char currentChar = json.charAt(index);
            if (currentChar == '{') {
                count++;
            } else if (currentChar == '}') {
                count--;
            }
            index++;
        }
        return index - 1;
    }
    public Map<String, Integer> getDuplicatesJson() {
        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : duplicatesJson.entrySet()) {
            if (entry.getValue() >= 2) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
    public Map<String, BigDecimal> getTotalWeightPerGroupJson() {
        return totalWeightPerGroupJson;
    }
    public long getMaxWeightJson() {
        return maxWeightJson;
    }

    public long getMinWeightJson() {
        return minWeightJson;
    }
    public void clearDataJson() {
        duplicatesJson.clear();
        maxWeightJson = Long.MIN_VALUE;
        minWeightJson = Long.MAX_VALUE;
        totalWeightPerGroupJson.clear();
    }
}
