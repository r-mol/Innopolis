public class Professor extends UniversityMember{

    public Professor(String name, int age,Sex sex){
        super(name, age, sex);
    }
    public Professor(){
    }

    public String toString(){
        return "I'm a professor of Innopolis university and my name is " + getName() + ", I'm " + getAge() + " years old and I'm a " + getSex();
    }
}
