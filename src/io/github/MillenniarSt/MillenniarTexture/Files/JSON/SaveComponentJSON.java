package io.github.MillenniarSt.MillenniarTexture.Files.JSON;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

/*           //////////////////////////////////
*           //								    //
*           //    //          //                //
*           //    ////      ////    //////      //
*           //	  //  //  //  //  //            //
*           //	  //    //    //  //            //
*           //	  //          //    //////      //
*           //    //          //          //	//
*           //    //          //          //    //
*           //    //          //  ////////      //
*           //                                  //
*             /////// Millenniar Studios ///////
*/

public class SaveComponentJSON<Value> implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	private String view;
	private String key;
	private Value values;
	private boolean error;
	private final boolean array;
	private final ArrayList<SaveComponentJSON<?>> children;
	
	SaveComponentJSON(ComponentJSON<Value> comp) {
		this.view = comp.getView();
		this.key = comp.getKey();
		this.values = comp.getValues();
		this.error = comp.isError();
		this.array = comp.isArray();
		this.children = new ArrayList<>();
		for(TreeItem<String> item : comp.getChildren()) {
			this.children.add(((ComponentJSON<?>) item).getSaved());
		}
	}
	
	public String getView() {
		return view;
	}
	void setView(String view) {
		this.view = view;
	}
	public String getKey() {
		return key;
	}
	void setKey(String key) {
		this.key = key;
	}
	public Value getValues() {
		return values;
	}
	void setValues(Value values) {
		this.values = values;
	}
	public boolean isError() {
		return error;
	}
	void setError(boolean error) {
		this.error = error;
	}
	public boolean isArray() {
		return array;
	}
	public ArrayList<SaveComponentJSON<?>> getChildren() {
		return children;
	}
	void setChildren(ObservableList<TreeItem<String>> children) {
		this.children.clear();
		for(TreeItem<String> item : children) {
			this.children.add(((ComponentJSON<?>) item).getSaved());
		}
	}
}
