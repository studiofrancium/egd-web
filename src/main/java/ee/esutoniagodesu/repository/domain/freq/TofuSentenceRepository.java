package ee.esutoniagodesu.repository.domain.freq;

import ee.esutoniagodesu.domain.core.table.TofuSentence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the Reading entity.
 */
public interface TofuSentenceRepository extends JpaRepository<TofuSentence, Integer> {
    @Query(value = "select a from TofuSentence a ORDER BY a.id asc")
    Page<TofuSentence> findAll(Pageable pageable);
}
