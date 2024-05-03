
import java.io.*;
import java.util.*;

// Київ сьогодні зачаровує людей красою золотоверхих храмів, архітектурою будинків і мостів. Але багато пам'яток старовни не збереглося до наших днів. Повноводні річки Либідь, Почайна, Дарниця доповнюють величну красу старовинного міста. Найкрасивіша вулиця Києва - Хрещатик. Широкий, просторий, із зеленими алеями каштанів. Хіюа можна повірити, що там, де простягнувся Хрещатик, у давнину був глибокий яр? Посередині його протікав струмок і навколо шумів шлухий ліс. Яр цей називався Хрещатим. Від його назви пішла ціла назва центральної вулиці Києва.
//F:\kyiv.txt
public class SymbolAnalyzer {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the text or the path to the text document:");
        String input = scanner.nextLine();
        String text;
        if (isPathToFile(input)) {
            // If the input is a path to a file, read its contents
            try {
                text = readTextFromFile(input);
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
                return;
            }
        } else {
            // If the input is not a file path, assume it's text input
            text = input;
        }
//

        Map<Character, Integer> symbolCount = countSymbols(text);
        Map<Character, Double> pValues = calculatePValues(symbolCount, text.length());



        // Sort symbols by decreasing P value
        List<Map.Entry<Character, Double>> sortedSymbols = new ArrayList<>(pValues.entrySet());
        sortedSymbols.sort((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        // Output data in tabular form
        System.out.println("Symbol\tCount\tP Value");
        for (Map.Entry<Character, Double> entry : sortedSymbols) {
            char symbol = entry.getKey();
            if (symbol != ' ') { // Skip counting spaces
                int count = symbolCount.getOrDefault(symbol, 0);
                double pValue = entry.getValue();
                System.out.printf("%c\t%d\t%.4f\n", symbol, count, pValue);
            }
        }

        // Search for extremes (Pn min) and (Pn max)

        double minPValue = Double.MAX_VALUE;
        double maxPValue = Double.MIN_VALUE;
        char minSymbol = ' ';
        char maxSymbol = ' ';
        for (Map.Entry<Character, Double> entry : pValues.entrySet()) {
            char symbol = entry.getKey();
            double pValue = entry.getValue();
            if (symbol != ' ' && pValue < minPValue) {
                minPValue = pValue;
                minSymbol = symbol;
            }
            if (symbol != ' ' && pValue > maxPValue) {
                maxPValue = pValue;
                maxSymbol = symbol;
            }
        }
        System.out.println("Min Pn: " + minPValue + " (Symbol: " + minSymbol + ")");
        System.out.println("Max Pn: " + maxPValue + " (Symbol: " + maxSymbol + ")");

        // Output data in graphic form
        System.out.println("Graph form:");
        for (Map.Entry<Character, Double> entry : sortedSymbols) {
            char symbol = entry.getKey();
            if (symbol != ' ') { // Skip counting spaces
                int count = symbolCount.getOrDefault(symbol, 0);
                double pValue = entry.getValue();
                System.out.print(symbol + " ");
                for (int i = 0; i < count; i++ ){
                    System.out.print("*");
                }
                System.out.println(" ");
            }
        }
    }



    // Function to check if the input is a path to a file
    private static boolean isPathToFile(String input) {

        return new File(input).isFile();
    }

    // Function to read text from a file
    private static String readTextFromFile(String filePath) throws IOException {
        StringBuilder text = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }
        }
        return text.toString();
    }

    // Function to count the number of repetitions of each symbol
    private static Map<Character, Integer> countSymbols(String text) {
        Map<Character, Integer> symbolCount = new HashMap<>();
        for (char symbol : text.toCharArray()) {
            if (symbol != ' ') { // Exclude spaces
                symbolCount.put(symbol, symbolCount.getOrDefault(symbol, 0) + 1);
            }
        }
        return symbolCount;
    }

    // Function to calculate Pn for each symbol
    private static Map<Character, Double> calculatePValues(Map<Character, Integer> symbolCount, int totalSymbols) {
        Map<Character, Double> pValues = new HashMap<>();
        for (Map.Entry<Character, Integer> entry : symbolCount.entrySet()) {
            char symbol = entry.getKey();
            int count = entry.getValue();
            double pValue = (double) count / totalSymbols;
            pValues.put(symbol, pValue);
        }
        return pValues;
    }

    // Function to get the symbol with a specific P value
    private static char getSymbolWithPValue(Map<Character, Integer> symbolCount, Map<Character, Double> pValues, double pValue) {
        for (Map.Entry<Character, Double> entry : pValues.entrySet()) {
            if (entry.getValue() == pValue) {
                return entry.getKey();
            }
        }
        return ' ';
    }

}
