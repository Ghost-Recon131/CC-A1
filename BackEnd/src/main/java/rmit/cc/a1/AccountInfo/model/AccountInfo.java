package rmit.cc.a1.AccountInfo.model;



import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rmit.cc.a1.Account.model.Account;

import javax.persistence.*;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "Student_Info")
public class AccountInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    // Forign key to join tables
    @ManyToOne
    @JoinColumn(nullable = false, name = "student_user_id")
    private Account account;

    @Column(name = "gender")
    private String gender;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "dob")
    private String dob;

    @Column(name = "phone")
    private Long phone;

    @Column(name = "university")
    private String university;

    @Column(name = "uniLevel")
    private String uniLevel;

    @Column(name = "field")
    private String field;

    public AccountInfo(Account account, String gender, String nationality, String dob, Long phone, String university, String uniLevel, String field) {
        this.account = account;
        this.gender = gender;
        this.nationality = nationality;
        this.dob = dob;
        this.phone = phone;
        this.university = university;
        this.uniLevel = uniLevel;
        this.field = field;
    }

}