abstract class AbstractContainer<T> implements InterfaceContainer{
    T content;

    @Override
    public void show() {
        System.out.println(content.toString());
    }
}

class Container <T> extends AbstractContainer<T>{
    Container(T content) {
        this.content = content;
    }
    Container(){
    }

    public String toString(){
        return "Container";
    }
}