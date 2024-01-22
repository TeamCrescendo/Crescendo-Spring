package com.crescendo.post_message.repository;

import com.crescendo.post_message.entity.PostMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostMessageRepository extends JpaRepository<PostMessage, String> {

    List<PostMessage> findAllByMemberAccount(String account);

    List<PostMessage> findAllByPostMessageReceiver(String receiver);

    List<PostMessage> findALlByMemberAccount(String account);
}
