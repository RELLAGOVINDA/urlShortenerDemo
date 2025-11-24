package com.urlshortener.URL.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.urlshortener.URL.dto.CreateLinkRequest;
import com.urlshortener.URL.entity.Link;
import com.urlshortener.URL.service.LinkService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/links")
@CrossOrigin("*")
public class LinkController {
	private final LinkService linkService ;
	

	public LinkController(LinkService linkService) {
		super();
		this.linkService = linkService;
	}

	@PostMapping
	public ResponseEntity<Link> create(@RequestBody CreateLinkRequest req) {
		Link link = linkService.create(req.getTarget(), req.getSlug(), req.getTitle());
		return ResponseEntity.status(HttpStatus.CREATED).body(link);
	}

	@GetMapping
	public Page<Link> list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
		return linkService.listLinks(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
	}

	@GetMapping("/{slug}/stats")
	public ResponseEntity<?> stats(@PathVariable String slug) {
		var link = linkService.findBySlug(slug).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		var clicks = linkService.getClicksForLink(link.getId());
		Map<String, Object> res = Map.of("link", link, "clicks", clicks);
		return ResponseEntity.ok(res);
	}
}