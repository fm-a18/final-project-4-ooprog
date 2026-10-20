package utils.generators;

import utils.FileHandler;
import utils.RecordManager;

public class UserIdGenerator {
    private static int ctr = 0;
    private static boolean isSeeded = false;

    public static String userIdGenerator() {
        if (!isSeeded) {
            ctr = FileHandler.highestNumber(RecordManager.FILE_USER_LIST, RecordManager.US_USER_ID, "UD");
            isSeeded = true;
        }
        ctr++;
        return "UD" + String.format("%04d", ctr);
    }
}