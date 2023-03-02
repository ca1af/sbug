package com.sparta.sbug.common.paging;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


/**
 * 페이징 DTO
 */
//lombok
@Builder
@Getter
@Setter
public class PageDto {
    private int currentPage;
    private int size;
    private String sortBy;
    private String order;

    /**
     * 생성자
     *
     * @param currentPage 현재 페이지 (default = 1)
     * @param size        한 페이지의 사이즈 (default = 5)
     * @param sortBy      정렬 기준 (default = "createdAt")
     */
    public PageDto(int currentPage, int size, String sortBy, String order) {
        if (currentPage <= 0) {
            currentPage = 1;
        }

        if (size <= 0) {
            size = 5;
        }

        if (sortBy == null) {
            sortBy = "createdAt";
        }

        if (order == null) {
            order = "asc";
        }

        this.currentPage = currentPage;
        this.size = size;
        this.sortBy = sortBy;
        this.order = order;
    }

    public PageDto() {
        this.currentPage = 1;
        this.size = 5;
        this.sortBy = "createdAt";
        this.order = "asc";
    }

    /**
     * 나타낼 페이지, 한 페이지에서의 크기(size), 정렬의 기준이 될 속성(sortBy)으로
     * PageRequest(Pageable 인터페이스의 구현체)를 만듭니다.
     *
     * @return Pageable
     */
    public Pageable toPageable() {
        // 페이지는 index:0부터 시작하기 때문에 값에서 -1을 해주면서 넣습니다.
        if (order.equals("desc")) {
            return PageRequest.of(currentPage - 1, size, Sort.by(sortBy).descending());
        } else {
            return PageRequest.of(currentPage - 1, size, Sort.by(sortBy).ascending());
        }
    }
}
