package com.go2wheel.weblizedutil.commands;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.go2wheel.weblizedutil.ApplicationState;
import com.go2wheel.weblizedutil.LocaledMessageService;
import com.go2wheel.weblizedutil.SecurityService;
import com.go2wheel.weblizedutil.SettingsInDb;
import com.go2wheel.weblizedutil.annotation.CandidatesFromSQL;
import com.go2wheel.weblizedutil.annotation.DbTableName;
import com.go2wheel.weblizedutil.exception.InvalidCronExpressionFieldException;
import com.go2wheel.weblizedutil.job.CronExpressionBuilder;
import com.go2wheel.weblizedutil.job.CronExpressionBuilder.CronExpressionField;
import com.go2wheel.weblizedutil.model.KeyValue;
import com.go2wheel.weblizedutil.model.ReusableCron;
import com.go2wheel.weblizedutil.model.UserAccount;
import com.go2wheel.weblizedutil.model.UserGrp;
import com.go2wheel.weblizedutil.service.GlobalStore;
import com.go2wheel.weblizedutil.service.GlobalStore.SavedFuture;
import com.go2wheel.weblizedutil.service.KeyValueDbService;
import com.go2wheel.weblizedutil.service.ReusableCronDbService;
import com.go2wheel.weblizedutil.service.SqlService;
import com.go2wheel.weblizedutil.service.UserAccountDbService;
import com.go2wheel.weblizedutil.util.ExceptionUtil;
import com.go2wheel.weblizedutil.util.StringUtil;
import com.go2wheel.weblizedutil.value.CommonMessageKeys;
import com.go2wheel.weblizedutil.value.FacadeResult;
import com.go2wheel.weblizedutil.value.FacadeResult.CommonActionResult;
import com.jcraft.jsch.JSchException;

@ShellComponent()
public class BackupCommand {

	public static final String DESCRIPTION_FILENAME = "description.yml";

	public static final String DANGEROUS_ALERT = "I know what i am doing.";

	public static final int RESTART_CODE = 101;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private GlobalStore globalStore;

	@Autowired
	private SqlService sqlService;

	@Autowired
	private SettingsInDb settingsInDb;

	@Autowired
	private Environment environment;

	@Autowired
	private ApplicationState appState;


	@Autowired
	private UserAccountDbService userAccountDbService;


	@Autowired
	private ReusableCronDbService reusableCronDbService;


	@Autowired
	private LocaledMessageService localedMessageService;

	@PostConstruct
	public void post() {

	}

	@ShellMethod(value = "显示配置相关信息。")
	public List<String> systemInfo(@ShellOption(help = "环境变量名", defaultValue = "") String envname) throws IOException {
		if (StringUtil.hasAnyNonBlankWord(envname)) {
			return Arrays.asList(String.format("%s: %s", envname, environment.getProperty(envname)));
		}
		return Arrays.asList(
				formatKeyVal("server profile dirctory", settingsInDb.getDataDir().toAbsolutePath().toString()),
				formatKeyVal("database url", environment.getProperty("spring.datasource.url")),
				formatKeyVal("working directory", Paths.get("").toAbsolutePath().normalize().toString()),
				formatKeyVal("download directory",
						settingsInDb.getDownloadPath().normalize().toAbsolutePath().toString()),
				formatKeyVal("log file", environment.getProperty("logging.file")),
				formatKeyVal("spring.config.name", environment.getProperty("spring.config.name")),
				formatKeyVal("spring.config.location", environment.getProperty("spring.config.location")),
				formatKeyVal("Spring active profile", String.join(",", environment.getActiveProfiles())));
	}

	private String formatKeyVal(String string, String string2) {
		return String.format("%s: %s", string, string2);
	}

	@ShellMethod(value = "Exit the shell.", key = { "quit", "exit" })
	public void quit(@ShellOption(help = "退出值", defaultValue = "0") int exitValue,
			@ShellOption(help = "重启") boolean restart) {
		if (restart) {
			System.exit(101);
		}
		System.exit(exitValue);
	}

	@ShellMethod(value = "列出后台任务")
	public FacadeResult<?> asyncList() throws JSchException, IOException {
		List<SavedFuture> gobjects = globalStore.getFutureGroupAll(BackupCommand.class.getName());
		
		List<String> ls =  gobjects.stream()
				.map(sf -> String.format("Task %s, Done: %s",sf.getDescription(), sf.getCf().isDone()))
				.collect(Collectors.toList());
		return FacadeResult.doneExpectedResultDone(ls);
		
	}
	
