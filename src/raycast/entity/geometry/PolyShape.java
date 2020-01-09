package raycast.entity.geometry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import raycast.entity.Entity;
import raycast.entity.property.Sprite;

public class PolyShape implements Entity {

	private int pointCount;
	private double[][] points;
	private double minX, minY, maxX, maxY;
	private RectangleBounds recBounds;
	private Sprite sprite;

	public PolyShape() {
		sprite = new Sprite() {

			@Override
			public void draw(GraphicsContext gc) {
				gc.setLineWidth(getWidth());
				if (getStroke() != null) {
					gc.setStroke(getStroke());
					gc.strokePolygon(points[0], points[1], pointCount);
				}
				if (getFill() != null) {
					gc.setFill(getFill());
					gc.fillPolygon(points[0], points[1], pointCount);
				}
			}
		};
		sprite.setFill(Color.CYAN);
		sprite.setStroke(Color.GREEN);
	}

	public PolyShape randomize(double centerX, double centerY, double size, int minPoints, int maxPoints) {
		return null;
	}

	public PolyShape setPoints(double... nums) {
		minX = nums[0];
		minY = nums[1];
		
		maxX = nums[0];		
		maxY = nums[1];

		pointCount = nums.length / 2;

		points = new double[2][pointCount];
		int pointIndex = 0;
		for (int i = 0; i < nums.length; i = i + 2) {
			points[0][pointIndex] = nums[i];
			points[1][pointIndex] = nums[i + 1];
			pointIndex++;
			updateMinMax(nums[i], nums[i + 1]);
		}
		recBounds = new RectangleBounds(minX, minY, maxX - minX, maxY - minY);

		return this;
	}

	private void updateMinMax(double x, double y) {
		if (x < minX)
			minX = x;
		else if (x > maxX)
			maxX = x;

		if (y < minY)
			minY = y;
		else if (y > maxY)
			maxY = y;
	}

	public int pointCount() {
		return pointCount;
	}

	public double[][] points() {
		return points;
	}

	public double pX(int index) {
		return points[0][index];
	}

	public double pY(int index) {
		return points[1][index];
	}

	public boolean isDrawable() {
		return true;
	}

	public Sprite getDrawable() {
		return sprite;
	}

	public void drawCorners(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		for (int i = 0; i < pointCount; i++) {
			gc.fillText(Integer.toString(i), points[0][i] - 5, points[1][i] - 5);
			gc.fillOval(points[0][i] - 5, points[1][i] - 5, 10, 10);
		}
	}

	public RectangleBounds getBounds() {
		return recBounds;
	}

}
