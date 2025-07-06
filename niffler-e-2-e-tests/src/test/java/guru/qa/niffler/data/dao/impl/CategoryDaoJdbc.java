package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.sql.*;
import java.util.*;

public class CategoryDaoJdbc implements CategoryDao {

    private static final Config CFG = Config.getInstance();

    private final Connection connection;

    public CategoryDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public CategoryEntity create(CategoryEntity category) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO category (username, name, archived) " +
                        "VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, category.getUsername());
            ps.setString(2, category.getName());
            ps.setBoolean(3, category.isArchived());
            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            category.setId(generatedKey);
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM category WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    CategoryEntity ce = createCategoryEntity(rs);
                    return Optional.of(ce);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM category WHERE username = ? and name = ?"
        )) {
            ps.setObject(1, username);
            ps.setObject(1, categoryName);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    CategoryEntity ce = createCategoryEntity(rs);
                    return Optional.of(ce);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CategoryEntity> findAllByUsername(String username) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM category WHERE username = ?"
        )) {
            ps.setObject(1, username);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                List<CategoryEntity> categoryEntities = new ArrayList<>();
                while (rs.next()) {
                    CategoryEntity ce = createCategoryEntity(rs);
                    categoryEntities.add(ce);
                }
                if (!categoryEntities.isEmpty()) {
                    return categoryEntities;
                } else {
                    return Collections.emptyList();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCategory(CategoryEntity category) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE * FROM category WHERE id = ?"
        )) {
            ps.setObject(1, category.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryEntity update(CategoryEntity categoryEntity) {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE category " +
                        " SET name = ?, username = ?, archived = ? " +
                        " WHERE id = ?",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, categoryEntity.getName());
            ps.setString(2, categoryEntity.getUsername());
            ps.setBoolean(3, categoryEntity.isArchived());
            ps.setObject(4, categoryEntity.getId());
            ps.executeUpdate();
            return categoryEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private CategoryEntity createCategoryEntity(ResultSet rs) throws SQLException {
        CategoryEntity ce = new CategoryEntity();
        ce.setId(rs.getObject("id", UUID.class));
        ce.setUsername(rs.getString("username"));
        ce.setName(rs.getString("name"));
        ce.setArchived(rs.getBoolean("archived"));
        return ce;
    }
}
