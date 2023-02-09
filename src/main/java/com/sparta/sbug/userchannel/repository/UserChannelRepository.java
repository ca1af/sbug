package com.sparta.sbug.userchannel.repository;

import com.sparta.sbug.userchannel.enttiy.UserChannel;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserChannelRepository extends JpaRepository<UserChannel, Long> {
    @Query("delete from UserChannel uc where uc.channel.id = :channelId")
    @Modifying(clearAutomatically = true)
    void deleteAllByChannelId(@Param("channelId") Long channelId);
}
