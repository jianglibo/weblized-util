package com.go2wheel.weblizedutil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.ExitRequest;
import org.springframework.shell.Input;
import org.springframework.shell.InputProvider;
import org.springframework.shell.Shell;
import org.springframework.util.StringUtils;

@Configuration
public class ExecuteAndExitApplicationRunnerConfiguration {

	@Autowired
	private Shell shell;

//	@Bean
//	public CommandLineRunner exampleCommandLineRunner(ConfigurableEnvironment environment) {
//		return new CopyProjectCommandLineRunner(shell, environment);
//	}

	@Bean
	public ExitCodeExceptionMapper exitCodeExceptionMapper() {
		return exception -> {
			Throwable e = exception;
			while (e != null && !(e instanceof ExitRequest)) {
				e = e.getCause();
			}
			return e == null ? 1 : ((ExitRequest) e).status();
		};
	}
}

/**
 * Example CommandLineRunner that shows how overall shell behavior can be customized. In
 * this particular example, any program (process) arguments are assumed to be shell
 * commands that need to be executed (and the shell then quits).
 */
//@Order(InteractiveShellApplicationRunnerMine.PRECEDENCE - 2)
//class CopyProjectCommandLineRunner implements CommandLineRunner {
//
//	private Shell shell;
//
//	private final ConfigurableEnvironment environment;
//
//	public CopyProjectCommandLineRunner(Shell shell, ConfigurableEnvironment environment) {
//		this.shell = shell;
//		this.environment = environment;
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		List<String> commandsToRun = Arrays.stream(args)
//				.filter(w -> !w.startsWith("@"))
//				.collect(Collectors.toList());
//		if (!commandsToRun.isEmpty()) {
//			InteractiveShellApplicationRunnerMine.disable(environment);
//			shell.run(new StringInputProvider(commandsToRun));
//		}
//	}
//}

class StringInputProvider implements InputProvider {

	private final List<String> words;

	private boolean done;

	public StringInputProvider(List<String> words) {
		this.words = words;
	}

	@Override
	public Input readInput() {
		if (!done) {
			done = true;
			return new Input() {
				@Override
				public List<String> words() {
					return words;
				}

				@Override
				public String rawText() {
					return StringUtils.collectionToDelimitedString(words, " ");
				}
			};
		}
		else {
			return null;
		}
	}
}
