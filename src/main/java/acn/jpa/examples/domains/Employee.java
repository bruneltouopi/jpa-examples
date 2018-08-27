package acn.jpa.examples.domains;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by fabrice on 8/21/18.
 */
@Entity
//@TableGenerator(name = "Empl_Gen")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "EMP_TYPE",discriminatorType = DiscriminatorType.STRING)

public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String LOCAL_AREA_CODE = "613";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    private String name;
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
}
