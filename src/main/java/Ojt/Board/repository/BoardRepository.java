package Ojt.Board.repository;

import Ojt.Board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAll();

    Page<Board> findByTitleLike(String keyword, Pageable pageable);
    Optional<Board> findById(Long id);

    void deleteById(Long id);


}
