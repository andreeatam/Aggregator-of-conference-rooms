package model;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "candybars")
public class CandyBar extends Product{
    @ManyToMany(cascade = CascadeType.ALL) // when inserting a prof, insert also the courses
    @JoinTable(name = "candyBars_Sweets", joinColumns = @JoinColumn(name = "idCandyBar"), inverseJoinColumns = @JoinColumn(name = "idSweets"))
    List<Sweet> sweets;

    public CandyBar(String name, String description, List<Sweet> sweets) {
        super(name, description);
        this.sweets = sweets;
    }

    public CandyBar() {

    }

    public List<Sweet> getSweets() {
        return sweets;
    }

    public void setSweets(List<Sweet> sweets) {
        this.sweets =sweets;
    }


}

