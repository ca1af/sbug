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
    @Query("update Channel c set c.inUse = false  where c.id = :channelId")
    void disableChannelById(@Param("id") Long channelId);

    @Query("select Channel from Channel c where c.inUse = true")
    Page<Channel> findAllByInUseIsTrue(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("delete from Channel t where t.inUse = false and t.modifiedAt < :localDateTime ")
    void deleteChannels(@Param("localDateTime") LocalDateTime localDateTime);
}
