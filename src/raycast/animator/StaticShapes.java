package raycast.animator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import raycast.entity.geometry.PolyShape;

public class StaticShapes extends AbstractAnimator {

	private static final Color BACKGROUND = Color.BISQUE;

	@Override
	protected void handle(GraphicsContext gc, long now) {
		clearAndFill(gc, BACKGROUND);
		for (PolyShape ps : map.shapes()) {
			ps.getDrawable().draw(gc);
		}
	}

	@Override
	public String toString() {
		return "Static shape";
	}

}