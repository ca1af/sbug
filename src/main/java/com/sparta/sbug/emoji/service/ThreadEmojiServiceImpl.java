package com.sparta.sbug.emoji.service;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.emoji.entity.CommentEmoji;
import com.sparta.sbug.emoji.entity.EmojiType;
import com.sparta.sbug.emoji.entity.ThreadEmoji;
import com.sparta.sbug.emoji.repository.ThreadEmojiRepository;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.service.ThreadServiceImpl;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ThreadEmojiServiceImpl implements ThreadEmojiService {

    private final ThreadEmojiRepository threadEmojiRepository;
    private final ThreadServiceImpl threadService;


    // ThreadEmoji 생성
    @Override
    public String createThreadEmoji(String emojiType, User user, Long threadId) {
        Thread thread = threadService.getThread(threadId);
        Optional<ThreadEmoji> optionalThreadEmoji = threadEmojiRepository.findByEmojiTypeAndThreadAndUser(EmojiType.valueOf(emojiType),thread, user);
        if (optionalThreadEmoji.isPresent()) {
            throw new IllegalArgumentException("이미 동일한 이모지 반응이 존재합니다.");
        }
        ThreadEmoji threadEmoji = new ThreadEmoji(emojiType, user, thread);
        threadEmojiRepository.save(threadEmoji);
        return "Success";
    }

    // ThreadEmoji 삭제
    @Override
    public String deleteThreadEmoji(String emojiType, User user, Long threadId) {
        Thread thread = threadService.getThread(threadId);
        Optional<ThreadEmoji> optionalThreadEmoji = threadEmojiRepository.findByEmojiTypeAndThreadAndUser(EmojiType.valueOf(emojiType), thread, user);
        if (optionalThreadEmoji.isEmpty()) {
            throw new IllegalArgumentException("해당 이모지 반응을 찾을 수 없습니다.");
        }
        threadEmojiRepository.delete(optionalThreadEmoji.get());
        return "Success";
    }
}
