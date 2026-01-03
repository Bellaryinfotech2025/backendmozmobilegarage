package com.mozworld.repo;

 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.mozworld.entity.IssueEntity;

@Repository
public interface IssueRepository extends JpaRepository<IssueEntity, Long> {
	 Optional<IssueEntity> findByTrackingId(String trackingId);
}