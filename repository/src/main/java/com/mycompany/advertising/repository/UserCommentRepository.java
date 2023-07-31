package com.mycompany.advertising.repository;

import com.mycompany.advertising.repository.entity.UserCommentTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Amir on 2/13/2022.
 */
@Repository
public interface UserCommentRepository extends JpaRepository<UserCommentTo, Long> {
    void deleteById(Long id);

    @Modifying
    @Query(value = "DELETE FROM UserCommentTo uct where uct.id = ?1")
    int  deleteByIdCont(Long id);
}
