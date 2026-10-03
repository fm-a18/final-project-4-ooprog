package inventory;

public class Dog extends Pet {
    public static final String TYPE = "Dog";
    public static final String ID_PREFIX = "DO";

    public Dog(String petID, String name, String breed, char gender, int age, double price) {
        super(petID, name, breed, gender, age, price);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getPetIDPrefix() {
        return ID_PREFIX;
    }
}
