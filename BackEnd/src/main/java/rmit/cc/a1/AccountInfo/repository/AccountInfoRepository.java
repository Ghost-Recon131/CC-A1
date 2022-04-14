package rmit.cc.a1.AccountInfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmit.cc.a1.AccountInfo.model.AccountInfo;


@Repository
public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long> {

    AccountInfo getById(Long id);

}
