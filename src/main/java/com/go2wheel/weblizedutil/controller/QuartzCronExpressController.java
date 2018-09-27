package com.go2wheel.weblizedutil.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.go2wheel.weblizedutil.service.QuartzCronBuilderService;
import com.go2wheel.weblizedutil.ui.MainMenuItem;
import com.go2wheel.weblizedutil.value.AjaxDataResult;
import com.go2wheel.weblizedutil.value.AjaxErrorResult;
import com.go2wheel.weblizedutil.value.AjaxResult;

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
		model.addAttribute("buildCtx", quartzCronBuilderService.getContext(LocaleContextHolder.getLocale()));
		return "quartz-cron-builder";
	}
	
	@GetMapping("/next")
	public ResponseEntity<AjaxResult> nextExecuteTime(@RequestParam Long ms, @RequestParam String cron) {
		CronExpression ce;
		try {
			ce = new CronExpression(cron);
			Date d = new Date(ms);
			Date nd = ce.getNextValidTimeAfter(d);
			return ResponseEntity.ok(new AjaxDataResult<Long>(nd.getTime()));
		} catch (ParseException e) {
			return ResponseEntity.ok(AjaxErrorResult.errorResult(e.getMessage()));
		}
	}
	public MainMenuItem getMenuItem() {
		return null;
	}

}
