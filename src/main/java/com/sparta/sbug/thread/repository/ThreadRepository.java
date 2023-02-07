package com.sparta.sbug.thread.repository;

import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThreadRepository extends JpaRepository<Thread,Long> {
    Optional<Thread> findByIdAndUser(Long id, User user);

}
