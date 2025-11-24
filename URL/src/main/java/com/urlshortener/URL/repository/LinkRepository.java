package com.urlshortener.URL.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urlshortener.URL.entity.Link;

public interface LinkRepository extends JpaRepository<Link, Long> {
	Optional<Link> findBySlug(String slug);

	boolean existsBySlug(String slug);
}
