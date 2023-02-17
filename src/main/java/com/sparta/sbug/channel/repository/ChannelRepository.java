package com.sparta.sbug.channel.repository;

import com.sparta.sbug.channel.entity.Channel;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    @Modifying(clearAutomatically = true)
    @Query("update Channel c set c.inUse = false  where c.id = :channelId")
    void disableChannelById(@Param("id") Long channelId);
}
