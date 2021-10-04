class Author {
    String name;
    String email;
    char gender;
    public Author(String name, String email, char gender){
        this.name = name;
        this.email = email;
        this.gender = gender;
    }

    public String toString(){
        return String.format("Author[name = %s, email = %s, gender = %c",name, email, gender);
    }
}
