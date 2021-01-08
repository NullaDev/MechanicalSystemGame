package util;

/**
 * @author lcy0x1
 * @Date 2021-1-8
 */
public enum Face {
	RIGHT(1, 0, 0), UP(0, 1, 1), LEFT(-1, 0, 2), DOWN(0, -1, 3);

	public final int offx, offy, index;

	private Face(int offx, int offy, int ind) {
		this.offx = offx;
		this.offy = offy;
		this.index = ind;
	}

	public Face opposite() {
		return Face.values()[(index + 2) % 4];
	}

}
