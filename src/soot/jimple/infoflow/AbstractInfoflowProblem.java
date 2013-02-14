package soot.jimple.infoflow;

import heros.InterproceduralCFG;

import java.util.HashSet;
import java.util.Set;

import soot.EquivalentValue;
import soot.NullType;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.StaticFieldRef;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AbstractionWithPath;
import soot.jimple.infoflow.nativ.DefaultNativeCallHandler;
import soot.jimple.infoflow.nativ.NativeCallHandler;
import soot.jimple.infoflow.util.ITaintPropagationWrapper;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;

public abstract class AbstractInfoflowProblem extends DefaultJimpleIFDSTabulationProblem<Abstraction, InterproceduralCFG<Unit, SootMethod>> {

	/**
	 * Supported methods for tracking data flow paths through applications
	 * @author sarzt
	 *
	 */
	public enum PathTrackingMethod {
		/**
		 * Do not track any paths. Just search for connections between sources
		 * and sinks, but forget about the path between them.
		 */
		NoTracking,
		
		/**
		 * Perform a simple forward tracking. Whenever propagating taint
		 * information, also track the current statement on the path. Consumes
		 * a lot of memory.
		 */
		ForwardTracking
	}
	
	protected final Set<Unit> initialSeeds = new HashSet<Unit>();
	protected final InfoflowResults results;
	protected ITaintPropagationWrapper taintWrapper;
	protected PathTrackingMethod pathTracking = PathTrackingMethod.NoTracking;
	protected NativeCallHandler ncHandler = new DefaultNativeCallHandler();
	protected boolean debug = false;

	Abstraction zeroValue = null;
	
	public AbstractInfoflowProblem(InterproceduralCFG<Unit, SootMethod> icfg) {
		super(icfg);
		results = new InfoflowResults();
	}
	
	@Override
	public boolean followReturnsPastSeeds(){
		return true;
	}
	
	public void setTaintWrapper(ITaintPropagationWrapper wrapper){
		taintWrapper = wrapper;
	}
	
	public void setDebug(boolean debug){
		this.debug = debug;
	}
	
	/**
	 * Sets whether and how the paths between the sources and sinks shall be
	 * tracked
	 * @param method The method for tracking data flow paths through the
	 * program.
	 */
	public void setPathTracking(PathTrackingMethod method) {
		this.pathTracking = method;
		this.ncHandler.setPathTracking(method);
	}
	
	protected static String getStaticFieldRefStringRepresentation(StaticFieldRef ref){
		return ref.getField().getDeclaringClass().getName() + "."+ref.getFieldRef().name();
	}
	
	@Override
	public Abstraction createZeroValue() {
		if (zeroValue == null) {
			zeroValue = this.pathTracking == PathTrackingMethod.NoTracking ?
				new Abstraction(new EquivalentValue(new JimpleLocal("zero", NullType.v())), null, null) :
				new AbstractionWithPath(new EquivalentValue(new JimpleLocal("zero", NullType.v())), null, null, false);
		}

		return zeroValue;
	}
	
	@Override
	public Set<Unit> initialSeeds() {
		return initialSeeds;
	}
	
	@Override
	public boolean autoAddZero() {
		return false;
	}
	
}
