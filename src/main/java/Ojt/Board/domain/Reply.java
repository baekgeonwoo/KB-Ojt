package Ojt.Board.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @NotBlank(message = "댓글 내용은 필수 입니다.")
    private String rep;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    private String writerEmail;

    @Builder
    public Reply( String rep, Board board, String email) {
        this.rep = rep;
        this.board = board;
        this.writerEmail = email;
    }

}
