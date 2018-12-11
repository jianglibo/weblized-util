package com.go2wheel.weblizedutil.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.go2wheel.weblizedutil.controller.ControllerBase;
import com.go2wheel.weblizedutil.model.KeyValue;
import com.go2wheel.weblizedutil.service.KeyValueDbService;
import com.go2wheel.weblizedutil.value.KeyValueProperties;
import com.go2wheel.weblizedutil.yml.YamlInstance;

@Component
public class MainMenuGroups implements ApplicationContextAware, Cloneable {

	private ApplicationContext applicationContext;

	private Logger logger = LoggerFactory.getLogger(getClass());

	List<MainMenuGroup> groups = new ArrayList<>();

	private boolean cloned = false;

	@Autowired
	private KeyValueDbService keyValueDbService;

	public List<MainMenuGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<MainMenuGroup> groups) {
		this.groups = groups;
	}

	public MainMenuGroups clone(Collection<? extends GrantedAuthority> gas) {
		MainMenuGroups mgps = new MainMenuGroups();
		mgps.cloned = true;
		List<MainMenuGroup> clonedGroups = getGroups().stream().map(g -> g.customizeFromOrigin(gas))
				.collect(Collectors.toList());
		Collections.sort(clonedGroups);
		mgps.setGroups(clonedGroups);
		return mgps;
	}

	public List<MainMenuItem> getMenuItems() {
		return getGroups().stream().flatMap(g -> g.getItems().stream()).collect(Collectors.toList());
	}

	public MainMenuGroups prepare(String currentUri) {
		Assert.isTrue(cloned, "only cloned groups could be used.");
		getGroups().stream().flatMap(g -> g.getItems().stream()).forEach(mi -> mi.alterState(currentUri));
		
		for (int i = 1; i < getGroups().size(); i++) {
			MainMenuGroup mg = getGroups().get(i);
			if (mg.getItems().size() > 0) {
				mg.getItems().get(0).setGroupFirst(true);
			}
		}
		return this;
	}

	/**
	 * we get menuitem from the application.properties file, at same time merge the
	 * defines from controller, which in controller has higher priority.
	 */
	@PostConstruct
	public void after() {
		// add groupName to menu-item in application.properties file.
		// menus.groups[0].name=g2
		// menus.groups[0].order=1000
		// menus.groups[0].items[0].name=menu.home
		// menus.groups[0].items[0].order=1
		// menus.groups[0].items[0].path=/

		List<KeyValue> groupsIndb = keyValueDbService.findManyByKeyPrefix("menus");

		List<KeyValueProperties> kvl = new KeyValueProperties(groupsIndb, "menus").getListOfKVP("groups");
		
		if (kvl.isEmpty()) {
			try {
				KeyValue kv = new  KeyValue("menus.groups[0].name", "fixedgroup");
				keyValueDbService.save(kv);
				kv = new  KeyValue("menus.groups[0].order", "1000");
				keyValueDbService.save(kv);
				kv = new  KeyValue("menus.groups[0].items[0].name", "menu.blog");
				keyValueDbService.save(kv);
				kv = new  KeyValue("menus.groups[0].items[0].order", "1");
				keyValueDbService.save(kv);
				kv = new  KeyValue("menus.groups[0].items[0].path", "/blog");
				keyValueDbService.save(kv);
			} catch (Exception e) {	}
			groupsIndb = keyValueDbService.findManyByKeyPrefix("menus");
			kvl = new KeyValueProperties(groupsIndb, "menus").getListOfKVP("groups");
		}
		

		this.groups = kvl.stream().map(kvp -> {
			MainMenuGroup mg = new MainMenuGroup();
			mg.setName(kvp.getProperty("name"));
			List<KeyValueProperties> mikvl = kvp.getListOfKVP("items");
			
			List<MainMenuItem> mis = mikvl.stream().map(mikvp -> {
				MainMenuItemImpl mi = new MainMenuItemImpl();
				mi.setName(mikvp.getProperty("name"));
				mi.setOrder(mikvp.getInteger("order"));
				mi.setPath(mikvp.getProperty("path"));
				mi.setGroupName(mg.getName());
				return mi;
			}).collect(Collectors.toList());
			mg.setOrder(kvp.getInteger("order"));
			mg.setItems(mis);
			return mg;
		}).collect(Collectors.toList());

		getGroups().forEach(g -> {
			g.getItems().forEach(it -> {
				it.setGroupName(g.getName());
				if (!it.getName().startsWith("menu.")) {
					it.setName("menu." + it.getName());
				}
			});
		});

		Map<String, ? extends ControllerBase> cbs = applicationContext.getBeansOfType(ControllerBase.class);
		cbs.values().stream().map(cb -> cb.getMenuItem()).filter(Objects::nonNull)
				.map(mi -> {
					if (!mi.getName().startsWith("menu.")) {
						mi.setName("menu." + mi.getName());
					}
					return mi;
				}).forEach(mi -> {
					Optional<MainMenuGroup> mgOp = groups.stream().filter(gp -> mi.getGroupName().equals(gp.getName()))
							.findFirst();
					if (!mgOp.isPresent()) {
						MainMenuGroup mg = new MainMenuGroup(mi.getGroupName());
						groups.add(mg);
						mgOp = Optional.of(mg);
					}
					mgOp.get().getItems().add(mi);
				});

		// remove duplication items.
		getGroups().forEach(g -> {
			Map<String, List<MainMenuItem>> m = g.getItems().stream().collect(Collectors.groupingBy(mi -> {
				return mi.getName();
			}));

			List<MainMenuItem> lm = m.values().stream().map(list -> list.get(0)).collect(Collectors.toList());
			Collections.sort(lm);
			g.setItems(lm);
		});
		logger.info(YamlInstance.INSTANCE.yaml.dumpAsMap(groups));
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
