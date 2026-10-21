package utils.generators;

import utils.FileHandler;
import utils.RecordManager;
public class TransactionNumGenerator {
    private static int ctr = 0;
    private static boolean isSeeded = false;

    private static final String[] TRANSACTION_FILES = {
            RecordManager.FILE_RESERVED, RecordManager.FILE_ADOPTED,
            RecordManager.FILE_CANCELLATION_REQUEST, RecordManager.FILE_CANCELLED_LIST,
            RecordManager.FILE_RETURN_REQUEST, RecordManager.FILE_RETURNED_LIST
    };

    public static String transactionNumGenerator() {
        if (!isSeeded) {
            for (String file : TRANSACTION_FILES) {
                ctr = Math.max(ctr, FileHandler.highestNumber(file, RecordManager.RES_TRANSACTION_NUM, "TN"));
            }
            isSeeded = true;
        }
        ctr++;
        return "TN" + String.format("%06d", ctr);
    }
}