package com.capacitorjs.community.plugins.safearea;

import com.getcapacitor.JSObject;

public class SafeAreaInsets {
	private int top, bottom, right, left;

	public SafeAreaInsets() {
		this.clear();
	}

	public int top() { return this.top(null); }

	public int top(Integer value) {
		if(value != null)
			this.top = value;

		return this.top;
	}

	public int bottom() {
		return this.bottom(null);
	}

	public int bottom(Integer value) {
		if(value != null)
			this.bottom = value;

		return this.bottom;
	}

	public int right() {
		return this.right(null);
	}

	public int right(Integer value) {
		if(value != null)
			this.right = value;

		return this.right;
	}

	public int left() {
		return this.left(null);
	}

	public int left(Integer value) {
		if(value != null)
			this.left = value;

		return this.left;
	}

	public void clear() {
		this.top(0);
		this.bottom(0);
		this.right(0);
		this.left(0);
	}

	public JSObject toJSON() {
		JSObject json = new JSObject();

		json.put("top", this.top());
		json.put("bottom", this.bottom());
		json.put("right", this.right());
		json.put("left", this.left());

		return json;
	}
}
