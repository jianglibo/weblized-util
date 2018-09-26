package com.go2wheel.weblizedutil.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.go2wheel.weblizedutil.service.QuartzCronBuilderService;
import com.go2wheel.weblizedutil.ui.MainMenuItem;

@Controller
@RequestMapping(QuartzCronExpressController.MAPPING_PATH)
public class QuartzCronExpressController extends ControllerBase {

	public static final String MAPPING_PATH = "/weblized/quartz-cron-builder";
	
	@Autowired
	private QuartzCronBuilderService quartzCronBuilderService;

	public QuartzCronExpressController() {
		super(MAPPING_PATH);
	}

	@GetMapping("")
	public String status(Model model, HttpServletRequest request)
			throws IOException {
//		model.addAttribute("cronFields", quartzCronBuilderService.getFieldDefinitions(LocaleContextHolder.getLocale()));
//		model.addAttribute("patterns", quartzCronBuilderService.getPredefinedCronPattern(LocaleContextHolder.getLocale()));
		model.addAttribute("buildCtx", quartzCronBuilderService.getContext(LocaleContextHolder.getLocale()));
		return "quartz-cron-builder";
	}
	
	@PostMapping("")
	public String clearCompleted(Model model, HttpServletRequest request)
			throws IOException {
		model.asMap().clear();
		String sid = request.getSession(true).getId();
		globalStore.clearCompleted(sid);
		return "redirect:" + request.getRequestURI();
	}


	public MainMenuItem getMenuItem() {
		return null;
	}

}
