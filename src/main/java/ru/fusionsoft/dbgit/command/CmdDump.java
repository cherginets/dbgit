package ru.fusionsoft.dbgit.command;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import ru.fusionsoft.dbgit.core.DBGit;
import ru.fusionsoft.dbgit.core.DBGitIndex;
import ru.fusionsoft.dbgit.core.DBGitPath;
import ru.fusionsoft.dbgit.core.ExceptionDBGit;
import ru.fusionsoft.dbgit.core.GitMetaDataManager;
import ru.fusionsoft.dbgit.meta.IMapMetaObject;
import ru.fusionsoft.dbgit.meta.IMetaObject;
import ru.fusionsoft.dbgit.utils.ConsoleWriter;

public class CmdDump implements IDBGitCommand {
	private Options opts = new Options();
	
	public CmdDump() {
		opts.addOption("a", false, "adds files to git");
		opts.addOption("f", false, "dumps all objects that exist in index even there didn't changes in database");
	}
	
	public String getCommandName() {
		return "dump";
	}
	
	public String getParams() {
		return "";
	}
	
	public String getHelperInfo() {
		return "Examples: \n"
				+ "    dbgit dump\n"
				+ "    dbgit dump -a";
	}
	
	public Options getOptions() {
		return opts;
	}
	@Override
	public void execute(CommandLine cmdLine) throws Exception {		
		Boolean isAddToGit = cmdLine.hasOption('a');
		Boolean isAllDump = cmdLine.hasOption('f');
		
		ConsoleWriter.setDetailedLog(cmdLine.hasOption("v"));
		
		GitMetaDataManager gmdm = GitMetaDataManager.getInctance();
				
		DBGitIndex index = DBGitIndex.getInctance();
		
		if (!DBGitIndex.getInctance().isCorrectVersion())
			throw new ExceptionDBGit("Versions of Dbgit (" + DBGitIndex.VERSION + ") and repository(" + DBGitIndex.getInctance().getRepoVersion() + ") are different!");
		
		ConsoleWriter.detailsPrintLn("Checking files...");

		IMapMetaObject fileObjs = gmdm.loadFileMetaDataForce();
		
		ConsoleWriter.detailsPrintLn("Dumping...");
		
		for (IMetaObject obj : fileObjs.values()) {
			Timestamp timestampBefore = new Timestamp(System.currentTimeMillis());
			ConsoleWriter.detailsPrintLn("Processing " + obj.getName());
			String hash = obj.getHash();
			ConsoleWriter.detailsPrint("hash: " + hash + "\n", 2);
			
			ConsoleWriter.detailsPrint("Loading object from db...\n", 2);
			if (!gmdm.loadFromDB(obj)) {
				ConsoleWriter.println("Can't find " + obj.getName() + " in DB");
				continue;
			}
			ConsoleWriter.detailsPrint("db hash: " + obj.getHash() + "\n", 2);
			
			if (isAllDump || !obj.getHash().equals(hash)) {
				if (!obj.getHash().equals(hash))
					ConsoleWriter.detailsPrint("Hashes are different, saving to file...", 2);
				else
					ConsoleWriter.detailsPrint("-f switch found, saving to file...", 2);
				//сохранили файл если хеш разный
				obj.saveToFile();
				ConsoleWriter.detailsPrintlnGreen("OK");
				ConsoleWriter.detailsPrint("Adding to index...", 2);
				index.addItem(obj);				
				ConsoleWriter.detailsPrintlnGreen("OK");
				
				if (isAddToGit) {
					ConsoleWriter.detailsPrint("Adding to git...", 2);
					obj.addToGit();						
					ConsoleWriter.detailsPrintlnGreen("OK");
				}
			} else {
				ConsoleWriter.detailsPrint("Hashes match, no need to dump object...\n", 2);
			}
			Timestamp timestampAfter = new Timestamp(System.currentTimeMillis());
			Long diff = timestampAfter.getTime() - timestampBefore.getTime();
			ConsoleWriter.detailsPrint("Time..." + diff + " ms", 2);
			ConsoleWriter.detailsPrintLn("");
		}
		
		index.saveDBIndex();
		if (isAddToGit) {
			ConsoleWriter.detailsPrintLn("Adding to git");
			index.addToGit();
		}			
		ConsoleWriter.println("Done!");
	}
}