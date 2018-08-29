package acn.jpa.examples.listeners;

import acn.jpa.examples.domains.CompanyEmployee;
import acn.jpa.examples.domains.Employee;

import javax.persistence.PostPersist;

/**
 * Created by fabrice on 8/29/18.
 */
public class EmployeeAudit {
    @PostPersist
    public void auditNewHire(CompanyEmployee emp) {
        System.out.println("Listener "+ this.getClass().getName()+ "@PostPersist emp = [" + emp + "]");

    }
}