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

    /**
     * 채널 ID가 포함된 모든 UserChannel 엔티티를 삭제
     *
     * @param channelId 대상 채널 ID
     */
    @Query("delete from UserChannel uc where uc.channel.id = :channelId")
    @Modifying(clearAutomatically = true)
    void deleteAllByChannelId(@Param("channelId") Long channelId);

    /**
     * 유저 ID가 포함된 모든 UserChannel 엔티티를 삭제
     *
     * @param userId 대상 유저 ID
     */
    @Query("delete from UserChannel uc where uc.user.id = :userId")
    @Modifying(clearAutomatically = true)
    void deleteAllByUserId(@Param("userId") Long userId);

    /**
     * 유저 ID와 채널 ID가 모두 포함된 UserChannel 엔티티를 조회(select)
     *
     * @param user    대상 유저
     * @param channel 대상 채널
     * @return Optional&lt;UserChannel&gt;
     */
    Optional<UserChannel> findByUserAndChannel(User user, Channel channel);

    /**
     * 유저 ID와 채널 ID가 모두 포함된 UserChannel 엔티티가 있는지 확인(exist)
     *
     * @param user      대상 유저
     * @param channelId 대상 채널 ID
     * @return boolean
     */
    boolean existsByUserAndChannelId(User user, Long channelId);

    /**
     * 유저 ID를 포함하고 있는 모든 UserChannel 엔티티를 조회
     *
     * @param userId 대상 유저
     * @return List&lt;UserChannel&gt;
     */
    List<UserChannel> findAllChannelByUserId(Long userId);
}
