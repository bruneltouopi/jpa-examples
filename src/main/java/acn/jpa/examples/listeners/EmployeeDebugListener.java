package acn.jpa.examples.listeners;

import acn.jpa.examples.domains.Employee;

import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 * Created by fabrice on 8/29/18.
 */
public class EmployeeDebugListener {

    @PrePersist
    public void prePersist(Employee emp) {
        System.out.println(this.getClass().getCanonicalName()+" Persist called on: " + emp);
    }

    @PreUpdate
    public void preUpdate(Employee emp) {
        System.out.println(this.getClass().getCanonicalName()+" PreUpdate called on: " + emp);
    }

    @PreRemove
    public void preRemove(Employee emp) {
        System.out.println(this.getClass().getCanonicalName()+" PreRemove called on: " + emp);

    }

    @PostLoad
    public void postLoad(Employee emp) {
        System.out.println(this.getClass().getCanonicalName()+" PostLoad called on: " + emp);

    }
}
