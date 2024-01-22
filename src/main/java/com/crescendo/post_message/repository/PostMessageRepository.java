package com.crescendo.post_message.repository;

import com.crescendo.post_message.entity.PostMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostMessageRepository extends JpaRepository<PostMessage, String> {
}
