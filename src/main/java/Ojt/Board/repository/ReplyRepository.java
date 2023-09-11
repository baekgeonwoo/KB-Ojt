package Ojt.Board.repository;

import Ojt.Board.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    void deleteById(Long id);

}
