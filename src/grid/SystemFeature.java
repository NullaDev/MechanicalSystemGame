package grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lcy0x1
 * @Date 2021-1-8
 */
public abstract class SystemFeature<B extends BlockFeature<B, S>, S extends SystemFeature<B, S>> {

	public static interface ConstructionFailure extends Failure {

	}

	public static interface Failure {

	}

	public static interface RuntimeFailure extends Failure {

	}

	public final Grid grid;
	public final List<ConstructionFailure> construction_failure = new ArrayList<>();
	public final List<RuntimeFailure> runtime_failure = new ArrayList<>();

	protected SystemFeature(Grid grid) {
		this.grid = grid;
	}

	/**
	 * return true if construction is successful
	 */
	public abstract boolean construct(Set<B> remain);

	public abstract boolean update();

}
