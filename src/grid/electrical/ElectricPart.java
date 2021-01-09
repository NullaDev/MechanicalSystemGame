package grid.electrical;

import grid.BlockFeature;
import util.Face;

public abstract class ElectricPart extends BlockFeature<ElectricPart, ElectricSystem> {

	public static abstract class Wire extends ElectricPart {

		public abstract double resistance();
		
		public abstract boolean connected();
		
	}
	
	public static abstract class Transformer extends ElectricPart {
		
		public abstract double voltageRatio(Face face);
		
	}
	
	public static abstract class Device extends ElectricPart {
		

		public static class ElectricConfig {
			public double maxCurrent, maxVoltage, resistance, impedance;
		}

		public final ElectricConfig config;

		public Device(ElectricConfig config) {
			this.config = config;
		}

		public double maxCurrent() {
			return config.maxCurrent;
		}

		public double maxVoltage() {
			return config.maxVoltage;
		}

		public double resistance() {
			return config.resistance;
		}

		public double impedance() {
			return config.impedance;
		}

		public double driveCurrent() {
			return 0;
		}

		public double driveVoltage() {
			return 0;
		}
		
	}
	
	public abstract boolean connectTo(Face face);
	
	public abstract void preupdate(double current, double voltage);
	
	public abstract void update(double current, double voltage);

}
