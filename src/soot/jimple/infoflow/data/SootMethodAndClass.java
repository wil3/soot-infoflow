/*******************************************************************************
 * Copyright (c) 2012 Secure Software Engineering Group at EC SPRIDE.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors: Christian Fritz, Steven Arzt, Siegfried Rasthofer, Eric
 * Bodden, and others.
 ******************************************************************************/
package soot.jimple.infoflow.data;

import java.util.ArrayList;
import java.util.List;

import soot.SootMethod;
import soot.Type;
/**
 * data container which stores the string representation of a SootMethod and its corresponding class
 */
public class SootMethodAndClass {
	
	/**
	 * The class the method is called in
	 */
	private String declaredClass;
	/**
	 * The linenumber in the class the method is used
	 */
	private int lineNumber;
	
	private boolean isField = false;
	private final String methodName;
	private final String className;
	private final String returnType;
	private List<String> parameters = new ArrayList<String>();
	private int hashCode = 0;
	
	public SootMethodAndClass
			(String methodName,
			String className,
			String returnType,
			List<String> parameters){
		this.methodName = methodName;
		this.className = className;
		this.returnType = returnType;
		this.parameters = parameters;
	}
	
	public SootMethodAndClass(SootMethod sm) {
		//this.lineNumber = sm.getJavaSourceStartLineNumber();
		//this.declaredClass = sm.getDeclaringClass().getName();
		this.methodName = sm.getName();
		this.className = sm.getDeclaringClass().getName();
		this.returnType = sm.getReturnType().toString();
		this.parameters = new ArrayList<String>();
		for (Type p: sm.getParameterTypes())
			this.parameters.add(p.toString());
	}
	
	public SootMethodAndClass(SootMethodAndClass methodAndClass) {
		this.methodName = methodAndClass.methodName;
		this.className = methodAndClass.className;
		this.returnType = methodAndClass.returnType;
		this.parameters = new ArrayList<String>(methodAndClass.parameters);
	}

	public String getMethodName() {
		return this.methodName;
	}
	
	public String getClassName() {
		return this.className;
	}
	
	public String getReturnType() {
		return this.returnType;
	}
	
	public List<String> getParameters() {
		return this.parameters;
	}
	
	public String getSubSignature() {
		if (isField){
			return getFieldSubSignature();
		} else {
			return getMethodSubSignature();
		}
	}
	private String getFieldSubSignature(){
		return this.className + ": " + this.returnType + " " + this.methodName;
	}
	private String getMethodSubSignature(){
		String s = (this.returnType.length() == 0 ? "" : this.returnType + " ") + this.methodName + "(";
		for (int i = 0; i < this.parameters.size(); i++) {
			if (i > 0)
				s += ",";
			s += this.parameters.get(i).trim();
		}
		s += ")";
		return s;
	}

	public String getSignature() {
		if (isField){
			return getFieldSignature();
		} else {
			return getMethodSignature();
		}
	}
	private String getFieldSignature(){
		String inClass = (declaredClass == null) ? "" : declaredClass + ":" + lineNumber + " ";
		String s = inClass + "<" + this.className + ": " + this.returnType + " " + this.methodName + ">";
		return s;
	}
	
	private String getMethodSignature(){
		String inClass = (declaredClass == null) ? "" : declaredClass + ":" + lineNumber + " ";
		String s = inClass + "<" + this.className + ": " + (this.returnType.length() == 0 ? "" : this.returnType + " ")
				+ this.methodName + "(";
		for (int i = 0; i < this.parameters.size(); i++) {
			if (i > 0)
				s += ",";
			s += this.parameters.get(i).trim();
		}
		s += ")>";
		return s;
	}

	@Override
	public boolean equals(Object another) {
		if (super.equals(another))
			return true;
		if (!(another instanceof SootMethodAndClass))
			return false;
		SootMethodAndClass otherMethod = (SootMethodAndClass) another;
		
		if (!this.methodName.equals(otherMethod.methodName))
			return false;
		if (!this.parameters.equals(otherMethod.parameters))
			return false;
		if (!this.className.equals(otherMethod.className))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		if (this.hashCode == 0)
			this.hashCode = this.methodName.hashCode() + this.className.hashCode() * 5;
		// The parameter list is available from the outside, so we can't cache it
		return (this.parameters != null) ? this.hashCode + this.parameters.hashCode() * 7 : this.hashCode;
	
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(className);
		sb.append(": ");
		sb.append(returnType);
		sb.append(" ");
		sb.append("methodName(");
		boolean isFirst = true;
		for (String param : parameters) {
			if (!isFirst)
				sb.append(",");
			sb.append(param);
		}
		sb.append(")>");
		return sb.toString();
	}
	

	public String getDeclaredClass() {
		return declaredClass;
	}

	public void setDeclaredClass(String declaredClass) {
		this.declaredClass = declaredClass;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public boolean isField() {
		return isField;
	}

	public void setField(boolean isField) {
		this.isField = isField;
	}

}
