package acn.jpa.examples.domains;

import acn.jpa.examples.listeners.NameValidator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by fabrice on 8/21/18.
 */
@Entity
//@TableGenerator(name = "Empl_Gen")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(NameValidator.class)
@DiscriminatorColumn(name = "EMP_TYPE",discriminatorType = DiscriminatorType.STRING)

public class Employee implements Serializable,NamedEntity {

    private static final long serialVersionUID = 1L;
    public static final String LOCAL_AREA_CODE = "613";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    private String name;
    @Basic(optional = false)
    @Column(name = "age")
    private int age;
    @Column(name = "first_name")
    private String firstName;
    @Transient private String phoneNum =new String();

    @Basic(fetch = FetchType.LAZY)
    @Column(name="COMM")
    private String comments;

    @Basic(fetch=FetchType.LAZY)
    @Lob @Column(name="PIC")
    private byte[] picture;

    @Enumerated(EnumType.STRING)
    private EmployeeType employeeType;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Calendar dob;

    @Transient private long syncTime;
    @PostPersist
    @PostUpdate
    @PostLoad
    private void resetSyncTime() { syncTime = System.currentTimeMillis(); }

    @Embedded
    @AttributeOverrides({ //overrites the attributes
            @AttributeOverride(name = "city",column = @Column(name="ville")),
            @AttributeOverride(name = "street",column = @Column(name="rue"))
     }
    )
    private Address address;

    @ElementCollection(targetClass = VacationEntry.class)
    private Collection vacancyBookings;

    @ElementCollection
    @CollectionTable(name = "EMPL_PHONE")
    @MapKeyColumn(name = "PHONE_TYPE")
    @Column(name = "PHONE_NUM")
    private Map<String,String> phoneNumbers;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Department department;


    public Employee() {
    }

    public Employee(String name, String phoneNum, String comments, byte[] picture, EmployeeType employeeType, Date startDate, Calendar dob) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.comments = comments;
        this.picture = picture;
        this.employeeType = employeeType;
        this.startDate = startDate;
        this.dob = dob;
    }

    public Employee( int age,String name, String firstName) {
        this.name = name;
        this.age = age;
        this.firstName = firstName;
    }

    public String getPhoneNumber() { return phoneNum; }
    public void setPhoneNumber(String num) { this.phoneNum = num; }

    @Access(AccessType.PROPERTY) @Column(name="PHONE")
    protected String getPhoneNumberForDb() {
        if (phoneNum.length() == 10)
            return phoneNum;
        else
            return LOCAL_AREA_CODE + phoneNum;
    }
    protected void setPhoneNumberForDb(String num) {
        if (num.startsWith(LOCAL_AREA_CODE))
            phoneNum = num.substring(3);
        else
            phoneNum = num;
    }


    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Calendar getDob() {
        return dob;
    }

    public void setDob(Calendar dob) {
        this.dob = dob;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Employee{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", employeeType=").append(employeeType);
        sb.append('}');
        return sb.toString();
    }
}
