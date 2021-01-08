package grid;

import java.util.Collection;

import grid.Registrar.FeatureEntry;

/**
 * @author lcy0x1
 * @Date 2021-1-8
 */
public abstract class Block {

	public int px, py;

	private Collection<BlockFeature<?, ?>> list;

	public Block() {

	}

	public boolean addFeature(BlockFeature<?, ?> item) {
		if (item == null)
			return false;
		if (getFeature(Registrar.getType(item.getClass())) != null) {
			list.add(item);
			item.bindBlock(this);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public <B extends BlockFeature<B, S>, S extends SystemFeature<B, S>> B getFeature(FeatureEntry<B, S> entry) {
		if (entry == null)
			return null;
		for (BlockFeature<?, ?> bi : list) {
			if (entry.block_class.isAssignableFrom(bi.getClass()))
				return (B) bi;
		}
		return null;
	}

}
