package ru.narod.vn91.pointsop.utils;

public class Tuple6<A, B, C, D, E, F, G> {
	public final A a;
	public final B b;
	public final C c;
	public final D d;
	public final E e;
	public final F f;

	public Tuple6(A a, B b, C c, D d, E e, F f) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.a == null) ? 0 : this.a.hashCode());
		result = prime * result + ((this.b == null) ? 0 : this.b.hashCode());
		result = prime * result + ((this.c == null) ? 0 : this.c.hashCode());
		result = prime * result + ((this.d == null) ? 0 : this.d.hashCode());
		result = prime * result + ((this.e == null) ? 0 : this.e.hashCode());
		result = prime * result + ((this.f == null) ? 0 : this.f.hashCode());
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
		Tuple6 other = (Tuple6) obj;
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
		if (this.e == null) {
			if (other.e != null)
				return false;
		} else if (!this.e.equals(other.e))
			return false;
		if (this.f == null) {
			if (other.f != null)
				return false;
		} else if (!this.f.equals(other.f))
			return false;
		return true;
	}

}
