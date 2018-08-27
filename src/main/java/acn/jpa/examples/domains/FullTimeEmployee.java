package acn.jpa.examples.domains;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Created by fabrice on 8/21/18.
 */
@Entity
@DiscriminatorValue("F")
public class FullTimeEmployee extends Employee{
    private double salary;

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
