package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.data.entity.user.FriendshipEntity;
import guru.qa.niffler.data.entity.user.UserEntity;

import java.util.List;
import java.util.UUID;

public record UserJson(@JsonProperty("id")
                       UUID id,
                       @JsonProperty("username")
                       String username,
                       @JsonProperty("password")
                       String password,

                       CurrencyValues currency,

                       String firstname,
                       String surname,

                       byte[] photo,
                       byte[] photo_small,
                       String full_name,
                       List<FriendshipEntity> friendshipRequests,
                       List<FriendshipEntity> friendshipAddressees) {

    public static UserJson fromEntity(UserEntity entity) {

        return new UserJson(
                entity.getId(),
                entity.getUsername(),
                null,
                entity.getCurrency(),
                entity.getFirstname(),
                entity.getSurname(),
                entity.getPhoto(),
                entity.getPhotoSmall(),
                entity.getFullname(),
                entity.getFriendshipIncome(),
                entity.getFriendshipOutcome()
        );
    }

}
