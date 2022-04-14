package rmit.cc.a1.Account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rmit.cc.a1.Account.model.Account;

import javax.persistence.OneToMany;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String email);

    Account getById(Long id);

    // Delete all other data entries related to this user
    @OneToMany
    void deleteById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Account a " +
            "SET a.enabled = TRUE WHERE a.username = ?1")
    void confirmAccountEmail(String email);

}
