package acn.jpa.examples.domains;

import javax.persistence.*;

/**
 * Created by fabrice on 8/21/18.
 */
@Entity
@DiscriminatorValue("P")
@EntityListeners({})
public class PartTimeEmployee extends CompanyEmployee{
    private int rate;

    @PrePersist
    @PreUpdate
    public void verifyVacation() {
        System.out.println(this.getClass().getCanonicalName()+" PrePersist PreUpdate");
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
