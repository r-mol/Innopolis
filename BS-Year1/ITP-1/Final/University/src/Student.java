public class Student extends UniversityMember{

    public Student(String name, int age,Sex sex){
        super(name, age, sex);
    }

    public Student() {
    }

    public String toString(){
        return "I'm a student of Innopolis university and my name is " + getName() + ", I'm " + getAge() + " years old and I'm a " + getSex();
    }
}
