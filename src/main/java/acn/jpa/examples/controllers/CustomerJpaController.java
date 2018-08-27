/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acn.jpa.examples.controllers;

import acn.jpa.examples.domains.Department;
import acn.jpa.examples.domains.Employee;
import acn.jpa.examples.exceptions.NonexistentEntityException;
import acn.jpa.examples.exceptions.PreexistingEntityException;
import acn.jpa.examples.utils.JPAUtility;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author f.touopi.touopi
 */
public class CustomerJpaController implements Serializable {

 
    public EntityManager getEntityManager() {
        return JPAUtility.getEntityManager();
    }

    public void create(Employee employee) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(employee);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCustomer(employee.getId()) != null) {
                throw new PreexistingEntityException("Employee " + employee + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Employee employee) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            employee = em.merge(employee);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = employee.getId();
                if (findCustomer(id) == null) {
                    throw new NonexistentEntityException("The employee with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee employee;
            try {
                employee = em.getReference(Employee.class, id);
                employee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The employee with id " + id + " no longer exists.", enfe);
            }
            em.remove(employee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Employee> findCustomerEntities() {
        return findCustomerEntities(true, -1, -1);
    }

    public List<Employee> findCustomerEntities(int maxResults, int firstResult) {
        return findCustomerEntities(false, maxResults, firstResult);
    }

    private List<Employee> findCustomerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Employee.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Employee findCustomer(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Employee> rt = cq.from(Employee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void getFullNames(){
        EntityManager em=getEntityManager();
        CriteriaQuery<Tuple> cq=em.getCriteriaBuilder().createTupleQuery();
        Root<Employee> customerRoot=cq.from(Employee.class);
        cq.multiselect(customerRoot.get("firstName").alias("f"),customerRoot.get("name").alias("n")).distinct(true);
        TypedQuery<Tuple> q=em.createQuery(cq);
        System.out.println("FullNames:");
        for(Tuple t:q.getResultList()){
            System.out.println(t.get("n")+"-----"+t.get("f"));
        }
    }
    public void getListCustomerWithPredicates(Integer age){
        EntityManager em=getEntityManager();
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Employee> cq=cb.createQuery(Employee.class);

        Root<Employee> customerRoot=cq.from(Employee.class);
        cq.select(customerRoot);
        Predicate predicate=cb.conjunction();
        if (age!=null){
            ParameterExpression<Integer> p= cb.parameter(Integer.class,"myage");
            predicate=cb.and(predicate,cb.greaterThan(customerRoot.get("age"),p));
        }
        cq.where(predicate).orderBy(cb.asc(customerRoot.get("firstName")));

        TypedQuery<Employee> query=em.createQuery(cq).setParameter("myage",age);
        for(Employee employee :query.getResultList()){
            System.out.println(employee);
        }


    }

    public static void main(String[] args) {
        CustomerJpaController customerJpaController= new CustomerJpaController();
        EntityManager em= customerJpaController.getEntityManager();
        try {
            em.getTransaction().begin();
            Employee employee1 =new Employee(31,"Fabrice","Touopi");
            Employee employee2 =new Employee(40,"Eric","Nde");
            Employee employee3 =new Employee(29,"Leopold Singor","Touopi");

            Department department=new Department("Info");
            customerJpaController.getEntityManager().persist(department);

//            employee1.setDepartment(department);
//            employee2.setDepartment(department);
//            employee3.setDepartment(department);

            customerJpaController.create(employee1);
            customerJpaController.create(employee2);
            customerJpaController.create(employee3);
            em.getTransaction().commit();

            List<Employee> listEmployees =customerJpaController.findCustomerEntities();
            listEmployees.stream().forEach(System.out::println);
            customerJpaController.getFullNames();
            System.out.println("Employee greater than 30");
           customerJpaController.getListCustomerWithPredicates(30);
        } catch (Exception ex) {
            Logger.getLogger(CustomerJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
