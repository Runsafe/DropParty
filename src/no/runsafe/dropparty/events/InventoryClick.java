package no.runsafe.dropparty.events;

import no.runsafe.framework.api.event.inventory.IInventoryClick;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.inventory.RunsafeInventoryClickEvent;
import no.runsafe.framework.minecraft.inventory.RunsafeAnvilInventory;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;

public class InventoryClick implements IInventoryClick
{
	@Override
	public void OnInventoryClickEvent(RunsafeInventoryClickEvent event)
	{
		RunsafeMeta item = event.getCurrentItem();
		if (item == null) return;

		String displayName = item.getDisplayName();
		if (displayName == null) return;

		IPlayer player = event.getWhoClicked();

		if (event.getInventory() instanceof RunsafeAnvilInventory)
		{
			if (displayName.equalsIgnoreCase("drop_party_consumer"))
			{
				if (!player.hasPermission("runsafe.dropparty.createconsumer"))
				{
					player.sendColouredMessage("&cYou cannot do that.");
					event.setCancelled(true);
				}
			}
		}
	}
}
