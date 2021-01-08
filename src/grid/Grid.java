package grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import grid.Registrar.FeatureEntry;

/**
 * @author lcy0x1
 * @Date 2021-1-8
 */
public class Grid {

	private static long posToKey(int px, int py) {
		return (Integer.toUnsignedLong(px) << 32L) + Integer.toUnsignedLong(py);
	}

	private final Map<Long, Block> map = new HashMap<>();

	public List<SystemFeature<?, ?>> failure = new ArrayList<>();
	public List<SystemFeature<?, ?>> system_list = new ArrayList<>();

	public Grid() {

	}

	public void contruct() {
		system_list = new ArrayList<>();
		for (FeatureEntry<?, ?> ent : Registrar.LIST)
			construct(ent);
	}

	public Block getBlock(int px, int py) {
		return map.get(posToKey(px, py));
	}

	public void update() {
		if (failure.size() > 0)
			return;
		for (SystemFeature<?, ?> system : system_list) {
			if (!system.update())
				failure.add(system);
		}
	}

	private <B extends BlockFeature<B, S>, S extends SystemFeature<B, S>> void construct(FeatureEntry<B, S> entry) {
		Set<B> block_list = new HashSet<>();
		map.forEach((k, v) -> {
			B bi = v.getFeature(entry);
			if (bi != null)
				block_list.add(bi);
		});
		while (block_list.size() > 0) {
			SystemFeature<B, S> sys = entry.supplier.get(this);
			if (!sys.construct(block_list))
				;
			failure.add(sys);
			system_list.add(sys);
		}
	}

}
