package raycast;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import raycast.animator.AbstractAnimator;
import raycast.entity.geometry.PolyShape;

/**
 * this class represents the drawing area. it is backed by {@link Canvas} class.
 * this class itself does not handle any of the drawing. this task is accomplished
 * by the {@link AnimationTimer}.
 * 
 * @author Shahriar (Shawn) Emami
 * @version Jan 13, 2019
 */
public class CanvasMap implements CanvasMapInterface {

	/**
	 * <p>
	 * create a {@link Canvas} object call board. it provides the tools to draw in JavaFX. this is also a {@link Node}
	 * which means can be added to our JavaFX application. the object needed to draw on a {@link Canvas}
	 * is {@link GraphicsContext} which is retrieved using {@link Canvas#getGraphicsContext2D()} method.
	 * </p>
	 */
	private Canvas board;
	/**
	 * create a {@link AbstractAnimator} called animator. {@link AnimationTimer} provides 
	 * most common functionally needed to draw animations of ray casting.
	 */
	private AbstractAnimator animator;
	/**
	 * <p>
	 * create an {@link IntegerProperty} called rayCount to keep track of ray count changes.<br>
	 * this variable can be initialized with {@link SimpleIntegerProperty}
	 * </p>
	 * 
	 * <pre>
	 * IntegerProperty i1 = new SimpleIntegerProperty( 1);
	 * IntegerProperty i2 = new SimpleIntegerProperty();
	 * i1.bind( i2);
	 * i2.set( 100);
	 * System.out.println( i1.get()); // prints 100
	 * </pre>
	 * <p>
	 * create a getter that returns {@link IntegerProperty} and a method that returns {@link IntegerProperty#get()}
	 * </p>
	 */
	private IntegerProperty rayCount;
	
	/**
	 * <p>
	 * create a set of {@link BooleanProperty}s to track some drawing options.<br>
	 * create: drawLightSource, drawIntersectPoint, drawShapeJoints, drawSectors, drawBounds, drawFPS<br>
	 * these variables can be initialized with {@link SimpleBooleanProperty}
	 * </p>
	 * 
	 * <pre>
	 * BooleanProperty b1 = new SimpleBooleanProperty( false);
	 * BooleanProperty b2 = new SimpleBooleanProperty();
	 * b1.bind( b2);
	 * b2.set( true);
	 * System.out.println( b1.get()); // prints true
	 * </pre>
	 * <p>
	 * create a getter that returns {@link BooleanProperty} and a method that returns {@link BooleanProperty#get()}
	 * for each BooleanProperty.
	 * </p>
	 */
	private BooleanProperty drawLightSource, drawIntersectPoint, drawShapeJoints, drawSectors, drawBounds, drawFPS;
	
	private List<PolyShape> shapes;
	
	/**
	 * create a constructor and initialize all class variables.
	 */
	public CanvasMap() {
		board = new Canvas();
		rayCount = new SimpleIntegerProperty();
		drawBounds = new SimpleBooleanProperty();
		drawFPS = new SimpleBooleanProperty();
		drawIntersectPoint = new SimpleBooleanProperty();
		drawLightSource = new SimpleBooleanProperty();
		drawSectors = new SimpleBooleanProperty();
		drawShapeJoints = new SimpleBooleanProperty();
		shapes = new ArrayList<PolyShape>(20);
	}
	/**
	 * create the property class variables functions here
	 */
	public IntegerProperty rayCountProperty() {
		return rayCount;
	}
	
	public int getRayCount() {
		return rayCount.get();
	}
	
	public BooleanProperty drawLightSourceProperty() {
		return drawLightSource;
	}
	
	public boolean getDrawLightSource() {
		return drawLightSource.get();
	}

	public BooleanProperty drawIntersectPointProperty() {
		return drawIntersectPoint;
	}
	
	public boolean getDrawIntersectPoint() {
		return drawIntersectPoint.get();
	}

	public BooleanProperty drawShapeJointsProperty() {
		return drawShapeJoints;
	}
	
	public boolean getDrawShapeJoints() {
		return drawShapeJoints.get();
	}

	public BooleanProperty drawSectorsProperty() {
		return drawSectors;
	}
	
	public boolean getDrawSectors() {
		return drawSectors.get();
	}

	public BooleanProperty drawBoundsProperty() {
		return drawBounds;
	}
	
	public boolean getDrawBounds() {
		return drawBounds.get();
	}

	public BooleanProperty drawFPSProperty() {
		return drawFPS;
	}
	
	public boolean getDrawFPS() {
		return drawFPS.get();
	}
	
	/**
	 * create a method called setAnimator.
	 * set an {@link AbstractAnimator}. if an animator exists {@link CanvasMap#stop()} it and 
	 * call {@link CanvasMap#removeMouseEvents()}. then set the new animator and
	 * call {@link CanvasMap#start()} and {@link CanvasMap#registerMouseEvents()}.
	 * @param newAnimator - new {@link AbstractAnimator} object 
	 * @return the current instance of this object
	 */
	public CanvasMap setAnimator(AbstractAnimator newAnimator) {
		if (animator != null) {
			stop();
			removeMouseEvents();
		}
		animator = newAnimator;
		start();
		registerMouseEvents();
		return this;
	}
	
