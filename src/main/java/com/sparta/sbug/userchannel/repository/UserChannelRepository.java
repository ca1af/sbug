package com.sparta.sbug.userchannel.repository;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.userchannel.enttiy.UserChannel;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserChannelRepository extends JpaRepository<UserChannel, Long> {
    @Query("delete from UserChannel uc where uc.channel.id = :channelId")
    @Modifying(clearAutomatically = true)
    void deleteAllByChannelId(@Param("channelId") Long channelId);

    @Query("delete from UserChannel uc where uc.user.id = :userId")
    @Modifying(clearAutomatically = true)
    void deleteAllByUserId(@Param("userId") Long userId);

    Optional<UserChannel> findByUserAndChannel(User user, Channel channel);

    boolean existsByUserAndChannel(User user, Channel channel);

    List<UserChannel> findAllChannelByUserId(Long userId);
}
