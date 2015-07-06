package trainer.util;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import dictionary.dictionaryEntry.DictionaryEntry;

@SuppressWarnings("serial")
public class TTokensModel extends DefaultTableModel implements Iterable<DictionaryEntry> {

	public TTokensModel(){
		super(null, new String[]{
			"Token",
			"Categories"
		});
	}
	
	public void clear(){
		int limit = this.getRowCount();
		for(int i = 0; i < limit; i++){
			this.removeRow(0);
		}
	}
	
	public void addToken(String token, Vector<String> categories){
		int index = this.indexOf(token);
		if(index < 0){
			Object[] row = new Object[2];
			row[0] = token;
			row[1] = categories;
			this.addRow(row);
		}else{
			updateCategories(index, categories);
		}
	}
	
	public void replaceToken(String token, Vector<String> categories){
		int index = this.indexOf(token);
		if(index < 0)
			this.addToken(token, categories);
		else
			this.setValueAt(categories, index, 1);
	}
	
	public int indexOf(String token){
		int index = -1;
		for(int i=0; index < 0 && i < this.getRowCount(); i++){
			if( ( (String)this.getValueAt(i, 0) ).equalsIgnoreCase(token) ){
				index = i;
			}
		}
		return index;
	}
	
	private void updateCategories(int index, Vector<String> categories){
		@SuppressWarnings("unchecked")
		Vector<String> valueAt = (Vector<String>)this.getValueAt(index, 1);
		Vector<String> tmp = valueAt;
		for(String category: categories)
			if(!tmp.contains(category))
				tmp.add(category);
		this.setValueAt(tmp, index, 1);
	}

	@Override
	public Iterator<DictionaryEntry> iterator() {
		return new TTokensModelIterator();
	}

	private class TTokensModelIterator implements Iterator<DictionaryEntry> {
		
		private int cursor;
		
		public TTokensModelIterator(){
			this.cursor = 0;
		}
		
		@Override
		public boolean hasNext() {
			return this.cursor < TTokensModel.this.getRowCount();
		}

		@Override
		public DictionaryEntry next() {
			if(this.hasNext()){
				int current = this.cursor;
				this.cursor++;
				String token = (String)TTokensModel.this.getValueAt(current, 0);
				@SuppressWarnings("unchecked")
				Vector<String> categories = (Vector<String>)TTokensModel.this.getValueAt(current, 1);
				return new DictionaryEntry( token, categories.toArray(new String[categories.size()]) );
			}
			return null;
		}

		@Override
		public void remove() {}
		
	}
}