package domain;

public class City {
    private Long zipCode;
    private String name;

    public City() {
    }

    public City(Long zipCode, String name) {
        this.zipCode = zipCode;
        this.name = name;
    }

    public Long getZipCode() {
        return zipCode;
    }

    public void setZipCode(Long zipCode) {
        this.zipCode = zipCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "City[" + "zipCode=" + zipCode + ", name=" + name + ']';
    }
}
