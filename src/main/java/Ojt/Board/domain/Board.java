package Ojt.Board.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @Lob
    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "board")
    List<Reply> replies = new ArrayList<>();

    Long cntReply = 0L;

    private String writerEmail;

    private String imgUrl;

    @Builder
    public Board(String title, String content, String writerEmail) {
        this.title = title;
        this.content = content;
        this.cntReply = 0L;
        this.writerEmail = writerEmail;
        this.imgUrl = "../images/kb1.jpg";
    }


    public void addReply(Reply reply){
        this.replies.add(reply);
        this.cntReply +=1;
    }

    public void delReply(){
        this.cntReply -= 1;
    }
}
