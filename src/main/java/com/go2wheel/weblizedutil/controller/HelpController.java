package com.go2wheel.weblizedutil.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.go2wheel.weblizedutil.ui.MainMenuItemImpl;


@Controller
@RequestMapping(HelpController.MAPPING_PATH)
public class HelpController extends ControllerBase {
	
	public static final String MAPPING_PATH = "/app/help";
	
	
	public HelpController() {
		super(MAPPING_PATH);
	}
	
	@GetMapping("")
	public String getHelp(@RequestParam String path) {
		return "";
	}
	

	@Override
	public MainMenuItemImpl getMenuItem() {
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
		public HelpMenuItem clone() {
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