	@ShellMethod(value = "添加常用的CRON表达式")
	public FacadeResult<?> cronExpressionAdd(@ShellOption(help = "cron表达式") String expression,
			@ShellOption(help = "描述", defaultValue = "") String description) {
		ReusableCron rc = new ReusableCron(expression, description);
		try {
			rc = reusableCronDbService.save(rc);
			return FacadeResult.doneExpectedResult(rc, CommonActionResult.DONE);
		} catch (Exception e) {
			return FacadeResult.showMessageUnExpected(CommonMessageKeys.MALFORMED_VALUE, rc.getExpression());
		}
	}

	@ShellMethod(value = "构建CRON表达式")
	public String cronExpressionBuild(
			@ShellOption(help = "支持的格式示例：1 或者 1,8,10 或者 5-25 或者 5/15(从5开始以15递增。)", defaultValue = "0") String second,
			@ShellOption(help = "格式同second参数，范围0-59", defaultValue = "0") String minute,
			@ShellOption(help = "格式同second参数，范围0-23", defaultValue = "0") String hour,
			@ShellOption(help = "格式同second参数，范围1-31", defaultValue = "?") String dayOfMonth,
			@ShellOption(help = "格式同second参数，范围1-12", defaultValue = "*") String month,
			@ShellOption(help = "格式同second参数，范围1-7，其中1是周六。", defaultValue = "*") String dayOfWeek,
			@ShellOption(help = "空白或者1970-2099", defaultValue = "") String year) {

		CronExpressionBuilder ceb = new CronExpressionBuilder();
		try {

			CronExpressionBuilder.validCronField(CronExpressionField.SECOND, second);
			ceb.second(second);

			CronExpressionBuilder.validCronField(CronExpressionField.MINUTE, minute);
			ceb.minute(minute);

			CronExpressionBuilder.validCronField(CronExpressionField.HOUR, hour);
			ceb.hour(hour);

			CronExpressionBuilder.validCronField(CronExpressionField.DAY_OF_MONTH, dayOfMonth);
			ceb.dayOfMonth(dayOfMonth);

			CronExpressionBuilder.validCronField(CronExpressionField.MONTH, month);
			ceb.month(month);

			CronExpressionBuilder.validCronField(CronExpressionField.DAY_OF_WEEK, dayOfWeek);
			ceb.dayOfWeek(dayOfWeek);
			CronExpressionBuilder.validCronField(CronExpressionField.YEAR, year);
			ceb.year(year);

			return ceb.build();
		} catch (InvalidCronExpressionFieldException e) {
			return e.getMessage();
		}
	}

	@ShellMethod(value = "列出常用的CRON表达式")
	public FacadeResult<?> cronExpressionList() {
		return FacadeResult.doneExpectedResult(reusableCronDbService.findAll(), CommonActionResult.DONE);
	}

	@ShellMethod(value = "添加用户。")
	public FacadeResult<?> userAdd(@ShellOption(help = "用户名") String name, @ShellOption(help = "email地址") String email,
			@ShellOption(help = "手机号码", defaultValue = ShellOption.NULL) String mobile,
			@ShellOption(help = "描述", defaultValue = "") String description) {
		UserAccount ua = new UserAccount.UserAccountBuilder(name, email, "kku").withMobile(mobile).withDescription(description)
				.build();
		return FacadeResult.doneExpectedResult(userAccountDbService.save(ua), CommonActionResult.DONE);
	}

	@ShellMethod(value = "用户列表。")
	public FacadeResult<?> userList() {
		return FacadeResult.doneExpectedResult(userAccountDbService.findAll(), CommonActionResult.DONE);
	}

	@ShellMethod(value = "添加用户组。")
	public FacadeResult<?> userGroupAdd(@ShellOption(help = "组的英文名称") String ename,
			@ShellOption(help = "message的键值，如果需要国际化的话", defaultValue = ShellOption.NULL) String msgkey) {
		UserGrp ug = new UserGrp(ename, msgkey);
		return FacadeResult.doneExpectedResultDone(ug);
	}

	@ShellMethod(value = "支持的语言")
	public List<String> languageList() {
		List<String> las = new ArrayList<>();
		las.add(Locale.TRADITIONAL_CHINESE.getLanguage());
		las.add(Locale.ENGLISH.getLanguage());
		las.add(Locale.JAPANESE.getLanguage());
		return las;
	}

