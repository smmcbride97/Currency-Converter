import java.io.IOException;
import java.net.URL;
import java.util.Map;

import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Converter {
    private static final String url = "https://v6.exchangerate-api.com/v6/66c87903a96d51d6d67a9ee6/latest/USD";
    private Map<String, Double> rates;
    public static Converter instance = new Converter();

    private Converter() {
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            JsonNode json = mapper.readTree(new URL(url)).get("conversion_rates");
            rates = mapper.convertValue(json, Map.class);
            rates.replace("USD", 1.0);
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }

    public static double convert(String from, String to, double amount) {
        return 1 / instance.rates.get(from) * instance.rates.get(to) * amount;
    }

    public static void main(String[] args) {
        for (Map.Entry<String,Double> entry : Converter.instance.rates.entrySet()) 
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

        String from, to; Double amount;
        from = "USD"; to = "JPY"; amount = 100.0;
        System.out.printf("Convert %.2f %s to %s, you get: %.2f\n", amount, from, to, convert(from, to, amount));
        from = "USD"; to = "GBP"; amount = 100.0;
        System.out.printf("Convert %.2f %s to %s, you get: %.2f\n", amount, from, to, convert(from, to, amount));
        from = "GBP"; to = "JPY"; amount = 100.0;
        System.out.printf("Convert %.2f %s to %s, you get: %.2f\n", amount, from, to, convert(from, to, amount));

        Scanner sc = new Scanner(System.in);
        System.out.println("\nEnter currency from: ");
        from = sc.nextLine();
        System.out.println("Enter currency to: ");
        to = sc.nextLine();
        System.out.println("Enter currency amount: ");
        amount = sc.nextDouble();
        System.out.printf("Convert %.2f %s to %s, you get: %.2f\n", amount, from, to, convert(from, to, amount));
    }
}
