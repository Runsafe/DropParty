package no.runsafe.dropparty.events;


import no.runsafe.dropparty.DropHandler;
import no.runsafe.framework.configuration.IConfiguration;
import no.runsafe.framework.event.IConfigurationChanged;
import no.runsafe.framework.event.player.IPlayerRightClickSign;
import no.runsafe.framework.server.block.RunsafeSign;
import no.runsafe.framework.server.inventory.RunsafeInventory;
import no.runsafe.framework.server.item.RunsafeItemStack;
import no.runsafe.framework.server.player.RunsafePlayer;

public class RightClickSign implements IPlayerRightClickSign, IConfigurationChanged
{
	public RightClickSign(DropHandler dropHandler)
	{
		this.dropHandler = dropHandler;
	}

	@Override
	public boolean OnPlayerRightClickSign(RunsafePlayer player, RunsafeItemStack usingItem, RunsafeSign sign)
	{
		if (sign.getLine(0).equalsIgnoreCase("Right click to") && sign.getLine(1).equalsIgnoreCase("start party!"))
		{
			if (!this.dropHandler.dropIsRunning())
			{
				RunsafeInventory playerInventory = player.getInventory();
				if (playerInventory.contains(this.dropCurrency, this.dropCost))
					playerInventory.remove(this.dropCurrency, this.dropCost);
				else
					player.sendColouredMessage("&cYou do not have enough to start a drop party!");
			}
			else
			{
				player.sendColouredMessage("&cA drop party is already running!");
			}
		}
		return true;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.dropCost = configuration.getConfigValueAsInt("dropCost");
		this.dropCurrency = configuration.getConfigValueAsInt("dropCurrency");
	}

	private int dropCost;
	private int dropCurrency;
	private DropHandler dropHandler;
}