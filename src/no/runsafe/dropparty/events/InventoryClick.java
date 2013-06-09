package no.runsafe.dropparty.events;

import no.runsafe.framework.event.inventory.IInventoryClick;
import no.runsafe.framework.server.event.inventory.RunsafeInventoryClickEvent;
import no.runsafe.framework.server.inventory.RunsafeAnvilInventory;
import no.runsafe.framework.server.item.RunsafeItemStack;
import no.runsafe.framework.server.item.meta.RunsafeItemMeta;
import no.runsafe.framework.server.player.RunsafePlayer;

public class InventoryClick implements IInventoryClick
{
	@Override
	public void OnInventoryClickEvent(RunsafeInventoryClickEvent event)
	{
		RunsafeItemStack item = event.getCurrentItem();
		if (item == null) return;

		//String displayName = item.getDisplayName();
		//if (displayName == null) return;

		RunsafeItemMeta meta = item.getItemMeta();
		if (meta == null) return;

		String displayName = meta.getDisplayName();
		if (displayName == null) return;

		RunsafePlayer player = event.getWhoClicked();

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
