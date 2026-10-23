package inventory;

public class Bird extends Pet {
    public static final String TYPE = "Bird";

    public Bird(String petID, String name, String breed, char gender, int age, double price) {
        super(petID, name, breed, gender, age, price);
    }

    @Override
    public String getType() {
        return TYPE;
    }

}
