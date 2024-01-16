package com.crescendo.repository;

import com.crescendo.entity.PostMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostMessageRepository extends JpaRepository<PostMessage, String> {
}
