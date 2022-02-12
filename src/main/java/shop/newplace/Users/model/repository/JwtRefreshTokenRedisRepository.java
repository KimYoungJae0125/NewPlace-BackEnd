package shop.newplace.Users.model.repository;

import org.springframework.data.repository.CrudRepository;

import shop.newplace.Users.model.entity.JwtRefreshToken;

public interface JwtRefreshTokenRedisRepository extends CrudRepository<JwtRefreshToken, Long> {

}
