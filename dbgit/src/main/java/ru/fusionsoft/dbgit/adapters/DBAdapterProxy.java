package ru.fusionsoft.dbgit.adapters;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.Map;

import ru.fusionsoft.dbgit.core.SchemaSynonim;
import ru.fusionsoft.dbgit.dbobjects.DBConstraint;
import ru.fusionsoft.dbgit.dbobjects.DBFunction;
import ru.fusionsoft.dbgit.dbobjects.DBIndex;
import ru.fusionsoft.dbgit.dbobjects.DBPackage;
import ru.fusionsoft.dbgit.dbobjects.DBProcedure;
import ru.fusionsoft.dbgit.dbobjects.DBRole;
import ru.fusionsoft.dbgit.dbobjects.DBSchema;
import ru.fusionsoft.dbgit.dbobjects.DBSchemaObject;
import ru.fusionsoft.dbgit.dbobjects.DBSequence;
import ru.fusionsoft.dbgit.dbobjects.DBTable;
import ru.fusionsoft.dbgit.dbobjects.DBTableData;
import ru.fusionsoft.dbgit.dbobjects.DBTableField;
import ru.fusionsoft.dbgit.dbobjects.DBTableSpace;
import ru.fusionsoft.dbgit.dbobjects.DBTrigger;
import ru.fusionsoft.dbgit.dbobjects.DBUser;
import ru.fusionsoft.dbgit.dbobjects.DBView;
import ru.fusionsoft.dbgit.meta.IMapMetaObject;

public class DBAdapterProxy implements IDBAdapter {
	private IDBAdapter adapter;
	private SchemaSynonim ss;	
	

	public DBAdapterProxy(IDBAdapter adapter) {
		super();
		this.adapter = adapter;
		
		ss = SchemaSynonim.getInctance();
	}

	public void setConnection(Connection conn) {
		adapter.setConnection(conn);
	}

	public Connection getConnection() {
		return adapter.getConnection();
	}

	public void registryMappingTypes() {
		adapter.registryMappingTypes();
	}

	public IFactoryDBAdapterRestoteMetaData getFactoryRestore() {
		return adapter.getFactoryRestore();
	}

	public void setDumpSqlCommand(OutputStream stream, Boolean isExecSql) {
		adapter.setDumpSqlCommand(stream, isExecSql);
	}

	public OutputStream getStreamOutputSqlCommand() {
		return adapter.getStreamOutputSqlCommand();
	}

	public Boolean isExecSql() {
		return adapter.isExecSql();
	}

	public void startUpdateDB() {
		adapter.startUpdateDB();
	}

	public void restoreDataBase(IMapMetaObject updateObjs) throws Exception {
		//TODO replace IMapMetaObject
		adapter.restoreDataBase(updateObjs);
	}

	public void deleteDataBase(IMapMetaObject delObjs) throws Exception {
		//TODO replace IMapMetaObject
		adapter.deleteDataBase(delObjs);
	}
	/*
	protected IMapMetaObject mapSynonimSchema(IMapMetaObject objs) {
		
	}
	 */
	public void endUpdateDB() {
		adapter.endUpdateDB();
	}

	public IMapMetaObject loadCustomMetaObjects() {
		return adapter.loadCustomMetaObjects();
	}

	public Map<String, DBSchema> getSchemes() {
		return adapter.getSchemes();
	}

	public Map<String, DBTableSpace> getTableSpaces() {
		return adapter.getTableSpaces();
	}

	public Map<String, DBSequence> getSequences(String schema) {
		return schemaSynonimMap(adapter.getSequences(getSchemaMap(schema)));
	}

	public DBSequence getSequence(String schema, String name) {
		return schemaSynonimMap(adapter.getSequence(getSchemaMap(schema), name));
	}

	public Map<String, DBTable> getTables(String schema) {
		return schemaSynonimMap(adapter.getTables(getSchemaMap(schema)));
	}

	public DBTable getTable(String schema, String name) {
		return schemaSynonimMap(adapter.getTable(getSchemaMap(schema), name));
	}

	public Map<String, DBTableField> getTableFields(String schema, String nameTable) {
		return adapter.getTableFields(getSchemaMap(schema), nameTable);
	}

	public Map<String, DBIndex> getIndexes(String schema, String nameTable) {
		return schemaSynonimMap(adapter.getIndexes(getSchemaMap(schema), nameTable));
	}

	public Map<String, DBConstraint> getConstraints(String schema, String nameTable) {
		return schemaSynonimMap(adapter.getConstraints(getSchemaMap(schema), nameTable));
	}

	public Map<String, DBView> getViews(String schema) {
		return schemaSynonimMap(adapter.getViews(getSchemaMap(schema)));
	}

	public DBView getView(String schema, String name) {
		return schemaSynonimMap(adapter.getView(getSchemaMap(schema), name));
	}

	public Map<String, DBPackage> getPackages(String schema) {
		return schemaSynonimMap(adapter.getPackages(getSchemaMap(schema)));
	}

	public DBPackage getPackage(String schema, String name) {
		return schemaSynonimMap(adapter.getPackage(getSchemaMap(schema), name));
	}

	public Map<String, DBProcedure> getProcedures(String schema) {
		return schemaSynonimMap(adapter.getProcedures(getSchemaMap(schema)));
	}

	public DBProcedure getProcedure(String schema, String name) {
		return schemaSynonimMap(adapter.getProcedure(getSchemaMap(schema), name));
	}

	public Map<String, DBFunction> getFunctions(String schema) {
		return schemaSynonimMap(adapter.getFunctions(getSchemaMap(schema)));
	}

	public DBFunction getFunction(String schema, String name) {
		return schemaSynonimMap(adapter.getFunction(getSchemaMap(schema), name));
	}

	public Map<String, DBTrigger> getTriggers(String schema) {
		return schemaSynonimMap(adapter.getTriggers(getSchemaMap(schema)));
	}

	public DBTrigger getTrigger(String schema, String name) {
		return schemaSynonimMap(adapter.getTrigger(getSchemaMap(schema), name));
	}

	public DBTableData getTableData(String schema, String nameTable, int paramFetch) {
		return adapter.getTableData(getSchemaMap(schema), nameTable, paramFetch);
	}

	public Map<String, DBUser> getUsers() {
		return adapter.getUsers();
	}

	public Map<String, DBRole> getRoles() {
		return adapter.getRoles();
	}
	
	public <T extends DBSchemaObject> T schemaSynonimMap(T object) {
		if (object == null) 
			return null;
		object.setSchema(ss.getSynonimNvl(object.getSchema()));
		return object;
	} 
	
	public <T extends DBSchemaObject> Map<String, T> schemaSynonimMap(Map<String, T> objects) {
		if (objects == null) 
			return null;
		for (DBSchemaObject obj : objects.values()) {
			schemaSynonimMap(obj);
		} 		
		return objects;
	} 
	
	protected String getSchemaMap(String schema) {
		return ss.getSchemaNvl(schema);
	}
}
