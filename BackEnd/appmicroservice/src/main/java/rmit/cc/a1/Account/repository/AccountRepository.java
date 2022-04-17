package rmit.cc.a1.Account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmit.cc.a1.Account.model.Account;

import javax.persistence.OneToMany;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String email);

    Account getById(Long id);

    // Delete all other data entries related to this user
    @OneToMany
    void deleteById(Long id);

}
