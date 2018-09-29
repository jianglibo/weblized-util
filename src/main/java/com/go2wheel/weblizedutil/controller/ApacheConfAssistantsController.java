package com.go2wheel.weblizedutil.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.go2wheel.weblizedutil.service.QuartzCronBuilderService;
import com.go2wheel.weblizedutil.ui.MainMenuItem;

@Controller
@RequestMapping(ApacheConfAssistantsController.MAPPING_PATH)
public class ApacheConfAssistantsController extends ControllerBase {

	public static final String MAPPING_PATH = "/weblized/apache-conf-assistants";
	
	@Autowired
	private QuartzCronBuilderService quartzCronBuilderService;

	public ApacheConfAssistantsController() {
		super(MAPPING_PATH);
	}

	@GetMapping("")
	public String assistants(Model model, HttpServletRequest request)
			throws IOException {
		model.addAttribute("buildCtx", quartzCronBuilderService.getContext(LocaleContextHolder.getLocale()));
		return "apache-conf-assistants";
	}
	
	public MainMenuItem getMenuItem() {
		return null;
	}

}
