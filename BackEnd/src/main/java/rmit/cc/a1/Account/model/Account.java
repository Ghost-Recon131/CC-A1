package rmit.cc.a1.Account.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import rmit.cc.a1.utils.UserRole;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "Accounts")
public class Account implements UserDetails {

    // Account properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    // Email will be used as username
    @Email(message = "Please enter an email address")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Please enter your full name")
    @Column(name = "full_name")
    private String fullName;

    @NotBlank(message = "Please enter a password")
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole userRole;

    @Column(name = "secret_question")
    private String secretQuestion;

    @Column(name = "secret_question_answer")
    private String secretQuestionAnswer;

    // Default account is not locked
    @Column(name = "lock_status")
    private Boolean locked = false;

    // Default not enabled till email is verified
    @Column(name = "account_enabled")
    private Boolean enabled = true;

    @Column(name = "create_At")
    private Date create_At = new Date();

    @Column(name = "update_At")
    private Date update_At;


    public Account(String username, String fullName, String password, UserRole userRole, String secretQuestion, String secretQuestionAnswer) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.userRole = userRole;
        this.secretQuestion = secretQuestion;
        this.secretQuestionAnswer = secretQuestionAnswer;
    }

    // Setters
    @PrePersist
    protected void onCreate(){
        this.create_At = new Date();
    }

    @PreUpdate
    protected void onUpdate(){
        this.update_At = new Date();
    }


    // UserDetails interface methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
