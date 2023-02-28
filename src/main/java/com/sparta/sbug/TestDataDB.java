package com.sparta.sbug;

import com.sparta.sbug.admin.entity.Admin;
import com.sparta.sbug.admin.respository.AdminRepository;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.repository.CommentRepository;
import com.sparta.sbug.emoji.entity.ThreadEmoji;
import com.sparta.sbug.emoji.repository.ThreadEmojiRepository;
import com.sparta.sbug.schedule.entity.Schedule;
import com.sparta.sbug.schedule.entity.ScheduleStatus;
import com.sparta.sbug.schedule.repository.ScheduleRepository;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.repository.UserRepository;
import com.sparta.sbug.userchannel.enttiy.UserChannel;
import com.sparta.sbug.userchannel.repository.UserChannelRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TestDataDB {
    private final InitService initService;

    @PostConstruct
    public void initialize() {
        initService.init();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final ThreadRepository threadRepository;
        private final UserRepository userRepository;
        private final ChannelRepository channelRepository;
        private final UserChannelRepository userChannelRepository;
        private final PasswordEncoder passwordEncoder;
        private final CommentRepository commentRepository;
        private final ThreadEmojiRepository threadEmojiRepository;
        private final AdminRepository adminRepository;
        private final ScheduleRepository scheduleRepository;
        public void init() {
            // 유저 생성
            User user1 = User.builder().email("user1@naver.com").password(getEncode("password1")).nickname("뽀로로")
                    .build();
            user1.setProfileImage("default.png");
            User savedUser1 = userRepository.save(user1);


            User user2 = User.builder().email("user2@naver.com").password(getEncode("password2")).nickname("루피")
                    .build();
            user2.setProfileImage("default.png");
            userRepository.save(user2);

            User user3 = User.builder().email("user3@naver.com").password(getEncode("password3")).nickname("펭구")
                    .build();
            user3.setProfileImage("default.png");
            userRepository.save(user3);

            User user4 = User.builder().email("user4@naver.com").password(getEncode("password4")).nickname("뿡뿡이")
                    .build();
            user4.setProfileImage("default.png");
            userRepository.save(user4);

            // 채널 생성
            Channel channel = Channel.builder()
                    .channelName("channel").build();

            Channel channel2 = Channel.builder()
                    .channelName("channel2").build();

            Channel savedChannel1 = channelRepository.save(channel);
            Channel savedChannel2 = channelRepository.save(channel2);

            // 사용자-채널 바인딩(생성)
            UserChannel userChannel1 = UserChannel.builder().user(user1).channel(savedChannel1).build();
            UserChannel userChannel2 = UserChannel.builder().user(user2).channel(savedChannel1).build();
            UserChannel userChannel3 = UserChannel.builder().user(user3).channel(savedChannel2).build();
            UserChannel userChannel4 = UserChannel.builder().user(user4).channel(savedChannel2).build();
            userChannelRepository.save(userChannel1);
            userChannelRepository.saveAndFlush(userChannel2);
            userChannelRepository.saveAndFlush(userChannel3);
            userChannelRepository.saveAndFlush(userChannel4);

            // 쓰레드 생성
            Thread thread = new Thread(savedChannel1, savedUser1, "안녕하세요");
            Thread thread2 = new Thread(savedChannel2, savedUser1, "안녕하세요2");

            Thread savedThread = threadRepository.save(thread);
            Thread savedThread2 = threadRepository.save(thread2);

            savedChannel1.addThread(savedThread);
            savedChannel2.addThread(savedThread2);

            Comment comment1 = new Comment("코멘트1", user1);
            comment1.setThread(thread);
            commentRepository.save(comment1);

            Comment comment2 = new Comment("코멘트2", user1);
            comment2.setThread(thread);
            commentRepository.save(comment2);

            Comment comment3 = new Comment("코멘트3", user1);
            comment3.setThread(thread);
            commentRepository.save(comment3);

            Comment comment4 = new Comment("코멘트4", user1);
            comment4.setThread(thread);
            commentRepository.save(comment4);

            Comment comment5 = new Comment("코멘트5", user1);
            comment5.setThread(thread);
            commentRepository.save(comment5);

            Comment comment6 = new Comment("코멘트6", user1);
            comment6.setThread(thread);
            commentRepository.save(comment6);

            ThreadEmoji threadEmoji = new ThreadEmoji("SMILE", user1, thread);
            threadEmojiRepository.save(threadEmoji);

            Admin admin = Admin.builder().email("admin").nickname("관리자1").password(getEncode("adminpass")).build();
            adminRepository.save(admin);

            Schedule schedule1 = Schedule.builder()
                    .user(user1)
                    .content("1번 스케쥴")
                    .date(LocalDateTime.of(2023, 1, 5, 14, 40))
                    .status(ScheduleStatus.UNDONE)
                    .build();
            Schedule schedule2 = Schedule.builder()
                    .user(user1)
                    .content("2번 스케쥴")
                    .date(LocalDateTime.of(2023, 2, 7, 14, 40))
                    .status(ScheduleStatus.UNDONE)
                    .build();
            Schedule schedule3 = Schedule.builder()
                    .user(user1)
                    .content("3번 스케쥴")
                    .date(LocalDateTime.of(2023, 3, 9, 14, 40))
                    .status(ScheduleStatus.UNDONE)
                    .build();
            scheduleRepository.save(schedule1);
            scheduleRepository.save(schedule2);
            scheduleRepository.save(schedule3);


        }

        private String getEncode(String password) {
            return passwordEncoder.encode(password);
        }
    }
}
