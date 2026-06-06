package taxi;

public abstract class Person implements Validatable {
    protected String id;
    protected String name;
    protected String phone;
    public Person(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
    public abstract String getRole();
    @Override
    public boolean validate() {
        return name != null && !name.trim().isEmpty() 
            && phone != null && !phone.trim().isEmpty();
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
}
