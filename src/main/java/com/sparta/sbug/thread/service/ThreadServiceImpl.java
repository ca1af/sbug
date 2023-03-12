package com.sparta.sbug.thread.service;


import com.sparta.sbug.aws.service.S3Service;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.service.CommentService;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.emoji.dto.EmojiCountDto;
import com.sparta.sbug.emoji.dto.EmojiResponseDto;
import com.sparta.sbug.emoji.service.ThreadEmojiService;
import com.sparta.sbug.thread.dto.ImageResponseDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import com.sparta.sbug.thread.repository.query.ThreadSearchCond;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;


import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.Optional;


import static com.sparta.sbug.common.exceptions.ErrorCode.*;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service
public class ThreadServiceImpl implements ThreadService {

    private final ThreadRepository threadRepository;

    /**
     * 하위 레이어 데이터 서비스 - 코멘트 서비스
     */
    private final CommentService commentService;

    /**
     * 하위 레이어 데이터 서비스 - 쓰레드 이모지 서비스
     */
    private final ThreadEmojiService threadEmojiService;

    // for S3
    private final S3Service s3Service;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.credentials.access-key}")
    private String ACCESS_KEY;
    @Value("${cloud.aws.credentials.secret-key}")
    private String SECRET_KEY;

    // CRUD
    @Override
    public ThreadResponseDto createThread(Channel channel, String requestContent, User user) {
        Thread thread = Thread.builder()
                .requestContent(requestContent)
                .user(user)
                .build();
        thread.setChannel(channel);
        Thread savedThread = threadRepository.save(thread);
        return ThreadResponseDto.of(savedThread, null);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<ThreadResponseDto> getAllThreadsInChannel(Long channelId, PageDto pageDto) {
        Slice<Thread> threads = threadRepository.findThreadsByChannelIdAndInUseIsTrue(channelId, pageDto.toPageable());
        List<Long> threadIds = threads.getContent().stream().map(Thread::getId).toList();
        List<EmojiCountDto> emojiCountDtoList = threadEmojiService.getThreadsEmojiCount(threadIds);
        Map<Long, List<EmojiResponseDto>> threadEmojiCountMap = EmojiResponseDto.getEmojiCountMap(emojiCountDtoList);
        return threads.map(thread -> ThreadResponseDto.of(thread, threadEmojiCountMap));
    }

    @Override
    @Transactional(readOnly = true)
    public ThreadResponseDto getThread(Long threadId) {
        Thread thread = findThreadById(threadId);
        List<EmojiCountDto> emojiCountDtoList = threadEmojiService.getThreadEmojiCount(threadId);
        Map<Long, List<EmojiResponseDto>> threadEmojiCountMap = EmojiResponseDto.getEmojiCountMap(emojiCountDtoList);
        return ThreadResponseDto.of(thread, threadEmojiCountMap);
    }

    @Override
    @Transactional
    public void editThread(Long threadId, String requestContent, User user) {
        Thread thread = validateUserAuth(threadId, user);

        if (requestContent.trim().equals("")) {
            throw new CustomException(BAD_REQUEST_THREAD_CONTENT);
        }

        thread.updateThread(requestContent);
    }

    @Override
    @Transactional
    public void disableThread(Long threadId, User user) {
        validateUserAuth(threadId, user);
        commentService.disableCommentByThreadId(threadId);
        threadRepository.disableThreadById(threadId);
    }

    // 유저의 권한 검증
    @Override
    @Transactional
    public Thread validateUserAuth(Long threadId, User user) {
        Thread thread = findThreadById(threadId);
        if (!thread.getUser().getId().equals(user.getId())) {
            throw new CustomException(USER_THREAD_FORBIDDEN);
        }
        return thread;
    }

    // 쓰레드 데이터 조회
    @Override
    public Thread findThreadById(Long threadId) {
        Optional<Thread> optionalThread = threadRepository.findThreadByIdAndInUseIsTrue(threadId);
        if (optionalThread.isEmpty()) {
            throw new CustomException(THREAD_NOT_FOUND);
        }
        return optionalThread.get();
    }

    // 코멘트 생성
    @Override
    @Transactional
    public CommentResponseDto createComment(Long threadId, String content, User user) {
        Thread thread = findThreadById(threadId);
        return commentService.createComment(thread, content, user);
    }

    // Disable by admin //
    @Override
    public void disableThreadsByChannelId(Long channelId) {
        commentService.disableCommentByChannelId(channelId);
        threadRepository.disableThreadByChannelId(channelId);
    }

    @Override
    @Transactional
    public void disableThread(Long threadId) {
        commentService.disableCommentByThreadId(threadId);
        threadRepository.disableThreadById(threadId);
    }

    // Delete //
    @Override
    @Transactional
    @Scheduled(cron = "0 0 5 1 3,6,9,12 *")
    public void deleteThreadsOnSchedule() {
        LocalDateTime localDateTime = LocalDateTime.now().minusMonths(6);
        threadRepository.deleteThreads(localDateTime);
    }

    @Override
    public boolean reactThreadEmoji(String emojiType, User user, Long threadId) {
        Thread thread = findThreadById(threadId);
        return threadEmojiService.reactThreadEmoji(emojiType, user, thread);
    }

    @Override
    public List<ThreadResponseDto> findThreadBySearchCondition(ThreadSearchCond threadSearchCond){
        return threadRepository.findThreadBySearchCondition(threadSearchCond);
    }

    @Override
    public S3Presigner getPreSigner(){
        AwsCredentialsProvider awsCredentialsProvider;
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
        awsCredentialsProvider = StaticCredentialsProvider.create(awsBasicCredentials);

        Region region = Region.AP_NORTHEAST_2;
        return S3Presigner.builder()
                .region(region)
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }

    // 쓰레드 이미지 업로드
    @Override
    @Transactional
    public String imageUploadOnThread(Long threadId, Long channelId, String keyName, User user) {
        Thread thread = findThreadById(threadId);
        // 디렉토리 이름_ 채널에 유저 별 이미지 폴더 생성
        String dirName = "ch" + channelId + "/" + user.getId();
        // 저장경로가 포함된 객체 이름(key)
        String objKeyName = dirName + "/" + keyName; // ch1/1/test.png

        S3Presigner preSigner = getPreSigner();
        String url = s3Service.imageObjectPreSignedUrl(bucketName, objKeyName, preSigner);
        preSigner.close();

        thread.setImageFile(objKeyName);
        threadRepository.save(thread);

        return url;
    }

    @Override
    @Transactional
    public ImageResponseDto getImageOnThread(Long threadId) {
        Thread thread = findThreadById(threadId);
        ImageResponseDto responseDto = ImageResponseDto.of();

        S3Presigner preSigner = getPreSigner();
        responseDto.setImageFileUrl(s3Service.getImageObjectPreSignedUrl(bucketName, thread.getImageFile(), preSigner));

        preSigner.close();
        return responseDto;

    }
}
