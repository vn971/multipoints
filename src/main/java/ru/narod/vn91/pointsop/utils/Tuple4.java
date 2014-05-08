package ru.narod.vn91.pointsop.utils;

public class Tuple4<A, B, C, D> {
	public final A a;
	public final B b;
	public final C c;
	public final D d;

	public Tuple4(A a, B b, C c, D d) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.a == null) ? 0 : this.a.hashCode());
		result = prime * result + ((this.b == null) ? 0 : this.b.hashCode());
		result = prime * result + ((this.c == null) ? 0 : this.c.hashCode());
		result = prime * result + ((this.d == null) ? 0 : this.d.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tuple4 other = (Tuple4) obj;
		if (this.a == null) {
			if (other.a != null)
				return false;
		} else if (!this.a.equals(other.a))
			return false;
		if (this.b == null) {
			if (other.b != null)
				return false;
		} else if (!this.b.equals(other.b))
			return false;
		if (this.c == null) {
			if (other.c != null)
				return false;
		} else if (!this.c.equals(other.c))
			return false;
		if (this.d == null) {
			if (other.d != null)
				return false;
		} else if (!this.d.equals(other.d))
			return false;
		return true;
	}

}
