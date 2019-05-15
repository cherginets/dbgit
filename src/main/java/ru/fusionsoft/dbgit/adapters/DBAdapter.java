package ru.fusionsoft.dbgit.adapters;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;

import com.axiomalaska.jdbc.NamedParameterPreparedStatement;

import ru.fusionsoft.dbgit.core.ExceptionDBGitRestore;
import ru.fusionsoft.dbgit.core.ExceptionDBGitRunTime;
import ru.fusionsoft.dbgit.dbobjects.DBSequence;
import ru.fusionsoft.dbgit.meta.IMapMetaObject;
import ru.fusionsoft.dbgit.meta.IMetaObject;
import ru.fusionsoft.dbgit.meta.TreeMapMetaObject;
import ru.fusionsoft.dbgit.utils.ConsoleWriter;
import ru.fusionsoft.dbgit.utils.StringProperties;

/**
 * <div class="en">The base adapter adapter class. Contains general solutions independent of a particular database</div>
 * <div class="ru">Базовый класс адаптера БД. Содержит общие решения, независимые от конкретной БД</div>
 * 
 * @author mikle
 *
 */
public abstract class DBAdapter implements IDBAdapter {
	protected Connection connect;
	protected Boolean isExec = true;
	protected OutputStream streamSql = null;
	
	@Override
	public void setConnection(Connection conn) {
		connect = conn;
	}
	
	@Override
	public Connection getConnection() {
		return connect;
	} 
	
	@Override
	public void setDumpSqlCommand(OutputStream stream, Boolean isExec) {
		this.streamSql = stream;
		this.isExec = isExec;
	}
	
	@Override
	public OutputStream getStreamOutputSqlCommand() {
		return streamSql;
	}
	
	@Override
	public Boolean isExecSql() {
		return isExec;
	}
	
	@Override
	public void restoreDataBase(IMapMetaObject updateObjs) throws Exception {
		Connection connect = getConnection();
		IMapMetaObject currStep = updateObjs;
		
		try {
			
			for (IMetaObject obj : updateObjs.values()) {
				Integer step = 0;

				boolean res = false;
				
				while (!res) {
					res = getFactoryRestore().getAdapterRestore(obj.getType(), this).restoreMetaObject(obj, step);
					step++;

					if (step > 100) {
						throw new Exception("Error restore objects.... restoreMetaObject must return true if object restore.");
					}
				}
				
			}

			connect.commit();
		} catch (Exception e) {
			connect.rollback();
			throw new ExceptionDBGitRestore("Restore objects error", e);
		} finally {
			//connect.setAutoCommit(false);
		} 
		
	}
	
	@Override
	public void deleteDataBase(IMapMetaObject deleteObjs)  throws Exception {
		Connection connect = getConnection();
		try {
			//start transaction
			for (IMetaObject obj : deleteObjs.values()) {
				getFactoryRestore().getAdapterRestore(obj.getType(), this).removeMetaObject(obj);
			}
			connect.commit();
		} catch (Exception e) {
			connect.rollback();
			throw new ExceptionDBGitRestore("Remove objects error", e);
		} finally {
			//connect.setAutoCommit(false);
		} 

	}
	
	//-----------------------------------------------------------------
	
	public void rowToProperties(ResultSet rs, StringProperties properties) {
		try {
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				if (rs.getString(i) == null) continue ;
				properties.addChild(rs.getMetaData().getColumnName(i).toLowerCase(), rs.getString(i));
			}
		} catch(Exception e) {
			throw new ExceptionDBGitRunTime(e);
		}
	}
}
