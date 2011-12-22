package ru.narod.vn91.pointsop.utils;

public class Tuple9<A, B, C, D, E, F, G, H, I> {
	public final A a;
	public final B b;
	public final C c;
	public final D d;
	public final E e;
	public final F f;
	public final G g;
	public final H h;
	public final I i;

	public Tuple9(A a, B b, C c, D d, E e, F f, G g, H h, I i) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		this.g = g;
		this.h = h;
		this.i = i;
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
		result = prime * result + ((this.g == null) ? 0 : this.g.hashCode());
		result = prime * result + ((this.h == null) ? 0 : this.h.hashCode());
		result = prime * result + ((this.i == null) ? 0 : this.i.hashCode());
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
		Tuple9 other = (Tuple9) obj;
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
		if (this.g == null) {
			if (other.g != null)
				return false;
		} else if (!this.g.equals(other.g))
			return false;
		if (this.h == null) {
			if (other.h != null)
				return false;
		} else if (!this.h.equals(other.h))
			return false;
		if (this.i == null) {
			if (other.i != null)
				return false;
		} else if (!this.i.equals(other.i))
			return false;
		return true;
	}

}
