package grid;

/**
 * @author lcy0x1
 * @Date 2021-1-8
 */
public abstract class BlockFeature<B extends BlockFeature<B, S>, S extends SystemFeature<B, S>> {

	private Block base;

	public final Block getBase() {
		return base;
	}

	void bindBlock(Block block) {
		base = block;
	}

}
