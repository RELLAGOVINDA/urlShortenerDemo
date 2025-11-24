package com.urlshortener.URL.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.urlshortener.URL.entity.Click;

public interface ClickRepository extends JpaRepository<Click, Long> {
	List<Click> findByLinkId(Long linkId);
}