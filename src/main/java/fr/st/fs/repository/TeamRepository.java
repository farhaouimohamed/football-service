package fr.st.fs.repository;

import fr.st.fs.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Page<Team> findAll(Pageable pageable);
}
