package com.sparta.sbug.thread.dto;


import lombok.Getter;
import lombok.Setter;


@Getter
public class ImageResponseDto {
    @Setter
    private String imageFileUrl;
    public static ImageResponseDto of(){return new ImageResponseDto();}

}
