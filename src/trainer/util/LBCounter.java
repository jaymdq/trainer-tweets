package trainer.util;

import java.awt.Label;
import java.util.Vector;

import javax.swing.JComponent;

import stream.StreamObserver;
import stream.StreamWorkerAbs;

@SuppressWarnings("serial")
public class LBCounter extends Label implements StreamObserver{
	private String baseText;
	private Vector<JComponent> components = new Vector<JComponent>();
	private boolean componentStatus = false;
	
	public LBCounter(String baseText){
		super(baseText+"0");
		this.baseText = baseText;
	}
	
	@Override
	public void update(StreamWorkerAbs parent) {
		this.updateCounter(parent.getTotalTweets());
	}	
	
	public void updateCounter(int quantity){
		this.setText(this.baseText+quantity);
		if(quantity > 0 && !componentStatus){
			componentStatus = true;
			updateComponents();
		}else if(quantity == 0 && componentStatus){
			componentStatus = false;
			updateComponents();
		}
	}
	
	public void updateComponents(){
		for(JComponent c: this.components){
			c.setEnabled(componentStatus);
		}
	}
	
	public void forActivate(JComponent[] components){
		if(components == null) return;
		
		for(JComponent c: components){
			this.components.addElement(c);
		}		
	}
}
