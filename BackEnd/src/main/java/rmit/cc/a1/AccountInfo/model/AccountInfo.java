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
    @JoinColumn(nullable = false, name = "account_id")
    private Account account;

    @Column(name = "dob")
    private String dob;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    public AccountInfo(Account account, String dob, String phone, String address) {
        this.account = account;
        this.dob = dob;
        this.phone = phone;
        this.address = address;
    }

}