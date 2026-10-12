package utils.generators;

public class UserIdGenerator {
    private static int ctr= 0;

    public static String userIdGenerator() {
        ctr++;
        return "UD" + String.format("%04d", ctr);
    }

}
