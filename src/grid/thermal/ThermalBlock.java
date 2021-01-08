package grid.thermal;

import grid.BlockFeature;

/**
 * @author lcy0x1
 * @Date 2021-1-8
 */
public abstract class ThermalBlock extends BlockFeature<ThermalBlock, ThermalSystem> {

	public static class ThermalConfig {
		public double specific_heat, conductivity, radiation, env_temp, max_temp;
	}

	public final ThermalConfig config;

	private double temp_energy = 0;
	private double energy = 0;

	public ThermalBlock(ThermalConfig config) {
		this.config = config;
	}

	/**
	 * relative conductivity of this block, the ratio of heat that will be sent in
	 * each direction, in J/K
	 */
	public double conductivity() {
		return config.conductivity;
	}

	/**
	 * Environment Temperature in K
	 */
	public double envTemp() {
		return config.env_temp;
	}

	/**
	 * maximum temperature
	 */
	public double maxTemp() {
		return config.max_temp;
	}

	/**
	 * update the heat lost to environment
	 */
	public void postUpdate(int freespace) {
		energy += temp_energy;
		double tolose = (temperature() - envTemp()) * radiation() * freespace;
		if (energy > tolose)
			energy -= tolose;
		else
			energy = 0;
	}

	/**
	 * the amount of heat to loss to the environment, in J/K
	 */
	public double radiation() {
		return config.radiation;
	}

	/**
	 * relative specific heat of this block, in J/K
	 */
	public double specificHeat() {
		return config.specific_heat;
	}

	/**
	 * the temperature of this block, in K
	 */
	public double temperature() {
		return energy / specificHeat();
	}

	/**
	 * update the heat going to other objects;
	 */
	public void updateFace(ThermalBlock neighbor) {
		double tempdiff = temperature() - neighbor.temperature();
		double avgcond = 1 / (1 / conductivity() + 1 / neighbor.conductivity());
		neighbor.temp_energy += tempdiff * avgcond;
		temp_energy -= tempdiff * avgcond;
	}

}
