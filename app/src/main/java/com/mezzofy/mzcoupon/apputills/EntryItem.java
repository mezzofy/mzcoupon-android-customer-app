package com.mezzofy.mzcoupon.apputills;


public class EntryItem implements Item{

	public final String title;
	public final int groupid;
	public final int itemid;

	public EntryItem(String title,int groupid,int itemid) {
		this.title = title;
		this.groupid = groupid;
		this.itemid = itemid;
	}
	
	@Override
	public boolean isSection() {
		return false;
	}

}
