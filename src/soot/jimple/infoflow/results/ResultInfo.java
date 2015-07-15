package soot.jimple.infoflow.results;

import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Stmt;

public abstract class ResultInfo {

	private SootMethod method;
	private final Stmt stmt;
	/**
	 * The class the method is called in
	 */
	private SootClass declaringClass;

	public ResultInfo(Stmt stmt){
		this.stmt = stmt;
	}
	
	public SootClass getDeclaringClass(){
		return declaringClass;
	}
	public void setDeclaringClass(SootClass sootClass){
		this.declaringClass = sootClass;
	}
	
	public Stmt getStmt(){
		return stmt;
	}
	
	public SootMethod getMethod(){
		return method;
	}
	public void setMethod(SootMethod method){
		this.method = method;
	}
}
