package inventory;

public abstract class Pet {
    private String name;
    private String type;
    private String breed;
    private char gender;
    private int age;
    private String adoptionStatus;

    public Pet(String name, String type, String breed, char gender, int age, String adoptionStatus) {
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.gender = gender;
        this.age = age;
        this.adoptionStatus = adoptionStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAdoptionStatus() {
        return adoptionStatus;
    }

    public void setAdoptionStatus(String adoptionStatus) {
        this.adoptionStatus = adoptionStatus;
    }
}
