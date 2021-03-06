package com.go2wheel.weblizedutil.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.atteo.evo.inflector.English;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.go2wheel.weblizedutil.model.BaseModel;
import com.go2wheel.weblizedutil.model.KeyValue;
import com.go2wheel.weblizedutil.service.DbServiceBase;
import com.go2wheel.weblizedutil.ui.MainMenuItem;
import com.go2wheel.weblizedutil.ui.MainMenuItemImpl;
import com.go2wheel.weblizedutil.util.ExceptionUtil;
import com.go2wheel.weblizedutil.value.CommonMessageKeys;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.base.Splitter;

public abstract class CRUDController<T extends BaseModel, D extends DbServiceBase<?, T>> extends ControllerBase {
	
	private final Class<T> clazz;
	
	private final String lowerHyphenPlural;
	
	private final D dbService;
	
	private Converter<String, String> cf = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN);
	
	
	public CRUDController(Class<T> clazz,D dbService, String mappingUrl) {
		super(mappingUrl);
		this.clazz = clazz;
		this.dbService = dbService;
		this.lowerHyphenPlural = English.plural(cf.convert(clazz.getSimpleName()));
		Assert.isTrue(mappingUrl.endsWith(lowerHyphenPlural), "requestmapping url should match classname.");
	}
	
	abstract boolean copyProperties(T entityFromForm, T entityFromDb);
	
	protected void commonAttribute(Model model) {
		model.addAttribute("entityName", clazz.getName());
	}
	
	protected String getLowerHyphenClassName(Class<?> clz) {
		return cf.convert(clz.getName());
	}
	
	@GetMapping("")
	String getListPage(Model model, HttpServletRequest httpRequest) {
		model.addAttribute(LIST_OB_NAME, getItemList(httpRequest));
		commonAttribute(model);
		listExtraAttributes(model);
		return getListTpl();
	}
	
	@GetMapping("/create")
	String getCreate(Model model, HttpServletRequest httpRequest ) {
		model.addAttribute(OB_NAME, newModel());
		model.addAttribute("editting", false);
		commonAttribute(model);
		formAttribute(model);
		return getFormTpl();
	}
	
	/**
	 * If annotate Mapping in this super class, request will fail. 
	 * @param request
	 * @param ras
	 * @return
	 */
	String delete(HttpServletRequest request, RedirectAttributes ras) {
		String ids = request.getParameter("ids");
		Integer[] intIds =  Splitter.on(',').trimResults().omitEmptyStrings().splitToList(ids).stream().map(Integer::parseInt).toArray(size -> new Integer[size]);
		List<T> entities = dbService.findByIds(intIds);
		ras.addFlashAttribute("deleteResult", deleteEntities(entities, false));
		return redirectListingUrl(request);
	}
	
	@DeleteMapping("/{id}")
	String deleteOne(@PathVariable Integer id, HttpServletRequest request, RedirectAttributes ras) {
		List<T> entities = dbService.findByIds(new Integer[] {id});
		ras.addFlashAttribute("deleteResult", deleteEntities(entities, false));
		return redirectListingUrl(request);
	}

	protected String deleteEntities(List<T> entities, boolean execute) {
		if (execute) {
			entities.forEach(en -> dbService.delete(en));
			return CommonMessageKeys.MISSION_ACCOMPLISHED;
		} else {
			return CommonMessageKeys.FUNCTION_NOT_IMPLEMENTED;
		}
	}

	@PostMapping("/create")
	String postCreate(@Validated @ModelAttribute(OB_NAME) T entityFromForm, final BindingResult bindingResult,Model model, HttpServletRequest request, RedirectAttributes ras) {
	    if (bindingResult.hasErrors()) {
	    	commonAttribute(model);
	    	formAttribute(model);
	    	model.addAttribute("editting", false);
	        return getFormTpl();
		}
		try {
			entityFromForm = save(entityFromForm);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException) {
				DuplicateKeyException de = (DuplicateKeyException) e;
				parseDuplicateKeyException(de, ExceptionUtil.parseDuplicateException(de), bindingResult);
			} else {
				bindingResult.addError(new ObjectError(OB_NAME, getLowerHyphenClassName(e.getClass())));
			}
	    	commonAttribute(model);
	    	formAttribute(model);
	        return getFormTpl();
		}
	    ras.addFlashAttribute("formProcessSuccessed", "已创建");
	    return afterCreate(entityFromForm, request);
	}

	protected String afterCreate(T savedEntity, HttpServletRequest request) {
	    return redirectListingUrl(request);
	}

	protected void parseDuplicateKeyException(DuplicateKeyException de, String unique, BindingResult bindingResult) {
		bindingResult.addError(new ObjectError(clazz.getSimpleName(), cf.convert(de.getClass().getName())));
	}

	protected abstract void formAttribute(Model model);
	protected abstract void listExtraAttributes(Model model);

	@GetMapping("/{id}/edit")
	String getEdit(@PathVariable(name="id") T entityFromDb, Model model) {
		model.addAttribute(OB_NAME, entityFromDb);
		return getEditNoObName(model);
	}
	
	protected String getEditNoObName(Model model) {
		model.addAttribute("editing", true);
		commonAttribute(model);
		formAttribute(model);
		return getFormTpl();
	}
	

	@PutMapping("/{id}/edit")
	String putEdit(@Validated @ModelAttribute(OB_NAME) T entityFromForm,  final BindingResult bindingResult, @PathVariable(name="id") T entityFromDb,Model model, HttpServletRequest request, RedirectAttributes ras) {
		if (bindingResult.hasErrors()) {
			return getEditNoObName(model);
		}
		if (copyProperties(entityFromForm, entityFromDb)) {
			save(entityFromDb);
		}
        ras.addFlashAttribute("formProcessSuccessed", "已保存");
        return afterEdit(request, ras);
        
	}

	protected String afterEdit(HttpServletRequest request, RedirectAttributes ras) {
		return redirectListingUrl(request);
	}

	/**
	 * append parameters to mapping url.
	 * @param request
	 * @param keyValues
	 * @return
	 */
	public String redirectListingUrl(HttpServletRequest request, KeyValue...keyValues) {
		ServletUriComponentsBuilder ucb = ServletUriComponentsBuilder.fromRequest(request);
		for(KeyValue kv : keyValues) {
			ucb.queryParam(kv.getItemKey(), kv.getItemValue());
		}
		String uri = ucb.replacePath(getListingUrl()).build().toUriString();
		return "redirect:" + uri;
	}
	
	protected String redirectEditUrl(Integer id) {
		return "redirect:" + getListingUrl() + "/" + id + "/edit";
	}

	protected String redirectEditUrl() {
		return "redirect:" + getListingUrl() + "/{id}/edit";
	}
	
	@Override
	public MainMenuItem getMenuItem() {
		return new MainMenuItemImpl("appmodel", getLowerHyphenPlural(), getListingUrl(), getMenuOrder());
	}
	
	protected int getMenuOrder() {
		return 100000;
	}
	
	public List<T> getItemList(HttpServletRequest request) {
		return dbService.findAllSortByCreatedAtDesc();
	}
	
	public T save(T entity) {
		return dbService.save(entity);
	}
	
	public abstract T newModel();
	
	public String getFormTpl() {
		return lowerHyphenPlural + "-form";
	}
	
	public String getListTpl() {
		return lowerHyphenPlural + "-list";
	}

	public String getLowerHyphenPlural() {
		return lowerHyphenPlural;
	}

	public D getDbService() {
		return dbService;
	}
}

