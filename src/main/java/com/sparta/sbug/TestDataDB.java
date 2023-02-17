package com.sparta.sbug;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.repository.CommentRepository;
import com.sparta.sbug.emoji.entity.CommentEmoji;
import com.sparta.sbug.emoji.entity.ThreadEmoji;
import com.sparta.sbug.emoji.repository.CommentEmojiRepository;
import com.sparta.sbug.emoji.repository.ThreadEmojiRepository;
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
        private final CommentEmojiRepository commentEmojiRepository;

        public void init() {
            // 유저 생성
            User user1 = User.builder().email("user1").password(getEncode("password1")).nickname("뽀로로")
                    .build();
            User savedUser1 = userRepository.save(user1);

            User user2 = User.builder().email("user2").password(getEncode("password2")).nickname("루피")
                    .build();
            userRepository.save(user2);
            
            User user3 = User.builder().email("user3").password(getEncode("password3")).nickname("펭구")
                    .build();
            User savedUser3 = userRepository.save(user3);

            User user4 = User.builder().email("user4").password(getEncode("password4")).nickname("뿡뿡이")
                    .build();
            userRepository.save(user4);

            // 채널 생성
            Channel channel = Channel.builder()
                    .adminEmail(savedUser1.getEmail())
                    .channelName("channel").build();

            Channel channel2 = Channel.builder()
                    .adminEmail(savedUser3.getEmail())
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

            ThreadEmoji threadEmoji = new ThreadEmoji("SMILE", user1, thread);
            threadEmojiRepository.save(threadEmoji);
            ThreadEmoji threadEmoji1 = new ThreadEmoji("CRY", user1, thread);
            threadEmojiRepository.save(threadEmoji1);
            ThreadEmoji threadEmoji2 = new ThreadEmoji("HEART", user1, thread);
            threadEmojiRepository.save(threadEmoji2);
            ThreadEmoji threadEmoji3 = new ThreadEmoji("LIKE", user1, thread);
            threadEmojiRepository.save(threadEmoji3);
            CommentEmoji commentEmoji = new CommentEmoji("SMILE", user1, comment1);
            commentEmojiRepository.save(commentEmoji);
        }

        private String getEncode(String password) {
            return passwordEncoder.encode(password);
        }
    }
}
