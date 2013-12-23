package no.runsafe.dropparty.events;


import no.runsafe.dropparty.DropHandler;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.block.ISign;
import no.runsafe.framework.api.event.player.IPlayerRightClickSign;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.internal.LegacyMaterial;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.framework.minecraft.inventory.RunsafeInventory;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;

public class RightClickSign implements IPlayerRightClickSign, IConfigurationChanged
{
	public RightClickSign(DropHandler dropHandler)
	{
		this.dropHandler = dropHandler;
	}

	@Override
	public boolean OnPlayerRightClickSign(IPlayer player, RunsafeMeta usingItem, ISign sign)
	{
		if (sign.getLine(0).equalsIgnoreCase("Right click to") && sign.getLine(1).equalsIgnoreCase("start party!"))
		{
			if (!this.dropHandler.dropIsRunning())
			{
				if (this.dropHandler.hasLoot())
				{
					RunsafeInventory playerInventory = player.getInventory();
					Item currencyItem = Item.get(LegacyMaterial.getById(this.dropCurrency).name());
					if (playerInventory.contains(currencyItem, this.dropCost))
					{
						player.removeItem(currencyItem, this.dropCost);
						this.dropHandler.initiateDrop(player);
					}
					else
					{
						player.sendColouredMessage("&cYou do not have enough to start a drop party!");
					}
				}
				else
				{
					player.sendColouredMessage("&cThere is no loot in the party pipes!");
				}
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
	private final DropHandler dropHandler;
}
