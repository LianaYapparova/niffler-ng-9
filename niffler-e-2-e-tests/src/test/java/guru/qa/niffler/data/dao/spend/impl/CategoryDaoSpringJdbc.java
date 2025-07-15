package guru.qa.niffler.data.dao.spend.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.spend.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

public class CategoryDaoSpringJdbc implements CategoryDao {

    private static final Config CFG = Config.getInstance();
    @Override
    public CategoryEntity create(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO category (username, name, archived) " +
                            "VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, category.getUsername());
            ps.setString(2, category.getName());
            ps.setBoolean(3, category.isArchived());
            return ps;
        }, kh);
        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        category.setId(generatedKey);
        return category;
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        CategoryEntity categoryEntity = jdbcTemplate.queryForObject("SELECT * FROM category WHERE id = ?",
                new BeanPropertyRowMapper<>(CategoryEntity.class), id);
        return Optional.ofNullable(categoryEntity);
    }


    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        CategoryEntity categoryEntity = jdbcTemplate.queryForObject("SELECT * FROM category WHERE username = ? and name = ?",
                categoryEntityRowMapper, username, categoryName);
        return Optional.ofNullable(categoryEntity);
    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        List<CategoryEntity> categoryEntities = jdbcTemplate.query("SELECT * FROM category WHERE username = ?",
                new Object[]{username},
                categoryEntityRowMapper);
        if (categoryEntities.isEmpty()) {
            return Collections.emptyList();
        } else {
            return categoryEntities;
        }
    }

    @Override
    public void deleteCategory(CategoryEntity category) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        jdbcTemplate.update("delete from category where id = ?", category.getId());
    }

    @Override
    public CategoryEntity update(CategoryEntity categoryEntity) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        CategoryEntity categoryEntityUpdated = jdbcTemplate.queryForObject("UPDATE category " +
                        " SET name = ?, username = ?, archived = ? " +
                        " WHERE id = ?",
                new Object[]{categoryEntity.getName(), categoryEntity.getUsername(), categoryEntity.isArchived()}, categoryEntityRowMapper);
        return categoryEntityUpdated;
    }

    @Override
    public List<CategoryEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        List<CategoryEntity> categoryEntities = jdbcTemplate.query("SELECT * FROM category ",
                categoryEntityRowMapper);
        if (categoryEntities.isEmpty()) {
            return Collections.emptyList();
        } else {
            return categoryEntities;
        }
    }

    private final RowMapper<CategoryEntity> categoryEntityRowMapper = (resultSet, rowNum) -> {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(resultSet.getObject("id", UUID.class));
        categoryEntity.setName(resultSet.getString("name"));
        categoryEntity.setUsername(resultSet.getString("username"));
        categoryEntity.setArchived(resultSet.getBoolean("archived"));
        return categoryEntity;
    };
}
