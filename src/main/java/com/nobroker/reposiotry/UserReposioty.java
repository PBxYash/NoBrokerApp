package com.nobroker.reposiotry;

import com.nobroker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReposioty  extends JpaRepository<User,Long> {
    User findByEmail(String email );
}
