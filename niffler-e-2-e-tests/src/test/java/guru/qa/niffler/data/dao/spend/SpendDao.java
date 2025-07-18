package guru.qa.niffler.data.dao.spend;

import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {
    SpendEntity create(SpendEntity spend);

    Optional<SpendEntity> findSpendById(UUID id);

    List<SpendEntity> findAllByUserName(String username);

    void deleteSpend(SpendEntity spend);

    List<SpendEntity>  findAll();
}
