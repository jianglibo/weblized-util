package com.go2wheel.weblizedutil.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public List<MainMenuItem> getMenuItems() {
		return null;
	}
	
	@Override
	protected int getMenuOrder() {
		return 800;
	}
}
