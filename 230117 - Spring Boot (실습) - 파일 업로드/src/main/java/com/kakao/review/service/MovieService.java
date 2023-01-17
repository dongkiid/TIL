package com.kakao.review.service;

import com.kakao.review.domain.Movie;
import com.kakao.review.domain.MovieImage;
import com.kakao.review.dto.MovieDTO;
import com.kakao.review.dto.MovieImageDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {
    //데이터 삽입을 위한 메서드
    Long register(MovieDTO movieDTO);

    //DTO를 Entity로 변환
    //하나의 Entity 가 아니라 Movie 와 MovieImage로 변환이 되어야 해서
    //Map으로 리턴
    default Map<String, Object> dtoToEntity(MovieDTO movieDTO){
        Map<String, Object> entityMap = new HashMap<>();

        Movie movie = Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .build();
        entityMap.put("movie", movie);

        //MovieImageDTO 의 List
        List<MovieImageDTO> imageDTOList =
                movieDTO.getImageDTOList();

        //MovieImageDTO 의 List를 MovieImage Entity의 List로 변환
        if(imageDTOList != null && imageDTOList.size() > 0){
            List<MovieImage> movieImageList =
                    imageDTOList.stream()
                            .map(movieImageDTO -> {
                                MovieImage movieImage =
                                        MovieImage.builder()
                                        .path(movieImageDTO.getPath())
                                        .imgName(movieImageDTO.getImgName())
                                        .uuid(movieImageDTO.getUuid())
                                        .movie(movie)
                                        .build();
                                return movieImage;
                            }).collect(Collectors.toList());
            entityMap.put("imgList", movieImageList);
        }

        return entityMap;
    }
}
