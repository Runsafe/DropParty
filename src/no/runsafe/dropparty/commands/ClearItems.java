package no.runsafe.dropparty.commands;

import no.runsafe.dropparty.DropHandler;
import no.runsafe.framework.command.ExecutableCommand;
import no.runsafe.framework.server.ICommandExecutor;

import java.util.HashMap;

public class ClearItems extends ExecutableCommand
{
	public ClearItems(DropHandler dropHandler)
	{
		super("clear", "Clears the list of items waiting to be dropped.", "runsafe.dropparty.clear");
		this.dropHandler = dropHandler;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, HashMap<String, String> parameters)
	{
		if (this.dropHandler.dropIsRunning())
			return "&2You cannot empty item list right now.";

		this.dropHandler.clearItems();
		return "&2All items waiting to be dropped have been removed.";
	}

	private DropHandler dropHandler;
}
