package raycast.entity;

import raycast.entity.property.Drawable;

public interface Entity {
	
	public abstract Drawable<?> getDrawable();
	
	public abstract boolean isDrawable();
	
}
