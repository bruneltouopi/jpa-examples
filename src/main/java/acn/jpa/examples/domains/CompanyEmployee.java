package acn.jpa.examples.domains;

import acn.jpa.examples.listeners.EmployeeAudit;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Created by fabrice on 8/29/18.
 */
@MappedSuperclass
@EntityListeners(EmployeeAudit.class)
public abstract class CompanyEmployee extends Employee {
    protected int vacation;
// ...

    @PrePersist
    @PreUpdate
    public void verifyVacation() {
        System.out.println(this.getClass().getCanonicalName()+ "@PrePersist @PreUpdate");

    }
}
