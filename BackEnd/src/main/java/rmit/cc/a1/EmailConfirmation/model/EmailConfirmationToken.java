package rmit.cc.a1.EmailConfirmation.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rmit.cc.a1.Account.model.Account;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "email_confirmation_tokens")
public class EmailConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "email_confirmation_tokens_id")
    private long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime expireAt;

    private LocalDateTime confirmAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "student_user_id")
    private Account account;

    // Constructor
    public EmailConfirmationToken(String token, LocalDateTime createAt, LocalDateTime expireAt, Account account) {
        this.token = token;
        this.createAt = createAt;
        this.expireAt = expireAt;
        this.account = account;
    }

}
