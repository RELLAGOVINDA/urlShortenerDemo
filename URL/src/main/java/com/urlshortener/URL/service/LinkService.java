package com.urlshortener.URL.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.urlshortener.URL.entity.Click;
import com.urlshortener.URL.entity.Link;
import com.urlshortener.URL.repository.ClickRepository;
import com.urlshortener.URL.repository.LinkRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LinkService {
	private final LinkRepository linkRepository;
	private final ClickRepository clickRepository;

	

	public LinkService(LinkRepository linkRepository, ClickRepository clickRepository) {
		super();
		this.linkRepository = linkRepository;
		this.clickRepository = clickRepository;
	}

	public Link create(String target, String customSlug, String title) {
		String slug = (customSlug != null && !customSlug.isBlank()) ? customSlug : generateSlug();
		if (linkRepository.existsBySlug(slug))
			throw new IllegalArgumentException("Slug already used");

		Link link = new Link();
		link.setSlug(slug);
		link.setTarget(target);
		link.setTitle(title);
		return linkRepository.save(link);
	}

	public Optional<Link> findBySlug(String slug) {
		return linkRepository.findBySlug(slug);
	}

	@Transactional
	public void recordClick(Link link, String referrer, String ua, String ip) {
		Click c = new Click();
		c.setLink(link);
		c.setReferrer(referrer);
		c.setUserAgent(ua);
		c.setIp(ip);
		clickRepository.save(c);
		link.setClicks(link.getClicks() + 1);
		linkRepository.save(link);
	}

	@SuppressWarnings("deprecation")
	private String generateSlug() {
		String slug;
		do {
			slug = RandomStringUtils.randomAlphanumeric(7).toLowerCase();
		} while (linkRepository.existsBySlug(slug));
		return slug;
	}

	public Page<Link> listLinks(Pageable pageable) {
		return linkRepository.findAll(pageable);
	}

	public List<Click> getClicksForLink(Long linkId) {
		return clickRepository.findByLinkId(linkId);
	}
}
