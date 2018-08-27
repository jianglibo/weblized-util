package com.go2wheel.weblizedutil.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.go2wheel.weblizedutil.ui.MainMenuItem;
import com.go2wheel.weblizedutil.util.TplUtil;


@Controller(HomeController.MAPPING_PATH)
public class HomeController extends ControllerBase {
	

	public static final String MAPPING_PATH = "/";
	
	public HomeController() {
		super(MAPPING_PATH);
	}
	
	@ModelAttribute
	public void populateContext(Model model, HttpServletRequest request) {
		model.addAttribute("tplUtil", new TplUtil());
	}

	@GetMapping("")
	String home() {
		Authentication an = SecurityContextHolder.getContext().getAuthentication();
		an.getAuthorities();
		return "index";
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public List<MainMenuItem> getMenuItems() {
		return null;
	}

}
