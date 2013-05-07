package no.runsafe.dropparty.commands;

import no.runsafe.dropparty.DropHandler;
import no.runsafe.framework.command.ExecutableCommand;
import no.runsafe.framework.server.ICommandExecutor;
import no.runsafe.framework.server.player.RunsafePlayer;

import java.util.HashMap;

public class ForceStart extends ExecutableCommand
{
	public ForceStart(DropHandler dropHandler)
	{
		super("force", "Forces a drop party event to start", "runsafe.dropparty.force");
		this.dropHandler = dropHandler;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, HashMap<String, String> parameters)
	{
		if (this.dropHandler.dropIsRunning())
			return "&cA drop party is already in progress.";

		if (executor instanceof RunsafePlayer)
			this.dropHandler.initiateDrop((RunsafePlayer) executor);
		else
			this.dropHandler.initiateDrop(null);

		return "&2A drop party has been initiated.";
	}

	private DropHandler dropHandler;
}
