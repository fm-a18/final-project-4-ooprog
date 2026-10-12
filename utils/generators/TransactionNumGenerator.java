package utils.generators;

public class TransactionNumGenerator {
    private static int transactionCtr = 0;

    public static String transactionNumGenerator() {
        transactionCtr++;
        return "T" + String.format("%07d", transactionCtr);
    }
}
