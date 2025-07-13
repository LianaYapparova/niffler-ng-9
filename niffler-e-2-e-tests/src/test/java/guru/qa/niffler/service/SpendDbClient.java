package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.spend.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.spend.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.spend.impl.SpendDaoJdbc;
import guru.qa.niffler.data.dao.spend.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.SpendJson;

import java.sql.Connection;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {

    public static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl(),
                Connection.TRANSACTION_READ_COMMITTED
        );
    }

    public SpendJson createSpendJdBcSpring(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);

        SpendDaoSpringJdbc spendDaoSpringJdbc = new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()));
        CategoryDaoSpringJdbc categoryDaoSpringJdbc = new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()));

        CategoryEntity categoryEntity = categoryDaoSpringJdbc.create(spendEntity.getCategory());
        spendEntity.setCategory(categoryEntity);
        spendEntity = spendDaoSpringJdbc.create(spendEntity);

        return SpendJson.fromEntity(spendEntity);
    }
}
