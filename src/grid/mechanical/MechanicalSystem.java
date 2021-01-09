package grid.mechanical;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import grid.Block;
import grid.Grid;
import grid.Registrar;
import grid.SystemFeature;
import util.Face;
import util.Frac;

/**
 * @author lcy0x1
 * @Date 2021-1-8
 */
public final class MechanicalSystem extends SystemFeature<Machine, MechanicalSystem> {

	public class Connection {

		public final Machine machine;

		public final Frac relative_rate;

		private Connection(Machine block, Frac rate) {
			this.machine = block;
			map.put(block, this);
			this.relative_rate = rate;
		}

	}

	public class MechanicalConstructionFailure implements ConstructionFailure {

		public final Machine machine;
		public final Face face;

		private MechanicalConstructionFailure(Machine m, Face f) {
			machine = m;
			face = f;
		}

	}

	public class MechanicalRuntimeFailure implements RuntimeFailure {

		public final Machine machine;
		public final double torque, speed;

		private MechanicalRuntimeFailure(Machine m, double torque, double speed) {
			this.machine = m;
			this.torque = torque;
			this.speed = speed;
		}

	}

	public Machine root;
	public double speed = 0;

	private Map<Machine, Connection> map = new HashMap<>();

	public MechanicalSystem(Grid grid) {
		super(grid);
	}

	@Override
	public boolean construct(Set<Machine> remain) {
		Queue<Connection> queue = new ArrayDeque<>();
		Iterator<Machine> iter = remain.iterator();
		Machine first = iter.next();
		root = first;
		iter.remove();
		queue.add(new Connection(first, new Frac(1, 1)));
		while (queue.size() > 0) {
			Connection cur = queue.poll();
			Block curblock = cur.machine.getBase();
			for (Face f : Face.values()) {
				if (cur.machine.rotationFactor(f) == null)
					continue;
				Block b = grid.getBlock(curblock.px + f.offx, curblock.py + f.offy);
				if (b == null)
					continue;
				Machine m = b.getFeature(Registrar.MACHINE);
				if (m == null || m.rotationFactor(f.opposite()) == null)
					continue;
				Frac rate = Frac.mult(cur.relative_rate, cur.machine.rotationFactor(f));
				rate.divide(m.rotationFactor(f.opposite()));
				rate.lock();
				Connection target = map.get(m);
				if (target != null && !target.relative_rate.equals(rate))
					construction_failure.add(new MechanicalConstructionFailure(m, f));
				if (target != null)
					continue;
				if (!remain.contains(m))
					continue;
				queue.add(new Connection(m, rate));
				remain.remove(m);
			}
		}
		return construction_failure.size() == 0;
	}

	@Override
	public boolean update() {
		double inertia = 0;
		double torque = 0;
		for (Connection conn : map.values()) {
			inertia += conn.machine.inertia() * conn.relative_rate.getVal();
			torque += conn.machine.torque() * conn.relative_rate.getVal();
		}
		if (torque < 0 || speed > 0) {
			speed -= torque / inertia;
		}
		if (speed < 0)
			speed = 0;
		for (Connection conn : map.values()) {
			Machine m = conn.machine;
			double rate = conn.relative_rate.getVal() * Math.abs(speed);
			double tor = conn.relative_rate.getVal() * Math.abs(torque / inertia);
			if (m.maxSpeed() < rate || m.maxAccel() < tor)
				runtime_failure.add(new MechanicalRuntimeFailure(m, tor, rate));
		}
		if (runtime_failure.size() == 0)
			for (Connection conn : map.values())
				conn.machine.update(Math.abs(speed) * conn.relative_rate.getVal());
		return runtime_failure.size() == 0;
	}

}
