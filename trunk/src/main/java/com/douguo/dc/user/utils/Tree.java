package com.douguo.dc.user.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

public class Tree {

	public static void main(String args[]) {
		String[][] arryFun = new String[6][6];
		arryFun[0] = new String[] { "1", "0", "1", "1", "1", "1" };
		arryFun[1] = new String[] { "3", "0", "3", "1", "2", "1" };
		arryFun[2] = new String[] { "2", "1", "2", "2", "1", "1" };
		arryFun[3] = new String[] { "5", "1", "5", "2", "2", "1" };
		arryFun[4] = new String[] { "6", "2", "6", "3", "1", "1" };
		arryFun[5] = new String[] { "4", "3", "4", "2", "1", "1" };
		Tree t = new Tree();
		t.toJson(arryFun, null, null);
	}

	public void toJson(String[][] arry, List<String> sledIds,
			HttpServletResponse response) {
		List<MenuInfo> list = new ArrayList<MenuInfo>();
		Map<String, MenuInfo> mapAll = new HashMap<String, MenuInfo>();
		List<String> listLevel1Ids = new ArrayList<String>();
		for (String[] t : arry) {
			String strMenuId = t[0];
			String strParentId = t[1];
			String strFunctionId = t[2];
			String strLevel = t[3];
			String strSortId = t[4];
			String strVisiable = t[5];
			String strFunctionName = t[6];
			String strUri = t[7];

			MenuInfo menu = new MenuInfo();
			menu.setMenuId(strMenuId);
			menu.setParentId(strParentId);
			menu.setFunctionId(strFunctionId);
			menu.setLevel(strLevel);
			menu.setSortId(strSortId);
			menu.setVisiable(strVisiable);
			menu.setFunctionNmae(strFunctionName);
			menu.setUri(strUri);

			menu.setHasChildren(false);
			menu.setId(strMenuId);
			menu.setText(strFunctionName);
			menu.setValue(strFunctionId);
			menu.setShowcheck(true);
			menu.setComplete(true);
			menu.setIsexpand(true);
			if (sledIds != null && sledIds.contains(strFunctionId)) {// 选中
				menu.setCheckstate("1");
			} else {// 未选中
				menu.setCheckstate("0");
			}

			menu.setChildNodes(null);

			mapAll.put(strMenuId, menu);

			if (strLevel.equals("1")) {
				listLevel1Ids.add(strMenuId);
			}
		}

		for (String id : listLevel1Ids) {
			MenuInfo mm = mapAll.get(id);
			List<MenuInfo> lt = getListMenu(id, mapAll);
			if (lt != null) {
				mm.setHasChildren(true);
				mm.setChildNodes(lt);
			}

			list.add(mm);
		}

		JsonGenerator jsonGenerator = null;
		ObjectMapper objectMapper = null;
		objectMapper = new ObjectMapper();

		try {
			//
			if (response == null) {
				jsonGenerator = objectMapper.getJsonFactory()
						.createJsonGenerator(System.out, JsonEncoding.UTF8);
				objectMapper.writeValue(jsonGenerator, list);
			} else {
				objectMapper.writeValue(response.getWriter(), list);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(list.toArray());
	}

	private static List<MenuInfo> getListMenu(String id,
			Map<String, MenuInfo> mapAll) {
		List<MenuInfo> listT = null;
		for (Entry<String, MenuInfo> entry : mapAll.entrySet()) {
			MenuInfo m = entry.getValue();
			String mParentId = m.getParentId();

			if (id.equals(mParentId)) {
				List<MenuInfo> lt = getListMenu(m.getMenuId(), mapAll);
				if (lt != null) {
					m.setHasChildren(true);
					m.setChildNodes(lt);
				}
				if (listT == null) {
					listT = new ArrayList<MenuInfo>();
				}

				listT.add(m);
			}
		}
		if(listT != null){
			Collections.sort(listT, MenuInfoComparator.getComparator());
		}
		
		return listT;
	}
}

class MenuInfoComparator {
	public static java.util.Comparator getComparator() {
		return new java.util.Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				if (o1 instanceof MenuInfo) {
					return compare((MenuInfo) o1, (MenuInfo) o2);
				} else {
					return 1;
				}
			}

			public int compare(MenuInfo o1, MenuInfo o2) {
				String sortID1 = o1.getSortId();
				String sortID2 = o2.getSortId();

				return compare(Integer.valueOf(sortID1), Integer.valueOf(sortID2));
			}

			public int compare(Integer o1, Integer o2) {
				int val1 = o1.intValue();
				int val2 = o2.intValue();
				return (val1 < val2 ? -1 : (val1 == val2 ? 0 : 1));
			}
		};
	}

}

class MenuInfo {

	String menuId;
	String parentId;
	String functionId;
	String level;
	String sortId;
	String visiable;
	String functionNmae;
	String uri;

	//
	String id;
	String text;
	String value;
	boolean showcheck;
	boolean complete;
	boolean isexpand;
	String checkstate;
	boolean hasChildren;

	public String getFunctionNmae() {
		return functionNmae;
	}

	public void setFunctionNmae(String functionNmae) {
		this.functionNmae = functionNmae;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public boolean isShowcheck() {
		return showcheck;
	}

	public void setShowcheck(boolean showcheck) {
		this.showcheck = showcheck;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public boolean isIsexpand() {
		return isexpand;
	}

	public void setIsexpand(boolean isexpand) {
		this.isexpand = isexpand;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	@JsonProperty
	List<MenuInfo> ChildNodes;
	String url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCheckstate() {
		return checkstate;
	}

	public void setCheckstate(String checkstate) {
		this.checkstate = checkstate;
	}

	@JsonIgnore
	public List<MenuInfo> getChildNodes() {
		return ChildNodes;
	}

	@JsonIgnore
	public void setChildNodes(List<MenuInfo> childNodes) {
		ChildNodes = childNodes;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getSortId() {
		return sortId;
	}

	public void setSortId(String sortId) {
		this.sortId = sortId;
	}

	public String getVisiable() {
		return visiable;
	}

	public void setVisiable(String visiable) {
		this.visiable = visiable;
	}
}
