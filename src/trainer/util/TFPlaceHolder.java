package trainer.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TFPlaceHolder extends JTextField {
	
	private TFPlaceHolder instance = null;
	private String placeholder = "";
	private Font originalFont = this.getFont();
	private Font placeHolderFont = new Font("SansSerif", Font.ITALIC, 12);
	private Color originalColor = this.getForeground();
	private Color placeHolderColor = new Color(153, 153, 153);
		
	public TFPlaceHolder(){
		super();
		this.instance = this;
		this.addFocusListener(new OnFocusEvent());
	}
	
	public TFPlaceHolder(String placeholder){
		super();
		this.instance = this;
		this.addFocusListener(new OnFocusEvent());
		this.placeholder = placeholder;
		clear();
	}
	
	public void clear(){
		this.setText(placeholder);
		change(placeHolderFont, placeHolderColor);
	}
	
	public void setPlaceholder(String placeholder) {
		if(this.getText().trim().equals(this.placeholder))
			this.setText(placeholder);
		this.placeholder = placeholder;
	}

	private void change(Font font, Color color){
		this.setFont(font);
		this.setForeground(color);
	}
	
	private class OnFocusEvent implements FocusListener {
		
		@Override
		public void focusLost(FocusEvent e) {
			if(instance.getText().trim().isEmpty()){
				instance.setText(placeholder);
				change(placeHolderFont, placeHolderColor);
			}
		}
		
		@Override
		public void focusGained(FocusEvent e) {
			if(instance.getText().trim().equals(placeholder)){
				instance.setText("");
				change(originalFont, originalColor);
			}
		}
	}
}
