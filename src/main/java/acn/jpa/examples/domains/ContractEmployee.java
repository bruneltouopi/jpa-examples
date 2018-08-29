package acn.jpa.examples.domains;

import acn.jpa.examples.listeners.LongNameValidator;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ExcludeSuperclassListeners;
import javax.persistence.PrePersist;

/**
 * Created by fabrice on 8/29/18.
 */
@Entity
@ExcludeSuperclassListeners
@EntityListeners(LongNameValidator.class)
public class ContractEmployee extends Employee {
    private int dailyRate;
    private int term;

    @PrePersist
    public void verifyTerm() {
        System.out.println(ContractEmployee.class.getCanonicalName()+" PrePersist");
    }
// ...
}