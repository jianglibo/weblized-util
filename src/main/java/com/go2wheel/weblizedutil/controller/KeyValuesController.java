package com.go2wheel.weblizedutil.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.go2wheel.weblizedutil.model.KeyValue;
import com.go2wheel.weblizedutil.service.KeyValueDbService;
import com.go2wheel.weblizedutil.ui.MainMenuItem;


@Controller
@RequestMapping(KeyValuesController.MAPPING_PATH)
public class KeyValuesController  extends CRUDController<KeyValue, KeyValueDbService> {
	
	public static final String MAPPING_PATH = "/app/key-values";
	
	@Autowired
	public KeyValuesController(KeyValueDbService dbService) {
		super(KeyValue.class, dbService, MAPPING_PATH);
	}

	@Override
	boolean copyProperties(KeyValue entityFromForm, KeyValue entityFromDb) {
		entityFromDb.setItemKey(entityFromForm.getItemKey());
		entityFromDb.setItemValue(entityFromForm.getItemValue());
		return true;
	}

	@Override
	public KeyValue newModel() {
		return new KeyValue();
	}
	
	
	@Override
	protected String deleteEntities(List<KeyValue> entities, boolean execute) {
		return super.deleteEntities(entities, true);
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
	
	protected int getMenuOrder() {
		return 1000;
	}
}
