package com.go2wheel.weblizedutil.value;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.go2wheel.weblizedutil.value.ConfigValue.ConfigValueState;

public class BlockedPropertiesFileHolder {
	
	public static final String COMMENT_OUT_CHAR = ";";
	
	private List<String> lines;
	
	private Pattern blockNamePtn = Pattern.compile(String.format("\\s*\\[%s\\]\\s*", "([^\\[\\]]+)"));

	public BlockedPropertiesFileHolder(List<String> lines) {
		super();
		this.lines = lines;
		deduplicate();
	}
	
	private void deduplicate() {
		List<String> nl = new ArrayList<>();
		String currentLine = null;
		for(String line: lines) {
			if (!line.equals(currentLine)) {
				nl.add(line);
				currentLine = line;
			}
		}
		this.lines = nl;
	}

	private String getKvLine(ConfigValue cv, Object v) {
		return String.format("%s=%s", cv.getKey(), v);
	}
	
	
	public boolean commentOutConfigValue(ConfigValue cv) {
		if (cv.getState() == ConfigValueState.COMMENT_OUTED || cv.getState() == ConfigValueState.NOT_EXIST) {
			return false;
		}
		String c = COMMENT_OUT_CHAR + lines.get(cv.getLineIndex());
		lines.set(cv.getLineIndex(), c);
		return true;
	}
	
	public boolean setConfigValue(ConfigValue cv, String value) {
		if (value.equals(cv.getValue()) && cv.getState() == ConfigValueState.EXIST) {
			return false;
		}
		switch (cv.getState()) {
		case COMMENT_OUTED:
		case EXIST:
			lines.set(cv.getLineIndex(), getKvLine(cv, value));
			break;
		default:
			if (cv.getBlock() == null || cv.getBlock().trim().isEmpty()) {
				lines.add(cv.getKey() + "=" + value); // put to the last line.
			} else {
				int bp = findBlockPosition(cv.getBlock()); 
				if (bp != -1) {
					lines.add(bp + 1, getKvLine(cv, value));
				} else {
					lines.add(String.format("[%s]", cv.getBlock()));
					lines.add(getKvLine(cv, value));
				}
			}
		}
		return true;
	}
	
	public int findBlockPosition(String blockName) {
		for(int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			Matcher m = blockNamePtn.matcher(line);
			if (m.matches() && blockName.equals(m.group(1))) {
				return i;
			}
		}
		return -1;
	}
	
	public int findNextBlockPosition(int startPoint) {
		for(int i = startPoint + 1; i < getLines().size(); i++) {
			String line = getLines().get(i);
			if (blockNamePtn.matcher(line).matches()) {
				return i;
			}
		}
		return getLines().size() - 1;
	}
	
	/**
	 * blockName does't include surrounding square bracket.
	 * @param blockName
	 * @param cnfName
	 * @return
	 */
	public ConfigValue getConfigValue(String blockName, String cnfName) {
		int blkStart = findBlockPosition(blockName);
		int blkEnd = findNextBlockPosition(blkStart);
		return getConfigValue(blockName, cnfName, blkStart, blkEnd);
	}
	
	private ConfigValue getConfigValue(String block, String cnfName, int blkStart, int blkEnd) {
		Pattern commentOutPtn = getCommentOutedPtn(cnfName);
		Pattern existPtn = getExistedPtn(cnfName);
		ConfigValue commented = ConfigValue.getNotExistValue(block, cnfName);
		if (blkStart == -1) {
			return commented;
		}
		Matcher matcher = null;
		for(int i = blkStart; i < blkEnd; i++) {
			String line = lines.get(i);
			matcher = existPtn.matcher(line);
			if (matcher.matches()) {
				return ConfigValue.getExistValue(block, cnfName, matcher.group(1), i);
			}
			
			matcher = commentOutPtn.matcher(line);
			if (matcher.matches()) {
				commented = ConfigValue.getCommentOuted(block, cnfName, matcher.group(1), i);
			}
		}
		return commented;
	}


	public ConfigValue getConfigValue(String cnfName) {
		return getConfigValue(null, cnfName, 0, lines.size());
	}
	
	private Pattern getCommentOutedPtn(String nameOfItem) {
		return Pattern.compile("^\\s*;\\s*" + nameOfItem + "\\s*=\\s*([^\\s]+)\\s*$");
	}
	
	private Pattern getExistedPtn(String nameOfItem) {
		return Pattern.compile("^\\s*" + nameOfItem + "\\s*=\\s*([^\\s]+)\\s*$");
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}
}
