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
//    @Query("delete from UserChannel uc where uc.channel.id = :channelId")
    @Query(nativeQuery = true, value = "delete from user_channel where channel_id =:channelId")
    @Modifying(clearAutomatically = true)
    void deleteAllByChannelId(@Param("channelId") Long channelId);

    /**
     * 유저 ID가 포함된 모든 UserChannel 엔티티를 삭제
     *
     * @param userId 대상 유저 ID
     */
//    @Query("delete from UserChannel uc where uc.user.id = :userId")
    @Query(nativeQuery = true, value = "delete from user_channel where user_id =:userId")
    @Modifying(clearAutomatically = true)
    void deleteAllByUserId(@Param("userId") Long userId);

    /**
     * 유저 ID와 채널 ID가 모두 포함된 UserChannel 엔티티를 조회(Exists)
     * InUse 칼럼을 확인해서 True 인 객체들만 조회합니다.
     *
     * @param user    대상 유저
     * @param channel 대상 채널
     * @return Boolean
     */
    Boolean existsByUserAndChannelAndInUseIsTrue(User user, Channel channel);

    Optional<UserChannel> findByUserAndChannelIdAndInUseIsTrue(User user, Long channelId);

    /**
     * 유저 ID와 채널 ID가 모두 포함된 UserChannel 엔티티가 있는지 확인(exist)
     *
     * @param user      대상 유저
     * @param channelId 대상 채널
     * @return boolean
     */
    boolean existsByUserAndChannelIdAndInUseIsTrue(User user, Long channelId);

    /**
     * 유저 ID를 포함하고 있는 모든 UserChannel 엔티티를 조회
     *
     * @param userId 대상 유저
     * @return List&lt;UserChannel&gt;
     */
    List<UserChannel> findAllChannelByUserIdAndInUseIsTrue(Long userId);

    /**
     * UserChannel (중간 테이블)에는 유저와 채널의 FK가 존재합니다. 즉, 실질적으로 유저와 채널을 관계맺어주는 테이블입니다.
     * 따라서 이 테이블의 InUse 를 false 시켜서 유저가 가진 채널로써 조회되지 못하도록 합니다.
     * 유저 id 를 where 절에 넣었으므로, 유저가 채널 탈퇴시에 사용 할 수 있는 로직입니다.
     *
     * @param userId 대상 유저
     */
//    @Query("update UserChannel uc set uc.inUse = false where uc.user.id = :userId")
    @Query(nativeQuery = true, value = "update user_channel set in_use = false where user_id =:userId")
    @Modifying(clearAutomatically = true)
    void disableAllUserChannelByUserIdAndInUse(@Param("userId") Long userId);

    /**
     * 채널 ID 가 일치하는 모든 객체를 비활성화 합니다.
     * 채널 삭제 시 사용 할 수 있는 로직입니다.
     *
     * @param channelId 채널 ID
     */

//    @Query("update UserChannel uc set uc.inUse = false where uc.channel.id = :channelId")
    @Query(nativeQuery = true, value = "update user_channel set in_use = false where channel_id =:channelId")
    @Modifying(clearAutomatically = true)
    void disableAllUserChannelByChannelIdAndInUse(@Param("channelId") Long channelId);

}
