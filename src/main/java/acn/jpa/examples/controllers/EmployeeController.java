package acn.jpa.examples.controllers;

import acn.jpa.examples.domains.Employee;
import acn.jpa.examples.utils.JPAUtility;

import javax.persistence.EntityManager;

/**
 * Created by fabrice on 8/26/18.
 */
public class EmployeeController {


    public Employee insertEmployee(){
        EntityManager em=JPAUtility.getEntityManager();

        em.getTransaction().begin();
        Employee e=new Employee();
        e.setId(new Long(12));
        e.setName("Corneille");
        em.persist(e);
        em.getTransaction().commit();
        return e;
    }
}
