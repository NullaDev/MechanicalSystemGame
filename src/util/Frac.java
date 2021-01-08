package util;

/**
 * represents a fraction
 * 
 * @author lcy0x1
 * @Date 2020-9-24
 */
public class Frac implements Comparable<Frac> {

	public static final Frac ONE = new Frac(1, 1);

	public static Frac mult(Frac f0, Frac f1) {
		long gcd0 = gcd(f0.num, f1.den);
		long gcd1 = gcd(f1.num, f0.den);
		long num = Math.multiplyExact(f0.num / gcd0, f1.num / gcd1);
		long den = Math.multiplyExact(f0.den / gcd1, f1.den / gcd0);
		return new Frac(num, den);
	}

	private static long gcd(long a, long b) {
		long max = Math.max(a, b);
		long min = Math.min(a, b);
		return min == 0 ? max : gcd(min, max % min);
	}

	public long num, den;
	public boolean negative, locked = false;

	public Frac(long num, long den) {
		this.num = Math.abs(num);
		this.den = Math.abs(den);
		this.negative = (num < 0) ^ (den < 0);
		validate();
	}

	public void add(Frac o) {
		if (locked)
			throw new ArithmeticException("operation on locked fraction");
		long gcd = gcd(den, o.den);
		long v0 = Math.multiplyExact(num, o.den / gcd);
		long v1 = Math.multiplyExact(o.num, den / gcd);
		if (negative)
			v0 = -v0;
		if (o.negative)
			v1 = -v1;
		num = Math.addExact(v0, v1);
		this.negative = num < 0;
		this.num = Math.abs(num);
		den = Math.multiplyExact(den, o.den / gcd);
		validate();
	}

	@Override
	public int compareTo(Frac o) {
		if (equals(o))
			return 0;
		return Double.compare(getVal(), o.getVal());
	}

	public void divide(Frac base) {
		if (locked)
			throw new ArithmeticException("operation on locked fraction");
		if (base.num == 0)
			throw new ArithmeticException("divide by 0");
		long gcd0 = gcd(num, base.num);
		long gcd1 = gcd(base.den, den);
		num = Math.multiplyExact(num / gcd0, base.den / gcd1);
		den = Math.multiplyExact(den / gcd1, base.num / gcd0);
		this.negative ^= base.negative;
		validate();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Frac) {
			Frac f = (Frac) o;
			return f.num == num && f.den == den && f.negative == negative;
		}
		return false;
	}

	public double getVal() {
		return (this.negative ? -1 : 1) * num / den;
	}

	public void lock() {
		this.locked = true;
	}

	public void times(Frac base) {
		if (locked)
			throw new ArithmeticException("operation on locked fraction");
		long gcd0 = gcd(num, base.den);
		long gcd1 = gcd(base.num, den);
		num = Math.multiplyExact(num / gcd0, base.num / gcd1);
		den = Math.multiplyExact(den / gcd1, base.den / gcd0);
		this.negative ^= base.negative;
		validate();
	}

	@Override
	public String toString() {
		return num + "/" + den;
	}

	private void validate() {
		long gcd = gcd(num, den);
		num /= gcd;
		den /= gcd;
	}

}