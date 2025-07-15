package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.spend.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.spend.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.spend.impl.SpendDaoJdbc;
import guru.qa.niffler.data.dao.spend.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.SpendJson;

import java.sql.Connection;

public class SpendDbClient {

    public static final Config CFG = Config.getInstance();

    private final SpendDaoJdbc spendDaoJdbc = new SpendDaoJdbc();
    private final CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDaoJdbc
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            spendDaoJdbc.create(spendEntity)
                    );
                },
                Connection.TRANSACTION_READ_COMMITTED
        );
    }

    public SpendJson createSpendJdBcSpring(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);

        SpendDaoSpringJdbc spendDaoSpringJdbc = new SpendDaoSpringJdbc();
        CategoryDaoSpringJdbc categoryDaoSpringJdbc = new CategoryDaoSpringJdbc();

        CategoryEntity categoryEntity = categoryDaoSpringJdbc.create(spendEntity.getCategory());
        spendEntity.setCategory(categoryEntity);
        spendEntity = spendDaoSpringJdbc.create(spendEntity);

        return SpendJson.fromEntity(spendEntity);
    }
}