	/**
	 * <p>create a method called registerMouseEvents.
	 * register the mouse events for when the mouse is 
	 * {@link MouseEvent#MOUSE_MOVED} or {@link MouseEvent#MOUSE_DRAGGED}.<br>
	 * call {@link CanvasMap#addEventHandler} twice and pass to it
	 * {@link MouseEvent#MOUSE_DRAGGED}, {@link animator#mouseDragged} and
	 * {@link MouseEvent#MOUSE_MOVED}, {@link animator#mouseMoved}.</p>
	 * <p>a method can be passed directly as an argument if the method signature matches
	 * the functional interface. in this example you will pass the animator method using
	 * object::method syntax.</p>
	 */
	public void registerMouseEvents() {
		addEventHandler(MouseEvent.MOUSE_DRAGGED, animator::mouseDragged);
		addEventHandler(MouseEvent.MOUSE_MOVED, animator::mouseMoved);
	}

	/**
	 * <p>create a method called removeMouseEvents.
	 * remove the mouse events for when the mouse is 
	 * {@link MouseEvent#MOUSE_MOVED} or {@link MouseEvent#MOUSE_DRAGGED}.<br>
	 * call {@link CanvasMap#removeEventHandler } twice and pass to it
	 * {@link MouseEvent#MOUSE_DRAGGED}, {@link animator#mouseDragged} and
	 * {@link MouseEvent#MOUSE_MOVED}, {@link animator#mouseMoved}.</p>
	 * <p>a method can be passed directly as an argument if the method signature matches
	 * the functional interface. in this example you will pass the animator method using
	 * object::method syntax.</p>
	 */
	public void removeMouseEvents() {
		removeEventHandler(MouseEvent.MOUSE_DRAGGED, animator::mouseDragged);
		removeEventHandler(MouseEvent.MOUSE_MOVED, animator::mouseMoved);
	}

	/**
	 * <p>
	 * register the given {@link EventType} and {@link EventHandler}
	 * </p>
	 * @param event - an event such as {@link MouseEvent#MOUSE_MOVED}.
	 * @param handler - a lambda to be used when registered event is triggered.
	 */
	public < E extends Event> void addEventHandler( EventType< E> event, EventHandler< E> handler){
		board.addEventHandler( event, handler);
	}

	/**
	 * <p>
	 * remove the given {@link EventType} registered with {@link EventHandler}
	 * </p>
	 * @param event - an event such as {@link MouseEvent#MOUSE_MOVED}.
	 * @param handler - a lambda to be used when registered event is triggered.
	 */
	public < E extends Event> void removeEventHandler( EventType< E> event, EventHandler< E> handler){
		board.removeEventHandler( event, handler);
	}


	/**
	 * create a method called start.
	 * start the animator. {@link AnimationTimer#start()}
	 */
	public void start() {
		animator.start();
	}

	/**
	 * create a method called stop.
	 * stop the animator. {@link AnimationTimer#stop()}
	 */
	public void stop() {
		animator.stop();
	}

	/**
	 * create a method called getCanvas.
	 * get the JavaFX {@link Canvas} node 
	 * @return {@link Canvas} node 
	 */
	public Canvas getCanvas() {
		return board;
	}

	/**
	 * create a method called gc.
	 * get the {@link GraphicsContext} of {@link Canvas} that allows direct drawing.
	 * @return {@link GraphicsContext} of {@link Canvas}
	 */
	public GraphicsContext gc() {
		return board.getGraphicsContext2D();
	}

	/**
	 * create a method called h.
	 * get the height of the map, {@link Canvas#getHeight()}
	 * @return height of canvas
	 */
	public double h() {
		return board.getHeight();
	}

	/**
	 * create a method called w.
	 * get the width of the map, {@link Canvas#getWidth()}
	 * @return width of canvas
	 */
	public double w() {
		return board.getWidth();
	}
	
	@Override
	public List<PolyShape> shapes() {
		return shapes;
	}
	
	@Override
	public void addSampleShapes() {
		PolyShape shape1 = new PolyShape();
		shape1.setPoints(300, 250, 50, 70, 150, 170);
		shape1.getDrawable().setFill(Color.TEAL).setStroke(Color.BLACK).setWidth(5);
		shapes.add(shape1);
		
		PolyShape shape2 = new PolyShape();
		shape2.setPoints(30, 40, 400, 60, 90, 80);
		shape2.getDrawable().setFill(Color.PALEGREEN).setStroke(Color.BLACK).setWidth(3);
		shapes.add(shape2);
		
		PolyShape shape3 = new PolyShape();
		shape3.setPoints(350, 200, 475, 110, 130, 500);
		shape3.getDrawable().setFill(Color.TOMATO).setStroke(Color.BLACK).setWidth(4);
		shapes.add(shape3);
		
	}
}
