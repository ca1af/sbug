package com.sparta.sbug.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


//lombok
@Builder
@Getter
@Setter
public class PageDto {
    private int currentPage;
    private int size;
    private String sortBy;

    public PageDto(int currentPage, int size, String sortBy) {
        if (currentPage <= 0) {
            currentPage = 1;
        }

        if (size <= 0) {
            size = 5;
        }

        if (sortBy == null) {
            sortBy = "createdAt";
        }

        this.currentPage = currentPage;
        this.size = size;
        this.sortBy = sortBy;
    }

    /**
     * 나타낼 페이지, 한 페이지에서의 크기(size), 정렬의 기준이 될 속성(sortBy)으로
     * PageRequest(Pageable 인터페이스의 구현체)를 만듭니다.
     */
    public Pageable toPageable() {
        // 페이지는 index:0부터 시작하기 때문에 값에서 -1을 해주면서 넣습니다.
        return PageRequest.of(currentPage - 1, size, Sort.by(sortBy).descending());
    }
}
