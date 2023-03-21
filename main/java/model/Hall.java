package model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "halls")
public class Hall extends Product{
    private String location;
    private Integer capacity;

    public Hall(String name, String description, String location, Integer capacity) {
        super(name, description);
        this.location=location;
        this.capacity=capacity;
    }

    public Hall() {

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
}

//    @Override
//    public String toString() {
//        return "Id: " + getId() +"\n"+
//                "Name: " + getName() +"\n"+
//                "Rating: " + getRating() +"\n"+
//                "Location: " + location + "\n"+
//                "Capacity: " + capacity + "\n"+
//                "Description: " + getDescription() + "\n";

   // }

}

