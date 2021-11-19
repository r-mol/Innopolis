public class Animal {
    private String name;
    private String OwnName;
    private int age;

    Animal (String name, String OwnName, int age) {
        this.name = name;
        this.OwnName = OwnName;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnName() {
        return OwnName;
    }

    public void setOwnName(String OwnName) {
        this.OwnName = OwnName;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", OwnName='" + OwnName + '\'' +
                ", age=" + age +
                '}';
    }
}
