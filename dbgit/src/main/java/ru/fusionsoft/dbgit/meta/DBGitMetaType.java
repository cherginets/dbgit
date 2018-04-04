package ru.fusionsoft.dbgit.meta;


/**
 * Types meta objects
 * @author mikle
 *
 */
public enum DBGitMetaType {
	DBGitUser("usr"){		
		public Class<?> getMetaClass() {
			return MetaUser.class;
		}
		
		public Integer getPriority() {
			return 1;
		}
	},
	DBGitRole("role"){		
		public Class<?> getMetaClass() {
			return MetaRole.class;
		}
		
		public Integer getPriority() {
			return 3;
		}
	},
	DBGitTableSpace("ts"){		
		public Class<?> getMetaClass() {
			return MetaTableSpace.class;
		}
		
		public Integer getPriority() {
			return 5;
		}
	},
	DBGitSchema("sch"){		
		public Class<?> getMetaClass() {
			return MetaSchema.class;
		}
		
		public Integer getPriority() {
			return 10;
		}
	},
	
	DBGitSequence("seq"){		
		public Class<?> getMetaClass() {
			return MetaSequence.class;
		}
		
		public Integer getPriority() {
			return 20;
		}
	},
	
	DBGitTable("tbl"){		
		public Class<?> getMetaClass() {
			return MetaTable.class;
		}
		
		public Integer getPriority() {
			return 30;
		}
	},
	
    DbGitPakage("pkg") {		
		public Class<?> getMetaClass() {
			return MetaPackage.class;
		}
		
		public Integer getPriority() {
			return 40;
		}
	},
    
    DbGitTrigger("trg") {		
		public Class<?> getMetaClass() {
			return MetaTrigger.class;
		}
		
		public Integer getPriority() {
			return 50;
		}
	},
	
	DbGitProcedure("prc") {		
		public Class<?> getMetaClass() {
			return MetaProcedure.class;
		}
		
		public Integer getPriority() {
			return 60;
		}
	},
	
	DbGitFunction("fnc") {		
		public Class<?> getMetaClass() {
			return MetaFunction.class;
		}
		
		public Integer getPriority() {
			return 70;
		}
	},
	
	DbGitView("vw") {		
		public Class<?> getMetaClass() {
			return MetaView.class;
		}
		
		public Integer getPriority() {
			return 80;
		}
	},
	
	DbGitTableData("csv") {		
		public Class<?> getMetaClass() {
			return MetaTableData.class;
		}
		
		public Integer getPriority() {
			return 90;
		}
	},
	
	DbGitBlob("blob") {		
		public Class<?> getMetaClass() {
			return MetaBlobData.class;
		}
		
		public Integer getPriority() {
			return 100;
		}
	}
	;
	
	private final String val;
	
	DBGitMetaType(String val) {
		this.val = val;
	}
	
	public String getValue() {
        return val;
    }
	
	/**
	 * 
	 * @return class working with meta object this type 
	 */
	public abstract Class<?> getMetaClass();
	
	/**
	 * Priority type. Use in TreeMapMetaObject
	 * @return
	 */
	public abstract Integer getPriority();
}
