package guru.qa.niffler.data.repository.userdata.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.user.FriendshipEntity;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.mapper.UserdataUserEntityRowMapper;
import guru.qa.niffler.data.repository.userdata.UserDataRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserDataRepositoryJdbc implements UserDataRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity userEntity) {
        try (PreparedStatement userPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                        "VALUES ( ?, ?, ?, ?, ?, ?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement friendshipPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                     "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)")) {
            userPs.setString(1, userEntity.getUsername());
            userPs.setString(2, userEntity.getCurrency().name());
            userPs.setString(3, userEntity.getFirstname());
            userPs.setString(4, userEntity.getSurname());
            userPs.setBytes(5, userEntity.getPhoto());
            userPs.setBytes(6, userEntity.getPhotoSmall());
            userPs.setString(7, userEntity.getFullname());
            userPs.executeUpdate();
            final UUID generatedKey;
            try (ResultSet rs = userPs.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            userEntity.setId(generatedKey);

            userEntity.getFriendshipIncome().forEach(fs -> fs.setAddressee(userEntity));
            userEntity.getFriendshipOutcome().forEach(fs -> fs.setRequester(userEntity));

            Stream.concat(userEntity.getFriendshipIncome().stream(), userEntity.getFriendshipOutcome().stream())
                    .forEach(friendship -> {
                        try {
                            friendshipPs.setObject(1, friendship.getRequester().getId());
                            friendshipPs.setObject(2, friendship.getAddressee().getId());
                            friendshipPs.setString(3, friendship.getStatus().name());
                            friendshipPs.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
                            friendshipPs.addBatch();
                            friendshipPs.clearParameters();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
            friendshipPs.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userEntity;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "select * from \"user\" u join friendship f on u.id = f.requester_id OR u.id = f.addressee_id where u.id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                UserEntity user = null;
                List<FriendshipEntity> friendshipEntitiesIncome = new ArrayList<>();
                List<FriendshipEntity> friendshipEntitiesOutCome = new ArrayList<>();
                while (rs.next()) {
                    FriendshipEntity fs = new FriendshipEntity();
                    if (user == null) {
                        user = UserdataUserEntityRowMapper.instance.mapRow(rs, 1);
                    }
                    if (rs.getObject("requester_id").toString().equals(user.getId().toString())) {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setId(rs.getObject("addressee_id", UUID.class));
                        fs.setRequester(user);
                        fs.setAddressee(userEntity);
                        fs.setStatus(rs.getString("status").equals(FriendshipStatus.PENDING.name()) ? FriendshipStatus.PENDING : FriendshipStatus.ACCEPTED);
                        fs.setCreatedDate(new Date(System.currentTimeMillis()));
                        friendshipEntitiesOutCome.add(fs);
                    } else {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setId(rs.getObject("requester_id", UUID.class));
                        fs.setRequester(userEntity);
                        fs.setAddressee(user);
                        fs.setStatus(rs.getString("status").equals(FriendshipStatus.PENDING.name()) ? FriendshipStatus.PENDING : FriendshipStatus.ACCEPTED);
                        fs.setCreatedDate(new Date(System.currentTimeMillis()));
                        friendshipEntitiesIncome.add(fs);
                    }
                }
                if (user == null) {
                    return Optional.empty();
                } else {
                    user.setFriendshipIncome(friendshipEntitiesIncome);
                    user.setFriendshipOutcome(friendshipEntitiesOutCome);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFriendshipRequest(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement friendshipPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                     "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)")) {
            friendshipPs.setObject(1, requester.getId());
            friendshipPs.setObject(2, addressee.getId());
            friendshipPs.setString(3, FriendshipStatus.PENDING.name());
            friendshipPs.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            friendshipPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement friendshipPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) VALUES (?, ?, ?, ?)")) {
            friendshipPs.setObject(1, requester.getId());
            friendshipPs.setObject(2, addressee.getId());
            friendshipPs.setString(3, FriendshipStatus.ACCEPTED.name());
            friendshipPs.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            friendshipPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.empty();
    }
}
