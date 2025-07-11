package guru.qa.niffler.data.entity.user;

import guru.qa.niffler.data.entity.Entity;
import guru.qa.niffler.model.CurrencyValues;

import guru.qa.niffler.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserEntity implements Entity {
    private UUID id;
    private String username;
    private String password;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private String fullname;
    private byte[] photo;
    private byte[] photoSmall;

    public static UserEntity fromJson(User json) {
        UserEntity u = new UserEntity();
        u.setPassword(json.password());
        u.setId(json.id());
        u.setUsername(json.username());
        u.setCurrency(json.currency());
        u.setFirstname(json.firstname());
        u.setSurname(json.surname());
        u.setPhoto(json.photo());
        u.setPhotoSmall(json.photo_small());
        u.setFullname(json.full_name());
        return u;
    }
}
