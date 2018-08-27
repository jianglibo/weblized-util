package com.go2wheel.weblizedutil.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.go2wheel.weblizedutil.exception.ExceptionWrapper;
import com.go2wheel.weblizedutil.exception.UnexpectlyCallMethodException;
import com.go2wheel.weblizedutil.jooqschema.tables.records.UserAccountRecord;
import com.go2wheel.weblizedutil.model.UserAccount;
import com.go2wheel.weblizedutil.model.UserRole;
import com.go2wheel.weblizedutil.repository.UserAccountRepository;

@Service
@Validated
public class UserAccountDbService extends DbServiceBase<UserAccountRecord, UserAccount> {
	
	@Autowired
	private UserRoleDbService userRoleDbService;

	@Autowired
	public UserAccountDbService(UserAccountRepository serverRepository) {
		super(serverRepository);
	}
	
	public UserAccount findByEmail(String email) {
		return ((UserAccountRepository)repo).findByEmail(email);
	}
	
	public UserAccount findByMobile(String mobile) {
		return ((UserAccountRepository)repo).findByMobile(mobile);
	}

	public UserAccount findByName(String name) {
		return ((UserAccountRepository)repo).findByName(name);
	}

	public List<UserAccount> findLikeName(String partOfName) {
		return ((UserAccountRepository)repo).findLikeName(partOfName);
	}
	
	public UserAccount findByUsernameOrEmailOrMobile(String usernameOrEmailOrMobile) throws UsernameNotFoundException {
		UserAccount ua = ((UserAccountRepository)repo).findByUsernameOrEmailOrMobile(usernameOrEmailOrMobile);
		if (ua == null) {
			throw new UsernameNotFoundException("userAccountDbService.wrongCombination");
		}
		return ua;
	}
	
	public UserAccount createUser(@Valid UserAccount pojo, PasswordEncoder passwordEncoder) throws UnexpectlyCallMethodException {
		UserAccount origin = pojo;
		if (pojo.getId() == null || pojo.getId() == 0) {
			pojo.setPassword(passwordEncoder.encode(pojo.getPassword()));
			pojo = super.save(pojo);
			
			List<String> rolenames = origin.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toList());
			List<UserRole> foundUrs = userRoleDbService.findByNames(rolenames);
			
			List<String> unfoundRolenames = rolenames.stream().filter(rn -> !foundUrs.stream().anyMatch(ur -> rn.equals(ur.getAuthority()))).collect(Collectors.toList());
			unfoundRolenames.forEach(rn -> {
				UserRole ur = userRoleDbService.save(new UserRole(rn));
				foundUrs.add(ur);
			});
			return ((UserAccountRepository)repo).saveRolesAndReturn(pojo, foundUrs);
		} else {
			throw new UnexpectlyCallMethodException(this.getClass().getName(), "createUser");
		}
	}
	
	@Override
	public UserAccount save(@Valid UserAccount pojo) {
		if (pojo.getId() == null || pojo.getId() == 0) {
			throw new ExceptionWrapper(new UnexpectlyCallMethodException(this.getClass().getName(), "save"));
		}
		return super.save(pojo);
	}
	
}
