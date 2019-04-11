package ru.fusionsoft.dbgit.command;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import ru.fusionsoft.dbgit.core.DBGit;
import ru.fusionsoft.dbgit.core.ExceptionDBGit;
import ru.fusionsoft.dbgit.utils.ConsoleWriter;

public class CmdCommit implements IDBGitCommand {

	private Options opts = new Options();
	
	public CmdCommit() {
		opts.addOption("a", false, "dumps db changes to the dbgit repository and adds them to the git index");
		opts.addOption("m", true, "adds message to commit. You must add message as parameter");
	}
	
	@Override
	public String getCommandName() {
		return "commit";
	}

	@Override
	public String getParams() {
		return "";
	}

	@Override
	public String getHelperInfo() {
		return "Makes git commit. You can specify file to commit with parameter. \n"
				+ "You can use switches, run the command with -h for details\n"
				+ "Examples:\n"
				+ "    dbgit commit -m <Message>             - creates commit with message <Message>\n"
				+ "    dbgit commit -a -m <Message>          - dumps db changes to dbgit repository and makes commit with message <Message>\n"
				+ "    dbgit commit <file_name> -m <Message> - commits <file_name> only with message <Message>";
	}

	@Override
	public Options getOptions() {
		return opts;
	}

	@Override
	public void execute(CommandLine cmdLine) throws Exception {
		String[] args = cmdLine.getArgs();
		
		String filePath = "";
		if(!(args == null || args.length == 0)) {
			filePath = args[0];			
		}		
		
		String msg = "";
		
		if (cmdLine.hasOption("m")) {
			msg = cmdLine.getOptionValue("m");
		}
		ConsoleWriter.println("commiting...");		
		DBGit.getInstance().gitCommit(cmdLine.hasOption("a"), msg, filePath);
	}

}
