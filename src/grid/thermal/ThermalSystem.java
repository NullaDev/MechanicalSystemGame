package grid.thermal;

import java.util.HashSet;
import java.util.Set;

import grid.Block;
import grid.Grid;
import grid.Registrar;
import grid.SystemFeature;
import util.Face;

/**
 * @author lcy0x1
 * @Date 2021-1-8
 */
public final class ThermalSystem extends SystemFeature<ThermalBlock, ThermalSystem> {

	public class ThermalRuntimeFailure implements RuntimeFailure {

		public final ThermalBlock block;
		public final double temp;

		public ThermalRuntimeFailure(ThermalBlock b, double t) {
			this.block = b;
			this.temp = t;
		}

	}

	private Set<ThermalBlock> blocks;

	public ThermalSystem(Grid grid) {
		super(grid);
	}

	@Override
	public boolean construct(Set<ThermalBlock> remain) {
		blocks = new HashSet<>(remain);
		remain.clear();
		return true;
	}

	@Override
	public boolean update() {
		for (ThermalBlock b : blocks) {
			for (Face f : Face.values()) {
				Block b1 = grid.getBlock(b.getBase().px + f.offx, b.getBase().py + f.offy);
				if (b1 == null)
					continue;
				ThermalBlock tb = b1.getFeature(Registrar.THERMAL);
				if (tb == null)
					continue;
				b.updateFace(tb);
			}
		}
		for (ThermalBlock b : blocks) {
			int free = 0;
			for (Face f : Face.values()) {
				Block b1 = grid.getBlock(b.getBase().px + f.offx, b.getBase().py + f.offy);
				if (b1 == null)
					free++;
			}
			b.postUpdate(free);
			if (b.temperature() > b.maxTemp())
				runtime_failure.add(new ThermalRuntimeFailure(b, b.temperature()));
		}
		return runtime_failure.size() == 0;
	}

}
