package acn.jpa.examples.domains;

import javax.persistence.*;

/**
 * Created by fabrice on 8/21/18.
 */
@Entity
@DiscriminatorValue("P")
public class PartTimeEmployee extends Employee{
    private int rate;

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
