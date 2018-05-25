/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acn.hackation.mavenproject1;

import acn.hackation.mavenproject1.exceptions.NonexistentEntityException;
import acn.hackation.mavenproject1.exceptions.PreexistingEntityException;

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

    public void create(Customer customer) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCustomer(customer.getId()) != null) {
                throw new PreexistingEntityException("Customer " + customer + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Customer customer) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            customer = em.merge(customer);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = customer.getId();
                if (findCustomer(id) == null) {
                    throw new NonexistentEntityException("The customer with id " + id + " no longer exists.");
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
            Customer customer;
            try {
                customer = em.getReference(Customer.class, id);
                customer.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customer with id " + id + " no longer exists.", enfe);
            }
            em.remove(customer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Customer> findCustomerEntities() {
        return findCustomerEntities(true, -1, -1);
    }

    public List<Customer> findCustomerEntities(int maxResults, int firstResult) {
        return findCustomerEntities(false, maxResults, firstResult);
    }

    private List<Customer> findCustomerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Customer.class));
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

    public Customer findCustomer(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Customer.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Customer> rt = cq.from(Customer.class);
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
        Root<Customer> customerRoot=cq.from(Customer.class);
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
        CriteriaQuery<Customer> cq=cb.createQuery(Customer.class);

        Root<Customer> customerRoot=cq.from(Customer.class);
        cq.select(customerRoot);
        Predicate predicate=cb.conjunction();
        if (age!=null){
            ParameterExpression<Integer> p= cb.parameter(Integer.class,"myage");
            predicate=cb.and(predicate,cb.greaterThan(customerRoot.get("age"),p));
        }
        cq.where(predicate).orderBy(cb.asc(customerRoot.get("firstName")));

        TypedQuery<Customer> query=em.createQuery(cq).setParameter("myage",age);
        for(Customer customer:query.getResultList()){
            System.out.println(customer);
        }


    }
    
    public static void main(String[] args) {
        CustomerJpaController customerJpaController= new CustomerJpaController();
        EntityManager em= customerJpaController.getEntityManager();
        try {
            em.getTransaction().begin();
            Customer customer1=new Customer(31,"Fabrice","Touopi");
            Customer customer2=new Customer(40,"Eric","Nde");
            Customer customer3=new Customer(29,"Leopold Singor","Touopi");

            Department department=new Department("Info");
            customerJpaController.getEntityManager().persist(department);

//            customer1.setDepartment(department);
//            customer2.setDepartment(department);
//            customer3.setDepartment(department);

            customerJpaController.create(customer1);
            customerJpaController.create(customer2);
            customerJpaController.create(customer3);
            em.getTransaction().commit();

            List<Customer> listCustomers=customerJpaController.findCustomerEntities();
            listCustomers.stream().forEach(System.out::println);
            customerJpaController.getFullNames();
            System.out.println("Customer greater than 30");
           customerJpaController.getListCustomerWithPredicates(30);
        } catch (Exception ex) {
            Logger.getLogger(CustomerJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
