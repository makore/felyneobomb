package ua.felyne.game.shared.agwtapi.graphics.GWT;


import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import ua.felyne.game.shared.agwtapi.graphics.ICanvas;

import java.util.ArrayList;

public class Canvas implements ICanvas {

	private com.google.gwt.canvas.client.Canvas canvas;
	private Context2d context;
	private ArrayList<com.google.gwt.canvas.client.Canvas> buffer;
	
	public Canvas()
	{
		canvas = com.google.gwt.canvas.client.Canvas.createIfSupported();
		if (canvas == null) {
			RootPanel.get(null).add(new Label("No canvas!!!"));
			return;
		}
		buffer = new ArrayList<com.google.gwt.canvas.client.Canvas>();

	}
	
	public Context2d getContext() {
		return context;
	}
	
	public Context2d getContext2dCanvasBuffer(int n) {
		return buffer.get(n).getContext2d();
	}
	
	public CanvasElement getCanvasElementBuffer(int n) {
		return buffer.get(n).getCanvasElement();
	}
	
	@Override
	public void init(String name, int width, int height)
	{
		canvas.setWidth(width+"px");
		canvas.setHeight(height+"px");
		canvas.setCoordinateSpaceWidth(width);
		canvas.setCoordinateSpaceHeight(height);
		context = canvas.getContext2d();
		RootPanel.get("game").add(canvas);
	}
	
	@Override
	public void writeFPS(double fps) {
		context.save();
		context.setFillStyle("#fff");
		context.setFont("bold 12px sans-serif");
		context.fillText("FPS: " + Double.toString(fps), 10, 10);
		context.restore();
	}
	

	/*Funciones de buffers*/
	@Override
	public int addBuffer()
	{
		com.google.gwt.canvas.client.Canvas cv = com.google.gwt.canvas.client.Canvas.createIfSupported();
		cv.setWidth(canvas.getOffsetHeight()+"px");
		cv.setHeight(canvas.getOffsetWidth()+"px");
		cv.setCoordinateSpaceWidth(canvas.getOffsetWidth());
		cv.setCoordinateSpaceHeight(canvas.getOffsetHeight());
		buffer.add(cv);
		return buffer.size()-1;
	}
	
	public void clearBuffer(int width, int height) {
		context.setFillStyle(CssColor.make(255, 255, 255));
		context.fillRect(0, 0, width, height);
		for(int i=0; i < buffer.size(); i++)
		{
			buffer.get(0).getContext2d().save();
			buffer.get(0).getContext2d().setTransform(1, 0, 0, 1, 0, 0);
			buffer.get(0).getContext2d().clearRect(0,0,width, height);
			buffer.get(0).getContext2d().restore();
		}
	}
	
	public boolean rotate(int id, double angle)
	{
		if(id < buffer.size())
		{
			buffer.get(id).getContext2d().rotate(angle);
			return true;
		}
		else
			return false;
	}
	
	public boolean scale(int id, double x, double y)
	{
		if(id < buffer.size())
		{
			buffer.get(id).getContext2d().scale(x, y);
			return true;
		}
		else
			return false;
	}
	
	public void drawInBuffer(int id)
	{
		buffer.get(id).getContext2d().drawImage(context.getCanvas(), 0, 0);
	}
	
	public void drawFromBuffer(int n) {
		canvas.getContext2d().drawImage(getCanvasElementBuffer(n), 0, 0);
	}
	
	public Context2d getContext2d(int id)
	{
		return buffer.get(id).getContext2d();
	}
	
}