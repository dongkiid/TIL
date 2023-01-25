package com.example.board.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer") //toString을 만들 때 writer의 toString 호출 안함
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    private String title;
    private String content;


    @ManyToOne(fetch = FetchType.LAZY)
    //처음에는 가져오지 ㅇ낳고 사용할 때 가져온다.
    private Member writer;

    //Entity에는 setter를 만들지 않기 때문에
    // 수정하는 메서드를 따로 만들어 줘야 한다.

    // 타이틀을 수정하는 메서드
    public void changeTitle(String title){
        //조건을 달아 유효성검사를 할 수 있다.
        if(title == null || title.trim().length() == 0){
            this.title =" 냉무 ";
            return;
        }
        //소문자로만 넣고 싶을 경우
        this.title = title.toLowerCase();
    }

    //내용을 수정하는 메서드
    public void changeContent(String content){
        this.content = content;
    }


}
