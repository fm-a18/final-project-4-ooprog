package utils.generators;

import java.util.Random;

public class TransactionNumGenerator {

    public static String transactionNumGenerator(){
        int n = 6;
        StringBuilder transactionNum = new StringBuilder();
        String randomNum = "0123456789";
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            transactionNum.append(randomNum.charAt(rand.nextInt(randomNum.length())));
        }
        return "TN" + transactionNum.toString();
    }
}
