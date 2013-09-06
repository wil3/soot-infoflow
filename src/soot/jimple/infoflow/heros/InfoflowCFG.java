package soot.jimple.infoflow.heros;

import heros.solver.IDESolver;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.ide.icfg.JimpleBasedBiDiICFG;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.MHGPostDominatorsFinder;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class InfoflowCFG extends JimpleBasedBiDiICFG {

	public class UnitContainer {
		
		private final Unit unit;
		private final SootMethod method;
		
		public UnitContainer(Unit u) {
			unit = u;
			method = null;
		}
		
		public UnitContainer(SootMethod sm) {
			unit = null;
			method = sm;
		}
		
		@Override
		public int hashCode() {
			return 31 * (unit == null ? 0 : unit.hashCode())
					+ 31 * (method == null ? 0 : method.hashCode());
		}
		
		@Override
		public boolean equals(Object other) {
			if ((other == null) || !(other instanceof UnitContainer))
				return false;
			UnitContainer container = (UnitContainer) other;
			if (this.unit == null) {
				if (container.unit != null)
					return false;
			}
			else
				if (!this.unit.equals(container.unit))
					return false;
			if (this.method == null) {
				if (container.method != null)
					return false;
			}
			else
				if (!this.method.equals(container.method))
					return false;
			return true;
		}

		public Unit getUnit() {
			return unit;
		}
		
		public SootMethod getMethod() {
			return method;
		}
		
	}
	
	protected final LoadingCache<Unit,UnitContainer> unitToPostdominator =
			IDESolver.DEFAULT_CACHE_BUILDER.build( new CacheLoader<Unit,UnitContainer>() {
				@Override
				public UnitContainer load(Unit unit) throws Exception {
					SootMethod method = getMethodOf(unit);
					DirectedGraph<Unit> graph = bodyToUnitGraph.getUnchecked(method.getActiveBody());
					MHGPostDominatorsFinder<Unit> postdominatorFinder = new MHGPostDominatorsFinder<Unit>(graph);
					Unit postdom = postdominatorFinder.getImmediateDominator(unit);
					if (postdom == null)
						return new UnitContainer(method);
					else
						return new UnitContainer(postdom);
				}
			});

	public UnitContainer getPostdominatorOf(Unit u) {
		return unitToPostdominator.getUnchecked(u);
	}
	
}
