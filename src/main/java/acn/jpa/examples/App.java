package acn.jpa.examples;

import acn.jpa.examples.controllers.CustomerJpaController;
import acn.jpa.examples.controllers.EmployeeController;
import acn.jpa.examples.domains.Customer;
import acn.jpa.examples.domains.Department;
import acn.jpa.examples.domains.Employee;
import acn.jpa.examples.domains.FullTimeEmployee;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by fabrice on 8/21/18.
 */
public class App {
    static CustomerJpaController customerJpaController= new CustomerJpaController();
    static EmployeeController employeeController =new EmployeeController();

    public static void main(String[] args) {
        App app=new App();

        EntityManager em= customerJpaController.getEntityManager();
        try {
            em.getTransaction().begin();

            Department department=new Department("Info");

            Customer customer1=new Customer(31,"Fabrice","Touopi");
            Customer customer2=new Customer(40,"Eric","Nde");
            Customer customer3=new Customer(29,"Leopold Singor","Touopi");

            customer1.setDepartment(department);

            department.getCustomers().add(customer1);


            em.persist(customer1);

            em.persist(department);
            em.persist(customer2);
            em.persist(customer3);

            Employee employee =new Employee();
           // employee.setId(Long.valueOf(10));
            employee.setName("citadel");
            em.persist(employee);

            Employee employee1=new FullTimeEmployee();
                    employee1.setName("Fabrice");
                    
            em.getTransaction().commit();

            List<Customer> listCustomers=customerJpaController.findCustomerEntities();
            listCustomers.stream().forEach(System.out::println);
            customerJpaController.getFullNames();
            System.out.println("Customer greater than 30 with Predicates");
            customerJpaController.getListCustomerWithPredicates(30);
        } catch (Exception ex) {
            Logger.getLogger(CustomerJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
