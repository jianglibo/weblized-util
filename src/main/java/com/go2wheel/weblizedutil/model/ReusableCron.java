package com.go2wheel.weblizedutil.model;

import com.go2wheel.weblizedutil.util.ObjectUtil;
import com.go2wheel.weblizedutil.validator.CronExpressionConstraint;

public class ReusableCron extends BaseModel {

	@CronExpressionConstraint
	private String expression;
	
	private String description;
	
	public ReusableCron() {
	}
	
	public ReusableCron(String expression, String description) {
		this.expression = expression;
		this.description = description;
	}
	

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public static String getExpressionFromToListRepresentation(String maybeToListRepresentation) {
		return ObjectUtil.getValueWetherIsToListRepresentationOrNot(maybeToListRepresentation, "expression");
	}

	@Override
	public String toListRepresentation(String... fields) {
		return ObjectUtil.toListRepresentation(this, "expression", "description");
	}
}
