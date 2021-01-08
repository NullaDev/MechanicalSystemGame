package grid.mechanical;

import grid.BlockFeature;
import util.Face;
import util.Frac;

/**
 * @author lcy0x1
 * @Date 2021-1-8
 */
public abstract class Machine extends BlockFeature<Machine, MechanicalSystem> {

	public static class MachineConfig {
		public double maxSpeed, maxAccel, inertia;
	}

	protected MachineConfig config;

	public Machine(MachineConfig config) {
		this.config = config;
	}

	/**
	 * The rotation inertia of this machine, measured against its core, in kg*m^2
	 */
	public double inertia() {
		return config.inertia;
	}

	/**
	 * The maximum acceleration it can withstand, in rad/s^2
	 */
	public double maxAccel() {
		return config.maxAccel;
	}

	/**
	 * The maximum speed it can withstand, in rad/s
	 */
	public double maxSpeed() {
		return config.maxSpeed;
	}

	/**
	 * The rotation speed up factor of the specified face of this machine A rotation
	 * factor of 2 means that when the core of this machine is rotating at rate w,
	 * the specified face rotates at rate 2w
	 * 
	 * null means that this face does not serve as output
	 */
	public abstract Frac rotationFactor(Face face);

	/**
	 * The friction of this machine, measured against its core, in N*m If this is a
	 * driver, return negative value
	 */
	public abstract double torque();

	public abstract void update(double speed);

}
