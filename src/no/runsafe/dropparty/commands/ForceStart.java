package no.runsafe.dropparty.commands;

import no.runsafe.dropparty.DropHandler;
import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.player.IPlayer;

import java.util.Map;

public class ForceStart extends ExecutableCommand
{
	public ForceStart(DropHandler dropHandler)
	{
		super("force", "Forces a drop party event to start", "runsafe.dropparty.force");
		this.dropHandler = dropHandler;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, Map<String, String> parameters)
	{
		if (this.dropHandler.dropIsRunning())
			return "&cA drop party is already in progress.";

		if (executor instanceof IPlayer)
			this.dropHandler.initiateDrop((IPlayer) executor);
		else
			this.dropHandler.initiateDrop(null);

		return "&2A drop party has been initiated.";
	}

	private final DropHandler dropHandler;
}
