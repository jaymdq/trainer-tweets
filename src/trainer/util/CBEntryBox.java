package trainer.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class CBEntryBox extends JComboBox<String> {
	protected HashMap<Integer, JComponent[]> components = new HashMap<Integer, JComponent[]>(2);
	protected CBEntryBox instance = null; 
	
	public CBEntryBox(){
		this.instance = this;
		this.addActionListener(new OnChangeListener());
	}
	
	public void addEntry(String text, JComponent[] component){
		this.addItem(text);
		this.components.put(this.getItemCount(), component);
	}
	
	
	private class OnChangeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Integer item = instance.getSelectedIndex()+1;
			for(Integer i : components.keySet()){
				if(i.compareTo(item) == 0){
					for(JComponent component: components.get(i))
						component.setVisible(true);
				}else{
					for(JComponent component: components.get(i))
						component.setVisible(false);
				}
			}
		}
		
	}
	
}
