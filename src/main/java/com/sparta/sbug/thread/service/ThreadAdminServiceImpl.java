package com.sparta.sbug.thread.service;

import com.sparta.sbug.thread.repository.ThreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ThreadAdminServiceImpl implements ThreadAdminService{
    private final ThreadRepository threadRepository;

    @Override
    public void disableThread(Long threadId) {
        threadRepository.findById(threadId).orElseThrow(
                () -> new IllegalArgumentException("찾는 쓰레드가 없습니다.")
        );
        // 셀렉트 쿼리가 필요한가? disable쪽에서 낼텐데...
        // 그러나 어차피 영속성 컨텍스트로 관리되기 때문에(1차캐시) 두번 해보자.
        threadRepository.disableThreadById(threadId);
    }
    @Override
    public void disableThreadsByChannelId(Long channelId){
        threadRepository.disableThreadByChannelId(channelId);
    }
}
