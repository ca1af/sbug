package com.sparta.sbug.emoji.service;

import com.sparta.sbug.emoji.entity.ThreadEmoji;
import com.sparta.sbug.emoji.repository.ThreadEmojiRepository;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.service.ThreadServiceImpl;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ThreadEmojiServiceImpl implements ThreadEmojiService {

    private final ThreadEmojiRepository threadEmojiRepository;
    private final ThreadServiceImpl threadService;


    // ThreadEmoji 생성
    @Override
    public String createThreadEmoji(Long threadId, String emojiType, User user){
        Thread thread = threadService.getThread(threadId);
        ThreadEmoji threadEmoji = new ThreadEmoji(emojiType, user, thread);
        threadEmojiRepository.save(threadEmoji);
        return "Success";
    }

    // ThreadEmoji 삭제
    @Override
    public String deleteThreadEmoji(Long emojiId, User user) {
        ThreadEmoji threadEmoji = threadEmojiRepository.findByIdAndUser(emojiId,user).orElseThrow();
        threadEmojiRepository.delete(threadEmoji);
        return "Success";
    }


}
