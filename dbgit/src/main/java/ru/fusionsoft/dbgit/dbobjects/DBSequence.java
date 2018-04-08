package ru.fusionsoft.dbgit.dbobjects;

import ru.fusionsoft.dbgit.utils.CalcHash;

public class DBSequence extends DBSQLObject {
	protected Long value;
	private String name;

	public DBSequence(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}
	
	@Override
	public String getHash() {
		
		CalcHash ch = new CalcHash();
		ch.addData(getSchema());
		ch.addData(getName());
		ch.addData(getSql());
		ch.addData(getValue().toString());
		
		return ch.calcHashStr();		
	}
}
