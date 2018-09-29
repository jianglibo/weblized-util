package com.go2wheel.weblizedutil.controller;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.go2wheel.weblizedutil.ui.MainMenuItem;
import com.go2wheel.weblizedutil.ui.MainMenuItemImpl;


@Controller
@RequestMapping(HelpController.MAPPING_PATH)
public class HelpController extends ControllerBase {
	
	public static final String MAPPING_PATH = "/app/help";
	
	private Pattern editptn = Pattern.compile("^(.*)/\\d+/edit$");
	
	
	public HelpController() {
		super(MAPPING_PATH);
	}
	
	@GetMapping("")
	public String getHelp(@RequestParam String path, Model model, HttpServletRequest request) throws Exception {
		Matcher m = editptn.matcher(path);
		if (m.matches()) {
			path = m.group(1) + "/create";
		}
		if ("/".equals(path)) {
			path = "/index.html";
		}
		String tpl = "help" + path;
		
		String tpllc = tpl + "_" + LocaleContextHolder.getLocale().getLanguage();
		
		Resource rc = applicationContext.getResource("classpath:templates/" + tpllc + ".html");
		if (rc.exists()) {
			return tpllc;
		} else {
			rc = applicationContext.getResource("classpath:templates/" + tpl + ".html");
			if (rc.exists()) {
				return tpl;
			} else {
				model.addAttribute("uri", path);
				return "help/nohelp";
			}
		}
	}
	

	@Override
	public MainMenuItem getMenuItem() {
		return new HelpMenuItem("help", "help", MAPPING_PATH, 9999);
	}
	
	public static class HelpMenuItem extends MainMenuItemImpl {
		
		private String basePath;
		
		public HelpMenuItem(String groupName, String name, String path, int order) {
			super(groupName, name, path, order);
			this.basePath = path;
		}
		
		public HelpMenuItem() {
		}

		@Override
		public HelpMenuItem customizeFromOrigin(Collection<? extends GrantedAuthority> gas) {
			HelpMenuItem mi = new HelpMenuItem();
			mi.setName(getName());
			mi.setPath(getPath());
			mi.setOrder(getOrder());
			mi.setBasePath(getBasePath());
			return mi;
		}
		
		@Override
		public void alterState(String currentUrl) {
			super.alterState(currentUrl);
			String s = basePath + "?path=" + currentUrl; 
			setPath(s);
		}

		public String getBasePath() {
			return basePath;
		}

		public void setBasePath(String basePath) {
			this.basePath = basePath;
		}
	}

}
