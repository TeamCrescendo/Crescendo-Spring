package com.crescendo.post_message.repository;

import com.crescendo.post_message.entity.PostMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostMessageRepository extends JpaRepository<PostMessage, String> {

    List<PostMessage> findAllByMemberAccount(String account);

    List<PostMessage> findAllByPostMessageReceiver(String receiver);

    List<PostMessage> findALlByMemberAccount(String account);




    //내가 보낸쪽지나 받은 쪽지 전부다 받아오는 쿼리
    @Query("SELECT p FROM PostMessage p WHERE p.member.account= ?1 or p.postMessageReceiver=?1 ORDER BY p.writtenMessageDate DESC ")
    List<PostMessage> findALL(String account);

}
