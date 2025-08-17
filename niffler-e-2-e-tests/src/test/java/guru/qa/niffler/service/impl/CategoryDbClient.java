package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.spend.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;

import java.sql.Connection;

public class CategoryDbClient {

    private final CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();
    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    private static final Config CFG = Config.getInstance();

    public CategoryJson createCategory(CategoryJson categoryJson) {
        return jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    return CategoryJson.fromEntity(
                            categoryDaoJdbc.create(categoryEntity)
                    );
                },

                Connection.TRANSACTION_READ_COMMITTED);
    }

    public CategoryJson updateCategory(CategoryJson archived) {
        return jdbcTxTemplate.execute(() -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(archived);
                    return CategoryJson.fromEntity(
                            categoryDaoJdbc.update(categoryEntity)
                    );
                },
                Connection.TRANSACTION_READ_COMMITTED
        );
    }
}
