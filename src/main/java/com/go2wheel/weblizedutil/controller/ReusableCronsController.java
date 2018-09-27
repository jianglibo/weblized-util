package com.go2wheel.weblizedutil.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.go2wheel.weblizedutil.model.ReusableCron;
import com.go2wheel.weblizedutil.service.ReusableCronDbService;
import com.go2wheel.weblizedutil.ui.MainMenuItem;


@Controller
@RequestMapping(ReusableCronsController.MAPPING_PATH)
public class ReusableCronsController extends CRUDController<ReusableCron, ReusableCronDbService> {
	
	public static final String MAPPING_PATH = "/app/reusable-crons";
	
	@Autowired
	public ReusableCronsController(ReusableCronDbService dbService) {
		super(ReusableCron.class, dbService, MAPPING_PATH);
	}
	
	@Override
	@DeleteMapping
	String delete(HttpServletRequest request, RedirectAttributes ras) {
		return super.delete(request, ras);
	}
	
	@Override
	protected String deleteEntities(List<ReusableCron> entities, boolean execute) {
			return super.deleteEntities(entities, true);
	}

	@Override
	boolean copyProperties(ReusableCron entityFromForm, ReusableCron entityFromDb) {
		entityFromDb.setExpression(entityFromForm.getExpression());
		entityFromDb.setDescription(entityFromForm.getDescription());
		return true;
	}

	@Override
	public ReusableCron newModel() {
		return new ReusableCron();
	}

	@Override
	protected void formAttribute(Model model) {
	}

	@Override
	protected void listExtraAttributes(Model model) {
	}
	
	@Override
	public MainMenuItem getMenuItem() {
		return null;
	}
	
	@Override
	protected int getMenuOrder() {
		return 800;
	}
}