	@ShellMethod(value = "支持的语言")
	public String languageSet(String language) {
		if (!languageList().contains(language)) {
			return String.format("Supported languages are: %s", String.join(", ", languageList()));
		}
		Locale l = Locale.forLanguageTag(language);
		appState.setLocal(l);
		return "switch to language: " + language;
	}



	@ShellMethod(value = "查看最后一个命令的详细执行结果")
	public String facadeResultLast() {
		FacadeResult<?> fr = appState.getFacadeResult();
		if (fr == null) {
			return "";
		} else {
			if (fr.getException() != null) {
				return ExceptionUtil.stackTraceToString(fr.getException());
			} else if (fr.getResult() != null) {
				return fr.getResult().toString();
			} else if (fr.getMessage() != null && !fr.getMessage().isEmpty()) {
				return localedMessageService.getMessage(fr.getMessage());
			} else {
				return "";
			}
		}
	}
	
	@ShellMethod(value = "执行SQL SELECT.")
	public FacadeResult<?> sqlSelect(
			@DbTableName
			@ShellOption(help = "数据表名称") String tableName,
			@ShellOption(help = "返回的记录数", defaultValue="10") int limit) {
		return FacadeResult.doneExpectedResultDone(sqlService.select(tableName, limit));
	}
	
	@ShellMethod(value = "执行SQL DELETE.")
	public FacadeResult<?> sqlDelete(
			@DbTableName
			@ShellOption(help = "数据表名称") String tableName,
			@ShellOption(help = "记录的ID") int id) {
		return FacadeResult.doneExpectedResultDone(sqlService.delete(tableName, id));
	}
	
	
	@ShellMethod(value = "获取HSQLDB的CRYPT_KEY")
	public FacadeResult<?> securityKeygen(@ShellOption(help = "编码方式", defaultValue="AES") String enc) throws ClassNotFoundException, SQLException {
		return securityService.securityKeygen(enc);
	}
	
	@ShellMethod(value = "将SSHkey从文件复制到数据库或反之。")
	public FacadeResult<?> securityCopySshkey(@ShellOption(help = "db到文件") boolean toFile,
			@ShellOption(help = "删除ssh文件") boolean deleteFile) throws ClassNotFoundException, IOException {
		return securityService.securityCopySshkey(toFile, deleteFile);
	}
	
	@ShellMethod(value = "将KnownHosts从文件复制到数据库或反之。")
	public FacadeResult<?> securityCopyKnownHosts(@ShellOption(help = "db到文件") boolean toFile) throws ClassNotFoundException, IOException {
		return securityService.securityCopyKnownHosts(toFile);
	}

	
	@Autowired
	private KeyValueDbService keyValueDbService;
	
	public static final String KEY_VALUE_CANDIDATES_SQL = "SELECT ITEM_KEY FROM KEY_VALUE WHERE ITEM_KEY LIKE '%%%s%%'";
	
	@ShellMethod(value = "查询键值对。")
	public FacadeResult<?> keyValueGet(
			@CandidatesFromSQL(KEY_VALUE_CANDIDATES_SQL)
			@ShellOption(help = "键值，可以用dot分隔") String key
			) {
		List<KeyValue> kvs = keyValueDbService.findManyByKeyPrefix(key);
		return FacadeResult.doneExpectedResultDone(kvs);
	}
	
	@ShellMethod(value = "删除键值对。")
	public FacadeResult<?> keyValueDelete(
			@CandidatesFromSQL(KEY_VALUE_CANDIDATES_SQL)
			@ShellOption(help = "键值，可以用dot分隔") String key
			) {
		KeyValue kv = keyValueDbService.findOneByKey(key);
		if (kv == null) {
			return FacadeResult.doneExpectedResultPreviousDone(CommonMessageKeys.DB_ITEMNOTEXISTS);
		} else {
			keyValueDbService.delete(kv);
			return FacadeResult.doneExpectedResultDone(kv);
		}
		
	}

	
	@ShellMethod(value = "新建或更新键值对。")
	public FacadeResult<?> keyValueUpdateOrCreate(
			@CandidatesFromSQL(KEY_VALUE_CANDIDATES_SQL)
			@ShellOption(help = "键值，可以用dot分隔") String key,
			@ShellOption(help = "值") String value
			) {
		KeyValue kv = keyValueDbService.findOneByKey(key);
		if (kv != null) {
			if (!kv.getItemValue().equals(value)) {
				kv.setItemValue(value);
				kv = keyValueDbService.save(kv);
			}
		} else {
			kv = new KeyValue(key, value);
			kv = keyValueDbService.save(kv);
		}
		return FacadeResult.doneExpectedResultDone(kv);
	}
}
