package raycast.animator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import raycast.entity.geometry.PolyShape;
import utility.IntersectUtil;

public class MultiRayAnimator extends AbstractAnimator {

	private double[] intersectPoint = new double[4];
	private double[] intersectResult = new double[4];

	@Override
	protected void handle(GraphicsContext gc, long now) {
		clearAndFill(gc, Color.ORCHID);
		for (PolyShape shape : map.shapes()) {
			shape.getDrawable().draw(gc);
		}

		drawRays(gc, mouse.x(), mouse.y(), Color.GHOSTWHITE);
	}

	public void drawLine(GraphicsContext gc, Color color, double sx, double sy, double ex, double ey) {
		gc.setLineWidth(1);
		gc.setStroke(color);
		gc.setFill(Color.MAGENTA);
		gc.strokeLine(sx, sy, ex, ey);
		if (map.getDrawIntersectPoint()) {
			gc.fillOval(ex - 5, ey - 5, 10, 10);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getName();
	}

	public void drawRays(GraphicsContext gc, double startX, double startY, Color lightColor) {
		double endX, endY;
		double rayIncrementer = 360d / map.getRayCount();

		for (double rayAngle = 0; rayAngle < 360; rayAngle += rayIncrementer) {
			endX = Math.cos(Math.toRadians(rayAngle));
			endY = Math.sin(Math.toRadians(rayAngle));

			intersectPoint[0] = startX + endX * 700;
			intersectPoint[1] = startY + endY * 700;

			for (PolyShape shape : map.shapes()) {

				for (int i = 0, j = shape.pointCount() - 1; i < shape.pointCount(); i++, j = i - 1) {
					boolean doesIntersect = IntersectUtil.getIntersection(intersectResult, startX, startY,
							startX + endX, startY + endY, shape.pX(i), shape.pY(i), shape.pX(j), shape.pY(j));
					if (doesIntersect && intersectPoint[2] > intersectResult[2]) {
						System.arraycopy(intersectResult, 0, intersectPoint, 0, intersectPoint.length);
					}
				}
			}

			drawLine(gc, Color.BLACK, startX, startY, intersectPoint[0], intersectPoint[1]);
			intersectPoint[2] = Double.MAX_VALUE;
		}
	}
}