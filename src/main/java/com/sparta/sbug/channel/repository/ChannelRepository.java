package com.sparta.sbug.channel.repository;

import com.sparta.sbug.channel.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {

}
