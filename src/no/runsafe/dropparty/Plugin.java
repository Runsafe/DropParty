package no.runsafe.dropparty;

import no.runsafe.dropparty.commands.ClearItems;
import no.runsafe.dropparty.commands.ForceStart;
import no.runsafe.dropparty.events.InventoryClick;
import no.runsafe.dropparty.events.InventoryMoveItem;
import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.command.Command;

public class Plugin extends RunsafeConfigurablePlugin
{
	@Override
	protected void PluginSetup()
	{
		// Commands
		this.addComponent(DropHandler.class);

		Command dropParty = new Command("dropparty", "Lists drop-party related commands", null);
		dropParty.addSubCommand(getInstance(ForceStart.class));
		dropParty.addSubCommand(getInstance(ClearItems.class));

		this.addComponent(dropParty);

		// Events
		this.addComponent(InventoryMoveItem.class);
		this.addComponent(InventoryClick.class);
	}
}
