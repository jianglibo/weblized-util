package com.go2wheel.weblizedutil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.go2wheel.weblizedutil.controller.ControllerBase;
import com.go2wheel.weblizedutil.exception.UnExpectedInputException;
import com.go2wheel.weblizedutil.ui.MainMenuGroups;
import com.go2wheel.weblizedutil.ui.MainMenuItem;
import com.go2wheel.weblizedutil.util.ExceptionUtil;
import com.jcraft.jsch.JSchException;

@ControllerAdvice(assignableTypes = {ControllerBase.class})
public class ExceptionAdvice {
	
	private Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);
	
	@Autowired
	private MainMenuGroups menuGroups;
	
	@ExceptionHandler(JSchException.class)
	public String exception(JSchException e, Model model) {
		ExceptionUtil.logErrorException(logger, e);
		model.addAttribute("exp", e);
		if (e.getMessage() != null && e.getMessage().contains("UnknownHostKey")) {
			model.addAttribute("extra", "如果是UnknownHostKey错误， 请尝试执行'ssh-keyscan -H -t rsa 目标服务器地址 >> .ssh/idsra'");
		}
		return "error-jsch";
	}
	
	@ExceptionHandler(UnExpectedInputException.class)
	public String unExpectedInputException(UnExpectedInputException e, Model model) {
		ExceptionUtil.logErrorException(logger, e);
		model.addAttribute("exp", e);
		return "error-unexpectedinput";
	}
	
	@ExceptionHandler
	public String exceptionHandler(Exception e, Model model, HttpServletRequest request) {
		ExceptionUtil.logErrorException(logger, e);
		populateMainMenu(model, request);
		model.addAttribute("exception",e);
		model.addAttribute("stacktrace", ExceptionUtil.stackTraceToString(e));
		return "error-exception";
	}
	
	private void populateMainMenu(Model model, HttpServletRequest request) {
		List<MainMenuItem> items = menuGroups.clone(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).prepare(request.getRequestURI()).getMenuItems();
		model.addAttribute("menus", items);
	}

}
