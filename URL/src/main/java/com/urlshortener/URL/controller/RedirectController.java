package com.urlshortener.URL.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.urlshortener.URL.entity.Link;
import com.urlshortener.URL.service.LinkService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@CrossOrigin("*")
public class RedirectController {
	private final LinkService linkService ;

	public RedirectController(LinkService linkService) {
		super();
		this.linkService = linkService;
	}

	@GetMapping("/{slug}")
	public void redirect(@PathVariable String slug, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		var opt = linkService.findBySlug(slug);
		if (opt.isEmpty()) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		Link link = opt.get();

		String referrer = req.getHeader("Referer");
		String ua = req.getHeader("User-Agent");
		String ip = req.getRemoteAddr();
		linkService.recordClick(link, referrer, ua, ip);

		resp.setHeader("Location", link.getTarget());
		resp.setStatus(HttpServletResponse.SC_FOUND); // 302
	}
}