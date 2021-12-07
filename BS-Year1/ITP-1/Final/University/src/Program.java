import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class Program {
    private static Collection<UniversityMember> members = new LinkedList<>();

    public static void main(String[] args){
        initializeMembers();
        displayMembers(members);

        Container<UniversityMember>[] members = (Container<UniversityMember>[]) Array.newInstance(Container.class,2);
        UniversityMember student = new Student("Roman Molochkov",18,Sex.MALE);
        UniversityMember professor  = new Professor("Artem Burmaykov",37,Sex.MALE);
        Container<UniversityMember> container1 = new Container<>(student);
        Container<UniversityMember> container2 = new Container<>(professor);
        members[0] = container1;
        members[1] = container2;
        try {
            for(int i = 0;i< members.length+1;i++){
                if(i> members.length-1){
                    throw new MyException("Index is out of range");
                }
                System.out.println(members[i].toString());
            }
            System.out.println(members[3].toString());
        } catch (MyException e){
            System.out.println(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    static class MyException extends Exception{
        public MyException(String message){
            super(message);
        }
    }

    public static void initializeMembers(){
        members.add(new Student("Roman",18, Sex.MALE));
        members.add(new Professor("Artem",37,Sex.MALE));
    }

    public static void displayMembers(Collection<UniversityMember> members){
        members.forEach(System.out::println);
    }
}
