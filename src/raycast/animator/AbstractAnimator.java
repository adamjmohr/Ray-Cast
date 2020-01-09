package raycast.animator;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import raycast.CanvasMap;
import raycast.entity.FpsCounter;
import raycast.entity.geometry.PolyShape;
import utility.Point;

/**
 * this class must extend {@link AnimationTimer}. job of this class is to hold
 * common functionality among animators.
 * 
 * @author Shahriar (Shawn) Emami
 * @version Jan 13, 2019
 */
public abstract class AbstractAnimator extends AnimationTimer {

	/**
	 * create a protected class variable of type {@link CanvasMap} and name it map.
	 */
	protected CanvasMap map;
	/**
	 * create a protected class variable of type {@link Point} and name it mouse.
	 */
	protected Point mouse;

	private FpsCounter fps;

	/**
	 * create a protected constructor and initialize the
	 * {@link AbstractAnimator#mouse} variable
	 */
	protected AbstractAnimator() {
		mouse = new Point();
		fps = new FpsCounter(1, 25);
		fps.getDrawable().setFill(Color.RED).setStroke(Color.BLACK).setWidth(1);
	}

	/**
	 * create a setter called setCanvas to inject (set) the {@link CanvasMap}
	 * 
	 * @param map - {@link CanvasMap} object
	 */
	public void setCanvas(CanvasMap map) {
		this.map = map;
	}

	/**
	 * create a method called mouseDragged that is called every time the position of
	 * mouse changes.
	 * 
	 * @param e - {@link MouseEvent} object that hold the details of the mouse. use
	 *          {@link MouseEvent#getX} and {@link MouseEvent#getY}
	 */
	public void mouseDragged(MouseEvent e) {
		mouse.set(e.getX(), e.getY());
	}

	/**
	 * create a method called mouseMoved that is called every time the position of
	 * mouse changes.
	 * 
	 * @param e - {@link MouseEvent} object that hold the details of the mouse. use
	 *          {@link MouseEvent#getX} and {@link MouseEvent#getY}
	 */
	public void mouseMoved(MouseEvent e) {
		mouse.move(e.getX(), e.getY());
		mouse.set(e.getX(), e.getY());
	}

	public void clearAndFill(GraphicsContext gc, Color background) {
		gc.setFill(background);
		gc.clearRect(0, 0, map.w(), map.h());
		gc.fillRect(0, 0, map.w(), map.h());
	}

	/**
	 * <p>
	 * create a method called handle that is inherited from
	 * {@link AnimationTimer#handle(long)}. this method is called by JavaFX
	 * application, it should not be called directly.
	 * </p>
	 * <p>
	 * inside of this method call the abstract handle method
	 * {@link AbstractAnimator#handle(GraphicsContext, long)}.
	 * {@link GraphicsContext} can be retrieved from {@link CanvasMap#gc()}
	 * </p>
	 * 
	 * @param now - current time in nanoseconds, represents the time that this
	 *            function is called.
	 */
	public void handle(long now) {
		GraphicsContext gc = map.gc();

		if (map.getDrawFPS())
			fps.calculateFPS(now);

		handle(map.gc(), now);
		if (map.getDrawLightSource()) {
			gc.setFill(Color.MAGENTA);
			gc.fillOval(mouse.x() - 5, mouse.y() - 5, 10, 10);
		}

		if (map.getDrawShapeJoints() || map.getDrawBounds()) {
			for (PolyShape ps : map.shapes()) {
				if (map.getDrawBounds()) {
					ps.getBounds().draw(gc);
				}
				if (map.getDrawShapeJoints()) {
					ps.drawCorners(gc);
				}
			}
		}
		if (map.getDrawFPS()) {
			fps.getDrawable().draw(gc);
		}
	}

	/**
	 * create a protected abstract method called handle, this method to be
	 * overridden by subclasses.
	 * 
	 * @param gc  - {@link GraphicsContext} object.
	 * @param now - current time in nanoseconds, represents the time that this
	 *            function is called.
	 */
	protected abstract void handle(GraphicsContext gc, long now);
}
