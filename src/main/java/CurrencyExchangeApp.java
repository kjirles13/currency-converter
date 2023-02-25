import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class CurrencyExchangeApp {

    public static void main(String[] args) {
        String spacer = "============================";

        Scanner userInput = new Scanner(System.in);

        System.out.println(spacer);
        System.out.println("Currency Exchange Calculator");
        System.out.println(spacer + "\n");
        System.out.println("Welcome to the currency exchange calculator!\n");
        System.out.print("Please enter an amount in USD without any dollar sign symbols: ");

        //Get initial currency value
        boolean validInput = false;
        BigDecimal initialAmount = new BigDecimal("0");

        while (!validInput) {
            try {
                initialAmount = new BigDecimal(userInput.nextLine());
                validInput = true;
            } catch (NumberFormatException nfe) {
                System.out.println("\nWhoops, something went wrong. Either that's not a number or it's not in the correct format.");
                System.out.print("Remember no dollar sign, and use this format (10.50). Try again: ");
            }
        }

        System.out.println("\nCalculating...");

        // Access API and get currency rates
        Map<String, Double> currencyRates = API.accessAPI();

        System.out.println("\nWhat type of currency are you converting this to?");
        System.out.println("\nOnly enter that currency's three letter identification symbol.");
        System.out.print("If you need a list of acceptable currency types and their identification symbol, please enter 'L': ");

        String conversionCurrencyType = "";

        // Get currency type to convert
        do {
            conversionCurrencyType = userInput.nextLine();

            if (conversionCurrencyType.equalsIgnoreCase("L")) {
                printCurrencyList(currencyRates);
                System.out.println(spacer);
                System.out.print("\nWhat type of currency are you converting this to?: ");
            } else if (!currencyRates.containsKey(conversionCurrencyType.toUpperCase(Locale.ROOT))) {
                System.out.println("\nPlease only enter that currency's three letter identification symbol.");
                System.out.println("If you need a list of acceptable currency types and their identification symbol, please enter 'L'\n");
                System.out.print("What are type of currency are you converting this to?: ");
            }
        } while (!currencyRates.containsKey(conversionCurrencyType.toUpperCase(Locale.ROOT)));

        // Call method to convert currency
        BigDecimal finalAmount = calculateCurrency(currencyRates, initialAmount, (conversionCurrencyType.toUpperCase(Locale.ROOT)));

        System.out.println("\n" + spacer);
        System.out.println(String.format("$%.2f in %s is %.2f", initialAmount, conversionCurrencyType.toUpperCase(Locale.ROOT), finalAmount));
        System.out.println(spacer);

        userInput.close();
    }

    public static void printCurrencyList(Map<String, Double> currencyRates) {
        System.out.println("\nList of acceptable currency inputs:\n");

        for (Map.Entry<String, Double> rate : currencyRates.entrySet()) {
            System.out.println(String.format("%s : %s", rate.getKey(), rate.getValue()));
        }
    }

    public static BigDecimal calculateCurrency(Map<String, Double> currencyRates, BigDecimal amount, String currencySymbol) {
        BigDecimal conversionRate = BigDecimal.valueOf(currencyRates.get(currencySymbol));
        BigDecimal finalAmount = amount.multiply(conversionRate);
        return finalAmount;
    }
}