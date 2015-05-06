package ee.esutoniagodesu.repository.domain.test;

import ee.esutoniagodesu.domain.test.dto.ArticleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the Article entity.
 */
public interface ArticleDTORepository extends JpaRepository<ArticleDTO, Integer> {
    @Query(value = "select a from ArticleDTO a where a.createdBy=?1 or a.shared=true ORDER BY a.id desc")
    Page<ArticleDTO> findByCreatedBy(String createdBy, Pageable pageable);
}