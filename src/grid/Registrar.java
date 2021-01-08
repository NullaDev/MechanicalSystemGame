package grid;

import grid.mechanical.Machine;
import grid.mechanical.MechanicalSystem;
import grid.thermal.ThermalBlock;
import grid.thermal.ThermalSystem;

/**
 * @author lcy0x1
 * @Date 2021-1-8
 */
public class Registrar {

	@FunctionalInterface
	public static interface SystemFactory<B extends BlockFeature<B, S>, S extends SystemFeature<B, S>> {

		public S get(Grid grid);

	}

	public static class FeatureEntry<B extends BlockFeature<B, S>, S extends SystemFeature<B, S>> {

		public final Class<B> block_class;
		public final Class<S> system_class;
		public final SystemFactory<B, S> supplier;

		public FeatureEntry(Class<B> bcls, Class<S> scls, SystemFactory<B, S> sup) {
			this.block_class = bcls;
			this.system_class = scls;
			this.supplier = sup;
		}

	}

	public static final FeatureEntry<Machine, MechanicalSystem> MACHINE = new FeatureEntry<>(Machine.class,
			MechanicalSystem.class, MechanicalSystem::new);
	public static final FeatureEntry<ThermalBlock, ThermalSystem> THERMAL = new FeatureEntry<>(ThermalBlock.class,
			ThermalSystem.class, ThermalSystem::new);

	public static final FeatureEntry<?, ?>[] LIST = { MACHINE, THERMAL };

	public static FeatureEntry<?, ?> getType(Class<?> cls) {
		for (FeatureEntry<?, ?> ent : LIST) {
			if (ent.block_class.isAssignableFrom(cls) || ent.system_class.isAssignableFrom(cls))
				return ent;
		}
		return null;
	}

}
