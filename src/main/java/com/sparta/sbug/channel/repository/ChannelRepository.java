package com.sparta.sbug.channel.repository;

import com.sparta.sbug.channel.entity.Channel;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    @Modifying(clearAutomatically = true)
//    @Query("update Channel c set c.inUse = false  where c.id = :channelId")
    @Query(value = "update channel SET in_use where id =:channelId", nativeQuery = true)
    void disableChannelById(@Param("id") Long channelId);

//    @Query("select Channel from Channel c where c.inUse = true")
    @Query(value = "select channel c from c where in_use=:true", nativeQuery = true)
    Page<Channel> findAllByInUseIsTrue(Pageable pageable);

    @Modifying(clearAutomatically = true)
//    @Query("delete from Channel t where t.inUse = false and t.modifiedAt < :localDateTime ")
    @Query(value = "delete from channel c where in_use=false and modified_at<:localDateTime",nativeQuery = true)
    void deleteChannels(@Param("localDateTime") LocalDateTime localDateTime);
}