//public abstract class CRUDController<T extends BaseModel, D extends DbServiceBase<?, T>> extends ControllerBase {
//	
//	private final Class<T> clazz;
//	
//	private final String lowerHyphenPlural;
//	
//	private final D dbService;
//	
//	private Converter<String, String> cf = CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_HYPHEN);
//	
//	
//	public CRUDController(Class<T> clazz,D dbService, String mappingUrl) {
//		super(mappingUrl);
//		this.clazz = clazz;
//		this.dbService = dbService;
//		this.lowerHyphenPlural = English.plural(cf.convert(clazz.getSimpleName()));
//		Assert.isTrue(mappingUrl.endsWith(lowerHyphenPlural), "requestmapping url should match classname.");
//	}
//	
//	abstract boolean copyProperties(T entityFromForm, T entityFromDb);
//	
//	protected void commonAttribute(Model model) {
//		model.addAttribute("entityName", clazz.getName());
//	}
//	
//	protected String getLowerHyphenClassName(Class<?> clz) {
//		return cf.convert(clz.getName());
//	}
//	
//	@GetMapping("")
//	String getListPage(Model model, HttpServletRequest httpRequest) {
//		model.addAttribute(LIST_OB_NAME, getItemList());
//		commonAttribute(model);
//		listExtraAttributes(model);
//		return getListTpl();
//	}
//	
//	@GetMapping("/create")
//	String getCreate(Model model, HttpServletRequest httpRequest ) {
//		model.addAttribute(OB_NAME, newModel());
//		model.addAttribute("editting", false);
//		commonAttribute(model);
//		formAttribute(model);
//		return getFormTpl();
//	}
//	
//	@DeleteMapping
//	String delete(HttpServletRequest request, RedirectAttributes ras) {
//		String ids = request.getParameter("ids");
//		Integer[] intIds =  Splitter.on(',').trimResults().omitEmptyStrings().splitToList(ids).stream().map(Integer::parseInt).toArray(size -> new Integer[size]);
//		List<T> entities = dbService.findByIds(intIds);
//		ras.addFlashAttribute("deleteResult", deleteEntities(entities, false));
//		return redirectMappingUrl();
//	}
//	
//
//	protected String deleteEntities(List<T> entities, boolean execute) {
//		if (execute) {
//			entities.forEach(en -> dbService.delete(en));
//			return CommonMessageKeys.MISSION_ACCOMPLISHED;
//		} else {
//			return CommonMessageKeys.FUNCTION_NOT_IMPLEMENTED;
//		}
//	}
//
//	@PostMapping("/create")
//	String postCreate(@Validated @ModelAttribute(OB_NAME) T entityFromForm, final BindingResult bindingResult,Model model, RedirectAttributes ras) {
//	    if (bindingResult.hasErrors()) {
//	    	commonAttribute(model);
//	    	formAttribute(model);
//	    	model.addAttribute("editting", false);
//	        return getFormTpl();
//		}
//		try {
//			entityFromForm = save(entityFromForm);
//		} catch (Exception e) {
//			if (e instanceof DuplicateKeyException) {
//				DuplicateKeyException de = (DuplicateKeyException) e;
//				parseDuplicateKeyException(de, ExceptionUtil.parseDuplicateException(de), bindingResult);
//			} else {
//				bindingResult.addError(new ObjectError(OB_NAME, getLowerHyphenClassName(e.getClass())));
//			}
//	    	commonAttribute(model);
//	    	formAttribute(model);
//	        return getFormTpl();
//		}
//	    ras.addFlashAttribute("formProcessSuccessed", true);
//	    return afterCreate(entityFromForm);
//	}
//
//	protected String afterCreate(T entityFromForm) {
//	    return redirectMappingUrl();
//	}
//
//	protected void parseDuplicateKeyException(DuplicateKeyException de, String unique, BindingResult bindingResult) {
//		bindingResult.addError(new ObjectError(clazz.getSimpleName(), cf.convert(de.getClass().getName())));
//	}
//
//	protected abstract void formAttribute(Model model);
//	protected abstract void listExtraAttributes(Model model);
//
//	@GetMapping("/{id}/edit")
//	String getEdit(@PathVariable(name="id") T entityFromDb, Model model) {
//		model.addAttribute(OB_NAME, entityFromDb);
//		model.addAttribute("editing", true);
//		commonAttribute(model);
//		formAttribute(model);
//		return getFormTpl();
//	}
//
//
//	@PutMapping("/{id}/edit")
//	String putEdit(@Validated @ModelAttribute(OB_NAME) T entityFromForm, @PathVariable(name="id") T entityFromDb,  final BindingResult bindingResult,Model model, RedirectAttributes ras) {
//		if (bindingResult.hasErrors()) {
//			formAttribute(model);
//			return getFormTpl();
//		}
//		if (copyProperties(entityFromForm, entityFromDb)) {
//			save(entityFromDb);
//		}
//        ras.addFlashAttribute("formProcessSuccessed", true);
//	    return redirectMappingUrl();
//	}
//	
//	public String redirectMappingUrl() {
//		return "redirect:" + getMappingUrl();
//	}
//	
//	protected String redirectEditGet(Integer id) {
//		return "redirect:" + getMappingUrl() + "/" + id + "/edit";
//	}
//
//	
//	@Override
//	public MainMenuItem getMenuItem() {
//		return new MainMenuItemImpl("appmodel", getLowerHyphenPlural(), getMappingUrl(), getMenuOrder());
//	}
//	
//	protected int getMenuOrder() {
//		return 100000;
//	}
//	
//	public List<T> getItemList() {
//		return dbService.findAllSortByCreatedAtDesc();
//	}
//	
//	public T save(T entity) {
//		return dbService.save(entity);
//	}
//	
//	public abstract T newModel();
//	
//	public String getFormTpl() {
//		return lowerHyphenPlural + "-form";
//	}
//	
//	public String getListTpl() {
//		return lowerHyphenPlural + "-list";
//	}
//
//	public String getLowerHyphenPlural() {
//		return lowerHyphenPlural;
//	}
//
//	public D getDbService() {
//		return dbService;
//	}
//}
