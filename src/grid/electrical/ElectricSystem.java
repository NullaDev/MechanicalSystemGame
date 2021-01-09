package grid.electrical;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import grid.Block;
import grid.Grid;
import grid.Registrar;
import grid.SystemFeature;
import util.Face;

public final class ElectricSystem extends SystemFeature<ElectricPart, ElectricSystem> {

	public class Node {

		public ElectricPart e;
		public List<Node> conn = new ArrayList<>();

		private Node(ElectricPart e) {
			nodes.put(e,this);
			this.e = e;
		}

		private void connect(Node n) {
			conn.add(n);
			n.conn.add(this);
		}

	}

	private Map<ElectricPart, Node> nodes = new HashMap<>();

	public ElectricSystem(Grid grid) {
		super(grid);
	}

	@Override
	public boolean construct(Set<ElectricPart> remain) {
		Queue<Node> queue = new ArrayDeque<>();
		Iterator<ElectricPart> iter = remain.iterator();
		ElectricPart first = iter.next();
		iter.remove();
		queue.add(new Node(first));
		while (queue.size() > 0) {
			Node cur = queue.poll();
			Block curblock = cur.e.getBase();
			for (Face f : Face.values()) {
				if (!cur.e.connectTo(f))
					continue;
				Block b = grid.getBlock(curblock.px + f.offx, curblock.py + f.offy);
				if (b == null)
					continue;
				ElectricPart e = b.getFeature(Registrar.ELECTRIC);
				if (e == null || !e.connectTo(f.opposite()))
					continue;
				if(nodes.containsKey(e)) {
					cur.connect(nodes.get(e));
					continue;
				}
				if (!remain.contains(e))
					continue;
				Node n = new Node(e);
				queue.add(n);
				cur.connect(n);
				remain.remove(e);
			}
		}
		
		
		
		return construction_failure.size() == 0;
	}

	@Override
	public boolean update() {
		// TODO Auto-generated method stub
		return true;
	}

}
